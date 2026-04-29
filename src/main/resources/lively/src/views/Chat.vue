<template>
  <ElContainer class="chat-app">
    <ElHeader class="chat-header">
      <Toolbar />
    </ElHeader>

    <ElContainer class="chat-body">
      <ElMain class="chat-main">
        <ElScrollbar ref="scroll" class="scrollbar">
          <div class="message-list">
            <div
                v-for="message in messages"
                :key="message.id"
                class="message-row"
                :class="message.role === 'user' ? 'row-user' : 'row-assistant'"
            >
              <!-- 头像 -->
              <div class="avatar" :class="message.role === 'user' ? 'avatar-user' : 'avatar-ai'">
                <span class="avatar-emoji">{{ message.role === 'user' ? '😎' : '🌿' }}</span>
              </div>

              <!-- 气泡 -->
              <div class="bubble" :class="message.role === 'user' ? 'bubble-user' : 'bubble-ai'">
                <Message :content="message.content" />
                <div class="bubble-footer">
                  <span class="role-name">{{ message.role === 'user' ? '我' : '小药童' }}</span>
                  <span class="timestamp">{{ useDateFormat(message.spawn) }}</span>
                </div>
              </div>
            </div>
          </div>
        </ElScrollbar>
      </ElMain>

      <ElFooter class="chat-footer" height="auto">
        <div class="input-area">
          <el-input
              v-model="inputText"
              type="textarea"
              :rows="2"
              placeholder="输入你的问题... (Ctrl+Enter 发送)"
              @keyup.ctrl.enter="sendMessage"
              :disabled="sending"
              class="input-box"
          />
          <el-button
              type="primary"
              @click="sendMessage"
              :loading="sending"
              round
              class="send-btn"
          >
            {{ sending ? '思考中' : '发送' }}
          </el-button>
        </div>
      </ElFooter>
    </ElContainer>
  </ElContainer>
</template>

<script setup lang="ts">
import { reactive, ref, nextTick, onBeforeUnmount } from 'vue'
import {
  ElContainer,
  ElHeader,
  ElMain,
  ElFooter,
  ElScrollbar,
  ElInput,
  ElButton
} from 'element-plus'
import Toolbar from '@/component/Toolbar.vue'
import { useDateFormat } from '@/hooks/datetime'
import Message from '@/component/Message.vue'

interface Message {
  id: number
  role: 'user' | 'assistant'
  content: string
  spawn: Date
}

const messages = reactive<Message[]>([])
const inputText = ref('')
const sending = ref(false)
const scroll = ref<InstanceType<typeof ElScrollbar>>()

const currentAssistantMsgId = ref<number | null>(null)
let abortController: AbortController | null = null

const scrollToBottom = async () => {
  await nextTick()
  if (scroll.value?.wrapRef) {
    scroll.value.wrapRef.scrollTop = scroll.value.wrapRef.scrollHeight
  }
}

const cancelCurrentStream = () => {
  if (abortController) {
    abortController.abort()
    abortController = null
  }
  if (currentAssistantMsgId.value !== null) {
    const idx = messages.findIndex((m) => m.id === currentAssistantMsgId.value)
    if (idx !== -1) {
      messages.splice(idx, 1)
    }
    currentAssistantMsgId.value = null
  }
  sending.value = false
}

const sendMessage = async () => {
  const content = inputText.value.trim()
  if (!content) return

  if (sending.value) {
    cancelCurrentStream()
  }

  const userMsg: Message = {
    id: Date.now(),
    role: 'user',
    content,
    spawn: new Date(),
  }
  messages.push(userMsg)
  inputText.value = ''
  await scrollToBottom()

  const assistantId = Date.now() + 1
  currentAssistantMsgId.value = assistantId
  const assistantMsg: Message = {
    id: assistantId,
    role: 'assistant',
    content: '',
    spawn: new Date(),
  }
  messages.push(assistantMsg)
  await scrollToBottom()

  sending.value = true
  abortController = new AbortController()

  try {
    const response = await fetch('http://localhost:8080/chat/message', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ content }),
      signal: abortController.signal,
    })

    if (!response.ok) throw new Error(`HTTP ${response.status}`)
    if (!response.body) throw new Error('No response body')

    const reader = response.body.getReader()
    const decoder = new TextDecoder('utf-8')
    let buffer = ''

    while (true) {
      const { done, value } = await reader.read()
      if (done) break

      buffer += decoder.decode(value, { stream: true })
      const lines = buffer.split('\n')
      buffer = lines.pop() || ''

      for (const line of lines) {
        if (!line.startsWith('data: ')) continue
        const data = line.slice(6).trim()
        if (data === '[DONE]') continue

        const idx = messages.findIndex((m) => m.id === currentAssistantMsgId.value)
        if (idx === -1) return

        try {
          const json = JSON.parse(data)
          const delta = json.choices?.[0]?.delta?.content
          if (delta) {
            messages[idx].content += delta
            scrollToBottom()
          }
        } catch {
          messages[idx].content += data
          scrollToBottom()
        }
      }
    }
  } catch (error: any) {
    if (error?.name === 'AbortError') return
    console.error('Stream error:', error)
    const idx = messages.findIndex((m) => m.id === currentAssistantMsgId.value)
    if (idx !== -1) {
      messages[idx].content = '抱歉，服务暂时不可用，请稍后重试。'
    }
  } finally {
    sending.value = false
    currentAssistantMsgId.value = null
    abortController = null
  }
}

onBeforeUnmount(() => {
  cancelCurrentStream()
})
</script>

<style scoped>
/* ===== 全局容器 ===== */
.chat-app {
  height: 100vh;
  width: 100vw;
  background-color: #fcf7f0;   /* 暖杏色底 */
  font-family: 'Nunito', 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

/* ===== 头部 ===== */
.chat-header {
  height: 64px !important;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(8px);
  box-shadow: 0 2px 12px rgba(140, 190, 140, 0.15);
  display: flex;
  align-items: center;
  padding: 0 24px;
  border-bottom: 1px solid #e8f0e3;
}

/* ===== 主体区域 ===== */
.chat-body {
  height: calc(100vh - 64px);
  width: 100vw;
}

.chat-main {
  height: calc(100vh - 64px);
  padding: 0;
  position: relative;
  overflow: hidden;
}

/* 消息列表 */
.message-list {
  max-width: 900px;
  margin: 0 auto;
  padding: 24px 20px 120px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 单条消息行 */
.message-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.row-user {
  flex-direction: row-reverse;
}

/* ===== 头像 ===== */
.avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.avatar-user {
  background: linear-gradient(135deg, #ffe6c2, #ffc971);
}

.avatar-ai {
  background: linear-gradient(135deg, #c8f5d5, #8fd19e);
}

.avatar-emoji {
  line-height: 1;
}

/* ===== 气泡 ===== */
.bubble {
  max-width: 75%;
  padding: 12px 18px;
  border-radius: 24px;
  position: relative;
  word-break: break-word;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.06);
  transition: transform 0.2s ease;
}

.bubble:hover {
  transform: translateY(-2px);
}

.bubble-user {
  background: linear-gradient(135deg, #d4f5e9, #b2e4d5);
  border-bottom-right-radius: 8px;
  margin-right: 4px;
}

.bubble-ai {
  background: #ffffff;
  border-bottom-left-radius: 8px;
  margin-left: 4px;
}

/* 气泡底部信息 */
.bubble-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
  font-size: 12px;
  color: #8b9a8b;
}

.role-name {
  font-weight: 600;
  color: #5b8266;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.timestamp {
  opacity: 0.7;
}

/* ===== 输入区域 ===== */
.chat-footer {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(12px);
  border-top: 1px solid #e8efe4;
  padding: 0;
}

.input-area {
  display: flex;
  align-items: flex-end;
  gap: 16px;
  padding: 16px 24px;
  max-width: 900px;
  margin: 0 auto;
}

.input-box {
  flex: 1;
  border-radius: 28px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
}

/* 圆形发送按钮 */
.send-btn {
  height: 48px;
  padding: 0 28px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.5px;
  background: linear-gradient(135deg, #6dbf8a, #4c9a6b);
  border: none;
  box-shadow: 0 4px 14px rgba(77, 166, 100, 0.4);
  transition: all 0.2s ease;
}

.send-btn:hover {
  transform: scale(1.03);
  box-shadow: 0 6px 18px rgba(77, 166, 100, 0.5);
}

/* ===== 滚动条美化 ===== */
:deep(.el-scrollbar__wrap) {
  overflow-x: hidden;
}

:deep(.el-scrollbar__bar.is-vertical) {
  width: 6px;
}

:deep(.el-scrollbar__thumb) {
  background-color: #c8e0cf;
  border-radius: 3px;
}

/* ===== 背景装饰 (淡淡药草叶子) ===== */
.chat-main::before {
  content: '';
  position: absolute;
  top: 20px;
  right: 20px;
  width: 260px;
  height: 260px;
  background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 200 200'%3E%3Cpath fill='%238FC89A' opacity='0.08' d='M124.5,-62.2C146.1,-31.5,138.9,6.8,117.6,38.9C96.3,71.1,60.8,97.1,23.6,102.2C-13.5,107.4,-52.3,91.6,-79.1,63.7C-105.9,35.8,-120.8,-4.2,-108.5,-37.6C-96.2,-71,-56.8,-97.8,-19.3,-89.1C18.2,-80.5,102.9,-92.9,124.5,-62.2Z' transform='translate(100 100)'/%3E%3C/svg%3E") no-repeat;
  background-size: contain;
  pointer-events: none;
  z-index: 0;
}
</style>