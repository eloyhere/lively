<template>
  <div style="width: calc(100vw - 180px); height: calc(100vh - 100px); user-select: none;">
    <ElScrollbar>
      <ElStatistics title="用户数量" ></ElStatistics>
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
import {onMounted, onUnmounted, reactive, ref, type Ref} from "vue";
import {invalidate, type MaybeInvalid, validate} from "semantic-typescript";
import {useOrigin} from "@/hooks/url.ts";
import {ConsumerService} from "@/interaction/service.ts";
interface Structure extends Record<string, number>{
  freeMemory: number;
  maxMemory: number;
  processors: number;
  totalMemory: number;
}

const consumerService: ConsumerService = new ConsumerService();

const consumerCount: Ref<bigint> = ref<bigint>(0n);

const maxMemory: Ref<MaybeInvalid<HTMLElement>> = ref<MaybeInvalid<HTMLElement>>();
const maxMemoryChart: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const freeMemory: Ref<MaybeInvalid<HTMLElement>> = ref<MaybeInvalid<HTMLElement>>();
const freeMemoryChart: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const totalMemory: Ref<MaybeInvalid<HTMLElement>> = ref<MaybeInvalid<HTMLElement>>();
const totalMemoryChart: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const processors: Ref<number> = ref<number>(0);
const structures: Array<Structure> = reactive<Array<Structure>>([]);
const interval: Ref<MaybeInvalid<number>> = ref<MaybeInvalid<number>>();

const websocket: Ref<WebSocket> = ref<WebSocket>(new WebSocket(`${useOrigin("ws")}/websocket/monitor`));
websocket.value.addEventListener("open", (event): void => {
  websocket.value.send("Created.");
  if(invalidate(interval.value)){
    interval.value = setInterval(() =>{
      if(websocket.value.readyState === WebSocket.OPEN){
        websocket.value.send("monitor");
      }
    }, 1000);
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
        data: ["空闲内存"],
        top: 10
      },
      series: [
        {
          name: "空闲内存",
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
        data: ["已分配内存"],
        top: 10
      },
      series: [
        {
          name: "已分配内存",
          data: structures.map((structure) => structure.totalMemory / 1048576),
          type: "line",
          stack: "x",
          areaStyle: {}
        }
      ]
    });
  }
});
websocket.value.addEventListener("error", (event: Event) => {
  ElMessage({
    message: "连接服务器出现问题。",
    type: "warning"
  });
});
onMounted((): void => {
  if(validate(maxMemory.value)){
    maxMemoryChart.value = echarts.init(maxMemory.value);
  }
  if(validate(freeMemory.value)){
    freeMemoryChart.value = echarts.init(freeMemory.value);
  }
  if(validate(totalMemory.value)){
    totalMemoryChart.value = echarts.init(totalMemory.value);
  }
  consumerService.countBy({}).then((value) => {
    consumerCount.value = value;
  })
});
onUnmounted((): void => {
  if(websocket.value.readyState === WebSocket.OPEN){
    websocket.value.close();
  }
})
</script>


<style scoped>

</style>