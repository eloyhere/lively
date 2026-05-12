<template>
  <div class="tcm-wrong-book">
    <div class="page-header">
      <button class="back-btn" @click="$router.replace('/practises')">← 返回</button>
      <h1 class="page-header__title">📕 错题本</h1>
      <span class="page-header__count">{{ store.wrongQuestions.length }} 题</span>
    </div>

    <div v-if="store.wrongQuestions.length === 0" class="empty-state">
      <div class="empty-state__icon">🎉</div>
      <p class="empty-state__text">暂无错题，继续保持！</p>
    </div>

    <div v-else class="wrong-list">
      <div
        v-for="(q, i) in store.wrongQuestions"
        :key="q.id"
        class="wrong-item"
        @click="reviewQuestion(q.id)"
      >
        <div class="wrong-item__header">
          <span class="wrong-item__index">#{{ i + 1 }}</span>
          <span class="wrong-item__subject" :style="{ color: getSubjectColor(q.subject) }">
            {{ getSubjectName(q.subject) }}
          </span>
          <button
            class="wrong-item__remove"
            @click.stop="store.removeFromWrongBook(q.id)"
          >
            ✕
          </button>
        </div>
        <p class="wrong-item__question">{{ q.question }}</p>
        <p class="wrong-item__answer">
          正确答案：{{ q.answer.map(a => String.fromCharCode(65 + a)).join('、') }}
        </p>
      </div>
    </div>

    <div v-if="store.wrongQuestions.length > 0" class="bottom-action">
      <button class="btn btn--primary" @click="retryAll">
        🔄 全部重练
      </button>
      <button class="btn btn--danger" @click="clearAll">
        🗑 清空错题本
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
  // 找到该题在错题列表中的索引，开始错题模式并跳转
  const idx = store.wrongQuestions.findIndex(q => q.id === id)
  if (idx >= 0) {
    store.startSession('wrong', 'all')
    store.goToQuestion(idx)
    router.push('/practises/quiz')
  }
}

function retryAll() {
  store.startSession('wrong', 'all')
  router.push('/practises/quiz')
}

function clearAll() {
  if (confirm('确定要清空错题本吗？此操作不可撤销。')) {
    store.wrongQuestions.forEach(q => store.removeFromWrongBook(q.id))
  }
}
</script>

<style scoped>
.tcm-wrong-book {
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
.empty-state__icon {
  font-size: 48px;
  margin-bottom: 12px;
}
.empty-state__text {
  font-size: 15px;
  color: #9a8577;
  margin: 0;
}

.wrong-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 24px;
}
.wrong-item {
  background: #fff;
  border-radius: 14px;
  padding: 18px;
  border: 1px solid #e8d5c8;
  cursor: pointer;
  transition: all 0.2s;
}
.wrong-item:hover {
  border-color: #c9a090;
  box-shadow: 0 2px 10px rgba(139, 90, 60, 0.08);
  transform: translateY(-1px);
}
.wrong-item__header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}
.wrong-item__index {
  font-size: 12px;
  font-weight: 600;
  color: #9a8577;
}
.wrong-item__subject {
  font-size: 12px;
  font-weight: 500;
}
.wrong-item__remove {
  margin-left: auto;
  background: none;
  border: none;
  font-size: 14px;
  color: #d4c4b0;
  cursor: pointer;
  padding: 2px 6px;
  border-radius: 4px;
}
.wrong-item__remove:hover {
  color: #b85c5c;
  background: #faf4f4;
}
.wrong-item__question {
  font-size: 14px;
  color: #4a3728;
  line-height: 1.6;
  margin: 0 0 6px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.wrong-item__answer {
  font-size: 13px;
  color: #5a8a5a;
  font-weight: 500;
  margin: 0;
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
.btn--danger {
  background: #faf4f4;
  color: #b85c5c;
  border: 1px solid #e8d5c8;
}
.btn--danger:hover {
  background: #f5e8e8;
}
</style>
