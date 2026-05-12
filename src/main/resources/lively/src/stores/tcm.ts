import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Question, AnswerRecord, QuizMode, SubjectKey, QuizStats, SubjectStats } from '@/declaration/tcm'
import { shuffleArray } from '@/data/tcm-questions'

const API_PREFIX = '/tcm/public/question'

export const useTcmStore = defineStore('tcm', () => {
  // ========== 题库 ==========
  const allQuestions = ref<Question[]>([])
  const loading = ref(false)
  const loadError = ref(false)
  const errorMessage = ref('')

  /**
   * 从后端 API 加载题库
   * 完全依赖后台数据库，不再降级到本地内置题库
   */
  async function loadQuestions(): Promise<void> {
    if (allQuestions.value.length > 0) return
    loading.value = true
    loadError.value = false
    errorMessage.value = ''
    try {
      const response = await fetch(`${API_PREFIX}/findAll`)
      if (!response.ok) throw new Error(`HTTP ${response.status}`)
      const data = await response.json()
      // 将后端数据转换为前端 Question 格式
      allQuestions.value = convertFromBackend(data)
      console.log(`[TCM] 从后端加载了 ${allQuestions.value.length} 道题目`)
      if (allQuestions.value.length === 0) {
        loadError.value = true
        errorMessage.value = '题库暂无题目，请联系管理员在后台添加题目'
      }
    } catch (e) {
      console.warn('[TCM] 后端加载失败:', e)
      allQuestions.value = []
      loadError.value = true
      errorMessage.value = '题库加载失败，请检查网络连接或联系管理员'
    } finally {
      loading.value = false
    }
  }

  /**
   * 强制刷新题库（管理后台修改题目后调用）
   */
  async function refreshQuestions(): Promise<void> {
    allQuestions.value = []
    await loadQuestions()
  }

  /**
   * 将后端 TcmQuestion 实体转换为前端 Question 格式
   */
  function convertFromBackend(data: Array<Record<string, unknown>>): Question[] {
    return data.map((item, index) => {
      let options: string[] = []
      let answer: number[] = []

      // 解析 options - 处理后端返回的非标准JSON格式
      try {
        if (typeof item.options === 'string') {
          const opts = item.options as string
          // 尝试标准JSON解析
          if (opts.startsWith('[') && opts.includes('"')) {
            options = JSON.parse(opts)
          } else if (opts.startsWith('[') && opts.endsWith(']')) {
            // 处理格式: [选项A, 选项B, 选项C] -> 提取内容
            const content = opts.slice(1, -1)
            // 按逗号分割，但要注意中文逗号
            options = content.split(/,\s*/).map(s => s.trim()).filter(s => s)
          } else {
            options = []
          }
        } else {
          options = (item.options as string[]) || []
        }
      } catch { options = [] }

      // 解析 answer - 处理后端返回的非标准JSON格式
      try {
        if (typeof item.answer === 'string') {
          const ans = item.answer as string
          // 尝试标准JSON解析
          if (ans.startsWith('[') && ans.includes(',')) {
            answer = JSON.parse(ans)
          } else if (ans.startsWith('[') && ans.endsWith(']')) {
            // 处理格式: [0] 或 [0, 1]
            const content = ans.slice(1, -1)
            answer = content.split(/,\s*/).map(s => parseInt(s.trim())).filter(n => !isNaN(n))
          } else {
            answer = []
          }
        } else {
          answer = (item.answer as number[]) || []
        }
      } catch { answer = [] }

      return {
        id: item.id ?? index + 1,
        type: (item.type as Question['type']) || 'single',
        subject: (item.subject as SubjectKey) || 'basic',
        question: item.question || '',
        options,
        answer,
        explanation: item.explanation || '',
        keyPoint: item.keyPoint || '',
        difficulty: (item.difficulty as 1 | 2 | 3) || 1
      }
    })
  }

  function getQuestionsBySubject(subject: SubjectKey | 'all'): Question[] {
    if (subject === 'all') return allQuestions.value
    return allQuestions.value.filter(q => q.subject === subject)
  }

  // ========== 收藏 ==========
  const favoriteIds = ref<number[]>([])

  function toggleFavorite(id: number) {
    const idx = favoriteIds.value.indexOf(id)
    if (idx > -1) {
      favoriteIds.value.splice(idx, 1)
    } else {
      favoriteIds.value.push(id)
    }
  }

  function isFavorite(id: number): boolean {
    return favoriteIds.value.includes(id)
  }

  const favoriteQuestions = computed(() =>
    allQuestions.value.filter(q => favoriteIds.value.includes(q.id))
  )

  // ========== 错题本 ==========
  const wrongQuestionIds = ref<number[]>([])

  function addToWrongBook(id: number) {
    if (!wrongQuestionIds.value.includes(id)) {
      wrongQuestionIds.value.push(id)
    }
  }

  function removeFromWrongBook(id: number) {
    const idx = wrongQuestionIds.value.indexOf(id)
    if (idx > -1) {
      wrongQuestionIds.value.splice(idx, 1)
    }
  }

  const wrongQuestions = computed(() =>
    allQuestions.value.filter(q => wrongQuestionIds.value.includes(q.id))
  )

  // ========== 刷题会话 ==========
  const sessionMode = ref<QuizMode>('sequential')
  const sessionSubject = ref<SubjectKey | 'all'>('all')
  const sessionQuestions = ref<Question[]>([])
  const currentIndex = ref(0)
  const selectedOptions = ref<number[]>([])
  const showAnswer = ref(false)
  const sessionAnswers = ref<AnswerRecord[]>([])
  const sessionStartTime = ref(0)
  const isSessionActive = ref(false)

  const currentQuestion = computed<Question | null>(() => {
    return sessionQuestions.value[currentIndex.value] || null
  })

  const sessionProgress = computed(() => {
    if (sessionQuestions.value.length === 0) return 0
    return ((currentIndex.value + 1) / sessionQuestions.value.length) * 100
  })

  const sessionCorrectCount = computed(() =>
    sessionAnswers.value.filter(a => a.isCorrect).length
  )

  const sessionAccuracy = computed(() => {
    if (sessionAnswers.value.length === 0) return 0
    return (sessionCorrectCount.value / sessionAnswers.value.length) * 100
  })

  function startSession(mode: QuizMode, subject: SubjectKey | 'all') {
    sessionMode.value = mode
    sessionSubject.value = subject
    let pool: Question[]

    switch (mode) {
      case 'wrong':
        pool = wrongQuestions.value.length > 0 ? [...wrongQuestions.value] : allQuestions.value
        break
      case 'favorite':
        pool = favoriteQuestions.value.length > 0 ? [...favoriteQuestions.value] : allQuestions.value
        break
      case 'random':
        pool = shuffleArray(getQuestionsBySubject(subject))
        break
      case 'sequential':
      default:
        pool = getQuestionsBySubject(subject)
        break
    }

    sessionQuestions.value = pool
    currentIndex.value = 0
    selectedOptions.value = []
    showAnswer.value = false
    sessionAnswers.value = []
    sessionStartTime.value = Date.now()
    isSessionActive.value = true
  }

  function selectOption(index: number) {
    if (showAnswer.value) return
    const q = currentQuestion.value
    if (!q) return

    if (q.type === 'single' || q.type === 'judge') {
      selectedOptions.value = [index]
    } else {
      const idx = selectedOptions.value.indexOf(index)
      if (idx > -1) {
        selectedOptions.value.splice(idx, 1)
      } else {
        selectedOptions.value.push(index)
      }
    }
  }

  function submitAnswer() {
    const q = currentQuestion.value
    if (!q || selectedOptions.value.length === 0) return false

    showAnswer.value = true
    const isCorrect =
      selectedOptions.value.length === q.answer.length &&
      selectedOptions.value.every(o => q.answer.includes(o))

    sessionAnswers.value.push({
      questionId: q.id,
      userAnswer: [...selectedOptions.value],
      isCorrect,
      timestamp: Date.now()
    })

    if (!isCorrect) {
      addToWrongBook(q.id)
    }

    totalStats.value.totalAnswered++
    totalStats.value.totalCorrect += isCorrect ? 1 : 0
    const today = new Date().toISOString().slice(0, 10)
    if (totalStats.value.lastStudyDate !== today) {
      totalStats.value.todayAnswered = 0
      totalStats.value.todayCorrect = 0
      totalStats.value.lastStudyDate = today
    }
    totalStats.value.todayAnswered++
    totalStats.value.todayCorrect += isCorrect ? 1 : 0
    if (isCorrect) {
      totalStats.value.streak++
    } else {
      totalStats.value.streak = 0
    }

    return isCorrect
  }

  function nextQuestion() {
    if (currentIndex.value < sessionQuestions.value.length - 1) {
      currentIndex.value++
      selectedOptions.value = []
      showAnswer.value = false
    }
  }

  function prevQuestion() {
    if (currentIndex.value > 0) {
      currentIndex.value--
      selectedOptions.value = []
      showAnswer.value = false
    }
  }

  function goToQuestion(index: number) {
    currentIndex.value = index
    selectedOptions.value = []
    showAnswer.value = false
  }

  function endSession() {
    isSessionActive.value = false
  }

  // ========== 统计 ==========
  const totalStats = ref<QuizStats>({
    totalAnswered: 0,
    totalCorrect: 0,
    todayAnswered: 0,
    todayCorrect: 0,
    streak: 0,
    lastStudyDate: ''
  })

  const overallAccuracy = computed(() => {
    if (totalStats.value.totalAnswered === 0) return 0
    return (totalStats.value.totalCorrect / totalStats.value.totalAnswered) * 100
  })

  const todayAccuracy = computed(() => {
    if (totalStats.value.todayAnswered === 0) return 0
    return (totalStats.value.todayCorrect / totalStats.value.todayAnswered) * 100
  })

  const subjectStatsList = computed<SubjectStats[]>(() => {
    const subjects: SubjectKey[] = ['basic', 'diagnostics', 'herbology', 'prescriptions', 'acupuncture']
    return subjects.map(s => {
      const subjectAnswers = sessionAnswers.value.filter(a => {
        const q = allQuestions.value.find(qq => qq.id === a.questionId)
        return q && q.subject === s
      })
      const total = subjectAnswers.length
      const correct = subjectAnswers.filter(a => a.isCorrect).length
      return {
        subject: s,
        total,
        correct,
        accuracy: total > 0 ? (correct / total) * 100 : 0
      }
    })
  })

  return {
    allQuestions,
    loading,
    loadError,
    errorMessage,
    loadQuestions,
    refreshQuestions,
    getQuestionsBySubject,
    favoriteIds,
    favoriteQuestions,
    toggleFavorite,
    isFavorite,
    wrongQuestionIds,
    wrongQuestions,
    addToWrongBook,
    removeFromWrongBook,
    sessionMode,
    sessionSubject,
    sessionQuestions,
    currentIndex,
    selectedOptions,
    showAnswer,
    sessionAnswers,
    sessionStartTime,
    isSessionActive,
    currentQuestion,
    sessionProgress,
    sessionCorrectCount,
    sessionAccuracy,
    startSession,
    selectOption,
    submitAnswer,
    nextQuestion,
    prevQuestion,
    goToQuestion,
    endSession,
    totalStats,
    overallAccuracy,
    todayAccuracy,
    subjectStatsList
  }
}, {
  persist: {
    key: 'tcm-quiz-store',
    pick: ['favoriteIds', 'wrongQuestionIds', 'totalStats']
  }
})
