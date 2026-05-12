<template>
  <div class="tcm-quiz">
    <!-- 顶部状态栏 -->
    <div class="quiz-header">
      <button class="quiz-header__back" @click="handleBack">
        ← 返回
      </button>
      <div class="quiz-header__progress">
        <div class="quiz-header__progress-text">
          {{ store.currentIndex + 1 }} / {{ store.sessionQuestions.length }}
        </div>
        <div class="quiz-header__progress-bar">
          <div
            class="quiz-header__progress-fill"
            :style="{ width: store.sessionProgress + '%' }"
          ></div>
        </div>
      </div>
      <TimerDisplay v-if="store.sessionStartTime" :start-time="store.sessionStartTime" />
    </div>

    <!-- 答题统计条 -->
    <div class="quiz-stats-bar">
      <span class="quiz-stats-bar__item quiz-stats-bar__item--correct">
        ✓ {{ store.sessionCorrectCount }}
      </span>
      <span class="quiz-stats-bar__item quiz-stats-bar__item--wrong">
        ✗ {{ store.sessionAnswers.length - store.sessionCorrectCount }}
      </span>
      <span class="quiz-stats-bar__item">
        正确率 {{ store.sessionAccuracy.toFixed(1) }}%
      </span>
    </div>

    <!-- 题目卡片 -->
    <div class="quiz-body" v-if="question">
      <QuestionCard
        :question="question"
        :index="store.currentIndex"
        :selected="store.selectedOptions"
        :show-answer="store.showAnswer"
        :is-fav="store.isFavorite(question.id)"
        @select="(i) => store.selectOption(i)"
        @toggle-favorite="store.toggleFavorite(question.id)"
      />
    </div>

    <!-- 底部操作栏 -->
    <div class="quiz-footer">
      <button
        class="btn btn--ghost"
        :disabled="store.currentIndex === 0"
        @click="store.prevQuestion()"
      >
        上一题
      </button>

      <div class="quiz-footer__center">
        <button
          v-if="!store.showAnswer"
          class="btn btn--primary"
          :disabled="store.selectedOptions.length === 0"
          @click="handleSubmit"
        >
          提交答案
        </button>
        <template v-else>
          <button
            v-if="store.currentIndex < store.sessionQuestions.length - 1"
            class="btn btn--primary"
            @click="store.nextQuestion()"
          >
            下一题
          </button>
          <button
            v-else
            class="btn btn--accent"
            @click="finishQuiz"
          >
            查看结果
          </button>
        </template>
      </div>

      <!-- 题号导航 -->
      <button class="btn btn--ghost" @click="showMap = true">
        题号
      </button>
    </div>

    <!-- 题号导航弹窗 -->
    <transition name="fade">
      <div v-if="showMap" class="overlay" @click.self="showMap = false">
        <div class="question-map">
          <h3 class="question-map__title">答题卡</h3>
          <div class="question-map__grid">
            <button
              v-for="(q, i) in store.sessionQuestions"
              :key="q.id"
              class="question-map__item"
              :class="getMapItemClass(i)"
              @click="jumpTo(i)"
            >
              {{ i + 1 }}
            </button>
          </div>
          <div class="question-map__legend">
            <span class="legend-item"><span class="legend-dot legend-dot--correct"></span>正确</span>
            <span class="legend-item"><span class="legend-dot legend-dot--wrong"></span>错误</span>
            <span class="legend-item"><span class="legend-dot legend-dot--current"></span>当前</span>
            <span class="legend-item"><span class="legend-dot legend-dot--unanswered"></span>未答</span>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useTcmStore } from '@/stores/tcm'
import QuestionCard from '@/component/tcm/QuestionCard.vue'
import TimerDisplay from '@/component/tcm/TimerDisplay.vue'

const router = useRouter()
const store = useTcmStore()
const showMap = ref(false)

const question = computed(() => store.currentQuestion)

function handleSubmit() {
  store.submitAnswer()
}

function handleBack() {
  if (store.sessionAnswers.length > 0) {
    if (confirm('答题进行中，确定返回首页吗？')) {
      store.endSession()
      router.replace('/practises')
    }
  } else {
    store.endSession()
    router.replace('/practises')
  }
}

function finishQuiz() {
  store.endSession()
  router.push('/practises/result')
}

function jumpTo(index: number) {
  store.goToQuestion(index)
  showMap.value = false
}

function getMapItemClass(index: number) {
  const classes: string[] = []
  if (index === store.currentIndex) classes.push('question-map__item--current')
  const answer = store.sessionAnswers.find(a => a.questionId === store.sessionQuestions[index]?.id)
  if (answer) {
    classes.push(answer.isCorrect ? 'question-map__item--correct' : 'question-map__item--wrong')
  }
  return classes
}
</script>

<style scoped>
.tcm-quiz {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 16px 56px 24px;
}

/* Header */
.quiz-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 8px 0 14px;
}
.quiz-header__back {
  background: none;
  border: none;
  font-size: 14px;
  color: #8b7355;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: all 0.2s;
}
.quiz-header__back:hover {
  background: #f5ebe0;
  color: #5c3d2e;
}
.quiz-header__progress {
  flex: 1;
}
.quiz-header__progress-text {
  font-size: 13px;
  color: #8b7355;
  margin-bottom: 6px;
  text-align: center;
}
.quiz-header__progress-bar {
  height: 6px;
  background: #e8ddd0;
  border-radius: 3px;
  overflow: hidden;
}
.quiz-header__progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #8b5a3c, #c9a66b);
  border-radius: 3px;
  transition: width 0.4s ease;
}

/* Stats bar */
.quiz-stats-bar {
  display: flex;
  justify-content: center;
  gap: 24px;
  padding: 10px 0 14px;
  font-size: 13px;
  font-weight: 500;
}
.quiz-stats-bar__item--correct { color: #5a8a5a; }
.quiz-stats-bar__item--wrong { color: #b85c5c; }
.quiz-stats-bar__item { color: #8b7355; }

/* Body */
.quiz-body {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 16px;
}

/* Footer */
.quiz-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 0 12px;
  border-top: 1px solid #e8ddd0;
}
.quiz-footer__center {
  display: flex;
  gap: 12px;
}

/* Buttons */
.btn {
  padding: 12px 28px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}
.btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}
.btn--primary {
  background: linear-gradient(135deg, #8b5a3c, #a67c52);
  color: #fff;
}
.btn--primary:hover:not(:disabled) {
  box-shadow: 0 4px 12px rgba(139, 90, 60, 0.3);
  transform: translateY(-1px);
}
.btn--accent {
  background: linear-gradient(135deg, #c9a66b, #8b5a3c);
  color: #fff;
}
.btn--accent:hover:not(:disabled) {
  box-shadow: 0 4px 12px rgba(139, 90, 60, 0.3);
  transform: translateY(-1px);
}
.btn--ghost {
  background: #f5ebe0;
  color: #5c3d2e;
}
.btn--ghost:hover:not(:disabled) {
  background: #efe0d0;
}

/* Overlay */
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(92, 61, 46, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
  padding: 20px;
}
.question-map {
  background: #fffcf8;
  border-radius: 16px;
  padding: 28px;
  width: 100%;
  max-width: 420px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 8px 32px rgba(139, 90, 60, 0.2);
}
.question-map__title {
  font-size: 17px;
  font-weight: 600;
  color: #5c3d2e;
  margin: 0 0 18px;
  text-align: center;
}
.question-map__grid {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 8px;
  margin-bottom: 18px;
}
.question-map__item {
  width: 100%;
  aspect-ratio: 1;
  border-radius: 8px;
  border: 2px solid #e8ddd0;
  background: #fff;
  font-size: 13px;
  font-weight: 600;
  color: #8b7355;
  cursor: pointer;
  transition: all 0.15s;
  display: flex;
  align-items: center;
  justify-content: center;
}
.question-map__item:hover {
  border-color: #a67c52;
}
.question-map__item--current {
  border-color: #8b5a3c;
  background: #f5ebe0;
  color: #5c3d2e;
}
.question-map__item--correct {
  background: #f0f7f0;
  border-color: #5a8a5a;
  color: #3d6b3d;
}
.question-map__item--wrong {
  background: #faf0f0;
  border-color: #b85c5c;
  color: #8a4a4a;
}
.question-map__legend {
  display: flex;
  justify-content: center;
  gap: 18px;
  font-size: 12px;
  color: #8b7355;
}
.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
}
.legend-dot {
  width: 10px;
  height: 10px;
  border-radius: 3px;
}
.legend-dot--correct { background: #5a8a5a; }
.legend-dot--wrong { background: #b85c5c; }
.legend-dot--current { background: #8b5a3c; }
.legend-dot--unanswered { background: #e8ddd0; }

/* Animations */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
