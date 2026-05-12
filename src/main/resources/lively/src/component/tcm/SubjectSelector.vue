<template>
  <div class="subject-selector">
    <div
      class="subject-card"
      :class="{ 'subject-card--active': modelValue === 'all' }"
      @click="$emit('update:modelValue', 'all')"
    >
      <div class="subject-card__icon">📚</div>
      <div class="subject-card__name">全部科目</div>
      <div class="subject-card__count">{{ totalCount }} 题</div>
    </div>
    <div
      v-for="subject in subjects"
      :key="subject.key"
      class="subject-card"
      :class="{ 'subject-card--active': modelValue === subject.key }"
      :style="{ '--accent': subject.color }"
      @click="$emit('update:modelValue', subject.key)"
    >
      <div class="subject-card__icon">{{ subject.icon }}</div>
      <div class="subject-card__name">{{ subject.name }}</div>
      <div class="subject-card__count">{{ getSubjectCount(subject.key) }} 题</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { SubjectKey } from '@/declaration/tcm'
import { SUBJECTS } from '@/declaration/tcm'
import { useTcmStore } from '@/stores/tcm'

const props = defineProps<{
  modelValue: SubjectKey | 'all'
}>()

defineEmits<{
  'update:modelValue': [value: SubjectKey | 'all']
}>()

const subjects = SUBJECTS
const store = useTcmStore()

const totalCount = computed(() => store.allQuestions.length)

function getSubjectCount(key: SubjectKey): number {
  return store.allQuestions.filter(q => q.subject === key).length
}
</script>

<style scoped>
.subject-selector {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 12px;
}
.subject-card {
  padding: 18px 12px;
  border-radius: 14px;
  border: 2px solid #e8ddd0;
  background: #fff;
  cursor: pointer;
  transition: all 0.25s;
  text-align: center;
  user-select: none;
}
.subject-card:hover {
  border-color: var(--accent, #8b5a3c);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(139, 90, 60, 0.08);
}
.subject-card--active {
  border-color: var(--accent, #8b5a3c);
  background: linear-gradient(135deg, color-mix(in srgb, var(--accent, #8b5a3c) 8%, white), white);
  box-shadow: 0 2px 8px color-mix(in srgb, var(--accent, #8b5a3c) 15%, transparent);
}
.subject-card__icon {
  font-size: 30px;
  margin-bottom: 8px;
}
.subject-card__name {
  font-size: 13px;
  font-weight: 600;
  color: #4a3728;
  margin-bottom: 3px;
  line-height: 1.3;
}
.subject-card__count {
  font-size: 12px;
  color: #9a8577;
}
</style>
