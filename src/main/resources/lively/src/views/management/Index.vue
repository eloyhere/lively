<template>
  <ElContainer style="width: calc(100vw - 180px); height: calc(100vh - 100px); user-select: none;">
    <ElHeader style="height: 120px;">
      <ElCard style="width: 200px; height: 120px;">
        <ElStatistic title="用户数量" :value="consumerCountTransition">
          <template #title>
            <ElLink icon="CaretTop" type="success">用户数量</ElLink>
          </template>
        </ElStatistic>
      </ElCard>
    </ElHeader>
    <ElMain style="width: calc(100vw - 180px); height: calc(100vh - 220px); user-select: none;">
      <ElScrollbar>
        <div style="display: flex;flex-direction: column;align-items: start;justify-content: center;gap: 10px;">

          <ElDescriptions title="设备信息" border>
            <ElDescriptionsItem label="操作系统">
              {{ deviceInformation.os || "未知" }}
            </ElDescriptionsItem>
            <ElDescriptionsItem label="浏览器">
              {{ deviceInformation.browser || "未知" }}
            </ElDescriptionsItem>
            <ElDescriptionsItem label="设备类型">
              {{ deviceInformation.device || "未知" }}
            </ElDescriptionsItem>
            <ElDescriptionsItem label="平台">
              {{ deviceInformation.platform || "未知" }}
            </ElDescriptionsItem>
            <ElDescriptionsItem label="IP地址">
              {{ deviceInformation.ip || "未知" }}
            </ElDescriptionsItem>
            <ElDescriptionsItem label="远程主机">
              {{ deviceInformation.remoteHost || "未知" }}
            </ElDescriptionsItem>
            <ElDescriptionsItem label="远程端口">
              {{ deviceInformation.remotePort || "未知" }}
            </ElDescriptionsItem>
            <ElDescriptionsItem label="服务器名称">
              {{ deviceInformation.serverName || "未知" }}
            </ElDescriptionsItem>
            <ElDescriptionsItem label="服务器端口">
              {{ deviceInformation.serverPort || "未知" }}
            </ElDescriptionsItem>
            <ElDescriptionsItem label="上下文路径">
              {{ deviceInformation.contextPath || "未知" }}
            </ElDescriptionsItem>
            <ElDescriptionsItem label="请求方法">
              {{ deviceInformation.method || "未知" }}
            </ElDescriptionsItem>

            <ElDescriptionsItem label="协议">
              {{ deviceInformation.protocol || "未知" }}
            </ElDescriptionsItem>

            <ElDescriptionsItem label="字符编码">
              {{ deviceInformation.characterEncoding || "未知" }}
            </ElDescriptionsItem>

            <!-- 区域设置 -->
            <ElDescriptionsItem label="语言区域">
              {{ deviceInformation.locale || "未知" }}
            </ElDescriptionsItem>

            <ElDescriptionsItem label="时区">
              {{ deviceInformation.timezone || "未知" }}
            </ElDescriptionsItem>
          </ElDescriptions>

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

          <ElSpace wrap>
            <ElCard>
              <div ref="hourlyConsumerStatisticElement" style="width: 500px; height: 300px"></div>
            </ElCard>
            <ElCard>
              <div ref="dailyConsumerStatisticElement" style="width: 500px; height: 300px"></div>
            </ElCard>
            <ElCard>
              <div ref="weeklyConsumerStatisticElement" style="width: 500px; height: 300px"></div>
            </ElCard>
            <ElCard>
              <div ref="monthlyConsumerStatisticElement" style="width: 500px; height: 300px"></div>
            </ElCard>
            <ElCard>
              <div ref="yearlyConsumerStatisticElement" style="width: 500px; height: 300px"></div>
            </ElCard>
          </ElSpace>
        </div>
      </ElScrollbar>
    </ElMain>
  </ElContainer>

</template>
<script setup lang="ts">
import * as echarts from "echarts";
import { ElContainer, ElDescriptions, ElDescriptionsItem, ElMain, ElMessage, ElScrollbar } from "element-plus";
import { onActivated, onDeactivated, onMounted, onUnmounted, type Reactive, reactive, ref, type Ref } from "vue";
import { invalidate, isNumber, type MaybeInvalid, validate } from "semantic-typescript";
import { AuthenticationService, ConsumerService, StatisticService, type ConsumerSpawnInformation, type DeviceInformation } from "../../hooks/service.ts";
import {useTransition, useWebSocket} from "@vueuse/core";
import type { Consumer } from "@/declaration/entity.ts";
interface Structure extends Record<string, number> {
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

const hourlyConsumerStatistic: Reactive<Map<number, number>> = reactive<Map<number, number>>(new Map<number, number>());
const hourlyConsumerStatisticElement: Ref<MaybeInvalid<HTMLDivElement>> = ref<HTMLDivElement>();
const hourlyConsumerStatisticEcharts: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const dailyConsumerStatistic: Reactive<Map<number, number>> = reactive<Map<number, number>>(new Map<number, number>());
const dailyConsumerStatisticElement: Ref<MaybeInvalid<HTMLDivElement>> = ref<HTMLDivElement>();
const dailyConsumerStatisticEcharts: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const weeklyConsumerStatistic: Reactive<Map<number, number>> = reactive<Map<number, number>>(new Map<number, number>());
const weeklyConsumerStatisticElement: Ref<MaybeInvalid<HTMLDivElement>> = ref<HTMLDivElement>();
const weeklyConsumerStatisticEcharts: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const monthlyConsumerStatistic: Reactive<Map<number, number>> = reactive<Map<number, number>>(new Map<number, number>());
const monthlyConsumerStatisticElement: Ref<MaybeInvalid<HTMLDivElement>> = ref<HTMLDivElement>();
const monthlyConsumerStatisticEcharts: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const yearlyConsumerStatistic: Reactive<Map<number, number>> = reactive<Map<number, number>>(new Map<number, number>());
const yearlyConsumerStatisticElement: Ref<MaybeInvalid<HTMLDivElement>> = ref<HTMLDivElement>();
const yearlyConsumerStatisticEcharts: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const statisticService: StatisticService = new StatisticService();

const deviceInformation: Ref<DeviceInformation> = ref<DeviceInformation>({
  method: "GET",
  os: "",
  remoteHost: "",
  timezone: undefined,
  ip: "",
  contextPath: "",
  remotePort: 0,
  serverName: "",
  serverPort: 0,
  locale: "",
  platform: "",
  protocol: "",
  browser: "",
  characterEncoding: "",
  device: ""
});

const authenticationService: AuthenticationService = new AuthenticationService();

const maxMemory: Ref<MaybeInvalid<HTMLElement>> = ref<MaybeInvalid<HTMLElement>>();
const maxMemoryChart: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const freeMemory: Ref<MaybeInvalid<HTMLElement>> = ref<MaybeInvalid<HTMLElement>>();
const freeMemoryChart: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const totalMemory: Ref<MaybeInvalid<HTMLElement>> = ref<MaybeInvalid<HTMLElement>>();
const totalMemoryChart: Ref<MaybeInvalid<echarts.ECharts>> = ref<MaybeInvalid<echarts.ECharts>>();

const processors: Ref<number> = ref<number>(0);
const structures: Array<Structure> = reactive<Array<Structure>>([]);
const interval: Ref<MaybeInvalid<number>> = ref<MaybeInvalid<number>>();

interface Handler {
  create(): void;
  dispose(): void;
  draw(): void;
  pause(): void;
  play(): void;
}
const active: Ref<boolean> = ref<boolean>(true);
const handler: Handler = {
  create: (): void => {
    if (validate(maxMemory.value) && invalidate(maxMemoryChart.value)) {
      maxMemoryChart.value = echarts.init(maxMemory.value);
    }
    if (validate(freeMemory.value) && invalidate(freeMemoryChart.value)) {
      freeMemoryChart.value = echarts.init(freeMemory.value);
    }
    if (validate(totalMemory.value) && invalidate(totalMemoryChart.value)) {
      totalMemoryChart.value = echarts.init(totalMemory.value);
    }
  },
  draw: (): void => {
    if (active.value) {
      if (validate(maxMemory.value)) {
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
      if (validate(freeMemory.value)) {
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
      if (validate(totalMemory.value)) {
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
    if (validate(maxMemoryChart.value)) {
      maxMemoryChart.value.dispose();
    }
    if (freeMemoryChart.value) {
      freeMemoryChart.value.dispose();
    }
    if (totalMemoryChart.value) {
      totalMemoryChart.value.dispose();
    }
  },
  pause: () => {
    active.value = false;
  },
  play: () => {
    active.value = true;
    if (validate(maxMemoryChart.value)) {
      maxMemoryChart.value.resize();
    }
    if (freeMemoryChart.value) {
      freeMemoryChart.value.resize();
    }
    if (totalMemoryChart.value) {
      totalMemoryChart.value.resize();
    }
  },
};
useWebSocket(`ws://localhost:8080/websocket/monitor`, {
  heartbeat: {
    message: "ping",
    interval: 5000,
    pongTimeout: 5000
  },
  onMessage: (websocket: WebSocket, event: MessageEvent): void => {
    if (event.data !== "Created.") {
      const structure: Structure = JSON.parse(event.data);
      structures.push(structure);
      processors.value = structure.processors;
      if (structures.length > 20) {
        let temporary: Array<Structure> = [...structures.filter((s, index) => index > 1)];
        structures.length = 0;
        structures.push(...temporary);
      }
    }
    handler.draw();
  }
});
onMounted((): void => {
  handler.create();
  consumerService.countBy({}).then((value: number) => {
    consumerCount.value = value;
  });
  authenticationService.device().then((information: DeviceInformation) => {
    deviceInformation.value = information;
  });
  statisticService.consumer().then((value) => {
    let hourly: Record<number, number> = value.hourly;

    for (let key in hourly) {
      let count: number = isNumber(hourly[key]) ? hourly[key] : 0;
      hourlyConsumerStatistic.set(parseInt(key), count);
    }
    if (validate(hourlyConsumerStatisticElement.value)) {
      hourlyConsumerStatisticEcharts.value = echarts.init(hourlyConsumerStatisticElement.value);
      hourlyConsumerStatisticEcharts.value.setOption({
        xAxis: {
          name: "小时",
          type: "category",
          data: [...hourlyConsumerStatistic.keys()],
        },
        yAxis: {
          type: "value",
          name: "注册数量"
        },
        legend: {
          data: ["小时环比增长"],
          top: 10
        },
        series: [
          {
            name: "小时环比增长",
            data: [...hourlyConsumerStatistic.values()],
            type: "line"
          }
        ]
      });
    }

    let daily: Record<number, number> = value.daily;
    for (let key in daily) {
      let count: number = isNumber(daily[key]) ? daily[key] : 0;
      dailyConsumerStatistic.set(parseInt(key), count);
    }
    if (validate(dailyConsumerStatisticElement.value)) {
      dailyConsumerStatisticEcharts.value = echarts.init(dailyConsumerStatisticElement.value);
      dailyConsumerStatisticEcharts.value.setOption({
        xAxis: {
          type: "category",
          name: "日",
          data: [...dailyConsumerStatistic.keys()],
        },
        yAxis: {
          type: "value",
          name: "注册数量"
        },
        legend: {
          data: ["日环比增长"],
          top: 10
        },
        series: [
          {
            name: "日环比增长",
            data: [...dailyConsumerStatistic.values()],
            type: "line"
          }
        ]
      });
    }

    let weekly: Record<number, number> = value.weekly;
    for (let key in weekly) {
      let count: number = isNumber(weekly[key]) ? weekly[key] : 0;
      weeklyConsumerStatistic.set(parseInt(key), count);
    }
    if (validate(weeklyConsumerStatisticElement.value)) {
      weeklyConsumerStatisticEcharts.value = echarts.init(weeklyConsumerStatisticElement.value);
      weeklyConsumerStatisticEcharts.value.setOption({
        xAxis: {
          type: "category",
          name: "星期",
          data: [...weeklyConsumerStatistic.keys()],
        },
        yAxis: {
          type: "value",
          name: "注册数量"
        },
        legend: {
          data: ["周环比增长"],
          top: 10
        },
        series: [
          {
            name: "周环比增长",
            data: [...weeklyConsumerStatistic.values()],
            type: "line"
          }
        ]
      });
    }

    let monthly: Record<number, number> = value.monthly;
    for (let key in monthly) {
      let count: number = isNumber(monthly[key]) ? monthly[key] : 0;
      monthlyConsumerStatistic.set(parseInt(key), count);
    }
    if (validate(monthlyConsumerStatisticElement.value)) {
      monthlyConsumerStatisticEcharts.value = echarts.init(monthlyConsumerStatisticElement.value);
      monthlyConsumerStatisticEcharts.value.setOption({
        xAxis: {
          type: "category",
          name: "月",
          data: [...monthlyConsumerStatistic.keys()],
        },
        yAxis: {
          type: "value",
          name: "注册数量"
        },
        legend: {
          data: ["月环比增长"],
          top: 10
        },
        series: [
          {
            name: "月环比增长",
            data: [...monthlyConsumerStatistic.values()],
            type: "line"
          }
        ]
      });
    }

    let yearly: Record<number, number> = value.yearly;
    for (let key in yearly) {
      let count: number = isNumber(yearly[key]) ? yearly[key] : 0;
      yearlyConsumerStatistic.set(parseInt(key), count);
    }
    if (validate(yearlyConsumerStatisticElement.value)) {
      yearlyConsumerStatisticEcharts.value = echarts.init(yearlyConsumerStatisticElement.value);
      yearlyConsumerStatisticEcharts.value.setOption({
        xAxis: {
          type: "category",
          name: "年",
          data: [...yearlyConsumerStatistic.keys()],
        },
        yAxis: {
          type: "value",
          name: "注册数量"
        },
        legend: {
          data: ["年环比增长"],
          top: 10
        },
        series: [
          {
            name: "年环比增长",
            data: [...yearlyConsumerStatistic.values()],
            type: "line"
          }
        ]
      });
    }
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
});
</script>


<style scoped></style>