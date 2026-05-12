<template>
  <div class="tcm-home">
    <!-- 加载状态 -->
    <div v-if="store.loading" class="loading-state">
      <div class="loading-spinner"></div>
      <p>正在加载题库...</p>
    </div>

    <!-- 错误提示 -->
    <div v-else-if="store.loadError" class="error-state">
      <div class="error-state__icon">⚠️</div>
      <h2 class="error-state__title">题库加载失败</h2>
      <p class="error-state__message">{{ store.errorMessage }}</p>
      <button class="error-state__retry" @click="retryLoad">重新加载</button>
    </div>

    <!-- 正常内容 -->
    <template v-else>
    <!-- 顶部欢迎区 -->
    <div class="hero">
      <div class="hero__content">
        <h1 class="hero__title">中医刷题</h1>
        <p class="hero__subtitle">系统学习中医知识，高效备考执业医师</p>
      </div>
      <div class="hero__stats">
        <div class="stat-card">
          <div class="stat-card__value">{{ store.totalStats.todayAnswered }}</div>
          <div class="stat-card__label">今日已练</div>
        </div>
        <div class="stat-card">
          <div class="stat-card__value stat-card__value--accent">{{ todayAccuracy }}%</div>
          <div class="stat-card__label">今日正确率</div>
        </div>
        <div class="stat-card">
          <div class="stat-card__value">{{ store.totalStats.streak }}</div>
          <div class="stat-card__label">连续正确</div>
        </div>
      </div>
    </div>

    <!-- 科目选择 -->
    <section class="section">
      <h2 class="section__title">选择科目</h2>
      <SubjectSelector v-model="selectedSubject" />
    </section>

    <!-- 刷题模式 -->
    <section class="section">
      <h2 class="section__title">选择模式</h2>
      <div class="mode-grid">
        <div class="mode-card" @click="startQuiz('sequential')">
          <div class="mode-card__icon">📋</div>
          <div class="mode-card__info">
            <h3 class="mode-card__name">顺序刷题</h3>
            <p class="mode-card__desc">按题号顺序逐题练习</p>
          </div>
          <span class="mode-card__arrow">→</span>
        </div>
        <div class="mode-card" @click="startQuiz('random')">
          <div class="mode-card__icon">🎲</div>
          <div class="mode-card__info">
            <h3 class="mode-card__name">随机刷题</h3>
            <p class="mode-card__desc">打乱顺序随机出题</p>
          </div>
          <span class="mode-card__arrow">→</span>
        </div>
        <div class="mode-card mode-card--warn" @click="startQuiz('wrong')">
          <div class="mode-card__icon">📖</div>
          <div class="mode-card__info">
            <h3 class="mode-card__name">错题重练</h3>
            <p class="mode-card__desc">针对性复习错题本</p>
          </div>
          <span class="mode-card__badge" v-if="store.wrongQuestions.length > 0">
            {{ store.wrongQuestions.length }}
          </span>
          <span class="mode-card__arrow">→</span>
        </div>
        <div class="mode-card mode-card--gold" @click="startQuiz('favorite')">
          <div class="mode-card__icon">⭐</div>
          <div class="mode-card__info">
            <h3 class="mode-card__name">收藏练习</h3>
            <p class="mode-card__desc">复习收藏的重点题目</p>
          </div>
          <span class="mode-card__badge" v-if="store.favoriteQuestions.length > 0">
            {{ store.favoriteQuestions.length }}
          </span>
          <span class="mode-card__arrow">→</span>
        </div>
      </div>
    </section>

    <!-- 快捷入口 -->
    <section class="section">
      <div class="quick-links">
        <router-link to="/practises/wrong-book" class="quick-link quick-link--brown">
          <span class="quick-link__icon">📕</span>
          <span class="quick-link__text">错题本</span>
          <span class="quick-link__count">{{ store.wrongQuestions.length }}</span>
        </router-link>
        <router-link to="/practises/favorites" class="quick-link quick-link--amber">
          <span class="quick-link__icon">⭐</span>
          <span class="quick-link__text">收藏夹</span>
          <span class="quick-link__count">{{ store.favoriteQuestions.length }}</span>
        </router-link>
        <router-link to="/practises/stats" class="quick-link quick-link--tan">
          <span class="quick-link__icon">📊</span>
          <span class="quick-link__text">学习统计</span>
        </router-link>
      </div>
    </section>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useTcmStore } from '@/stores/tcm'
import type { QuizMode, SubjectKey } from '@/declaration/tcm'
import SubjectSelector from '@/component/tcm/SubjectSelector.vue'

const router = useRouter()
const store = useTcmStore()
const selectedSubject = ref<SubjectKey | 'all'>('all')

const todayAccuracy = computed(() => store.todayAccuracy.toFixed(1))

onMounted(async () => {
  await store.loadQuestions()
})

async function retryLoad() {
  await store.refreshQuestions()
}

function startQuiz(mode: QuizMode) {
  store.startSession(mode, selectedSubject.value)
  router.push('/practises/quiz')
}
</script>

<style scoped>
.tcm-home {
  padding: 36px 56px 56px;
}

/* Hero */
.hero {
  background: linear-gradient(135deg, #8b5a3c 0%, #a67c52 50%, #c9a66b 100%);
  border-radius: 20px;
  padding: 32px 28px;
  color: #fff;
  margin-bottom: 28px;
  box-shadow: 0 4px 20px rgba(139, 90, 60, 0.2);
}
.hero__title {
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 6px;
  letter-spacing: 2px;
}
.hero__subtitle {
  font-size: 14px;
  opacity: 0.9;
  margin: 0 0 24px;
}
.hero__stats {
  display: flex;
  gap: 14px;
}
.stat-card {
  flex: 1;
  background: rgba(255,255,255,0.18);
  backdrop-filter: blur(8px);
  border-radius: 12px;
  padding: 14px;
  text-align: center;
  border: 1px solid rgba(255,255,255,0.15);
}
.stat-card__value {
  font-size: 26px;
  font-weight: 700;
}
.stat-card__value--accent {
  color: #ffd58c;
}
.stat-card__label {
  font-size: 12px;
  opacity: 0.85;
  margin-top: 4px;
}

/* Section */
.section {
  margin-bottom: 28px;
}
.section__title {
  font-size: 17px;
  font-weight: 600;
  color: #5c3d2e;
  margin: 0 0 16px;
  padding-left: 2px;
}

/* Mode Grid */
.mode-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}
.mode-card {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 20px 18px;
  background: #fff;
  border-radius: 14px;
  border: 1px solid #e8ddd0;
  cursor: pointer;
  transition: all 0.25s;
  position: relative;
}
.mode-card:hover {
  border-color: #a67c52;
  box-shadow: 0 4px 16px rgba(139, 90, 60, 0.12);
  transform: translateY(-2px);
}
.mode-card__icon {
  font-size: 30px;
  flex-shrink: 0;
}
.mode-card__info {
  flex: 1;
  min-width: 0;
}
.mode-card__name {
  font-size: 15px;
  font-weight: 600;
  color: #4a3728;
  margin: 0 0 3px;
}
.mode-card__desc {
  font-size: 12px;
  color: #9a8577;
  margin: 0;
}
.mode-card__arrow {
  font-size: 18px;
  color: #c9b8a8;
  flex-shrink: 0;
}
.mode-card:hover .mode-card__arrow {
  color: #a67c52;
}
.mode-card__badge {
  position: absolute;
  top: -6px;
  right: -6px;
  background: #c9a66b;
  color: #fff;
  font-size: 11px;
  font-weight: 700;
  min-width: 22px;
  height: 22px;
  border-radius: 11px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 6px;
  box-shadow: 0 2px 6px rgba(139, 90, 60, 0.25);
}
.mode-card--warn .mode-card__badge {
  background: #a67c52;
}
.mode-card--gold .mode-card__badge {
  background: #c9a66b;
}

/* Quick Links */
.quick-links {
  display: flex;
  gap: 14px;
}
.quick-link {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 18px;
  border-radius: 12px;
  text-decoration: none;
  transition: all 0.2s;
}
.quick-link--brown {
  background: #f5ebe0;
  color: #8b5a3c;
  border: 1px solid #e8ddd0;
}
.quick-link--brown:hover { background: #efe0d0; }
.quick-link--amber {
  background: #faf3e8;
  color: #a67c52;
  border: 1px solid #f0e4d0;
}
.quick-link--amber:hover { background: #f5ebd8; }
.quick-link--tan {
  background: #f8f4ef;
  color: #7a5c3e;
  border: 1px solid #ebe3d8;
}
.quick-link--tan:hover { background: #f2ebe0; }
.quick-link__icon {
  font-size: 20px;
}
.quick-link__text {
  font-size: 14px;
  font-weight: 600;
}
.quick-link__count {
  margin-left: auto;
  font-size: 13px;
  font-weight: 700;
  opacity: 0.7;
}

/* Loading State */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  color: #8b5a3c;
}
.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #e8ddd0;
  border-top-color: #a67c52;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin-bottom: 16px;
}
@keyframes spin {
  to { transform: rotate(360deg); }
}
.loading-state p {
  font-size: 15px;
  color: #9a8577;
}

/* Error State */
.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  text-align: center;
}
.error-state__icon {
  font-size: 48px;
  margin-bottom: 16px;
}
.error-state__title {
  font-size: 20px;
  font-weight: 600;
  color: #5c3d2e;
  margin: 0 0 8px;
}
.error-state__message {
  font-size: 14px;
  color: #9a8577;
  margin: 0 0 24px;
  max-width: 400px;
}
.error-state__retry {
  background: #a67c52;
  color: #fff;
  border: none;
  padding: 10px 28px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}
.error-state__retry:hover {
  background: #8b5a3c;
}
</style>
