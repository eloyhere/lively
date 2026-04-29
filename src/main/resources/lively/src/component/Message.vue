<template>
  <div ref="content" class="message">

  </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, watch, useTemplateRef, nextTick } from "vue";
import { User } from "@element-plus/icons-vue";

interface Message {
  content: string;
}

const props = defineProps<Message>();

const content = useTemplateRef<HTMLDivElement>("content");

let typingTimer: ReturnType<typeof setTimeout> | null = null;
let currentTypingIndex = 0;
let lastContent = "";

const stopTyping = () => {
  if (typingTimer) {
    clearTimeout(typingTimer);
    typingTimer = null;
  }
  currentTypingIndex = 0;
};

const startTyping = (text: string) => {
  if (!content.value) return;
  content.value.innerText = "";
  if (!text) return;

  const typeNextChar = () => {
    if (!content.value) return;
    if (currentTypingIndex < text.length) {
      content.value.innerText += text[currentTypingIndex];
      currentTypingIndex++;
      typingTimer = setTimeout(typeNextChar, 50);
    } else {
      stopTyping();
    }
  };
  typeNextChar();
};

watch(
    () => props.content,
    (newContent) => {
      if (newContent === lastContent) return;
      lastContent = newContent;
      stopTyping();

      if (newContent) {
        // 如果 DOM 已经存在，直接开始打字
        if (content.value) {
          startTyping(newContent);
        } else {
          // DOM 尚未挂载（多见于一次性完整赋值，如用户消息），
          // 等待 nextTick 后再启动
          nextTick(() => {
            // 防止等待期间内容又发生了变化
            if (lastContent === newContent) {
              startTyping(newContent);
            }
          });
        }
      } else if (content.value) {
        content.value.innerText = "";
      }
    },
    { immediate: true }
);

onBeforeUnmount(() => stopTyping());
</script>

<style scoped>
.message {
  width: 100%;
  padding: 8px 12px;
  border-radius: 12px;
  transition: background-color 0.2s ease;
}
.message:hover {
  background-color: var(--el-fill-color-light);
  cursor: pointer;
}
.message-content {
  word-break: break-word;
  white-space: pre-wrap;
}
</style>