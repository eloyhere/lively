<template>
  <div class="tcm-stats">
    <div class="page-header">
      <button class="back-btn" @click="$router.replace('/practises')">← 返回</button>
      <h1 class="page-header__title">📊 学习统计</h1>
    </div>

    <!-- 总览卡片 -->
    <div class="overview-grid">
      <div class="overview-card overview-card--purple">
        <div class="overview-card__value">{{ store.totalStats.totalAnswered }}</div>
        <div class="overview-card__label">累计答题</div>
      </div>
      <div class="overview-card overview-card--green">
        <div class="overview-card__value">{{ store.overallAccuracy.toFixed(1) }}%</div>
        <div class="overview-card__label">总正确率</div>
      </div>
      <div class="overview-card overview-card--blue">
        <div class="overview-card__value">{{ store.totalStats.todayAnswered }}</div>
        <div class="overview-card__label">今日答题</div>
      </div>
      <div class="overview-card overview-card--amber">
        <div class="overview-card__value">{{ store.totalStats.streak }}</div>
        <div class="overview-card__label">最高连续</div>
      </div>
    </div>

    <!-- 各科目统计 -->
    <div class="subject-stats">
      <h2 class="section-title">各科目统计</h2>
      <div class="subject-stat-list">
        <div
          v-for="subject in subjectDataList"
          :key="subject.key"
          class="subject-stat-card"
        >
          <div class="subject-stat-card__header">
            <span class="subject-stat-card__icon">{{ subject.icon }}</span>
            <span class="subject-stat-card__name">{{ subject.name }}</span>
            <span class="subject-stat-card__accuracy" :style="{ color: subject.color }">
              {{ subject.accuracy }}%
            </span>
          </div>
          <div class="subject-stat-card__bar">
            <div
              class="subject-stat-card__fill"
              :style="{ width: subject.accuracy + '%', backgroundColor: subject.color }"
            ></div>
          </div>
          <div class="subject-stat-card__footer">
            <span>{{ subject.total }} 题</span>
            <span>正确 {{ subject.correct }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 知识掌握度 -->
    <div class="mastery-section">
      <h2 class="section-title">知识掌握度</h2>
      <div class="mastery-grid">
        <div
          v-for="subject in subjectDataList"
          :key="subject.key"
          class="mastery-item"
          :style="{ '--color': subject.color }"
        >
          <div class="mastery-item__ring">
            <svg viewBox="0 0 36 36" class="mastery-item__svg">
              <path
                class="mastery-item__bg"
                d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
              />
              <path
                class="mastery-item__progress"
                :stroke-dasharray="subject.accuracy + ', 100'"
                d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
              />
            </svg>
            <span class="mastery-item__value">{{ subject.accuracy }}%</span>
          </div>
          <span class="mastery-item__name">{{ subject.icon }} {{ subject.name }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useTcmStore } from '@/stores/tcm'
import { SUBJECTS } from '@/declaration/tcm'
import type { SubjectKey } from '@/declaration/tcm'

const store = useTcmStore()

const subjectDataList = computed(() =>
  SUBJECTS.map(s => {
    const total = store.allQuestions.filter(q => q.subject === s.key).length
    const stats = store.subjectStatsList.find(st => st.subject === s.key)
    const answered = stats?.total || 0
    const correct = stats?.correct || 0
    const accuracy = answered > 0 ? Math.round((correct / answered) * 100) : 0
    return {
      key: s.key,
      icon: s.icon,
      name: s.name,
      color: s.color,
      total,
      answered,
      correct,
      accuracy
    }
  })
)
</script>

<style scoped>
.tcm-stats {
  padding: 28px 56px 56px;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 24px;
}
.back-btn {
  background: none;
  border: none;
  font-size: 14px;
  color: #8b7355;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
}
.back-btn:hover { background: #f5ebe0; color: #5c3d2e; }
.page-header__title {
  font-size: 20px;
  font-weight: 700;
  color: #5c3d2e;
  margin: 0;
}

/* Overview */
.overview-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 14px;
  margin-bottom: 28px;
}
.overview-card {
  background: #fff;
  border-radius: 16px;
  padding: 22px;
  text-align: center;
  box-shadow: 0 1px 4px rgba(139, 90, 60, 0.06);
  border: 1px solid #f0e8de;
}
.overview-card__value {
  font-size: 28px;
  font-weight: 800;
  color: #5c3d2e;
}
.overview-card--purple .overview-card__value { color: #8b5a3c; }
.overview-card--green .overview-card__value { color: #5a8a5a; }
.overview-card--blue .overview-card__value { color: #a67c52; }
.overview-card--amber .overview-card__value { color: #c9a66b; }
.overview-card__label {
  font-size: 13px;
  color: #9a8577;
  margin-top: 4px;
}

/* Subject Stats */
.section-title {
  font-size: 17px;
  font-weight: 600;
  color: #5c3d2e;
  margin: 0 0 16px;
}
.subject-stats {
  margin-bottom: 28px;
}
.subject-stat-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.subject-stat-card {
  background: #fff;
  border-radius: 14px;
  padding: 18px;
  box-shadow: 0 1px 4px rgba(139, 90, 60, 0.06);
  border: 1px solid #f0e8de;
}
.subject-stat-card__header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}
.subject-stat-card__icon { font-size: 18px; }
.subject-stat-card__name {
  flex: 1;
  font-size: 14px;
  font-weight: 500;
  color: #4a3728;
}
.subject-stat-card__accuracy {
  font-size: 16px;
  font-weight: 700;
}
.subject-stat-card__bar {
  height: 6px;
  background: #f5ebe0;
  border-radius: 3px;
  overflow: hidden;
  margin-bottom: 6px;
}
.subject-stat-card__fill {
  height: 100%;
  border-radius: 3px;
  transition: width 0.6s ease;
}
.subject-stat-card__footer {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #9a8577;
}

/* Mastery */
.mastery-section {
  margin-bottom: 28px;
}
.mastery-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(130px, 1fr));
  gap: 18px;
}
.mastery-item {
  text-align: center;
}
.mastery-item__ring {
  position: relative;
  width: 84px;
  height: 84px;
  margin: 0 auto 10px;
}
.mastery-item__svg {
  width: 100%;
  height: 100%;
  transform: rotate(-90deg);
}
.mastery-item__bg {
  fill: none;
  stroke: #f5ebe0;
  stroke-width: 3;
}
.mastery-item__progress {
  fill: none;
  stroke: var(--color);
  stroke-width: 3;
  stroke-linecap: round;
  transition: stroke-dasharray 0.8s ease;
}
.mastery-item__value {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 700;
  color: #4a3728;
}
.mastery-item__name {
  font-size: 12px;
  color: #8b7355;
}
</style>
