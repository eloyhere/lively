/** 题目类型 */
export type QuestionType = 'single' | 'multiple' | 'judge'

/** 科目枚举 */
export type SubjectKey = 'basic' | 'diagnostics' | 'herbology' | 'prescriptions' | 'acupuncture'

/** 科目信息 */
export interface SubjectInfo {
  key: SubjectKey
  name: string
  icon: string
  color: string
  description: string
}

/** 题目 */
export interface Question {
  id: number
  type: QuestionType
  subject: SubjectKey
  question: string
  options: string[]
  answer: number[]
  explanation: string
  keyPoint: string
  difficulty: 1 | 2 | 3
}

/** 答题记录 */
export interface AnswerRecord {
  questionId: number
  userAnswer: number[]
  isCorrect: boolean
  timestamp: number
}

/** 刷题模式 */
export type QuizMode = 'sequential' | 'random' | 'wrong' | 'favorite'

/** 刷题会话 */
export interface QuizSession {
  mode: QuizMode
  subject: SubjectKey | 'all'
  questionIds: number[]
  currentIndex: number
  answers: AnswerRecord[]
  startTime: number
}

/** 统计数据 */
export interface QuizStats {
  totalAnswered: number
  totalCorrect: number
  todayAnswered: number
  todayCorrect: number
  streak: number
  lastStudyDate: string
}

/** 科目统计 */
export interface SubjectStats {
  subject: SubjectKey
  total: number
  correct: number
  accuracy: number
}

/** 所有科目定义 */
export const SUBJECTS: SubjectInfo[] = [
  {
    key: 'basic',
    name: '中医基础理论',
    icon: '📖',
    color: '#8B5CF6',
    description: '阴阳五行、脏腑经络、气血津液、病因病机'
  },
  {
    key: 'diagnostics',
    name: '中医诊断学',
    icon: '🔍',
    color: '#EC4899',
    description: '四诊合参、八纲辨证、脏腑辨证'
  },
  {
    key: 'herbology',
    name: '中药学',
    icon: '🌿',
    color: '#10B981',
    description: '中药性味归经、功效主治、配伍禁忌'
  },
  {
    key: 'prescriptions',
    name: '方剂学',
    icon: '💊',
    color: '#F59E0B',
    description: '方剂组成、功效主治、配伍意义'
  },
  {
    key: 'acupuncture',
    name: '针灸学',
    icon: '📍',
    color: '#3B82F6',
    description: '经络腧穴、刺灸方法、针灸治疗'
  }
]

/** 题目类型文本映射 */
export const QUESTION_TYPE_LABELS: Record<QuestionType, string> = {
  single: '单选题',
  multiple: '多选题',
  judge: '判断题'
}

/** 难度文本映射 */
export const DIFFICULTY_LABELS: Record<number, string> = {
  1: '基础',
  2: '中等',
  3: '困难'
}

/** 难度颜色映射 */
export const DIFFICULTY_COLORS: Record<number, string> = {
  1: '#10B981',
  2: '#F59E0B',
  3: '#EF4444'
}
