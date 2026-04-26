<template>
  <!-- Canvas 背景层 -->
  <canvas ref="bgCanvas" class="bg-canvas"></canvas>

  <ElScrollbar height="100vh" class="splash-scrollbar">
    <div class="splash-container">
      <!-- 板块 1：主视觉 -->
      <section class="sticky-section section-hero">
        <div class="hero-content">
          <img src="/smile.png" alt="附子" class="fuzi-main" />
          <h1 class="hero-title">中医药来啦！</h1>
          <p class="hero-subtitle">与 <span class="highlight">附子</span> 一同学习中医</p>
          <p class="hero-desc">
            一个由人工智能驱动的游戏化平台，引领您的中医学习之旅。从阴阳五行到临床辨证，附子将陪伴你每一步。
          </p>
          <el-button type="primary" size="large" round class="cta-button" @click="handleStart">
            开启探索
            <el-icon class="btn-icon"><Promotion /></el-icon>
          </el-button>
          <div class="scroll-hint">
            <el-icon :size="28" class="bounce"><ArrowDown /></el-icon>
          </div>
        </div>
      </section>

      <!-- 板块 2：游戏化教学 -->
      <section class="sticky-section section-gamified">
        <div class="content-card">
          <el-icon :size="72" class="card-icon"><Trophy /></el-icon>
          <h2>游戏化教学</h2>
          <p>
            在 Lively 中，学习中医就像一场冒险。完成每日任务积累经验值，解锁稀有成就徽章，挑战限时问答赢取积分。
            你还可以在排行榜上与全球中医爱好者一较高下，让枯燥的典籍记忆变成充满乐趣的闯关之旅。
          </p>
          <div class="tag-group">
            <el-tag type="warning" effect="dark" round>任务</el-tag>
            <el-tag type="danger" effect="dark" round>成就</el-tag>
            <el-tag type="success" effect="dark" round>排行榜</el-tag>
          </div>
        </div>
      </section>

      <!-- 板块 3：AI 辅导 -->
      <section class="sticky-section section-ai">
        <div class="content-card">
          <el-icon :size="72" class="card-icon"><ChatDotRound /></el-icon>
          <h2>人工智能辅导</h2>
          <p>
            搭载深度学习算法，我们的 AI 助手会实时分析你的答题速度与正确率，精准定位薄弱环节。
            它会为你生成个性化的记忆口诀、易混淆药材对比表，并按照艾宾浩斯遗忘曲线推送复习提醒，
            就像拥有一位24小时在线的中医私人教师。
          </p>
          <el-link type="primary" class="demo-link">
            试试演示 <el-icon><ArrowRight /></el-icon>
          </el-link>
        </div>
      </section>

      <!-- 板块 4：个性化路径 -->
      <section class="sticky-section section-path">
        <div class="content-card">
          <el-icon :size="72" class="card-icon"><MagicStick /></el-icon>
          <h2>个性化学习路径</h2>
          <p>
            无论你是备战执业医师考试，还是单纯热爱传统医学，Lively 都能为你规划专属课程。
            系统会根据你的学习目标、当前水平和可用时间，动态调整章节顺序与难度，
            并自动记录你已掌握的每一味药材、每一个方剂，生成可视化成长报告。
          </p>
          <div class="progress-bars">
            <span class="bar-label">中药学</span>
            <el-progress :percentage="78" :stroke-width="8" status="success" />
            <span class="bar-label">诊断学</span>
            <el-progress :percentage="52" :stroke-width="8" status="warning" />
          </div>
        </div>
      </section>

      <!-- 板块 5：附子 · Fuzi -->
      <section class="sticky-section section-fuzi">
        <div class="content-card fuzi-card">
          <img src="/smile.png" alt="附子" class="fuzi-round" />
          <h2>附子，捣蛋兔</h2>
          <p class="fuzi-lore">
            名字来源于经典中药 <strong>附子</strong>。它是一只黑发、箭尾的捣蛋鬼，头上顶着一片小叶子，
            总是眨着狡黠的大眼睛。它的设计融合了传统中医的厚重与现代科技感的灵动，
            核心使命是 <em>“为学习增添乐趣”</em>。
          </p>
          <p class="fuzi-quote">
            “保持好奇，保持趣味——即使在背诵上百个方剂时也不例外。”
          </p>
          <el-button type="warning" plain round @click="handleStart">遇见附子</el-button>
        </div>
      </section>

      <!-- 板块 6：行动号召 -->
      <section class="sticky-section section-cta">
        <div class="content-card cta-card">
          <h2>准备好开始您的旅程了吗？</h2>
          <p>加入成千上万正在革新中医学习方式的探索者吧。附子已经迫不及待想带你入门了。</p>
          <el-button type="primary" size="large" round class="cta-button" @click="handleStart">
            立即加入 Lively
            <el-icon class="btn-icon"><ArrowRight /></el-icon>
          </el-button>
          <el-link type="info" >了解更多关于我们的方法论</el-link>
        </div>
      </section>
    </div>
  </ElScrollbar>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue'
import { ElScrollbar } from 'element-plus'
import {
  Trophy,
  ChatDotRound,
  MagicStick,
  Promotion,
  ArrowDown,
  ArrowRight
} from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const handleStart = () => {
  router.push({ path: '/home', replace: true })
}

// ==================== Canvas 绘制与动画 ====================
let canvas: HTMLCanvasElement | null = null
let ctx: CanvasRenderingContext2D | null = null
let w = window.innerWidth
let h = window.innerHeight
let animFrameId: number | null = null

interface Star {
  x: number
  y: number
  vx: number
  vy: number
  size: number
  currentSize: number
  emoji: string
}
const stars: Star[] = []
const STAR_COUNT = 30
const starEmojis = ['⭐', '🌟', '✨', '💫', '🌠']

interface Gan {
  x: number
  y: number
  vx: number
  vy: number
  size: number
  currentSize: number
  emoji: string
}
const gans: Gan[] = []
const GAN_COUNT = 40
const ganEmojiPool = [
  '⚡', '🌳', '💨', '🌿', '🔥', '☀️', '💡', '⭐', '🌅', '⛰️',
  '☁️', '🪨', '❄️', '🪙', '🌙', '💎', '💧', '🌊', '🌧️', '⛲'
]

let sunMoonAngle = 0
const orbitCenterX = 120
const orbitCenterY = 120
const orbitRadius = 55

let isPressing = false
let pressX = 0
let pressY = 0
let pressStartTime = 0
const PRESS_THRESHOLD = 300
const MAX_FORCE_FACTOR = 2.5
const FORCE_RISE_DURATION = 2000
const BASE_PUSH = 0.4

function initStars() {
  stars.length = 0
  for (let i = 0; i < STAR_COUNT; i++) {
    const size = 18 + Math.random() * 18
    stars.push({
      x: Math.random() * w,
      y: Math.random() * h,
      vx: (Math.random() - 0.5) * 0.6,
      vy: (Math.random() - 0.5) * 0.6,
      size,
      currentSize: size,
      emoji: starEmojis[Math.floor(Math.random() * starEmojis.length)]
    })
  }
}

function initGans() {
  gans.length = 0
  for (let i = 0; i < GAN_COUNT; i++) {
    const size = 20 + Math.random() * 18
    gans.push({
      x: Math.random() * w,
      y: Math.random() * h,
      vx: (Math.random() - 0.5) * 0.5,
      vy: (Math.random() - 0.5) * 0.5,
      size,
      currentSize: size,
      emoji: ganEmojiPool[Math.floor(Math.random() * ganEmojiPool.length)]
    })
  }
}

function applyContinuousWind(now: number) {
  if (!isPressing) return
  const pressDuration = now - pressStartTime
  if (pressDuration < PRESS_THRESHOLD) return

  const extraTime = pressDuration - PRESS_THRESHOLD
  const forceFactor = Math.min(1 + (extraTime / FORCE_RISE_DURATION) * (MAX_FORCE_FACTOR - 1), MAX_FORCE_FACTOR)
  const basePush = BASE_PUSH * forceFactor

  const pushItem = (item: Star | Gan) => {
    const dx = item.x - pressX
    const dy = item.y - pressY
    const dist = Math.sqrt(dx * dx + dy * dy) || 1
    const dirX = dx / dist
    const dirY = dy / dist
    const factor = Math.max(0, 1 - dist / 400)
    const push = basePush * factor
    let newX = item.x + dirX * push
    let newY = item.y + dirY * push
    newY = Math.min(Math.max(newY, 10), h - 10)
    newX = Math.min(Math.max(newX, 10), w - 10)
    item.x = newX
    item.y = newY
  }

  stars.forEach(s => pushItem(s))
  gans.forEach(g => pushItem(g))
}

function draw() {
  if (!ctx || !canvas) return
  ctx.clearRect(0, 0, w, h)

  const now = performance.now()
  const flicker = Math.sin(now * 0.005) * 0.15 + 0.85

  for (const s of stars) {
    s.currentSize = s.size + Math.sin(now * 0.003 + s.x * 0.01) * 3
    ctx.save()
    ctx.font = `${s.currentSize}px "Segoe UI Emoji", "Apple Color Emoji", sans-serif`
    ctx.textAlign = 'center'
    ctx.textBaseline = 'middle'
    ctx.shadowColor = `rgba(255,215,100,${0.5 + flicker * 0.5})`
    ctx.shadowBlur = 6 + flicker * 4
    ctx.fillText(s.emoji, s.x, s.y)
    ctx.restore()
  }

  for (const g of gans) {
    g.currentSize = g.size + Math.sin(now * 0.003 + g.y * 0.01) * 2.5
    ctx.save()
    ctx.font = `${g.currentSize}px "Segoe UI Emoji", "Apple Color Emoji", sans-serif`
    ctx.textAlign = 'center'
    ctx.textBaseline = 'middle'
    ctx.shadowColor = `rgba(255,220,140,${0.4 + flicker * 0.5})`
    ctx.shadowBlur = 5 + flicker * 3
    ctx.fillText(g.emoji, g.x, g.y)
    ctx.restore()
  }

  const drawCelestial = (emoji: string, angle: number, fontSize: number) => {
    const x = orbitCenterX + orbitRadius * Math.cos(angle)
    const y = orbitCenterY + orbitRadius * Math.sin(angle)
    ctx!.save()
    ctx!.font = `${fontSize}px "Segoe UI Emoji", "Apple Color Emoji", sans-serif`
    ctx!.textAlign = 'center'
    ctx!.textBaseline = 'middle'
    ctx!.shadowColor = emoji === '☀️' ? 'rgba(255,200,100,0.9)' : 'rgba(180,200,255,0.8)'
    ctx!.shadowBlur = 10 + flicker * 5
    ctx!.fillText(emoji, x, y)
    ctx!.restore()
  }
  drawCelestial('☀️', sunMoonAngle, 36)
  drawCelestial('🌙', sunMoonAngle + Math.PI, 30)
}

function updatePositions() {
  stars.forEach(s => {
    s.x += s.vx
    s.y += s.vy
    if (s.x < -20) s.x = w + 20
    if (s.x > w + 20) s.x = -20
    if (s.y < -20) s.y = h + 20
    if (s.y > h + 20) s.y = -20
  })
  gans.forEach(g => {
    g.x += g.vx
    g.y += g.vy
    if (g.x < -20) g.x = w + 20
    if (g.x > w + 20) g.x = -20
    if (g.y < -20) g.y = h + 20
    if (g.y > h + 20) g.y = -20
  })
  sunMoonAngle += 0.005
}

function onMouseDown(e: MouseEvent) {
  isPressing = true
  pressX = e.clientX
  pressY = e.clientY
  pressStartTime = performance.now()
}
function onTouchStart(e: TouchEvent) {
  e.preventDefault()
  if (e.touches.length > 0) {
    isPressing = true
    pressX = e.touches[0].clientX
    pressY = e.touches[0].clientY
    pressStartTime = performance.now()
  }
}
function onMouseUp() { isPressing = false }
function onTouchEnd() { isPressing = false }
function onMouseMove(e: MouseEvent) {
  if (isPressing) { pressX = e.clientX; pressY = e.clientY }
}
function onTouchMove(e: TouchEvent) {
  if (isPressing && e.touches.length > 0) {
    pressX = e.touches[0].clientX
    pressY = e.touches[0].clientY
  }
}

function animate(now: number) {
  updatePositions()
  applyContinuousWind(now)
  draw()
  animFrameId = requestAnimationFrame(animate)
}

function resizeCanvas() {
  w = window.innerWidth
  h = window.innerHeight
  if (canvas) { canvas.width = w; canvas.height = h }
  const clamp = (items: any[]) => {
    items.forEach(item => {
      item.x = Math.min(Math.max(item.x, 10), w - 10)
      item.y = Math.min(Math.max(item.y, 10), h - 10)
    })
  }
  clamp(stars)
  clamp(gans)
}

onMounted(() => {
  canvas = document.querySelector('.bg-canvas') as HTMLCanvasElement
  if (canvas) {
    ctx = canvas.getContext('2d')
    canvas.width = w
    canvas.height = h
  }
  initStars()
  initGans()
  animFrameId = requestAnimationFrame(animate)

  window.addEventListener('mousedown', onMouseDown)
  window.addEventListener('touchstart', onTouchStart, { passive: false })
  window.addEventListener('mouseup', onMouseUp)
  window.addEventListener('touchend', onTouchEnd)
  window.addEventListener('mousemove', onMouseMove)
  window.addEventListener('touchmove', onTouchMove, { passive: false })
  window.addEventListener('resize', resizeCanvas)
})

onUnmounted(() => {
  if (animFrameId) cancelAnimationFrame(animFrameId)
  window.removeEventListener('mousedown', onMouseDown)
  window.removeEventListener('touchstart', onTouchStart)
  window.removeEventListener('mouseup', onMouseUp)
  window.removeEventListener('touchend', onTouchEnd)
  window.removeEventListener('mousemove', onMouseMove)
  window.removeEventListener('touchmove', onTouchMove)
  window.removeEventListener('resize', resizeCanvas)
})
</script>

<style scoped>
/* Canvas 背景层 */
.bg-canvas {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: -1;
  pointer-events: none;
}

/* 主内容容器 */
.splash-container {
  position: relative;
  min-height: 100vh;
  background: transparent;
  font-family: 'Inter', 'Helvetica Neue', sans-serif;
}

/* 滚动容器吸附修复 */
:deep(.el-scrollbar__wrap) {
  scroll-snap-type: y mandatory;
  scroll-snap-stop: always;
  overscroll-behavior: contain;
}

/* sticky 板块 */
.sticky-section {
  position: sticky;
  top: 0;
  height: 100vh;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  scroll-snap-align: start;
  color: #fff;
  overflow: hidden;
  background: transparent;
}

/* 内容卡片 */
.content-card {
  max-width: 680px;
  width: 100%;
  aspect-ratio: 16 / 9;
  text-align: center;
  padding: 2rem;
  background: rgba(45, 25, 15, 0.55);
  backdrop-filter: blur(14px);
  border-radius: 2rem;
  border: 1px solid rgba(255, 255, 255, 0.15);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.5);
  animation: floatUp 0.8s ease-out;
}

.hero-content {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  animation: floatUp 1s ease;
}
.fuzi-main {
  width: 400px;
  height: auto;
  margin-bottom: 0.5rem;
  filter: drop-shadow(0 0 20px rgba(252, 195, 85, 0.7));
  animation: float 3s ease-in-out infinite;
}
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-12px); }
}
.hero-title {
  font-size: 4.5rem;
  font-weight: 800;
  background: linear-gradient(135deg, #fdd97c, #eba45e);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0;
}
.hero-subtitle { font-size: 1.8rem; margin: 0.3rem 0 0.5rem; }
.highlight { color: #ffb86c; font-weight: 700; text-shadow: 0 0 12px rgba(255, 184, 108, 0.6); }
.hero-desc {
  color: #efe2c6;
  max-width: 450px;
  background: rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 1rem 1.5rem;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.3);
}
.cta-button {
  margin-top: 1.5rem;
  padding: 0.8rem 2.5rem;
  font-size: 1.2rem;
  font-weight: 600;
  background: linear-gradient(135deg, #b37b3e, #d5944a);
  border: none;
  transition: all 0.3s ease;
}
.cta-button:hover { transform: scale(1.05); box-shadow: 0 8px 24px rgba(199, 132, 60, 0.6); }
.btn-icon { margin-left: 0.4rem; vertical-align: middle; }
.scroll-hint { position: absolute; bottom: 2rem; color: rgba(255,255,255,0.5); animation: bounce 1.6s infinite; }
.bounce { animation: bounce 1.6s infinite; }
@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(8px); }
}
.card-icon { color: #ffd166; margin-bottom: 1rem; filter: drop-shadow(0 0 12px rgba(255,209,102,0.5)); }
h2 {
  font-size: 2.2rem;
  margin: 0.5rem 0;
  background: linear-gradient(to right, #fef7e0, #e0c9a6);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
p {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #f3e7d3;
  margin: 1rem 0;
  text-align: justify;
  text-align-last: center;
}
.tag-group { display: flex; gap: 0.8rem; justify-content: center; margin-top: 1.2rem; }
.progress-bars { margin-top: 1.2rem; text-align: left; width: 80%; margin-left: auto; margin-right: auto; }
.bar-label { font-size: 0.9rem; color: #f5e7d0; margin: 0.5rem 0 0.2rem; display: block; }
.fuzi-card { background: rgba(70, 30, 30, 0.55); }
.fuzi-round {
  width: 100px; border-radius: 50%;
  border: 3px solid #f4b860;
  box-shadow: 0 0 25px #f4a460;
  margin-bottom: 1rem;
  animation: float 4s ease-in-out infinite;
}
.fuzi-lore { font-size: 1rem; line-height: 1.7; }
.fuzi-quote { font-style: italic; color: #f7deb8; font-size: 0.95rem; }
.cta-card h2 { font-size: 2.5rem; }
.demo-link { margin-top: 0.8rem; font-size: 1rem; }
@keyframes floatUp {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}
@media (max-width: 768px) {
  .hero-title { font-size: 3rem; }
  h2 { font-size: 1.8rem; }
  .content-card { padding: 1.5rem; margin: 0 1rem; }
}
</style>