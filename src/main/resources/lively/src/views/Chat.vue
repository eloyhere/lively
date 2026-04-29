<template>
  <ElContainer style="height: 100vh; width: 100vw">
    <ElHeader>
      <Toolbar />
    </ElHeader>
    <ElContainer style="height: calc(100vh - 60px); width: 100vw">
      <ElMain style="height: calc(100vh - 60px); width: 100vw; padding: 0">
        <ElScrollbar ref="scroll">
          <ElTimeline>
            <ElTimelineItem v-for="(message, index) in messages" :key="message.id" :timestamp="useDateFormat(message.spawn)">
              <div style="width: fit-content; height: fit-content; display: flex; flex-direction: column">
                <ElIcon v-if="index % 2 === 0">
                  <UserFilled/>
                </ElIcon>
                <ElIcon v-if="index % 2 !== 0">
                  <Monitor/>
                </ElIcon>
                <Message :content="message.content">

                </Message>
                <p>{{ message.role }} {{ useDateFormat(message.spawn) }}</p>
              </div>
            </ElTimelineItem>
          </ElTimeline>
        </ElScrollbar>
      </ElMain>
      <ElFooter height="auto">
        <div class="input-area">
          <el-input v-model="inputText" type="textarea" :rows="2" placeholder="输入消息... (Ctrl+Enter 发送)" :autosize="{minRows: 10, maxRows: 10}"
                    @keyup.ctrl.enter="sendMessage" :disabled="sending" />
          <el-button type="primary" @click="sendMessage" :loading="sending">发送</el-button>
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
  ElTimeline,
  ElTimelineItem,
  ElCard,
  ElInput,
  ElButton,
} from 'element-plus'
import Toolbar from '@/component/Toolbar.vue'
import { useDateFormat } from '@/hooks/datetime'
import {Monitor, UserFilled} from "@element-plus/icons-vue";
import Message from "@/component/Message.vue";

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

// 当前流式消息对应的助手消息 ID
const currentAssistantMsgId = ref<number | null>(null)
// 用于中断请求的 AbortController
let abortController: AbortController | null = null

// 滚动到底部（放在每次内容更新后调用）
const scrollToBottom = async () => {
  await nextTick()
  if (scroll.value?.wrapRef) {
    scroll.value.wrapRef.scrollTop = scroll.value.wrapRef.scrollHeight
  }
}

// 取消当前流式请求，并移除占位的助手消息
const cancelCurrentStream = () => {
  if (abortController) {
    abortController.abort()
    abortController = null
  }
  // 移除最后一条未完成的助手消息
  if (currentAssistantMsgId.value !== null) {
    const idx = messages.findIndex((m) => m.id === currentAssistantMsgId.value)
    if (idx !== -1) {
      messages.splice(idx, 1)
    }
    currentAssistantMsgId.value = null
  }
  sending.value = false
}

// 发送消息
const sendMessage = async () => {
  const content = inputText.value.trim()
  if (!content) return

  // 如果正在发送，先取消上一个流
  if (sending.value) {
    cancelCurrentStream()
  }

  // 添加用户消息
  const userMsg: Message = {
    id: Date.now(),
    role: 'user',
    content,
    spawn: new Date(),
  }
  messages.push(userMsg)
  inputText.value = ''
  await scrollToBottom()

  // 创建助手消息占位
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

  // 创建 AbortController 用于后续取消
  abortController = new AbortController()

  try {
    // ========== 核心修改：直接用 fetch 读取流 ==========
    const response = await fetch('http://localhost:8080/chat/message', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      // 发送消息内容（根据你的后端接口可能需要调整，这里只传当前消息）
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
      // 最后一行可能不完整，留在 buffer 中
      buffer = lines.pop() || ''

      for (const line of lines) {
        if (!line.startsWith('data: ')) continue
        const data = line.slice(6).trim()
        if (data === '[DONE]') continue

        // 如果当前助手消息已被取消（例如发送了新消息），则停止更新
        const idx = messages.findIndex(
            (m) => m.id === currentAssistantMsgId.value
        )
        if (idx === -1) return

        try {
          const json = JSON.parse(data)
          const delta = json.choices?.[0]?.delta?.content
          if (delta) {
            messages[idx].content += delta
            scrollToBottom()
          }
        } catch {
          // 非 JSON 数据直接追加
          messages[idx].content += data
          scrollToBottom()
        }
      }
    }
  } catch (error: any) {
    if (error?.name === 'AbortError') {
      // 请求被主动中断，不需要错误提示
      return
    }
    console.error('Stream error:', error)
    const idx = messages.findIndex(
        (m) => m.id === currentAssistantMsgId.value
    )
    if (idx !== -1) {
      messages[idx].content = '抱歉，服务暂时不可用，请稍后重试。'
    }
  } finally {
    sending.value = false
    currentAssistantMsgId.value = null
    abortController = null
  }
}

// 组件卸载时取消请求
onBeforeUnmount(() => {
  cancelCurrentStream()
})
</script>

<style scoped>
.input-area {
  display: flex;
  gap: 12px;
  padding: 16px;
  background: white;
  border-top: 1px solid #e4e7ed;
}
.el-textarea {
  flex: 1;
}
</style>