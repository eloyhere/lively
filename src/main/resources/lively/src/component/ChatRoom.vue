<template>
  <ElContainer style="width: 100%; height: 100%; overflow: hidden">
    <!-- 消息列表区域 -->
    <ElMain class="chat-main">
      <ElScrollbar ref="scrollbarRef" class="message-scrollbar">
        <div class="message-list">
          <Message
              v-for="msg in messages"
              :key="msg.id"
              :model="msg"
              @mouseenter="handleMessageHover(msg)"
          />
        </div>
      </ElScrollbar>
    </ElMain>

    <!-- 底部输入区域 -->
    <ElFooter height="180px" class="chat-footer">
      <div class="input-container">
        <ElInput
            v-model="inputText"
            type="textarea"
            :rows="3"
            placeholder="输入消息... (Ctrl+Enter 发送)"
            @keyup.ctrl.enter="sendMessage"
            :disabled="!isConnected"
        />
        <div class="action-buttons">
          <ElButton
              type="primary"
              @click="sendMessage"
              :loading="sending"
              :disabled="!isConnected || !inputText.trim()"
          >
            发送
          </ElButton>
          <ElButton @click="clearInput" :disabled="!inputText">清空</ElButton>
        </div>
      </div>
    </ElFooter>
  </ElContainer>
</template>

<script setup lang="ts">
import { ref, watch, nextTick, onUnmounted, onMounted } from 'vue'
import { ElContainer, ElMain, ElFooter, ElScrollbar, ElInput, ElButton } from 'element-plus'
import { useWebSocket } from '@vueuse/core'
import Message from './Message.vue'

const model = defineModel<string>({ required: true })

interface ChatMessage {
  id: string
  role: 'user' | 'assistant'
  content: string
  src?: string
  createdAt?: Date
}

const messages = ref<ChatMessage[]>([])
const inputText = ref('')
const sending = ref(false)
const scrollbarRef = ref<InstanceType<typeof ElScrollbar>>()
const isConnected = ref(false)

// 手动控制连接，关闭自动重连
const { status, send, open, close } = useWebSocket('ws://localhost:8080/websocket/chat', {
  immediate: false,      // 不自动连接
  autoReconnect: false,  // 禁止自动重连
  onMessage: (ws, event) => {
    try {
      const data = JSON.parse(event.data)
      handleWebSocketMessage(data)
    } catch (e) {
      console.error('解析消息失败', e)
    }
  },
  onError: (ws, event) => {
    console.error('WebSocket 错误', event)
    isConnected.value = false
  },
  onClose: () => {
    console.log('WebSocket 关闭')
    isConnected.value = false
  },
  onOpen: () => {
    console.log('WebSocket 连接成功')
    isConnected.value = true
    // 连接成功后，如果当前有激活的对话，发送切换命令
    if (model.value) {
      send(JSON.stringify({ action: 'switch', chatId: model.value }))
    }
  }
})

// 建立连接（只在组件挂载时尝试一次）
const connect = () => {
  if (status.value !== 'OPEN' && status.value !== 'CONNECTING') {
    open()
  }
}

onMounted(() => {
  connect()
})

// 处理服务端消息
const handleWebSocketMessage = (data: any) => {
  switch (data.type) {
    case 'history':
      if (data.messages && Array.isArray(data.messages)) {
        messages.value = data.messages.map((m: any) => ({
          ...m,
          createdAt: new Date(m.createdAt),
          src: m.role === 'assistant' ? '/ai-avatar.png' : undefined
        }))
        scrollToBottom()
      }
      break
    case 'message':
      const newMsg: ChatMessage = {
        id: data.id,
        role: data.role,
        content: data.content,
        createdAt: new Date(data.createdAt),
        src: data.role === 'assistant' ? '/ai-avatar.png' : undefined
      }
      messages.value.push(newMsg)
      scrollToBottom()
      break
    case 'switched':
      // 切换成功，无需额外动作
      break
    case 'created':
      if (data.chatId) {
        model.value = data.chatId
      }
      break
    case 'error':
      console.error('服务端错误:', data.message)
      break
    default:
      console.log('未处理的消息类型:', data)
  }
}

// 发送消息
const sendMessage = async () => {
  const content = inputText.value.trim()
  if (!content || !isConnected.value) return

  sending.value = true
  const tempId = `temp-${Date.now()}`
  const userMsg: ChatMessage = {
    id: tempId,
    role: 'user',
    content: content,
    createdAt: new Date()
  }
  messages.value.push(userMsg)
  scrollToBottom()
  inputText.value = ''

  try {
    send(JSON.stringify({ action: 'message', content }))
  } catch (err) {
    console.error('发送失败', err)
    messages.value = messages.value.filter(m => m.id !== tempId)
  } finally {
    sending.value = false
  }
}

const clearInput = () => {
  inputText.value = ''
}

const scrollToBottom = async () => {
  await nextTick()
  if (scrollbarRef.value) {
    const wrapElement = scrollbarRef.value.wrapRef
    if (wrapElement) {
      wrapElement.scrollTop = wrapElement.scrollHeight
    }
  }
}

// 监听对话 ID 变化，切换对话（仅当连接已建立）
watch(() => model.value, (newChatId) => {
  if (newChatId && isConnected.value) {
    messages.value = []
    send(JSON.stringify({ action: 'switch', chatId: newChatId }))
    scrollToBottom()
  }
})

const handleMessageHover = (msg: ChatMessage) => {
  // 可选交互
  console.log('hover message', msg.id)
}

// 组件卸载时关闭连接
onUnmounted(() => {
  close()
})
</script>

<style scoped>
.chat-main {
  background-color: #f5f7fa;
  padding: 16px;
  overflow: hidden;
}
.message-scrollbar {
  height: 100%;
}
.message-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 8px;
}
.chat-footer {
  background-color: white;
  border-top: 1px solid #e4e7ed;
  padding: 12px 20px;
}
.input-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
  height: 100%;
}
.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
</style>