<template>
  <div class="tcm-favorites">
    <div class="page-header">
      <button class="back-btn" @click="$router.replace('/practises')">← 返回</button>
      <h1 class="page-header__title">⭐ 收藏夹</h1>
      <span class="page-header__count">{{ store.favoriteQuestions.length }} 题</span>
    </div>

    <div v-if="store.favoriteQuestions.length === 0" class="empty-state">
      <div class="empty-state__icon">📌</div>
      <p class="empty-state__text">暂无收藏题目</p>
      <p class="empty-state__hint">答题时点击 ☆ 即可收藏</p>
    </div>

    <div v-else class="fav-list">
      <div
        v-for="(q, i) in store.favoriteQuestions"
        :key="q.id"
        class="fav-item"
        @click="reviewQuestion(q.id)"
      >
        <div class="fav-item__header">
          <span class="fav-item__index">#{{ i + 1 }}</span>
          <span class="fav-item__subject" :style="{ color: getSubjectColor(q.subject) }">
            {{ getSubjectName(q.subject) }}
          </span>
          <button
            class="fav-item__unfav"
            @click.stop="store.toggleFavorite(q.id)"
          >
            ★
          </button>
        </div>
        <p class="fav-item__question">{{ q.question }}</p>
      </div>
    </div>

    <div v-if="store.favoriteQuestions.length > 0" class="bottom-action">
      <button class="btn btn--primary" @click="practiceFavorites">
        📝 收藏练习
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useTcmStore } from '@/stores/tcm'
import { SUBJECTS } from '@/declaration/tcm'
import type { SubjectKey } from '@/declaration/tcm'

const router = useRouter()
const store = useTcmStore()

function getSubjectName(key: SubjectKey): string {
  return SUBJECTS.find(s => s.key === key)?.name || ''
}

function getSubjectColor(key: SubjectKey): string {
  return SUBJECTS.find(s => s.key === key)?.color || '#6b7280'
}

function reviewQuestion(id: number) {
  const idx = store.favoriteQuestions.findIndex(q => q.id === id)
  if (idx >= 0) {
    store.startSession('favorite', 'all')
    store.goToQuestion(idx)
    router.push('/practises/quiz')
  }
}

function practiceFavorites() {
  store.startSession('favorite', 'all')
  router.push('/practises/quiz')
}
</script>

<style scoped>
.tcm-favorites {
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
  flex: 1;
}
.page-header__count {
  font-size: 13px;
  color: #8b7355;
  background: #f5ebe0;
  padding: 4px 12px;
  border-radius: 12px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
}
.empty-state__icon { font-size: 48px; margin-bottom: 12px; }
.empty-state__text { font-size: 15px; color: #9a8577; margin: 0 0 4px; }
.empty-state__hint { font-size: 13px; color: #c9b8a8; margin: 0; }

.fav-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 24px;
}
.fav-item {
  background: #fff;
  border-radius: 14px;
  padding: 18px;
  border: 1px solid #e8ddd0;
  cursor: pointer;
  transition: all 0.2s;
}
.fav-item:hover {
  border-color: #c9a66b;
  box-shadow: 0 2px 10px rgba(139, 90, 60, 0.08);
  transform: translateY(-1px);
}
.fav-item__header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}
.fav-item__index {
  font-size: 12px;
  font-weight: 600;
  color: #9a8577;
}
.fav-item__subject {
  font-size: 12px;
  font-weight: 500;
}
.fav-item__unfav {
  margin-left: auto;
  background: none;
  border: none;
  font-size: 18px;
  color: #c9a66b;
  cursor: pointer;
  padding: 2px 6px;
  border-radius: 4px;
}
.fav-item__unfav:hover {
  color: #a67c52;
}
.fav-item__question {
  font-size: 14px;
  color: #4a3728;
  line-height: 1.6;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.bottom-action {
  display: flex;
  gap: 14px;
}
.btn {
  flex: 1;
  padding: 14px;
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
</style>
