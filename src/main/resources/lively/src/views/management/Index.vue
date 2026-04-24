<template>
  <div style="width: calc(100vw - 180px); height: calc(100vh - 100px); user-select: none;display: flex;flex-direction: column">
    <ElSpace alignment="center" wrap style="margin-left: 10px; margin-right: 10px">
      <ElCard>
        <ElStatistic title="用户数量" :value="consumerCountTransition">
          <template #title>
            <ElLink icon="CaretTop" type="success">用户数量</ElLink>
          </template>
        </ElStatistic>
      </ElCard>
    </ElSpace>
    <ElScrollbar>

      <ElSpace wrap>

        <ElCard>
          <div ref="maxMemory" style="width: 500px; height: 300px"></div>
        </ElCard>
        <ElCard>
          <div ref="freeMemory" style="width: 500px; height: 300px"></div>
        </ElCard>
        <ElCard>
          <div ref="totalMemory" style="width: 500px; height: 300px"></div>
        </ElCard>
      </ElSpace>
    </ElScrollbar>
  </div>

</template>
<script setup lang="ts">
import * as echarts from "echarts";
import {ElMessage} from "element-plus";
import {onActivated, onDeactivated, onMounted, onUnmounted, type Reactive, reactive, ref, type Ref} from "vue";
import {invalidate, type MaybeInvalid, validate} from "semantic-typescript";
import {ConsumerService} from "../../hooks/service.ts";
import {useTransition} from "@vueuse/core";
interface Structure extends Record<string, number>{
  freeMemory: number;
  maxMemory: number;
  processors: number;
  totalMemory: number;
}

const consumerService: ConsumerService = new ConsumerService();
const consumerCount: Ref<number> = ref<number>(0);
const consumerCountTransition = useTransition(consumerCount, {
  duration: 1500
});

const maxMemory: Ref<MaybeInvalid<HTMLElement>> = ref<MaybeInvalid<HTMLElement>>();
const maxMemoryChart: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const freeMemory: Ref<MaybeInvalid<HTMLElement>> = ref<MaybeInvalid<HTMLElement>>();
const freeMemoryChart: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const totalMemory: Ref<MaybeInvalid<HTMLElement>> = ref<MaybeInvalid<HTMLElement>>();
const totalMemoryChart: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const processors: Ref<number> = ref<number>(0);
const structures: Array<Structure> = reactive<Array<Structure>>([]);
const interval: Ref<MaybeInvalid<number>> = ref<MaybeInvalid<number>>();

interface Handler{
  create(): void;
  dispose(): void;
  draw(): void;
  pause(): void;
  play(): void;
}
const active: Ref<boolean> = ref<boolean>(true);
const handler: Handler = {
  create: (): void => {
    if(validate(maxMemory.value) && invalidate(maxMemoryChart.value)){
      maxMemoryChart.value = echarts.init(maxMemory.value);
    }
    if(validate(freeMemory.value) && invalidate(freeMemoryChart.value)){
      freeMemoryChart.value = echarts.init(freeMemory.value);
    }
    if(validate(totalMemory.value) && invalidate(totalMemoryChart.value)){
      totalMemoryChart.value = echarts.init(totalMemory.value);
    }
  },
  draw: (): void => {
    if(active.value){
      if(validate(maxMemory.value)){
        maxMemoryChart.value?.setOption({
          xAxis: {
            data: structures.map((structure, index) => index),
            name: "时间点"
          },
          yAxis: {
            name: "内存大小 (MB)",
            nameLocation: "end",
            nameGap: 20,
          },
          legend: {
            data: ["最大堆内存"],
            top: 10
          },
          series: [
            {
              name: "最大堆内存",
              data: structures.map((structure) => structure.maxMemory / 1048576),
              type: "line",
              stack: "x",
              areaStyle: {}
            }
          ]
        });
      }
      if(validate(freeMemory.value)){
        freeMemoryChart.value?.setOption({
          xAxis: {
            data: structures.map((structure, index) => index),
            name: "时间点"
          },
          yAxis: {
            name: "内存大小 (MB)",
            nameLocation: "end",
            nameGap: 20,
          },
          legend: {
            data: ["空闲堆内存"],
            top: 10
          },
          series: [
            {
              name: "空闲堆内存",
              data: structures.map((structure) => structure.freeMemory / 1048576),
              type: "line",
              stack: "x",
              areaStyle: {}
            }
          ]
        });
      }
      if(validate(totalMemory.value)){
        totalMemoryChart.value?.setOption({
          xAxis: {
            data: structures.map((structure, index) => index),
            name: "时间点"
          },
          yAxis: {
            name: "内存大小 (MB)",
            nameLocation: "end",
            nameGap: 20,
          },
          legend: {
            data: ["已分配堆内存"],
            top: 10
          },
          series: [
            {
              name: "已分配堆内存",
              data: structures.map((structure) => structure.totalMemory / 1048576),
              type: "line",
              stack: "x",
              areaStyle: {}
            }
          ]
        });
      }
    }
  },
  dispose: (): void => {
    if(validate(maxMemoryChart.value)){
      maxMemoryChart.value.dispose();
    }
    if(freeMemoryChart.value){
      freeMemoryChart.value.dispose();
    }
    if(totalMemoryChart.value){
      totalMemoryChart.value.dispose();
    }
  },
  pause: () => {
    active.value = false;
  },
  play: () => {
    active.value = true;
    if(validate(maxMemoryChart.value)){
      maxMemoryChart.value.resize();
    }
    if(freeMemoryChart.value){
      freeMemoryChart.value.resize();
    }
    if(totalMemoryChart.value){
      totalMemoryChart.value.resize();
    }
  },
};
const websocket: Ref<WebSocket> = ref<WebSocket>(new WebSocket(`ws://localhost:8080/websocket/monitor`));
websocket.value.addEventListener("open", (event): void => {
  websocket.value.send("Created.");
  if(invalidate(interval.value)){
    interval.value = setInterval(() =>{
      if(websocket.value.readyState === WebSocket.OPEN){
        websocket.value.send("monitor");
      }
    }, 5000);
  }
});
websocket.value.addEventListener("message", (event: MessageEvent) => {
  if(event.data !== "Created."){
    const structure: Structure = JSON.parse(event.data);
    structures.push(structure);
    processors.value = structure.processors;
    if(structures.length > 20){
      let temporary: Array<Structure> = [...structures.filter((s, index) => index > 1)];
      structures.length = 0;
      structures.push(...temporary);
    }
  }
  handler.draw();
});
websocket.value.addEventListener("error", (event: Event) => {
  handler.dispose();
  ElMessage({
    message: "连接服务器出现问题。",
    type: "warning"
  });
});
onMounted((): void => {
  handler.create();
  consumerService.countBy({}).then((value: number) => {
    consumerCount.value = value;
  });
});
onActivated((): void => {
  handler.create();
  handler.play();
});
onDeactivated((): void => {
  handler.dispose();
});
onUnmounted((): void => {
  handler.dispose();
  if(websocket.value.readyState === WebSocket.OPEN){
    websocket.value.close();
  }
});
</script>


<style scoped>

</style>