<template>
  <div class="custom-list">
    <div
        v-for="(item, idx) in items"
        :key="getKey(item, idx)"
        class="list-item"
        :class="{ 'list-item-active': isActive(item, idx) }"
        @click="handleClick(item, idx)"
    >
      <!-- 自定义条目内容 -->
      <slot name="item" :item="item" :index="idx">
        <!-- 默认展示：如果 item 有 label 字段则显示，否则显示 item 本身 -->
        {{ item?.label ?? item }}
      </slot>
    </div>
    <div v-if="!items.length" class="list-empty">
      <slot name="empty">暂无数据</slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

// 组件属性
interface Props {
  /** 列表数据源 */
  items: any[]
  /** 用于生成唯一 key 的字段名，若未提供则使用索引 */
  keyField?: string
  /** 当前激活项的标识值（与 keyField 配合使用） */
  activeKey?: string | number
  /** 是否支持高亮激活项 */
  highlightActive?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  items: () => [],
  keyField: 'id',
  activeKey: undefined,
  highlightActive: true
})

// 事件定义
const emit = defineEmits<{
  /** 点击列表项时触发，参数为该项数据和索引 */
  (e: 'click', item: any, index: number): void
  /** 点击激活的项时也可单独处理，复用 click 即可 */
}>()

// 获取唯一标识
const getKey = (item: any, index: number): string | number => {
  if (props.keyField && item[props.keyField] !== undefined) {
    return item[props.keyField]
  }
  return index
}

// 判断某项是否为当前激活项
const isActive = (item: any, index: number): boolean => {
  if (!props.highlightActive || props.activeKey === undefined) return false
  const itemKey = getKey(item, index)
  return itemKey === props.activeKey
}

// 点击处理
const handleClick = (item: any, index: number) => {
  emit('click', item, index)
}
</script>

<style scoped>
.custom-list {
  width: 100%;
  background-color: var(--el-bg-color, #fff);
  border-radius: 8px;
  overflow: hidden;
}

.list-item {
  padding: 12px 16px;
  transition: background-color 0.2s ease;
  cursor: pointer;
  border-bottom: 1px solid var(--el-border-color-lighter, #ebeef5);
}

/* 鼠标悬浮变色 */
.list-item:hover {
  background-color: var(--el-fill-color-light, #f5f7fa);
}

/* 激活项高亮 */
.list-item-active {
  background-color: var(--el-color-primary-light-9, #ecf5ff);
  color: var(--el-color-primary, #409eff);
}

/* 最后一项无底边框 */
.list-item:last-child {
  border-bottom: none;
}

.list-empty {
  padding: 24px;
  text-align: center;
  color: var(--el-text-color-secondary, #909399);
}
</style>