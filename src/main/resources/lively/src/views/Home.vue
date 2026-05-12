<template>
  <div class="home">
    <!-- 背景模糊层（庚效果） -->
    <div v-if="globalEffects.blurOverlay" class="blur-overlay">
      <div class="blur-layer-1"></div>
      <div class="blur-layer-2"></div>
      <div class="blur-layer-3"></div>
      <div class="fog-noise"></div>
    </div>

    <header class="home-header">
      <div class="nav-links">
        <el-link icon="ChatDotRound" href="/chat" class="nav-item">聊天</el-link>
        <el-link icon="Guide" class="nav-item">关卡</el-link>
        <el-link icon="Reading" class="nav-item">书籍</el-link>
        <el-link icon="EditPen" class="nav-item" @click="goPractises">答题</el-link>
        <el-link icon="InfoFilled" class="nav-item">关于</el-link>
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
import { useRouter } from "vue-router"
import {
  ChatDotRound,
  Guide,
  Reading,
  EditPen,
  InfoFilled,
  Notebook
} from "@element-plus/icons-vue"
import AvatarCard from "@/component/AvatarCard.vue"
import { authenticationStore } from "@/stores/authentication.ts"

const router = useRouter()

function goPractises() {
  const auth = authenticationStore()
  if (auth.authenticated) {
    router.push("/practises")
  } else {
    router.push("/authentication/account")
  }
}

const canvasRef = ref<HTMLCanvasElement | null>(null)

// ----- 类型定义 -----
interface BookItem {
  id: number
  term: string
  description: string
}
const bookShelf = reactive<BookItem[]>([])

interface BookSource {
  id: number
  term: string
  desc: string
}

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

interface Trunk {
  index: number
  sky: string
  ground: string
}

interface GroundItem {
  symbol: string
  x: number
  y: number
  trunkIndex: number
  vx: number
  vy: number
  baseX: number
  baseY: number
  spawnTime: number
}

interface SkyItem {
  symbol: string
  x: number
  y: number
  baseY: number
  vx: number
  trunkIndex: number
}

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

// ----- 常量 -----
const HEADER_HEIGHT = 80
const CANVAS_PADDING = 10
const FLY_DURATION = 700
const WIND_DURATION = 1200
const WIND_MAX_DIST = 300
const WIND_MAX_PUSH = 45
const SHELF_MARGIN = 20
const SHELF_WIDTH = 130
const SHELF_HEIGHT = 90

// 书本文字样式
const BOOK_FONT = 'bold 17px "KaiTi", "楷体", "STKaiti", "SimSun", "宋体", serif'
const BOOK_COLOR = "#3e2723"
const BOOK_SHADOW = "rgba(0,0,0,0.3)"
const BOOK_OUTLINE_COLOR = "#f5e6d3"
const MIN_BOOK_SPACING = 60

// 用于计算文字宽度（缓存）
let tempCtx: CanvasRenderingContext2D | null = null

// ----- 医书库（大幅扩充至60本） -----
const bookPool: BookSource[] = [
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
  { id: 30, term: "伤寒明理论", desc: "成无己注解伤寒" },
  { id: 31, term: "针灸大成", desc: "杨继洲集针灸之大成" },
  { id: 32, term: "类经", desc: "张介宾类编《内经》" },
  { id: 33, term: "张氏医通", desc: "张璐著，杂病专书" },
  { id: 34, term: "本草备要", desc: "汪昂著，本草入门" },
  { id: 35, term: "医林改错", desc: "王清任解剖革新" },
  { id: 36, term: "温热经纬", desc: "王士雄温病学集成" },
  { id: 37, term: "理虚元鉴", desc: "虚劳专著" },
  { id: 38, term: "疡医大全", desc: "外科巨著" },
  { id: 39, term: "济阴纲目", desc: "武之望妇科精要" },
  { id: 40, term: "幼科发挥", desc: "万全儿科经验" },
  { id: 41, term: "目经大成", desc: "黄庭镜眼科书" },
  { id: 42, term: "喉科指掌", desc: "喉科专著" },
  { id: 43, term: "正体类要", desc: "伤科名著" },
  { id: 44, term: "仙授理伤续断秘方", desc: "现存最早伤科书" },
  { id: 45, term: "经效产宝", desc: "现存最早产科书" },
  { id: 46, term: "颅囟经", desc: "儿科古籍" },
  { id: 47, term: "食医心鉴", desc: "食疗专书" },
  { id: 48, term: "饮膳正要", desc: "营养学专著" },
  { id: 49, term: "十四经发挥", desc: "滑寿经络学" },
  { id: 50, term: "奇经八脉考", desc: "李时珍奇经专著" },
  { id: 51, term: "本草拾遗", desc: "陈藏器补本草" },
  { id: 52, term: "海药本草", desc: "李珣著海外药" },
  { id: 53, term: "滇南本草", desc: "兰茂地方本草" },
  { id: 54, term: "晶珠本草", desc: "藏药经典" },
  { id: 55, term: "植物名实图考", desc: "吴其濬植物药图" },
  { id: 56, term: "存存斋医话稿", desc: "赵晴初医话" },
  { id: 57, term: "冷庐医话", desc: "陆以湉医话" },
  { id: 58, term: "对山医话", desc: "毛对山医话" },
  { id: 59, term: "客尘医话", desc: "计楠医话" },
  { id: 60, term: "柳洲医话", desc: "魏之琇医话" },
  { id: 61, term: "黄帝针灸甲乙经", desc: "皇甫谧撰，现存最早针灸学专著" },
  { id: 62, term: "脉经", desc: "王叔和著，脉学规范之作" },
  { id: 63, term: "刘涓子鬼遗方", desc: "现存最早外科方书" },
  { id: 64, term: "集验方", desc: "姚僧垣经验方集" },
  { id: 65, term: "新修本草", desc: "世界第一部药典" },
  { id: 66, term: "海上方", desc: "孙思邈单验方集" },
  { id: 67, term: "博济方", desc: "王衮验方汇编" },
  { id: 68, term: "苏沈良方", desc: "苏轼、沈括经验方" },
  { id: 69, term: "史载之方", desc: "史堪方的临证记录" },
  { id: 70, term: "鸡峰普济方", desc: "张锐著方剂巨著" },
  { id: 71, term: "圣济总录", desc: "宋徽宗敕编医学百科全书" },
  { id: 72, term: "普济本事方", desc: "许叔微验方集" },
  { id: 73, term: "仁斋直指方", desc: "杨士瀛著，内外妇儿兼收" },
  { id: 74, term: "世医得效方", desc: "危亦林五世家传方" },
  { id: 75, term: "玉机微义", desc: "徐用诚著，集明前医学精粹" },
  { id: 76, term: "证治要诀", desc: "戴思恭临床心得" },
  { id: 77, term: "万病回春", desc: "龚廷贤综合临证全书" },
  { id: 78, term: "寿世保元", desc: "龚廷贤养生与治疗兼备" },
  { id: 79, term: "明医杂著", desc: "王纶临床随笔" },
  { id: 80, term: "古今医鉴", desc: "龚信辑古今名医经验" }
];

// ========== 天干系统 ==========
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

// 地面、天空、掉落物
const groundItems: GroundItem[] = []
const skyItems: SkyItem[] = []
let dropIdCounter = 0
let stemDrops: StemDrop[] = []

// 天干图标总数限制
const MAX_TRUNK_ITEMS = 100

// 全局效果
const globalEffects = reactive({
  shakeIntensity: 0,
  glowAll: false,
  glowTargetX: 0,
  glowTargetY: 0,
  glowActive: false,
  blurOverlay: false,
  greyAll: false,
  darkTarget: null as { x: number; y: number } | null
})

// 初始化天地符号
function initTrunkItems() {
  skyItems.length = 0
  groundItems.length = 0
  const now = performance.now()
  trunks.forEach((item) => {
    skyItems.push({
      symbol: item.sky,
      x: Math.random() * (canvasWidth - 50) + 25,
      y: Math.random() * 120 + 20,
      baseY: Math.random() * 60 + 30,
      vx: (Math.random() - 0.5) * 0.3,
      trunkIndex: item.index
    })
    let x: number, y: number
    do {
      x = Math.random() * (canvasWidth - 60) + 30
      y = Math.random() * (canvasHeight - 60) + 30
    } while (isInsideShelf(x, y))
    groundItems.push({
      symbol: item.ground,
      x,
      y,
      trunkIndex: item.index,
      vx: (Math.random() - 0.5) * 0.2,
      vy: (Math.random() - 0.5) * 0.15,
      baseX: x,
      baseY: y,
      spawnTime: now
    })
  })
}

// ---------- 书本系统 ----------
const books: Book[] = []

let windActive = false
let windCenterX = 0, windCenterY = 0, windStartTime = 0
let windBooksBase: { book: Book | GroundItem | SkyItem; baseX: number; baseY: number; targetX: number; targetY: number }[] = []

let canvasWidth = window.innerWidth
let canvasHeight = window.innerHeight - HEADER_HEIGHT
let shelfX = canvasWidth - SHELF_WIDTH - SHELF_MARGIN
let shelfY = SHELF_MARGIN

let mouseX = 0, mouseY = 0
let hoveredBook: Book | null = null

let rafId: number | null = null
let pageVisible = true
let isPressing = false
let pressX = 0, pressY = 0, pressStartTime = 0
const PRESS_THRESHOLD = 300
const MAX_FORCE_FACTOR = 2.5
const FORCE_RISE_DURATION = 2000

// 缓动函数
function easeOutIn(t: number): number {
  return t < 0.5 ? 0.5 * (1 - Math.pow(1 - (t * 2), 2)) : 0.5 + 0.5 * Math.pow((t - 0.5) * 2, 2)
}

// 获取书本文字宽度
function getBookTextWidth(ctx: CanvasRenderingContext2D, name: string): number {
  ctx.font = BOOK_FONT
  return ctx.measureText(`《${name}》`).width
}

// 绘制摊开书形状（棱角分明版）
function drawOpenBookShape(ctx: CanvasRenderingContext2D, x: number, y: number) {
  const width = 80
  const height = 55
  const pageWidth = (width - 4) / 2
  
  ctx.beginPath()
  
  ctx.moveTo(x - width / 2 + 8, y - height / 2 + 5)
  ctx.lineTo(x - pageWidth - 2, y - height / 2)
  ctx.lineTo(x - 2, y - height / 2 + height * 0.35)
  ctx.lineTo(x - 2, y + height / 2 - 10)
  
  ctx.quadraticCurveTo(x - pageWidth / 2, y + height / 2 + 8, x - width / 2, y + height / 2)
  ctx.lineTo(x - width / 2 + 8, y + height / 2 - 5)
  ctx.lineTo(x - width / 2 + 12, y - height / 2 + 5)
  ctx.closePath()
  
  ctx.moveTo(x + 2, y - height / 2 + height * 0.35)
  ctx.lineTo(x + pageWidth + 2, y - height / 2)
  ctx.lineTo(x + width / 2 - 8, y - height / 2 + 5)
  ctx.lineTo(x + width / 2 - 12, y + height / 2 - 5)
  ctx.lineTo(x + width / 2, y + height / 2)
  
  ctx.quadraticCurveTo(x + pageWidth / 2, y + height / 2 + 8, x + 2, y + height / 2 - 10)
  ctx.closePath()
}

// 绘制书本文字
function drawBookText(ctx: CanvasRenderingContext2D, x: number, y: number, name: string, alpha = 1) {
  ctx.save()
  ctx.globalAlpha = alpha
  ctx.font = BOOK_FONT
  ctx.textAlign = "center"
  ctx.textBaseline = "middle"
  ctx.shadowColor = BOOK_SHADOW
  ctx.shadowBlur = 2
  ctx.fillStyle = BOOK_COLOR
  ctx.fillText(`《${name}》`, x, y)
  ctx.restore()
}

// 碰撞检测半径（基于文字宽度的一半）
function getBookHitRadius(ctx: CanvasRenderingContext2D | null, book: Book): number {
  if (!ctx) return 40 // 默认值
  const w = getBookTextWidth(ctx, book.name)
  return Math.max(w / 2 + 5, 30)
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
    ctx.font = "bold 12px 'KaiTi', serif"
    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
    ctx.fillStyle = "#f5e6d3"
    ctx.fillText(`已存${bookShelf.length}本`, x + w / 2, y + h / 2)
  }
}

function draw() {
  const canvas = canvasRef.value
  if (!canvas || !pageVisible) return
  const ctx = canvas.getContext("2d")
  if (!ctx) return
  tempCtx = ctx

  ctx.clearRect(0, 0, canvasWidth, canvasHeight)
  drawShelf(ctx)

  const shakeX = globalEffects.shakeIntensity * (Math.sin(performance.now() * 0.05) * 2 - 1)
  const shakeY = globalEffects.shakeIntensity * (Math.cos(performance.now() * 0.05 + 1) * 2 - 1)

  // 绘制地面符号
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

  // 绘制书本（文字形式）
  for (let i = books.length - 1; i >= 0; i--) {
    const b = books[i]
    if (b.flying) {
      const now = performance.now()
      const progress = Math.min((now - b.flyStartTime) / FLY_DURATION, 1)
      const eased = easeOutIn(progress)
      const curX = b.flyStartX + (b.flyTargetX - b.flyStartX) * eased
      const curY = b.flyStartY + (b.flyTargetY - b.flyStartY) * eased
      drawBookText(ctx, curX, curY, b.name, 1 - eased * 0.5)
      if (progress >= 1) {
        if (!b.collected) { b.collected = true; addToShelf(b) }
        books.splice(i, 1)
      }
    } else {
      drawBookText(ctx, b.x + shakeX, b.y + shakeY, b.name)
      // 悬停高亮 - 书本图标
      if (hoveredBook === b) {
        ctx.save()
        ctx.globalAlpha = 0.3
        ctx.font = "56px Segoe UI Emoji, Apple Color Emoji, sans-serif"
        ctx.textAlign = "center"
        ctx.textBaseline = "middle"
        ctx.fillText("📖", b.x, b.y)
        ctx.restore()
      }
    }
  }

  // 天空符号
  ctx.font = `32px "Segoe UI Emoji", "Apple Color Emoji", sans-serif`
  for (const s of skyItems) {
    ctx.fillText(s.symbol, s.x + shakeX, s.y + shakeY)
  }

  // 掉落物
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
  if (book.flying) return
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
  if (!canvas || !tempCtx) return
  const rect = canvas.getBoundingClientRect()
  const scaleX = canvasWidth / rect.width
  const scaleY = canvasHeight / rect.height
  mouseX = (e.clientX - rect.left) * scaleX
  mouseY = (e.clientY - rect.top) * scaleY

  hoveredBook = null
  for (let i = books.length - 1; i >= 0; i--) {
    const b = books[i]
    if (b.flying) continue
    const hitRadius = getBookHitRadius(tempCtx, b)
    if (Math.hypot(mouseX - b.x, mouseY - b.y) <= hitRadius) {
      hoveredBook = b
      break
    }
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

function isInsideShelf(x: number, y: number): boolean {
  const m = 20
  return x > shelfX - m && x < shelfX + SHELF_WIDTH + m && y > shelfY - m && y < shelfY + SHELF_HEIGHT + m
}

function updateWind(now: number) {
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
  
  for (const g of groundItems) {
    g.x += g.vx
    g.y += g.vy + Math.sin(performance.now() / 3000 + g.baseX) * 0.1
    
    if (g.x < CANVAS_PADDING || g.x > canvasWidth - CANVAS_PADDING) {
      g.vx *= -1
      g.x = Math.min(Math.max(g.x, CANVAS_PADDING), canvasWidth - CANVAS_PADDING)
    }
    if (g.y < CANVAS_PADDING || g.y > canvasHeight - CANVAS_PADDING) {
      g.vy *= -1
      g.y = Math.min(Math.max(g.y, CANVAS_PADDING), canvasHeight - CANVAS_PADDING)
    }
    if (isInsideShelf(g.x, g.y)) {
      g.vx *= -1
      g.vy *= -1
    }
  }
}

function checkAndLimitTrunkItems() {
  const totalItems = groundItems.length + skyItems.length + stemDrops.length
  if (totalItems > MAX_TRUNK_ITEMS) {
    const excess = totalItems - MAX_TRUNK_ITEMS
    
    for (let i = 0; i < excess && groundItems.length > 10; i++) {
      groundItems.sort((a, b) => a.spawnTime - b.spawnTime)
      groundItems.shift()
    }
    
    if (stemDrops.length > 0) {
      const dropsToRemove = Math.min(excess, stemDrops.length)
      stemDrops.sort((a, b) => a.id - b.id)
      for (let i = 0; i < dropsToRemove; i++) {
        const drop = stemDrops[i]
        if (drop) {
          drop.vy += 3
          drop.vx += (Math.random() - 0.5) * 2
        }
      }
      stemDrops.splice(0, dropsToRemove)
    }
  }
}

// ========== 天干掉落与合成 (保持不变) ==========
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

function createGroundItem(symbol: string, x: number, y: number, trunkIndex: number) {
  return {
    symbol,
    x,
    y,
    trunkIndex,
    vx: (Math.random() - 0.5) * 0.2,
    vy: (Math.random() - 0.5) * 0.15,
    baseX: x,
    baseY: y,
    spawnTime: performance.now()
  }
}

// 落地合成逻辑（已移除戊和己的砸毁，戊/己不再强制消费，允许自然转化为地支）
function onLand(drop: StemDrop) {
  const landX = drop.x, landY = drop.y
  const RADIUS = 100
  let consumed = false
  switch (drop.trunkIndex) {
    case 0: {
      const found = findNearestGroundItem(landX, landY, RADIUS, 5)
      if (found) {
        consumed = true
      }
      break
    }
    case 1: {
      const found = findNearestGroundItem(landX, landY, RADIUS, 6)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[6]!)
        groundItems.splice(found.index, 1)
        groundItems.push(createGroundItem(combined.ground, landX, landY, combined.index))
        consumed = true
      }
      break
    }
    case 2: {
      const found = findNearestGroundItem(landX, landY, RADIUS, 7)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[7]!)
        groundItems.splice(found.index, 1)
        groundItems.push(createGroundItem(combined.ground, landX, landY, combined.index))
        consumed = true
      }
      break
    }
    case 3: {
      const found = findNearestGroundItem(landX, landY, RADIUS, 8)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[8]!)
        groundItems.splice(found.index, 1)
        groundItems.push(createGroundItem(combined.ground, landX, landY, combined.index))
        consumed = true
      }
      break
    }
    case 4: {
      const found = findNearestGroundItem(landX, landY, RADIUS, 9)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[9]!)
        groundItems.splice(found.index, 1)
        groundItems.push(createGroundItem(combined.ground, landX, landY, combined.index))
        consumed = true
      }
      break
    }
    case 5: {
      const found = findNearestGroundItem(landX, landY, RADIUS, 0)
      if (found) {
        groundItems.splice(found.index, 1)
        groundItems.push(createGroundItem(trunks[5]!.ground, landX, landY, 5))
        consumed = true
      }
      break
    }
    case 6: {
      const found = findNearestGroundItem(landX, landY, RADIUS, 1)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[1]!)
        groundItems.splice(found.index, 1)
        groundItems.push(createGroundItem(combined.ground, landX, landY, combined.index))
        consumed = true
      }
      break
    }
    case 7: {
      const found = findNearestGroundItem(landX, landY, RADIUS, 2)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[2]!)
        groundItems.splice(found.index, 1)
        groundItems.push(createGroundItem(combined.ground, landX, landY, combined.index))
        consumed = true
      }
      break
    }
    case 8: {
      const found = findNearestGroundItem(landX, landY, RADIUS, 3)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[3]!)
        groundItems.splice(found.index, 1)
        groundItems.push(createGroundItem(combined.ground, landX, landY, combined.index))
        consumed = true
      }
      break
    }
    case 9: {
      const found = findNearestGroundItem(landX, landY, RADIUS, 4)
      if (found) {
        const combined = combine(trunks[drop.trunkIndex]!, trunks[4]!)
        groundItems.splice(found.index, 1)
        groundItems.push(createGroundItem(combined.ground, landX, landY, combined.index))
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
        groundItems.push(createGroundItem(trunk.ground, drop.x, drop.y, drop.trunkIndex))
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
  checkAndLimitTrunkItems()
  draw()
  rafId = requestAnimationFrame(mainLoop)
}

function isTooCloseToOtherBooks(newX: number, newY: number): boolean {
  for (const book of books) {
    const dist = Math.hypot(newX - book.x, newY - book.y)
    if (dist < MIN_BOOK_SPACING) {
      return true
    }
  }
  return false
}

function scatterBooks() {
  const count = Math.floor(Math.random() * 21) + 20 // 散落 20-40 本
  const shuffled = [...bookPool].sort(() => Math.random() - 0.5)
  const selected = shuffled.slice(0, count)
  selected.forEach(b => {
    let x: number, y: number
    let attempts = 0
    const maxAttempts = 100
    do {
      x = Math.random() * (canvasWidth - 100) + 50
      y = Math.random() * (canvasHeight - 100) + 50
      attempts++
    } while ((isInsideShelf(x, y) || isTooCloseToOtherBooks(x, y)) && attempts < maxAttempts)
    
    if (attempts < maxAttempts) {
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
    }
  })
}

function updateShelfPosition() {
  shelfX = canvasWidth - SHELF_WIDTH - SHELF_MARGIN
  shelfY = SHELF_MARGIN
}

function handleResize() {
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
      if (b.x > shelfX && b.x < shelfX + SHELF_WIDTH) b.x = shelfX - 40
      if (b.y > shelfY && b.y < shelfY + SHELF_HEIGHT) b.y = shelfY - 40
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
  initTrunkItems()
  scatterBooks()
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
  pointer-events: none;
  animation: blurFadeIn 0.5s ease-out forwards;
}

.blur-overlay::before {
  content: '';
  position: absolute;
  inset: 0;
  background: radial-gradient(
    ellipse 80% 60% at 50% 50%,
    rgba(200, 180, 150, 0.15) 0%,
    rgba(150, 120, 100, 0.25) 40%,
    rgba(100, 80, 60, 0.35) 70%,
    rgba(80, 60, 40, 0.45) 100%
  );
  animation: fogPulse 2s ease-in-out infinite;
}

.blur-overlay::after {
  content: '';
  position: absolute;
  inset: 0;
  backdrop-filter: blur(8px);
  mask-image: radial-gradient(
    ellipse 60% 40% at 50% 50%,
    transparent 0%,
    transparent 30%,
    rgba(0, 0, 0, 0.3) 50%,
    rgba(0, 0, 0, 0.6) 70%,
    rgba(0, 0, 0, 0.8) 100%
  );
}

.blur-layer-1 {
  position: absolute;
  inset: 0;
  backdrop-filter: blur(12px);
  opacity: 0.4;
  mask-image: radial-gradient(
    circle at 30% 40%,
    rgba(0, 0, 0, 0.8) 0%,
    rgba(0, 0, 0, 0.3) 30%,
    transparent 60%
  );
}

.blur-layer-2 {
  position: absolute;
  inset: 0;
  backdrop-filter: blur(6px);
  opacity: 0.6;
  mask-image: radial-gradient(
    circle at 70% 60%,
    rgba(0, 0, 0, 0.7) 0%,
    rgba(0, 0, 0, 0.2) 40%,
    transparent 70%
  );
}

.blur-layer-3 {
  position: absolute;
  inset: 0;
  backdrop-filter: blur(3px);
  opacity: 0.8;
  mask-image: radial-gradient(
    ellipse 90% 70% at 50% 80%,
    rgba(0, 0, 0, 0.9) 0%,
    rgba(0, 0, 0, 0.4) 50%,
    transparent 80%
  );
}

.fog-noise {
  position: absolute;
  inset: 0;
  opacity: 0.03;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
  pointer-events: none;
}

@keyframes blurFadeIn {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}

@keyframes fogPulse {
  0%, 100% {
    opacity: 0.8;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.02);
  }
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