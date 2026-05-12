<template>
  <div class="timer-display">
    <span class="timer-display__icon">⏱</span>
    <span class="timer-display__time">{{ formattedTime }}</span>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'

const props = defineProps<{
  startTime: number
}>()

const elapsed = ref(0)
let timer: ReturnType<typeof setInterval> | null = null

onMounted(() => {
  timer = setInterval(() => {
    elapsed.value = Math.floor((Date.now() - props.startTime) / 1000)
  }, 1000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})

const formattedTime = computed(() => {
  const mins = Math.floor(elapsed.value / 60)
  const secs = elapsed.value % 60
  return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
})
</script>

<style scoped>
.timer-display {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 7px 16px;
  background: #f5ebe0;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  color: #5c3d2e;
  font-variant-numeric: tabular-nums;
}
.timer-display__icon {
  font-size: 16px;
}
</style>
