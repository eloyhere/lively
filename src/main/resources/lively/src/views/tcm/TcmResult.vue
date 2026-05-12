<template>
  <div class="tcm-result">
    <!-- 成绩卡片 -->
    <div class="result-card">
      <div class="result-card__circle" :class="gradeClass">
        <span class="result-card__score">{{ score }}</span>
        <span class="result-card__unit">分</span>
      </div>
      <h2 class="result-card__title">{{ gradeText }}</h2>
      <p class="result-card__subtitle">{{ gradeDesc }}</p>
    </div>

    <!-- 详细统计 -->
    <div class="stats-grid">
      <div class="stat-item">
        <div class="stat-item__value">{{ totalAnswered }}</div>
        <div class="stat-item__label">答题总数</div>
      </div>
      <div class="stat-item stat-item--correct">
        <div class="stat-item__value">{{ correctCount }}</div>
        <div class="stat-item__label">正确数</div>
      </div>
      <div class="stat-item stat-item--wrong">
        <div class="stat-item__value">{{ totalAnswered - correctCount }}</div>
        <div class="stat-item__label">错误数</div>
      </div>
      <div class="stat-item">
        <div class="stat-item__value">{{ accuracy }}%</div>
        <div class="stat-item__label">正确率</div>
      </div>
    </div>

    <!-- 各科目正确率 -->
    <div class="subject-breakdown">
      <h3 class="section-title">各科目表现</h3>
      <div v-for="stat in subjectStats" :key="stat.subject" class="subject-bar">
        <div class="subject-bar__header">
          <span class="subject-bar__name">{{ getSubjectName(stat.subject) }}</span>
          <span class="subject-bar__value">{{ stat.accuracy.toFixed(0) }}%</span>
        </div>
        <div class="subject-bar__track">
          <div
            class="subject-bar__fill"
            :style="{ width: stat.accuracy + '%', backgroundColor: getSubjectColor(stat.subject) }"
          ></div>
        </div>
        <div class="subject-bar__detail">{{ stat.correct }} / {{ stat.total }} 正确</div>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="result-actions">
      <button class="btn btn--primary" @click="retryWrong" v-if="hasWrong">
        📖 错题重练
      </button>
      <button class="btn btn--ghost" @click="goHome">
        🏠 返回首页
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useTcmStore } from '@/stores/tcm'
import { SUBJECTS } from '@/declaration/tcm'
import type { SubjectKey } from '@/declaration/tcm'

const router = useRouter()
const store = useTcmStore()

const totalAnswered = computed(() => store.sessionAnswers.length)
const correctCount = computed(() => store.sessionCorrectCount)
const accuracy = computed(() => store.sessionAccuracy.toFixed(1))
const hasWrong = computed(() => totalAnswered.value - correctCount.value > 0)

const score = computed(() => Math.round(store.sessionAccuracy))

const gradeClass = computed(() => {
  if (score.value >= 90) return 'grade--excellent'
  if (score.value >= 70) return 'grade--good'
  if (score.value >= 60) return 'grade--pass'
  return 'grade--fail'
})

const gradeText = computed(() => {
  if (score.value >= 90) return '太棒了！'
  if (score.value >= 70) return '表现不错！'
  if (score.value >= 60) return '继续加油！'
  return '还需努力！'
})

const gradeDesc = computed(() => {
  if (score.value >= 90) return '你对中医知识的掌握非常扎实'
  if (score.value >= 70) return '大部分知识点已经掌握，再接再厉'
  if (score.value >= 60) return '基础尚可，建议重点复习错题'
  return '建议回顾基础知识，系统学习后再来挑战'
})

const subjectStats = computed(() =>
  store.subjectStatsList.filter(s => s.total > 0)
)

function getSubjectName(key: SubjectKey): string {
  return SUBJECTS.find(s => s.key === key)?.name || ''
}

function getSubjectColor(key: SubjectKey): string {
  return SUBJECTS.find(s => s.key === key)?.color || '#6b7280'
}

function retryWrong() {
  store.startSession('wrong', 'all')
  router.push('/practises/quiz')
}

function goHome() {
  router.replace('/practises')
}
</script>

<style scoped>
.tcm-result {
  padding: 36px 56px 56px;
}

/* Result Card */
.result-card {
  text-align: center;
  padding: 40px 24px 32px;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 2px 12px rgba(139, 90, 60, 0.08);
  margin-bottom: 24px;
}
.result-card__circle {
  width: 130px;
  height: 130px;
  border-radius: 50%;
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-bottom: 18px;
}
.grade--excellent {
  background: linear-gradient(135deg, #5a8a5a, #3d6b3d);
  color: #fff;
}
.grade--good {
  background: linear-gradient(135deg, #8b5a3c, #a67c52);
  color: #fff;
}
.grade--pass {
  background: linear-gradient(135deg, #c9a66b, #a67c52);
  color: #fff;
}
.grade--fail {
  background: linear-gradient(135deg, #b85c5c, #8a4a4a);
  color: #fff;
}
.result-card__score {
  font-size: 44px;
  font-weight: 800;
  line-height: 1;
}
.result-card__unit {
  font-size: 14px;
  opacity: 0.85;
}
.result-card__title {
  font-size: 24px;
  font-weight: 700;
  color: #5c3d2e;
  margin: 0 0 8px;
}
.result-card__subtitle {
  font-size: 14px;
  color: #9a8577;
  margin: 0;
}

/* Stats Grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
  margin-bottom: 24px;
}
.stat-item {
  background: #fff;
  border-radius: 14px;
  padding: 18px 12px;
  text-align: center;
  box-shadow: 0 1px 4px rgba(139, 90, 60, 0.06);
}
.stat-item__value {
  font-size: 26px;
  font-weight: 700;
  color: #5c3d2e;
}
.stat-item--correct .stat-item__value { color: #5a8a5a; }
.stat-item--wrong .stat-item__value { color: #b85c5c; }
.stat-item__label {
  font-size: 12px;
  color: #9a8577;
  margin-top: 4px;
}

/* Subject Breakdown */
.subject-breakdown {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 1px 4px rgba(139, 90, 60, 0.06);
  margin-bottom: 24px;
}
.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #5c3d2e;
  margin: 0 0 18px;
}
.subject-bar {
  margin-bottom: 16px;
}
.subject-bar:last-child { margin-bottom: 0; }
.subject-bar__header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}
.subject-bar__name {
  font-size: 14px;
  font-weight: 500;
  color: #5c3d2e;
}
.subject-bar__value {
  font-size: 14px;
  font-weight: 600;
  color: #8b7355;
}
.subject-bar__track {
  height: 8px;
  background: #f5ebe0;
  border-radius: 4px;
  overflow: hidden;
}
.subject-bar__fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.6s ease;
}
.subject-bar__detail {
  font-size: 12px;
  color: #9a8577;
  margin-top: 4px;
}

/* Actions */
.result-actions {
  display: flex;
  gap: 14px;
}
.btn {
  flex: 1;
  padding: 16px;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}
.btn--primary {
  background: linear-gradient(135deg, #8b5a3c, #a67c52);
  color: #fff;
}
.btn--primary:hover {
  box-shadow: 0 4px 14px rgba(139, 90, 60, 0.3);
  transform: translateY(-1px);
}
.btn--ghost {
  background: #f5ebe0;
  color: #5c3d2e;
}
.btn--ghost:hover {
  background: #efe0d0;
}
</style>
