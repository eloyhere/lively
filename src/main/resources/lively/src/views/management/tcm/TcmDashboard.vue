<template>
  <div class="tcm-dashboard">
    <!-- 顶部操作栏 -->
    <div class="dashboard-header">
      <span class="dashboard-title">题库概览</span>
      <ElButton type="primary" :icon="Refresh" circle size="small" @click="loadData" :loading="loading" />
    </div>

    <!-- 统计卡片 -->
    <ElRow :gutter="16" class="stat-row">
      <ElCol :span="6">
        <ElCard shadow="hover" class="stat-card">
          <ElStatistic title="总题目数" :value="totalCount">
            <template #prefix>
              <ElIcon><Document /></ElIcon>
            </template>
          </ElStatistic>
        </ElCard>
      </ElCol>
      <ElCol :span="6">
        <ElCard shadow="hover" class="stat-card">
          <ElStatistic title="今日新增" :value="todayCount">
            <template #prefix>
              <ElIcon><Plus /></ElIcon>
            </template>
          </ElStatistic>
        </ElCard>
      </ElCol>
      <ElCol :span="6">
        <ElCard shadow="hover" class="stat-card">
          <ElStatistic title="科目覆盖率" :value="subjectCoverage" suffix="%">
            <template #prefix>
              <ElIcon><Grid /></ElIcon>
            </template>
          </ElStatistic>
        </ElCard>
      </ElCol>
      <ElCol :span="6">
        <ElCard shadow="hover" class="stat-card">
          <ElStatistic title="平均难度" :value="averageDifficulty" :precision="1">
            <template #prefix>
              <ElIcon><Star /></ElIcon>
            </template>
          </ElStatistic>
        </ElCard>
      </ElCol>
    </ElRow>

    <!-- 科目分布 + 题型分布 -->
    <ElRow :gutter="16" class="middle-row">
      <ElCol :span="14">
        <ElCard shadow="hover">
          <template #header>
            <span class="card-title">科目分布</span>
          </template>
          <div class="subject-list">
            <div v-for="item in subjectStats" :key="item.key" class="subject-item">
              <div class="subject-header">
                <span class="subject-name">{{ item.label }}</span>
                <span class="subject-count">{{ item.count }} 题</span>
                <span class="subject-percent">{{ item.percent }}%</span>
              </div>
              <ElProgress
                :percentage="item.percent"
                :stroke-width="18"
                :show-text="false"
                :color="item.color"
              />
            </div>
          </div>
        </ElCard>
      </ElCol>
      <ElCol :span="10">
        <ElCard shadow="hover">
          <template #header>
            <span class="card-title">题型分布</span>
          </template>
          <div class="type-chart">
            <div v-for="item in typeStats" :key="item.key" class="type-item">
              <ElProgress
                type="circle"
                :percentage="item.percent"
                :width="100"
                :stroke-width="8"
                :color="item.color"
              >
                <template #default>
                  <div class="type-circle-text">
                    <div class="type-circle-count">{{ item.count }}</div>
                    <div class="type-circle-label">{{ item.label }}</div>
                  </div>
                </template>
              </ElProgress>
            </div>
          </div>
        </ElCard>
      </ElCol>
    </ElRow>

    <!-- 难度分布 -->
    <ElRow :gutter="16" class="bottom-row">
      <ElCol :span="24">
        <ElCard shadow="hover">
          <template #header>
            <span class="card-title">难度分布</span>
          </template>
          <div class="difficulty-list">
            <div v-for="item in difficultyStats" :key="item.key" class="difficulty-item">
              <div class="difficulty-header">
                <span class="difficulty-name">{{ item.label }}</span>
                <span class="difficulty-count">{{ item.count }} 题 ({{ item.percent }}%)</span>
              </div>
              <ElProgress
                :percentage="item.percent"
                :stroke-width="20"
                :show-text="false"
                :color="item.color"
              />
            </div>
          </div>
        </ElCard>
      </ElCol>
    </ElRow>
  </div>
</template>

<script setup lang="ts">
import { TcmQuestionService } from "@/hooks/service.ts";
import type { TcmQuestion } from "@/declaration/entity.ts";
import { ref, computed, onMounted } from "vue";
import { Document, Plus, Grid, Star, Refresh } from "@element-plus/icons-vue";

const tcmQuestionService: TcmQuestionService = new TcmQuestionService();

const questions = ref<Array<TcmQuestion>>([]);
const loading = ref(false);

// 科目配置
const subjectConfig: Array<{ key: string; label: string; color: string }> = [
  { key: "basic", label: "中医基础理论", color: "#409EFF" },
  { key: "diagnostics", label: "中医诊断学", color: "#F56C6C" },
  { key: "herbology", label: "中药学", color: "#67C23A" },
  { key: "prescriptions", label: "方剂学", color: "#E6A23C" },
  { key: "acupuncture", label: "针灸学", color: "#909399" }
];

// 题型配置
const typeConfig: Array<{ key: string; label: string; color: string }> = [
  { key: "single", label: "单选题", color: "#409EFF" },
  { key: "multiple", label: "多选题", color: "#E6A23C" },
  { key: "judge", label: "判断题", color: "#67C23A" }
];

// 难度配置
const difficultyConfig: Array<{ key: number; label: string; color: string }> = [
  { key: 1, label: "基础", color: "#67C23A" },
  { key: 2, label: "中等", color: "#E6A23C" },
  { key: 3, label: "困难", color: "#F56C6C" }
];

// 总题目数
const totalCount = computed<number>(() => questions.value.length);

// 今日新增
const todayCount = computed<number>(() => {
  const now = new Date();
  // 使用本地时区获取今天零点
  const todayStart = new Date(now.getFullYear(), now.getMonth(), now.getDate());
  return questions.value.filter(q => {
    if (!q.spawn) return false;
    const spawnDate = new Date(q.spawn);
    return spawnDate >= todayStart;
  }).length;
});

// 科目覆盖率（基于题目数量比例，每个科目至少5题视为充分覆盖）
const subjectCoverage = computed<number>(() => {
  const total = subjectConfig.length;
  if (total === 0) return 0;
  const minPerSubject = 5; // 每个科目至少5题视为充分覆盖
  let coverageSum = 0;
  subjectConfig.forEach(s => {
    const count = questions.value.filter(q => q.subject === s.key).length;
    coverageSum += Math.min(count / minPerSubject, 1) * 100;
  });
  return Math.round(coverageSum / total);
});

// 平均难度
const averageDifficulty = computed<number>(() => {
  if (questions.value.length === 0) return 0;
  const sum = questions.value.reduce((acc, q) => acc + (q.difficulty || 0), 0);
  return Number((sum / questions.value.length).toFixed(1));
});

// 科目统计
const subjectStats = computed<Array<{ key: string; label: string; count: number; percent: number; color: string }>>(() => {
  const total = questions.value.length;
  return subjectConfig.map(s => {
    const count = questions.value.filter(q => q.subject === s.key).length;
    return {
      key: s.key,
      label: s.label,
      count,
      percent: total > 0 ? Math.round((count / total) * 100) : 0,
      color: s.color
    };
  });
});

// 题型统计
const typeStats = computed<Array<{ key: string; label: string; count: number; percent: number; color: string }>>(() => {
  const total = questions.value.length;
  return typeConfig.map(t => {
    const count = questions.value.filter(q => q.type === t.key).length;
    return {
      key: t.key,
      label: t.label,
      count,
      percent: total > 0 ? Math.round((count / total) * 100) : 0,
      color: t.color
    };
  });
});

// 难度统计
const difficultyStats = computed<Array<{ key: number; label: string; count: number; percent: number; color: string }>>(() => {
  const total = questions.value.length;
  return difficultyConfig.map(d => {
    const count = questions.value.filter(q => q.difficulty === d.key).length;
    return {
      key: d.key,
      label: d.label,
      count,
      percent: total > 0 ? Math.round((count / total) * 100) : 0,
      color: d.color
    };
  });
});

// 加载数据
async function loadData() {
  loading.value = true;
  try {
    const res = await tcmQuestionService.findAll();
    questions.value = res || [];
  } catch (e) {
    console.error("加载题库数据失败", e);
    questions.value = [];
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.tcm-dashboard {
  padding: 4px;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.dashboard-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.stat-row {
  margin-bottom: 16px;
}

.stat-card {
  text-align: center;
}

.stat-card :deep(.el-statistic__head) {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-card :deep(.el-statistic__content) {
  font-size: 28px;
  font-weight: 600;
}

.stat-card :deep(.el-statistic__number) {
  font-size: 28px;
  font-weight: 600;
}

.middle-row {
  margin-bottom: 16px;
}

.bottom-row {
  margin-bottom: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
}

/* 科目分布 */
.subject-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.subject-item {
  width: 100%;
}

.subject-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.subject-name {
  font-size: 14px;
  color: #303133;
  min-width: 100px;
}

.subject-count {
  font-size: 13px;
  color: #606266;
}

.subject-percent {
  font-size: 13px;
  font-weight: 600;
  color: #409EFF;
  min-width: 40px;
  text-align: right;
}

/* 题型分布 */
.type-chart {
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 16px 0;
}

.type-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.type-circle-text {
  text-align: center;
}

.type-circle-count {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.type-circle-label {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

/* 难度分布 */
.difficulty-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.difficulty-item {
  width: 100%;
}

.difficulty-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.difficulty-name {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
  min-width: 60px;
}

.difficulty-count {
  font-size: 13px;
  color: #606266;
}
</style>
