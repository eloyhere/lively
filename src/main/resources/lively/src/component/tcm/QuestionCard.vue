<template>
  <div class="question-card" :class="{ 'question-card--answered': showAnswer }">
    <!-- 题目头部 -->
    <div class="question-card__header">
      <div class="question-card__tags">
        <span class="tag tag--type" :style="{ backgroundColor: typeColor }">
          {{ typeLabel }}
        </span>
        <span class="tag tag--subject" :style="{ backgroundColor: subjectColor }">
          {{ subjectName }}
        </span>
        <span class="tag tag--difficulty" :style="{ color: difficultyColor }">
          {{ difficultyLabel }}
        </span>
      </div>
      <button
        class="question-card__fav"
        :class="{ 'question-card__fav--active': isFav }"
        @click="$emit('toggle-favorite')"
      >
        {{ isFav ? '★' : '☆' }}
      </button>
    </div>

    <!-- 题目内容 -->
    <div class="question-card__body">
      <h3 class="question-card__text">
        <span class="question-card__index">{{ index + 1 }}.</span>
        {{ question.question }}
      </h3>
    </div>

    <!-- 选项列表 -->
    <div class="question-card__options">
      <div
        v-for="(option, i) in question.options"
        :key="i"
        class="option"
        :class="getOptionClass(i)"
        @click="$emit('select', i)"
      >
        <span class="option__label" :class="getLabelClass(i)">
          {{ String.fromCharCode(65 + i) }}
        </span>
        <span class="option__text">{{ option }}</span>
        <span v-if="showAnswer && question.answer.includes(i)" class="option__icon option__icon--correct">✓</span>
        <span v-else-if="showAnswer && selected.includes(i) && !question.answer.includes(i)" class="option__icon option__icon--wrong">✗</span>
      </div>
    </div>

    <!-- 答案解析 -->
    <transition name="slide">
      <div v-if="showAnswer" class="question-card__explanation">
        <div class="explanation__header">
          <span class="explanation__answer" :class="isCorrect ? 'explanation__answer--correct' : 'explanation__answer--wrong'">
            {{ isCorrect ? '回答正确' : '回答错误' }}
          </span>
          <span class="explanation__correct-answer">
            正确答案：{{ correctAnswerText }}
          </span>
        </div>
        <div class="explanation__body">
          <p><strong>解析：</strong>{{ question.explanation }}</p>
          <p class="explanation__keypoint">
            <strong>考点：</strong>{{ question.keyPoint }}
          </p>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Question } from '@/declaration/tcm'
import { SUBJECTS, QUESTION_TYPE_LABELS, DIFFICULTY_LABELS, DIFFICULTY_COLORS } from '@/declaration/tcm'

const props = defineProps<{
  question: Question
  index: number
  selected: number[]
  showAnswer: boolean
  isFav: boolean
}>()

defineEmits<{
  'select': [index: number]
  'toggle-favorite': []
}>()

const typeLabel = computed(() => QUESTION_TYPE_LABELS[props.question.type])
const typeColor = computed(() => {
  switch (props.question.type) {
    case 'single': return '#8b5a3c'
    case 'multiple': return '#a67c52'
    case 'judge': return '#c9a66b'
    default: return '#8b7355'
  }
})

const subjectInfo = computed(() => SUBJECTS.find(s => s.key === props.question.subject))
const subjectName = computed(() => subjectInfo.value?.name || '')
const subjectColor = computed(() => subjectInfo.value?.color || '#8b7355')
const difficultyLabel = computed(() => DIFFICULTY_LABELS[props.question.difficulty])
const difficultyColor = computed(() => DIFFICULTY_COLORS[props.question.difficulty])

const correctAnswerText = computed(() =>
  props.question.answer.map(a => String.fromCharCode(65 + a)).join('、')
)

const isCorrect = computed(() =>
  props.selected.length === props.question.answer.length &&
  props.selected.every(o => props.question.answer.includes(o))
)

function getOptionClass(i: number) {
  if (!props.showAnswer) {
    return props.selected.includes(i) ? 'option--selected' : 'option--default'
  }
  const isAns = props.question.answer.includes(i)
  const isSel = props.selected.includes(i)
  if (isAns) return 'option--correct'
  if (isSel && !isAns) return 'option--wrong'
  return 'option--dimmed'
}

function getLabelClass(i: number) {
  if (!props.showAnswer) {
    return props.selected.includes(i) ? 'option__label--selected' : ''
  }
  const isAns = props.question.answer.includes(i)
  const isSel = props.selected.includes(i)
  if (isAns) return 'option__label--correct'
  if (isSel && !isAns) return 'option__label--wrong'
  return ''
}
</script>

<style scoped>
.question-card {
  background: #fff;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 1px 4px rgba(139, 90, 60, 0.06), 0 1px 2px rgba(139, 90, 60, 0.04);
  transition: box-shadow 0.3s;
  border: 1px solid #f0e8de;
}
.question-card:hover {
  box-shadow: 0 4px 16px rgba(139, 90, 60, 0.08);
}
.question-card__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18px;
}
.question-card__tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
.tag {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
  color: #fff;
}
.tag--difficulty {
  background: transparent !important;
  font-weight: 600;
}
.question-card__fav {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #d4c4b0;
  transition: all 0.2s;
  padding: 4px;
  line-height: 1;
}
.question-card__fav--active {
  color: #c9a66b;
}
.question-card__fav:hover {
  transform: scale(1.2);
}
.question-card__body {
  margin-bottom: 22px;
}
.question-card__text {
  font-size: 17px;
  font-weight: 500;
  color: #4a3728;
  line-height: 1.75;
  margin: 0;
}
.question-card__index {
  color: #8b7355;
  margin-right: 4px;
}
.question-card__options {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.option {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 15px 18px;
  border-radius: 12px;
  border: 2px solid #e8ddd0;
  cursor: pointer;
  transition: all 0.2s;
  user-select: none;
}
.option--default:hover {
  border-color: #c9a66b;
  background: #faf6f0;
}
.option--selected {
  border-color: #8b5a3c;
  background: #faf6f0;
}
.option--correct {
  border-color: #5a8a5a;
  background: #f4f8f4;
}
.option--wrong {
  border-color: #b85c5c;
  background: #faf4f4;
}
.option--dimmed {
  opacity: 0.4;
  cursor: default;
}
.option__label {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
  background: #f5ebe0;
  color: #8b7355;
  flex-shrink: 0;
  transition: all 0.2s;
}
.option__label--selected {
  background: #8b5a3c;
  color: #fff;
}
.option__label--correct {
  background: #5a8a5a;
  color: #fff;
}
.option__label--wrong {
  background: #b85c5c;
  color: #fff;
}
.option__text {
  flex: 1;
  font-size: 15px;
  color: #4a3728;
  line-height: 1.5;
}
.option__icon {
  font-size: 18px;
  font-weight: 700;
  flex-shrink: 0;
}
.option__icon--correct { color: #5a8a5a; }
.option__icon--wrong { color: #b85c5c; }

/* 解析区 */
.question-card__explanation {
  margin-top: 22px;
  padding: 20px;
  border-radius: 12px;
  background: #faf6f0;
  border-left: 4px solid #8b5a3c;
}
.explanation__header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
}
.explanation__answer {
  font-weight: 600;
  font-size: 15px;
}
.explanation__answer--correct { color: #5a8a5a; }
.explanation__answer--wrong { color: #b85c5c; }
.explanation__correct-answer {
  font-size: 14px;
  color: #8b7355;
}
.explanation__body {
  font-size: 14px;
  color: #5c4a3a;
  line-height: 1.7;
}
.explanation__body p { margin: 0 0 6px; }
.explanation__body p:last-child { margin-bottom: 0; }
.explanation__keypoint {
  color: #8b5a3c;
}

/* 动画 */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
  max-height: 300px;
  overflow: hidden;
}
.slide-enter-from,
.slide-leave-to {
  max-height: 0;
  opacity: 0;
  margin-top: 0;
  padding: 0 20px;
}
</style>
