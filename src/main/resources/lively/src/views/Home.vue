<template>
  <div class="home">
    <!-- 背景模糊层（庚效果） -->
    <div v-if="globalEffects.blurOverlay" class="blur-overlay"></div>

    <header class="home-header">
      <div class="nav-links">
        <el-link :icon="ChatDotRound" class="nav-item">聊天</el-link>
        <el-link :icon="Guide" class="nav-item">关卡</el-link>
        <el-link :icon="Reading" class="nav-item">书籍</el-link>
        <el-link :icon="EditPen" class="nav-item">答题</el-link>
        <el-link :icon="InfoFilled" class="nav-item">关于</el-link>
      </div>
      <div class="avatar-area">
        <el-badge :value="bookShelf.length" :hidden="bookShelf.length === 0" class="shelf-badge">
          <el-popover placement="bottom-end" :width="300" trigger="hover" :show-after="200" popper-class="bookshelf-popover">
            <template #reference>
              <el-button circle class="shelf-button">
                <el-icon :size="22"><Notebook /></el-icon>
              </el-button>
            </template>
            <div class="bookshelf-content">
              <h4 class="bookshelf-title">📚 已收集医书</h4>
              <div v-if="bookShelf.length === 0" class="empty-shelf">暂无书籍，点击散落的医书收集吧</div>
              <ul class="book-list">
                <li v-for="book in bookShelf" :key="book.id" class="book-item">
                  <el-icon class="book-icon"><Reading /></el-icon>
                  <div class="book-info">
                    <span class="book-name">{{ book.term }}</span>
                    <span class="book-desc">{{ book.description }}</span>
                  </div>
                </li>
              </ul>
            </div>
          </el-popover>
        </el-badge>
        <AvatarCard />
      </div>
    </header>

    <main class="main-area">
      <canvas
          ref="canvasRef"
          @click="handleCanvasClick"
          @mousemove="handleCanvasMouseMove"
          @mouseleave="clearHover"
          @mousedown="onPressStart"
          @mouseup="onPressEnd"
          @touchstart.prevent="onPressStart"
          @touchend="onPressEnd"
      ></canvas>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from "vue"
import {
  ChatDotRound,
  Guide,
  Reading,
  EditPen,
  InfoFilled,
  Notebook
} from "@element-plus/icons-vue"
import AvatarCard from "@/component/AvatarCard.vue"

const canvasRef = ref<HTMLCanvasElement | null>(null)

interface BookItem {
  id: number
  term: string
  description: string
}
const bookShelf = reactive<BookItem[]>([])

// ----- 常量 -----
const HEADER_HEIGHT = 80
const BOOK_EMOJI = "📖"
const BOOK_SIZE = 38
const CANVAS_PADDING = 10
const FLY_DURATION = 700
const WIND_DURATION = 1200
const WIND_MAX_DIST = 300
const WIND_MAX_PUSH = 45

const SHELF_MARGIN = 20
const SHELF_WIDTH = 130
const SHELF_HEIGHT = 90

// ----- 医书库 -----
const bookPool = [
  { id: 1, term: "黄帝内经", desc: "中医理论奠基之作" },
  { id: 2, term: "伤寒论", desc: "张仲景著，六经辨证" },
  { id: 3, term: "金匮要略", desc: "杂病论治专著" },
  { id: 4, term: "神农本草经", desc: "最早本草学专著" },
  { id: 5, term: "本草纲目", desc: "李时珍著，收药1892种" },
  { id: 6, term: "难经", desc: "扁鹊著，阐发脉学" },
  { id: 7, term: "温病条辨", desc: "吴鞠通著，三焦辨证" },
  { id: 8, term: "景岳全书", desc: "张介宾著，温补学派" },
  { id: 9, term: "针灸甲乙经", desc: "首部针灸学专著" },
  { id: 10, term: "肘后备急方", desc: "葛洪著，急救手册" },
  { id: 11, term: "千金要方", desc: "孙思邈著，医德方剂" },
  { id: 12, term: "外台秘要", desc: "集唐前方书大成" },
  { id: 13, term: "医宗金鉴", desc: "清代官修临床全书" },
  { id: 14, term: "濒湖脉学", desc: "李时珍27种脉象" },
  { id: 15, term: "汤头歌诀", desc: "方剂歌诀入门" },
  { id: 16, term: "药性赋", desc: "寒热温平四赋" },
  { id: 17, term: "医学心悟", desc: "八纲辨证纲领" },
  { id: 18, term: "傅青主女科", desc: "妇科经典" },
  { id: 19, term: "幼幼集成", desc: "儿科专著" },
  { id: 20, term: "审视瑶函", desc: "眼科大全" },
  { id: 21, term: "诸病源候论", desc: "隋代病源证候学" },
  { id: 22, term: "太平惠民和剂局方", desc: "宋代官修方书" },
  { id: 23, term: "小儿药证直诀", desc: "钱乙儿科专著" },
  { id: 24, term: "三因极一病证方论", desc: "三因学说奠基" },
  { id: 25, term: "脾胃论", desc: "李东垣补土派经典" },
  { id: 26, term: "格致余论", desc: "朱丹溪滋阴派著作" },
  { id: 27, term: "本草经集注", desc: "陶弘景整理注释" },
  { id: 28, term: "雷公炮炙论", desc: "中药炮制学专著" },
  { id: 29, term: "银海精微", desc: "眼科专书" },
  { id: 30, term: "伤寒明理论", desc: "成无己注解伤寒" }
]

// ========== 天干系统 ==========
interface Trunk {
  index: number
  sky: string
  ground: string
}
const trunks: Trunk[] = [
  { index: 0, sky: "⛈️", ground: "🌳" },
  { index: 1, sky: "💨", ground: "🌿" },
  { index: 2, sky: "☀️", ground: "🔥" },
  { index: 3, sky: "⭐", ground: "🪔" },
  { index: 4, sky: "🌈", ground: "⛰️" },
  { index: 5, sky: "☁️", ground: "🌾" },
  { index: 6, sky: "❄️", ground: "⚙️" },
  { index: 7, sky: "🌙", ground: "💎" },
  { index: 8, sky: "🌧️", ground: "🌊" },
  { index: 9, sky: "💧", ground: "⛲" }
]
const combine = (a: Trunk, b: Trunk): Trunk => {
  return trunks[(a.index + b.index) % trunks.length]!
}

// 地上物品
interface GroundItem {
  symbol: string
  x: number
  y: number
  trunkIndex: number
}
const groundItems: GroundItem[] = []

// 天上漂浮的天干符号
interface SkyItem {
  symbol: string
  x: number
  y: number
  baseY: number
  vx: number
  trunkIndex: number
}
const skyItems: SkyItem[] = []

// 天干掉落物
interface StemDrop {
  id: number
  trunkIndex: number
  x: number
  y: number
  vy: number
  landY: number
  landed: boolean
  landTime: number
  symbol: string
  effectsActive: boolean
}

let dropIdCounter = 0
let stemDrops: StemDrop[] = []

// 全局效果变量
const globalEffects = {
  shakeIntensity: 0,
  glowAll: false,
  glowTargetX: 0,
  glowTargetY: 0,
  glowActive: false,
  blurOverlay: false,
  greyAll: false,
  darkTarget: null as { x: number; y: number } | null
}

// 初始化天地
function initTrunkItems() {
  skyItems.length = 0
  groundItems.length = 0
  trunks.forEach((item) => {
    skyItems.push({
      symbol: item.sky,
      x: Math.random() * (canvasWidth - 50) + 25,
      y: Math.random() * 120 + 20,
      baseY: Math.random() * 60 + 30,
      vx: (Math.random() - 0.5) * 0.3,
      trunkIndex: item.index
    })
    let x, y;
    do {
      x = Math.random() * (canvasWidth - BOOK_SIZE * 2) + BOOK_SIZE
      y = Math.random() * (canvasHeight - BOOK_SIZE * 2) + BOOK_SIZE
    } while (isInsideShelf(x, y))
    groundItems.push({
      symbol: item.ground,
      x,
      y,
      trunkIndex: item.index
    })
  })
}

// ---------- 书本系统 ----------
interface Book {
  id: number
  name: string
  desc: string
  x: number
  y: number
  flying: boolean
  flyStartX: number
  flyStartY: number
  flyTargetX: number
  flyTargetY: number
  flyStartTime: number
  collected: boolean
}
const books: Book[] = []

let windActive = false
let windCenterX = 0
let windCenterY = 0
let windStartTime = 0
let windBooksBase: { book: Book | GroundItem | SkyItem; baseX: number; baseY: number; targetX: number; targetY: number }[] = []

let canvasWidth = window.innerWidth
let canvasHeight = window.innerHeight - HEADER_HEIGHT
let shelfX = canvasWidth - SHELF_WIDTH - SHELF_MARGIN
let shelfY = SHELF_MARGIN

let mouseX = 0
let mouseY = 0
let hoveredBook: Book | null = null

let rafId: number | null = null
let pageVisible = true

let isPressing = false
let pressX = 0
let pressY = 0
let pressStartTime = 0
const PRESS_THRESHOLD = 300
const MAX_FORCE_FACTOR = 2.5
const FORCE_RISE_DURATION = 2000

function easeOutIn(t: number): number {
  return t < 0.5 ? 0.5 * (1 - Math.pow(1 - (t * 2), 2)) : 0.5 + 0.5 * Math.pow((t - 0.5) * 2, 2)
}

// ------ 绘制 -------
function drawShelf(ctx: CanvasRenderingContext2D) {
  const x = shelfX, y = shelfY, w = SHELF_WIDTH, h = SHELF_HEIGHT
  ctx.shadowColor = "rgba(0,0,0,0.4)"
  ctx.shadowBlur = 8
  ctx.fillStyle = "#8B5A3C"
  ctx.fillRect(x, y, w, h)
  ctx.strokeStyle = "#5C3A21"
  ctx.lineWidth = 2
  ctx.strokeRect(x, y, w, h)
  ctx.strokeStyle = "#A77B55"
  ctx.lineWidth = 1.5
  for (let i = 1; i < 4; i++) {
    ctx.beginPath()
    ctx.moveTo(x, y + (h / 4) * i)
    ctx.lineTo(x + w, y + (h / 4) * i)
    ctx.stroke()
  }
  ctx.shadowColor = "transparent"
  ctx.shadowBlur = 0
  if (bookShelf.length > 0) {
    ctx.font = `${BOOK_SIZE}px "Segoe UI Emoji", "Apple Color Emoji", sans-serif`
    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
    ctx.fillStyle = "#000"
    ctx.fillText(BOOK_EMOJI, x + w / 2 - 6, y + h / 2 - 4)
    ctx.fillText(BOOK_EMOJI, x + w / 2 + 6, y + h / 2 + 4)
  }
}

function draw() {
  const canvas = canvasRef.value
  if (!canvas || !pageVisible) return
  const ctx = canvas.getContext("2d")
  if (!ctx) return

  ctx.clearRect(0, 0, canvasWidth, canvasHeight)
  drawShelf(ctx)

  const shakeX = globalEffects.shakeIntensity * (Math.sin(performance.now() * 0.05) * 2 - 1)
  const shakeY = globalEffects.shakeIntensity * (Math.cos(performance.now() * 0.05 + 1) * 2 - 1)

  ctx.font = `28px "Segoe UI Emoji", "Apple Color Emoji", sans-serif`
  ctx.textAlign = "center"
  ctx.textBaseline = "middle"
  for (const g of groundItems) {
    ctx.save()
    if (globalEffects.greyAll) ctx.filter = "grayscale(1)"
    if (globalEffects.glowAll) ctx.shadowColor = "rgba(255,255,200,0.9)"
    ctx.fillText(g.symbol, g.x + shakeX, g.y + shakeY)
    ctx.restore()
  }

  ctx.font = `${BOOK_SIZE}px "Segoe UI Emoji", "Apple Color Emoji", sans-serif`
  for (let i = books.length - 1; i >= 0; i--) {
    const b = books[i]
    if (b.flying) {
      const now = performance.now()
      const progress = Math.min((now - b.flyStartTime) / FLY_DURATION, 1)
      const eased = easeOutIn(progress)
      const curX = b.flyStartX + (b.flyTargetX - b.flyStartX) * eased
      const curY = b.flyStartY + (b.flyTargetY - b.flyStartY) * eased
      ctx.save()
      ctx.globalAlpha = 1 - eased * 0.5
      ctx.translate(curX, curY)
      ctx.scale(1 - eased * 0.4, 1 - eased * 0.4)
      ctx.fillText(BOOK_EMOJI, 0, 0)
      ctx.restore()
      if (progress >= 1) {
        if (!b.collected) { b.collected = true; addToShelf(b) }
        books.splice(i, 1)
      }
    } else {
      ctx.fillText(BOOK_EMOJI, b.x + shakeX, b.y + shakeY)
      if (hoveredBook === b) {
        ctx.save()
        ctx.globalAlpha = 0.2
        ctx.fillStyle = "#fff"
        ctx.beginPath()
        ctx.arc(b.x, b.y, BOOK_SIZE * 0.6, 0, Math.PI * 2)
        ctx.fill()
        ctx.restore()
      }
    }
  }

  ctx.font = `32px "Segoe UI Emoji", "Apple Color Emoji", sans-serif`
  for (const s of skyItems) {
    ctx.fillText(s.symbol, s.x + shakeX, s.y + shakeY)
  }

  if (hoveredBook && !hoveredBook.flying) {
    const b = hoveredBook
    const text = b.name
    ctx.font = `bold 16px "Microsoft YaHei", sans-serif`
    const textWidth = ctx.measureText(text).width
    const boxWidth = textWidth + 20
    const boxHeight = 28
    let drawX = b.x - boxWidth / 2
    let drawY = b.y - BOOK_SIZE * 0.8 - boxHeight
    if (drawX < 0) drawX = 5
    if (drawX + boxWidth > canvasWidth) drawX = canvasWidth - boxWidth - 5
    if (drawY < 0) drawY = b.y + 10
    ctx.save()
    ctx.fillStyle = "rgba(0,0,0,0.8)"
    ctx.strokeStyle = "#d88f3a"
    ctx.lineWidth = 1.5
    ctx.beginPath()
    ctx.roundRect(drawX, drawY, boxWidth, boxHeight, 6)
    ctx.fill()
    ctx.stroke()
    ctx.fillStyle = "#fff"
    ctx.font = `bold 14px "Microsoft YaHei", sans-serif`
    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
    ctx.fillText(text, drawX + boxWidth / 2, drawY + boxHeight / 2)
    ctx.restore()
  }

  ctx.font = `32px "Segoe UI Emoji", "Apple Color Emoji", sans-serif`
  for (const d of stemDrops) {
    let alpha = 1
    if (d.landed) {
      const elapsed = (performance.now() - d.landTime) / 1000
      if (elapsed > 3) continue
      alpha = 0.5 + Math.sin(elapsed * 10) * 0.3
    }
    ctx.save()
    ctx.globalAlpha = alpha
    ctx.fillText(d.symbol, d.x + shakeX, d.y + shakeY)
    ctx.restore()
  }
}

function addToShelf(book: Book) {
  if (!bookShelf.some(b => b.id === book.id)) {
    bookShelf.push({ id: book.id, term: book.name, description: book.desc })
  }
}

function startFlyToShelf(book: Book) {
  const targetX = shelfX + SHELF_WIDTH / 2
  const targetY = shelfY + SHELF_HEIGHT / 2
  book.flyStartX = book.x
  book.flyStartY = book.y
  book.flyTargetX = targetX
  book.flyTargetY = targetY
  book.flyStartTime = performance.now()
  book.flying = true
  if (hoveredBook === book) hoveredBook = null
}

function handleCanvasClick(e: MouseEvent) {}
function handleCanvasMouseMove(e: MouseEvent) {
  const canvas = canvasRef.value
  if (!canvas) return
  const rect = canvas.getBoundingClientRect()
  const scaleX = canvasWidth / rect.width
  const scaleY = canvasHeight / rect.height
  mouseX = (e.clientX - rect.left) * scaleX
  mouseY = (e.clientY - rect.top) * scaleY
  const hitRadius = BOOK_SIZE * 0.6
  hoveredBook = null
  for (let i = books.length - 1; i >= 0; i--) {
    const b = books[i]
    if (b.flying) continue
    if (Math.hypot(mouseX - b.x, mouseY - b.y) <= hitRadius) { hoveredBook = b; break }
  }
  if (isPressing) { pressX = mouseX; pressY = mouseY }
}
function clearHover() { hoveredBook = null }

function onPressStart(e: MouseEvent | TouchEvent) {
  isPressing = true
  pressStartTime = performance.now()
  const canvas = canvasRef.value!
  const rect = canvas.getBoundingClientRect()
  const sx = canvasWidth / rect.width, sy = canvasHeight / rect.height
  if (e instanceof MouseEvent) {
    pressX = (e.clientX - rect.left) * sx
    pressY = (e.clientY - rect.top) * sy
  } else {
    const touch = e.touches[0]!
    pressX = (touch.clientX - rect.left) * sx
    pressY = (touch.clientY - rect.top) * sy
  }
}
function onPressEnd() {
  if (!isPressing) return
  isPressing = false
  if (performance.now() - pressStartTime < PRESS_THRESHOLD) {
    if (hoveredBook && !hoveredBook.flying) startFlyToShelf(hoveredBook)
    else triggerWind(pressX, pressY)
  }
}

function triggerWind(cx: number, cy: number) {
  // ... (保持不变)
  if (books.length === 0 && groundItems.length === 0 && skyItems.length === 0) return
  windCenterX = cx; windCenterY = cy; windStartTime = performance.now(); windActive = true
  windBooksBase = []
  const pushItems = (items: any[], isSky = false) => {
    for (const item of items) {
      if (item.flying) continue
      const dx = item.x - cx, dy = item.y - cy
      const dist = Math.hypot(dx, dy)
      if (dist < 1) continue
      const factor = Math.max(0, 1 - dist / WIND_MAX_DIST)
      const push = WIND_MAX_PUSH * factor * (0.7 + Math.random() * 0.6)
      let tx = item.x + dx / dist * push, ty = item.y + dy / dist * push
      tx = Math.min(Math.max(tx, CANVAS_PADDING), canvasWidth - CANVAS_PADDING)
      ty = isSky ? Math.min(Math.max(ty, 10), 200) : Math.min(Math.max(ty, CANVAS_PADDING), canvasHeight - CANVAS_PADDING)
      if (!isSky && isInsideShelf(tx, ty)) { tx = item.x; ty = item.y }
      windBooksBase.push({ book: item, baseX: item.x, baseY: item.y, targetX: tx, targetY: ty })
    }
  }
  pushItems(books.filter(b => !b.flying), false)
  pushItems(groundItems, false)
  pushItems(skyItems, true)
}

function isInsideShelf(x: number, y: number) {
  const m = BOOK_SIZE * 0.8
  return x > shelfX - m && x < shelfX + SHELF_WIDTH + m && y > shelfY - m && y < shelfY + SHELF_HEIGHT + m
}

function updateWind(now: number) {
  // ... (保持不变)
  if (!windActive) return
  const p = Math.min((now - windStartTime) / WIND_DURATION, 1)
  const eased = easeOutIn(p)
  for (const { book, baseX, baseY, targetX, targetY } of windBooksBase) {
    if (book.flying) continue
    book.x = baseX + (targetX - baseX) * eased
    book.y = baseY + (targetY - baseY) * eased
  }
  if (p >= 1) { windActive = false; windBooksBase = [] }
}

function applyContinuousWind(now: number) {
  // ... (保持不变)
  if (!isPressing) return
  const duration = now - pressStartTime
  if (duration < PRESS_THRESHOLD) return
  const factor = Math.min(1 + (duration - PRESS_THRESHOLD) / FORCE_RISE_DURATION * (MAX_FORCE_FACTOR - 1), MAX_FORCE_FACTOR)
  const basePush = 0.4 * factor
  const push = (items: any[], isSky = false) => {
    for (const item of items) {
      if (item.flying) continue
      const dx = item.x - pressX, dy = item.y - pressY
      const dist = Math.hypot(dx, dy) || 1
      const factor = Math.max(0, 1 - dist / (WIND_MAX_DIST * 1.2))
      const move = basePush * factor
      let nx = item.x + dx / dist * move, ny = item.y + dy / dist * move
      if (isSky) ny = Math.min(Math.max(ny, 15), 220)
      else ny = Math.min(Math.max(ny, CANVAS_PADDING), canvasHeight - CANVAS_PADDING)
      nx = Math.min(Math.max(nx, CANVAS_PADDING), canvasWidth - CANVAS_PADDING)
      if (!isSky && isInsideShelf(nx, ny)) continue
      item.x = nx; item.y = ny
    }
  }
  push(books.filter(b => !b.flying), false)
  push(groundItems, false)
  push(skyItems, true)
}

function updateSkyDrift() {
  if (windActive || isPressing) return
  for (const s of skyItems) {
    s.x += s.vx
    if (s.x < 20 || s.x > canvasWidth - 20) { s.vx *= -1; s.x = Math.min(Math.max(s.x, 20), canvasWidth - 20) }
    s.y = s.baseY + Math.sin(performance.now() / 2000 + s.x) * 5
  }
}

// ========== 天干掉落与合成 ==========
function spawnStemDrop() {
  const idx = Math.floor(Math.random() * trunks.length)
  const drop: StemDrop = {
    id: dropIdCounter++,
    trunkIndex: idx,
    x: Math.random() * (canvasWidth - 40) + 20,
    y: -30,
    vy: 0,
    landY: Math.random() * (canvasHeight * 0.4) + canvasHeight * 0.2,
    landed: false,
    landTime: 0,
    symbol: trunks[idx]!.sky,
    effectsActive: true
  }
  stemDrops.push(drop)
}

function findNearestGroundItem(x: number, y: number, radius: number, targetIndex: number): { item: GroundItem; index: number } | null {
  let best: GroundItem | null = null
  let bestDist = Infinity
  let bestIdx = -1
  groundItems.forEach((g, i) => {
    if (g.trunkIndex !== targetIndex) return
    const d = Math.hypot(g.x - x, g.y - y)
    if (d < radius && d < bestDist) { bestDist = d; best = g; bestIdx = i }
  })
  return best ? { item: best, index: bestIdx } : null
}

// 落地合成逻辑（已移除戊和己的砸毁，戊/己不再强制消费，允许自然转化为地支）
function onLand(drop: StemDrop) {
  const landX = drop.x, landY = drop.y
  const RADIUS = 100
  let consumed = false
  switch (drop.trunkIndex) {
    case 0: { // 甲：与己合成
      const found = findNearestGroundItem(landX, landY, RADIUS, 5)
      if (found) {
        consumed = true
      }
      break
    }
    case 1: { // 乙：与庚合成辛
      const found = findNearestGroundItem(landX, landY, RADIUS, 6)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[6]!)
        groundItems.splice(found.index, 1)
        groundItems.push({ symbol: combined.ground, x: landX, y: landY, trunkIndex: combined.index })
        consumed = true
      }
      break
    }
    case 2: { // 丙：与辛合成癸
      const found = findNearestGroundItem(landX, landY, RADIUS, 7)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[7]!)
        groundItems.splice(found.index, 1)
        groundItems.push({ symbol: combined.ground, x: landX, y: landY, trunkIndex: combined.index })
        consumed = true
      }
      break
    }
    case 3: { // 丁：与壬合成乙
      const found = findNearestGroundItem(landX, landY, RADIUS, 8)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[8]!)
        groundItems.splice(found.index, 1)
        groundItems.push({ symbol: combined.ground, x: landX, y: landY, trunkIndex: combined.index })
        consumed = true
      }
      break
    }
    case 4: { // 戊：仅与癸合成丁，不砸毁，自身不消失
      const found = findNearestGroundItem(landX, landY, RADIUS, 9)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[9]!)
        groundItems.splice(found.index, 1)
        groundItems.push({ symbol: combined.ground, x: landX, y: landY, trunkIndex: combined.index })
        consumed = true
      }
      // 若无癸，则 consumed 保持 false，让戊自然闪烁后变为 ⛰️
      break
    }
    case 5: { // 己：仅与甲合成己（甲消失，己留在地面）
      const found = findNearestGroundItem(landX, landY, RADIUS, 0)
      if (found) {
        // 移除甲
        groundItems.splice(found.index, 1)
        // 己自己变成地面图标
        groundItems.push({ symbol: trunks[5]!.ground, x: landX, y: landY, trunkIndex: 5 })
        consumed = true
      }
      // 若无甲，则正常闪烁后变为 🌾
      break
    }
    case 6: { // 庚：与乙合成辛
      const found = findNearestGroundItem(landX, landY, RADIUS, 1)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[1]!)
        groundItems.splice(found.index, 1)
        groundItems.push({ symbol: combined.ground, x: landX, y: landY, trunkIndex: combined.index })
        consumed = true
      }
      break
    }
    case 7: { // 辛：与丙合成癸
      const found = findNearestGroundItem(landX, landY, RADIUS, 2)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[2]!)
        groundItems.splice(found.index, 1)
        groundItems.push({ symbol: combined.ground, x: landX, y: landY, trunkIndex: combined.index })
        consumed = true
      }
      break
    }
    case 8: { // 壬：与丁合成乙
      const found = findNearestGroundItem(landX, landY, RADIUS, 3)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[3]!)
        groundItems.splice(found.index, 1)
        groundItems.push({ symbol: combined.ground, x: landX, y: landY, trunkIndex: combined.index })
        consumed = true
      }
      break
    }
    case 9: { // 癸：与戊合成丁
      const found = findNearestGroundItem(landX, landY, RADIUS, 4)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[4]!)
        groundItems.splice(found.index, 1)
        groundItems.push({ symbol: combined.ground, x: landX, y: landY, trunkIndex: combined.index })
        consumed = true
      }
      break
    }
  }
  return consumed
}

function updateStemDrops(now: number) {
  for (let i = stemDrops.length - 1; i >= 0; i--) {
    const drop = stemDrops[i]!
    if (!drop.landed) {
      applyStemEffect(drop, now)
      drop.vy += 0.08
      drop.y += drop.vy
      if (drop.y >= drop.landY) {
        drop.y = drop.landY
        drop.landed = true
        drop.landTime = now
        const consumed = onLand(drop)
        if (consumed) {
          stemDrops.splice(i, 1)
          resetEffect(drop)
          continue
        }
      }
    } else {
      const elapsed = (now - drop.landTime) / 1000
      if (elapsed >= 3) {
        const trunk = trunks[drop.trunkIndex]!
        groundItems.push({ symbol: trunk.ground, x: drop.x, y: drop.y, trunkIndex: drop.trunkIndex })
        stemDrops.splice(i, 1)
        resetEffect(drop)
      }
    }
  }
}

function applyStemEffect(drop: StemDrop, now: number) {
  switch (drop.trunkIndex) {
    case 0: globalEffects.shakeIntensity = 3; break
    case 1: {
      for (const g of groundItems) {
        const dx = g.x - drop.x, dy = g.y - drop.y
        const dist = Math.hypot(dx, dy)
        if (dist < 100 && dist > 0) {
          g.x += dx / dist * 2
          g.y += dy / dist * 2
        }
      }
      break
    }
    case 2: globalEffects.glowAll = true; break
    case 3: {
      let nearest: GroundItem | null = null
      let nearestDist = Infinity
      for (const g of groundItems) {
        const d = Math.hypot(g.x - drop.x, g.y - drop.y)
        if (d < nearestDist) { nearestDist = d; nearest = g }
      }
      if (nearest && nearestDist < 150) {
        globalEffects.glowActive = true
        globalEffects.glowTargetX = nearest.x
        globalEffects.glowTargetY = nearest.y
      }
      break
    }
    case 4: {
      for (const g of groundItems) {
        if (Math.hypot(g.x - drop.x, g.y - drop.y) < 80) {
          g.x += (Math.random() - 0.5) * 3
          g.y += (Math.random() - 0.5) * 3
        }
      }
      break
    }
    case 5: {
      for (const g of groundItems) {
        const dist = Math.hypot(g.x - drop.x, g.y - drop.y)
        if (dist < 100) {
          g.x += (drop.x - g.x) * 0.05
          g.y += (drop.y - g.y) * 0.05
        }
      }
      break
    }
    case 6: globalEffects.blurOverlay = true; break
    case 7: globalEffects.greyAll = true; break
    case 8: {
      for (const g of groundItems) {
        const dx = g.x - drop.x, dy = g.y - drop.y
        const dist = Math.hypot(dx, dy)
        if (dist < 120 && dist > 0) {
          g.x += dx / dist * 3
          g.y += dy / dist * 3
        }
      }
      break
    }
    case 9: {
      let closest: GroundItem | null = null
      let closestDist = Infinity
      for (const g of groundItems) {
        const d = Math.hypot(g.x - drop.x, g.y - drop.y)
        if (d < closestDist) { closestDist = d; closest = g }
      }
      if (closest && closestDist < 120) {
        globalEffects.darkTarget = { x: closest.x, y: closest.y }
      }
      break
    }
  }
}

function resetEffect(drop: StemDrop) {
  switch (drop.trunkIndex) {
    case 0: globalEffects.shakeIntensity = 0; break
    case 2: globalEffects.glowAll = false; break
    case 3: globalEffects.glowActive = false; break
    case 6: globalEffects.blurOverlay = false; break
    case 7: globalEffects.greyAll = false; break
    case 9: globalEffects.darkTarget = null; break
  }
}

// =================== 主循环 ===================
function mainLoop(now: number) {
  if (!pageVisible) return
  updateStemDrops(now)
  updateWind(now)
  applyContinuousWind(now)
  updateSkyDrift()
  draw()
  rafId = requestAnimationFrame(mainLoop)
}

function scatterBooks() {
  // ... (保持不变)
  const count = Math.floor(Math.random() * 11) + 15
  const shuffled = [...bookPool].sort(() => Math.random() - 0.5)
  const selected = shuffled.slice(0, count)
  selected.forEach(b => {
    let x, y
    do {
      x = Math.random() * (canvasWidth - BOOK_SIZE * 2) + BOOK_SIZE
      y = Math.random() * (canvasHeight - BOOK_SIZE * 2) + BOOK_SIZE
    } while (isInsideShelf(x, y))
    books.push({
      id: b.id,
      name: b.term,
      desc: b.desc,
      x, y,
      flying: false,
      flyStartX: 0, flyStartY: 0, flyTargetX: 0, flyTargetY: 0,
      flyStartTime: 0,
      collected: false
    })
  })
}

function updateShelfPosition() {
  shelfX = canvasWidth - SHELF_WIDTH - SHELF_MARGIN
  shelfY = SHELF_MARGIN
}

function handleResize() {
  // ... (保持不变)
  const ow = canvasWidth, oh = canvasHeight
  canvasWidth = window.innerWidth
  canvasHeight = window.innerHeight - HEADER_HEIGHT
  updateShelfPosition()
  ;[...books, ...groundItems].forEach((b: any) => {
    b.x = (b.x / ow) * canvasWidth
    b.y = (b.y / oh) * canvasHeight
    b.x = Math.min(Math.max(b.x, CANVAS_PADDING), canvasWidth - CANVAS_PADDING)
    b.y = Math.min(Math.max(b.y, CANVAS_PADDING), canvasHeight - CANVAS_PADDING)
    if (isInsideShelf(b.x, b.y)) {
      if (b.x > shelfX && b.x < shelfX + SHELF_WIDTH) b.x = shelfX - BOOK_SIZE
      if (b.y > shelfY && b.y < shelfY + SHELF_HEIGHT) b.y = shelfY - BOOK_SIZE
    }
  })
  skyItems.forEach(s => {
    s.x = (s.x / ow) * canvasWidth
    s.y = Math.min(Math.max((s.y / oh) * canvasHeight, 15), 220)
    s.x = Math.min(Math.max(s.x, 20), canvasWidth - 20)
  })
  const canvas = canvasRef.value
  if (canvas) { canvas.width = canvasWidth; canvas.height = canvasHeight }
}

function handleVisibilityChange() {
  pageVisible = !document.hidden
  if (pageVisible && !rafId) rafId = requestAnimationFrame(mainLoop)
  else if (!pageVisible && rafId) { cancelAnimationFrame(rafId); rafId = null }
}

function cleanUp() {
  if (rafId) cancelAnimationFrame(rafId)
  if (stemInterval) clearInterval(stemInterval)
  books.length = 0
  stemDrops.length = 0
  skyItems.length = 0
  groundItems.length = 0
}

let stemInterval: number | null = null

onMounted(() => {
  document.addEventListener("visibilitychange", handleVisibilityChange)
  const canvas = canvasRef.value
  if (canvas) {
    canvas.width = canvasWidth
    canvas.height = canvasHeight
  }
  updateShelfPosition()
  scatterBooks()
  initTrunkItems()
  stemInterval = window.setInterval(spawnStemDrop, 3000)
  rafId = requestAnimationFrame(mainLoop)
  window.addEventListener("resize", handleResize)
})

onUnmounted(() => {
  document.removeEventListener("visibilitychange", handleVisibilityChange)
  cleanUp()
  window.removeEventListener("resize", handleResize)
})
</script>

<style scoped>
.home {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  position: relative;
  display: flex;
  flex-direction: column;
  background: radial-gradient(circle at 20% 30%, #ccb699, #8b5a3c 80%);
}
.blur-overlay {
  position: absolute;
  inset: 0;
  z-index: 5;
  backdrop-filter: blur(4px);
  pointer-events: none;
}
.home-header {
  height: 80px;
  background: rgba(90, 55, 35, 0.65);
  backdrop-filter: blur(12px);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 2rem;
  border-bottom: 1px solid rgba(255,255,255,0.15);
  z-index: 10;
  flex-shrink: 0;
}
.nav-links { display: flex; gap: 2rem; }
.nav-item {
  font-size: 1.15rem; color: #f5e6d3; transition: color 0.3s; align-items: center; gap: 0.4rem; text-decoration: none;
}
.nav-item:hover { color: #ffd58c; text-shadow: 0 0 12px rgba(255,213,140,0.7); }
.avatar-area { display: flex; align-items: center; gap: 1rem; }
.shelf-button {
  background: rgba(255,230,180,0.2); border: 1px solid rgba(255,255,255,0.3); color: #ffe9b0; transition: all 0.3s;
}
.shelf-button:hover { background: rgba(255,230,180,0.4); border-color: #ffd58c; box-shadow: 0 0 15px rgba(255,213,140,0.5); }
.shelf-badge :deep(.el-badge__content) { background: #d88f3a; border: none; }
.main-area { flex: 1; position: relative; overflow: hidden; }
.main-area canvas { position: absolute; top: 0; left: 0; width: 100%; height: 100%; }

canvas {
  will-change: transform;
  transform: translateZ(0);
}
</style>

<style>
.bookshelf-popover {
  background: rgba(60,40,25,0.95) !important; border: 1px solid rgba(255,255,255,0.2) !important;
  backdrop-filter: blur(10px); border-radius: 12px !important;
}
.bookshelf-content { color: #f5e6d3; }
.bookshelf-title { margin: 0 0 12px; font-size: 1rem; color: #ffd58c; }
.empty-shelf { text-align: center; color: rgba(255,255,255,0.5); font-size: 0.9rem; padding: 1rem; }
.book-list { list-style: none; padding: 0; margin: 0; max-height: 300px; overflow-y: auto; }
.book-item { display: flex; align-items: flex-start; gap: 10px; padding: 10px 12px; border-radius: 6px; transition: background 0.2s; }
.book-item:hover { background: rgba(255,255,255,0.1); }
.book-icon { color: #ffd58c; flex-shrink: 0; margin-top: 2px; }
.book-info { display: flex; flex-direction: column; gap: 2px; }
.book-name { font-size: 0.95rem; color: #f5e6d3; }
.book-desc { font-size: 0.8rem; color: rgba(255,255,255,0.5); }
</style>