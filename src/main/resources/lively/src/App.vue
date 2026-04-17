<template>
  <ElContainer class="tcm-container">
    <ElHeader class="tcm-header">
      <h1>中医五运六气与五脏六腑关系图</h1>
      <p class="subtitle">探索天人相应的中医整体观</p>
    </ElHeader>
    <ElMain class="tcm-main">
      <!-- SVG 可视化区域 -->
      <div class="svg-container">
        <svg
            :viewBox="`0 0 ${svgWidth} ${svgHeight}`"
            class="tcm-svg"
            @mousemove="handleMouseMove"
        >
          <!-- 背景网格 -->
          <defs>
            <!-- 渐变定义 -->
            <linearGradient id="woodGradient" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" stop-color="#4CAF50" stop-opacity="0.1" />
              <stop offset="100%" stop-color="#8BC34A" stop-opacity="0.3" />
            </linearGradient>
            <linearGradient id="fireGradient" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" stop-color="#FF5722" stop-opacity="0.1" />
              <stop offset="100%" stop-color="#FF9800" stop-opacity="0.3" />
            </linearGradient>
            <linearGradient id="earthGradient" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" stop-color="#FFEB3B" stop-opacity="0.1" />
              <stop offset="100%" stop-color="#FFC107" stop-opacity="0.3" />
            </linearGradient>
            <linearGradient id="metalGradient" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" stop-color="#9E9E9E" stop-opacity="0.1" />
              <stop offset="100%" stop-color="#607D8B" stop-opacity="0.3" />
            </linearGradient>
            <linearGradient id="waterGradient" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" stop-color="#2196F3" stop-opacity="0.1" />
              <stop offset="100%" stop-color="#03A9F4" stop-opacity="0.3" />
            </linearGradient>

            <!-- 标记定义 -->
            <marker
                id="arrow"
                viewBox="0 0 10 10"
                refX="9"
                refY="5"
                markerWidth="6"
                markerHeight="6"
                orient="auto"
            >
              <path d="M 0 0 L 10 5 L 0 10 z" fill="#666" />
            </marker>

            <!-- 阴阳符号 -->
            <symbol id="yinYang" viewBox="0 0 100 100">
              <circle cx="50" cy="50" r="50" fill="#333" />
              <path d="M50,0 C22.386,0 0,22.386 0,50 C0,77.614 22.386,100 50,100 C77.614,100 100,77.614 100,50 C100,22.386 77.614,0 50,0 Z M50,20 C64.359,20 76,31.641 76,46 C76,60.359 64.359,72 50,72 C35.641,72 24,60.359 24,46 C24,31.641 35.641,20 50,20 Z" fill="#fff" />
              <circle cx="50" cy="30" r="8" fill="#333" />
              <circle cx="50" cy="70" r="8" fill="#fff" />
            </symbol>
          </defs>

          <!-- 中心：太极图 -->
          <g class="center-tai-chi" @mouseenter="showDetail('taiChi')" @mouseleave="hideDetail">
            <use href="#yinYang" :x="centerX - 50" :y="centerY - 50" width="100" height="100" />
            <text :x="centerX" :y="centerY + 70" text-anchor="middle" class="label">太极阴阳</text>
          </g>

          <!-- 五运（五个外部圆） -->
          <g v-for="(luck, index) in fiveLucks" :key="luck.name">
            <!-- 轨道圆 -->
            <circle
                :cx="centerX"
                :cy="centerY"
                :r="orbitRadius"
                :stroke="luck.color"
                stroke-width="2"
                fill="none"
                stroke-dasharray="5,5"
                opacity="0.3"
            />

            <!-- 五运球体 -->
            <g
                class="luck-node"
                :transform="`translate(${getLuckPosition(index).x}, ${getLuckPosition(index).y})`"
                @mouseenter="showDetail('luck', luck)"
                @mouseleave="hideDetail"
                @click="focusOnLuck(luck)"
            >
              <!-- 外部光环 -->
              <circle
                  r="40"
                  :fill="`url(#${luck.id}Gradient)`"
                  opacity="0.3"
                  class="halo"
              />

              <!-- 主球体 -->
              <circle
                  r="30"
                  :fill="luck.color"
                  stroke="#fff"
                  stroke-width="2"
                  class="luck-sphere"
                  :class="{ 'active': focusedElement?.type === 'luck' && focusedElement?.data?.name === luck.name }"
              />

              <!-- 内部符号 -->
              <text
                  text-anchor="middle"
                  dy="0.3em"
                  class="luck-symbol"
                  font-size="24"
                  font-weight="bold"
                  fill="#fff"
              >
                {{ luck.symbol }}
              </text>

              <!-- 标签 -->
              <g class="luck-label">
                <rect
                    :x="-40"
                    :y="25"
                    width="80"
                    height="60"
                    rx="8"
                    fill="white"
                    fill-opacity="0.9"
                    stroke="#ddd"
                    stroke-width="1"
                />
                <text
                    text-anchor="middle"
                    :y="45"
                    class="label-title"
                    font-size="14"
                    font-weight="bold"
                    :fill="luck.color"
                >
                  {{ luck.name }}运
                </text>
                <text
                    text-anchor="middle"
                    :y="60"
                    class="label-desc"
                    font-size="12"
                    fill="#666"
                >
                  {{ luck.element }}
                </text>
              </g>
            </g>

            <!-- 连接到中心 -->
            <line
                :x1="centerX"
                :y1="centerY"
                :x2="getLuckPosition(index).x"
                :y2="getLuckPosition(index).y"
                :stroke="luck.color"
                stroke-width="2"
                stroke-dasharray="5,5"
                opacity="0.5"
            />
          </g>

          <!-- 六气（六边形） -->
          <g v-for="(qi, index) in sixQi" :key="qi.name">
            <g
                class="qi-node"
                :transform="`translate(${getQiPosition(index).x}, ${getQiPosition(index).y})`"
                @mouseenter="showDetail('qi', qi)"
                @mouseleave="hideDetail"
                @click="focusOnQi(qi)"
            >
              <!-- 六边形 -->
              <polygon
                  :points="getHexagonPoints(25)"
                  :fill="qi.color"
                  fill-opacity="0.7"
                  stroke="#fff"
                  stroke-width="2"
                  class="qi-hexagon"
                  :class="{ 'active': focusedElement?.type === 'qi' && focusedElement?.data?.name === qi.name }"
              />

              <!-- 符号 -->
              <text
                  text-anchor="middle"
                  dy="0.3em"
                  class="qi-symbol"
                  font-size="20"
                  font-weight="bold"
                  fill="#fff"
              >
                {{ qi.symbol }}
              </text>

              <!-- 标签 -->
              <g class="qi-label">
                <rect
                    :x="-35"
                    :y="30"
                    width="70"
                    height="50"
                    rx="6"
                    fill="white"
                    fill-opacity="0.9"
                    stroke="#ddd"
                    stroke-width="1"
                />
                <text
                    text-anchor="middle"
                    :y="45"
                    class="label-title"
                    font-size="12"
                    font-weight="bold"
                    :fill="qi.color"
                >
                  {{ qi.name }}
                </text>
                <text
                    text-anchor="middle"
                    :y="60"
                    class="label-desc"
                    font-size="10"
                    fill="#666"
                >
                  {{ qi.season }}
                </text>
              </g>
            </g>
          </g>

          <!-- 五脏六腑（内部连接） -->
          <g v-for="(organ, index) in organs" :key="organ.name">
            <!-- 连接线 -->
            <path
                v-if="organ.connection"
                :d="organ.connection"
                :stroke="organ.color"
                stroke-width="2"
                fill="none"
                opacity="0.4"
                marker-end="url(#arrow)"
            />

            <!-- 器官节点 -->
            <g
                class="organ-node"
                :transform="`translate(${organ.x}, ${organ.y})`"
                @mouseenter="showDetail('organ', organ)"
                @mouseleave="hideDetail"
                @click="focusOnOrgan(organ)"
            >
              <!-- 背景光晕 -->
              <circle
                  r="organ.type === 'zang' ? 25 : 20"
                  :fill="organ.color"
                  fill-opacity="0.1"
                  class="organ-halo"
              />

              <!-- 器官图形 -->
              <circle
                  v-if="organ.type === 'zang'"
                  r="organ.type === 'zang' ? 20 : 15"
                  :fill="organ.color"
                  stroke="#fff"
                  stroke-width="2"
                  class="organ-circle"
                  :class="{ 'active': focusedElement?.type === 'organ' && focusedElement?.data?.name === organ.name }"
              />

              <rect
                  v-else
                  :width="organ.type === 'fu' ? 30 : 25"
                  :height="organ.type === 'fu' ? 30 : 25"
                  :x="organ.type === 'fu' ? -15 : -12.5"
                  :y="organ.type === 'fu' ? -15 : -12.5"
                  :fill="organ.color"
                  stroke="#fff"
                  stroke-width="2"
                  rx="4"
                  class="organ-rect"
                  :class="{ 'active': focusedElement?.type === 'organ' && focusedElement?.data?.name === organ.name }"
              />

              <!-- 器官名称 -->
              <text
                  text-anchor="middle"
                  :y="organ.type === 'zang' ? 35 : 30"
                  class="organ-label"
                  font-size="11"
                  font-weight="bold"
                  :fill="organ.color"
              >
                {{ organ.name }}
              </text>

              <!-- 关联的五运 -->
              <text
                  v-if="organ.luck"
                  text-anchor="middle"
                  :y="-25"
                  class="organ-luck"
                  font-size="10"
                  fill="#999"
              >
                {{ organ.luck }}
              </text>
            </g>
          </g>

          <!-- 说明文字区域 -->
          <g class="legend">
            <rect
                x="20"
                y="20"
                width="200"
                height="200"
                rx="10"
                fill="white"
                fill-opacity="0.9"
                stroke="#ddd"
                stroke-width="1"
            />
            <text x="40" y="50" class="legend-title" font-size="16" font-weight="bold" fill="#333">
              图例说明
            </text>

            <!-- 五运 -->
            <circle cx="40" cy="80" r="8" fill="#4CAF50" />
            <text x="55" y="85" font-size="12" fill="#666">圆形：五运（木火土金水）</text>

            <!-- 六气 -->
            <polygon :points="`30,100 40,90 50,100 50,115 40,125 30,115`" fill="#2196F3" />
            <text x="55" y="110" font-size="12" fill="#666">六边形：六气</text>

            <!-- 五脏 -->
            <circle cx="40" cy="130" r="6" fill="#FF5722" />
            <text x="55" y="135" font-size="12" fill="#666">实心圆：五脏（阴）</text>

            <!-- 六腑 -->
            <rect x="34" y="145" width="12" height="12" rx="2" fill="#4CAF50" />
            <text x="55" y="155" font-size="12" fill="#666">方形：六腑（阳）</text>

            <!-- 提示 -->
            <text x="40" y="180" font-size="10" fill="#999" font-style="italic">
              鼠标悬停查看详情，点击聚焦
            </text>
          </g>

          <!-- 详细信息浮层 -->
          <g v-if="currentDetail" class="detail-panel">
            <rect
                :x="detailPosition.x"
                :y="detailPosition.y"
                width="280"
                height="detailHeight"
                rx="10"
                fill="white"
                fill-opacity="0.95"
                stroke="#ddd"
                stroke-width="1"
                filter="url(#shadow)"
            />

            <!-- 标题 -->
            <text
                :x="detailPosition.x + 20"
                :y="detailPosition.y + 30"
                class="detail-title"
                font-size="16"
                font-weight="bold"
                :fill="currentDetail.color"
            >
              {{ currentDetail.title }}
            </text>

            <!-- 描述 -->
            <text
                v-for="(line, index) in currentDetail.description"
                :key="index"
                :x="detailPosition.x + 20"
                :y="detailPosition.y + 55 + index * 20"
                class="detail-text"
                font-size="12"
                fill="#666"
            >
              {{ line }}
            </text>

            <!-- 关系 -->
            <text
                v-if="currentDetail.relationships"
                :x="detailPosition.x + 20"
                :y="detailPosition.y + 55 + currentDetail.description.length * 20 + 10"
                class="detail-relation-title"
                font-size="13"
                font-weight="bold"
                fill="#333"
            >
              相关关系：
            </text>

            <text
                v-for="(relation, index) in currentDetail.relationships"
                :key="index"
                :x="detailPosition.x + 20"
                :y="detailPosition.y + 80 + currentDetail.description.length * 20 + index * 20"
                class="detail-relation"
                font-size="11"
                fill="#666"
            >
              • {{ relation }}
            </text>
          </g>

          <!-- 动画路径（展示循环） -->
          <path
              v-if="showCycle"
              id="cyclePath"
              :d="cyclePath"
              fill="none"
              stroke="#FF4081"
              stroke-width="2"
              stroke-dasharray="5,5"
              opacity="0.6"
          >
            <animate
                attributeName="stroke-dashoffset"
                from="1000"
                to="0"
                dur="20s"
                repeatCount="indefinite"
            />
          </path>
        </svg>
      </div>

      <!-- 控制面板 -->
      <div class="control-panel">
        <el-button-group>
          <el-button
              :type="showCycle ? 'primary' : 'default'"
              @click="toggleCycle"
              size="small"
          >
            {{ showCycle ? '隐藏' : '显示' }}循环动画
          </el-button>
          <el-button
              type="info"
              @click="resetView"
              size="small"
          >
            重置视图
          </el-button>
        </el-button-group>

        <div class="hint">
          点击任一元素可聚焦查看详细关系
        </div>
      </div>

      <!-- 文字说明区域 -->
      <div class="description">
        <el-card class="description-card">
          <template #header>
            <div class="card-header">
              <h3>中医五运六气与五脏六腑的关系</h3>
            </div>
          </template>

          <div class="content">
            <p>
              五运六气是中医理论的重要组成部分，它反映了自然界气候变化对人体生理病理的影响规律。
            </p>

            <el-collapse v-model="activeCollapse">
              <el-collapse-item title="五运（木、火、土、金、水）" name="1">
                <p>五运代表五种气候运动状态，分别对应五行，影响人体五脏功能：</p>
                <ul>
                  <li><strong>木运</strong>：主生发，对应肝脏，与春季、风气相关</li>
                  <li><strong>火运</strong>：主长养，对应心脏，与夏季、暑气相关</li>
                  <li><strong>土运</strong>：主化生，对应脾脏，与长夏、湿气相关</li>
                  <li><strong>金运</strong>：主收敛，对应肺脏，与秋季、燥气相关</li>
                  <li><strong>水运</strong>：主封藏，对应肾脏，与冬季、寒气相关</li>
                </ul>
              </el-collapse-item>

              <el-collapse-item title="六气（风、热、暑、湿、燥、寒）" name="2">
                <p>六气是自然界六种气候变化，与三阴三阳相配：</p>
                <ul>
                  <li><strong>厥阴风木</strong>：初之气，主生发，肝所主</li>
                  <li><strong>少阴君火</strong>：二之气，主长养，心所主</li>
                  <li><strong>少阳相火</strong>：三之气，主炎热，三焦所主</li>
                  <li><strong>太阴湿土</strong>：四之气，主化生，脾所主</li>
                  <li><strong>阳明燥金</strong>：五之气，主收敛，肺所主</li>
                  <li><strong>太阳寒水</strong>：终之气，主封藏，肾所主</li>
                </ul>
              </el-collapse-item>

              <el-collapse-item title="五脏六腑的功能关系" name="3">
                <p>五脏六腑通过经络相连，构成一个整体功能系统：</p>
                <ul>
                  <li><strong>肝与胆</strong>：主疏泄，藏血，主谋虑</li>
                  <li><strong>心与小肠</strong>：主血脉，藏神，主神明</li>
                  <li><strong>脾与胃</strong>：主运化，统血，为后天之本</li>
                  <li><strong>肺与大肠</strong>：主气司呼吸，主宣发肃降</li>
                  <li><strong>肾与膀胱</strong>：藏精主水，主骨生髓，为先天之本</li>
                  <li><strong>心包与三焦</strong>：代心受邪，主持诸气</li>
                </ul>
              </el-collapse-item>

              <el-collapse-item title="临床应用与养生指导" name="4">
                <p>根据五运六气理论指导临床治疗和养生保健：</p>
                <ul>
                  <li><strong>因时制宜</strong>：根据不同季节气候变化调整治疗方案</li>
                  <li><strong>治未病</strong>：预测气候异常，提前预防相关疾病</li>
                  <li><strong>饮食调养</strong>：根据运气特点选择适宜食物</li>
                  <li><strong>情志调节</strong>：顺应四时调摄精神情志</li>
                </ul>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-card>
      </div>
    </ElMain>
  </ElContainer>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

// SVG 尺寸
const svgWidth = 1200
const svgHeight = 800
const centerX = svgWidth / 2
const centerY = svgHeight / 2
const orbitRadius = 300

// 五运数据
const fiveLucks = ref([
  {
    name: '木',
    element: '木行',
    color: '#4CAF50',
    symbol: '木',
    id: 'wood',
    description: [
      '木运主生发，对应春季',
      '性质：生长、升发、条达',
      '气候：风',
      '方位：东',
      '五味：酸',
      '五色：青'
    ],
    relationships: ['对应肝脏', '主筋', '开窍于目', '在志为怒']
  },
  {
    name: '火',
    element: '火行',
    color: '#FF5722',
    symbol: '火',
    id: 'fire',
    description: [
      '火运主长养，对应夏季',
      '性质：温热、上升、光明',
      '气候：暑、热',
      '方位：南',
      '五味：苦',
      '五色：赤'
    ],
    relationships: ['对应心脏', '主血脉', '开窍于舌', '在志为喜']
  },
  {
    name: '土',
    element: '土行',
    color: '#FFC107',
    symbol: '土',
    id: 'earth',
    description: [
      '土运主化生，对应长夏',
      '性质：承载、受纳、生化',
      '气候：湿',
      '方位：中',
      '五味：甘',
      '五色：黄'
    ],
    relationships: ['对应脾脏', '主肌肉', '开窍于口', '在志为思']
  },
  {
    name: '金',
    element: '金行',
    color: '#9E9E9E',
    symbol: '金',
    id: 'metal',
    description: [
      '金运主收敛，对应秋季',
      '性质：肃降、收敛、清洁',
      '气候：燥',
      '方位：西',
      '五味：辛',
      '五色：白'
    ],
    relationships: ['对应肺脏', '主皮毛', '开窍于鼻', '在志为悲']
  },
  {
    name: '水',
    element: '水行',
    color: '#2196F3',
    symbol: '水',
    id: 'water',
    description: [
      '水运主封藏，对应冬季',
      '性质：寒凉、滋润、下行',
      '气候：寒',
      '方位：北',
      '五味：咸',
      '五色：黑'
    ],
    relationships: ['对应肾脏', '主骨', '开窍于耳', '在志为恐']
  }
])

// 六气数据
const sixQi = ref([
  {
    name: '厥阴风木',
    season: '初之气',
    color: '#8BC34A',
    symbol: '风',
    description: [
      '主气：风木',
      '时间：大寒至春分',
      '特点：阳气初生',
      '主生长、升发',
      '易发：肝病、眩晕'
    ],
    relationships: ['对应肝脏', '主疏泄', '与胆相表里']
  },
  {
    name: '少阴君火',
    season: '二之气',
    color: '#FF9800',
    symbol: '热',
    description: [
      '主气：君火',
      '时间：春分至小满',
      '特点：阳气渐盛',
      '主温热、向上',
      '易发：心病、热病'
    ],
    relationships: ['对应心脏', '主血脉', '与小肠相表里']
  },
  {
    name: '少阳相火',
    season: '三之气',
    color: '#FF5722',
    symbol: '暑',
    description: [
      '主气：相火',
      '时间：小满至大暑',
      '特点：阳气最盛',
      '主炎热、外散',
      '易发：三焦病、暑病'
    ],
    relationships: ['对应三焦', '主气机', '通行水道']
  },
  {
    name: '太阴湿土',
    season: '四之气',
    color: '#FFC107',
    symbol: '湿',
    description: [
      '主气：湿土',
      '时间：大暑至秋分',
      '特点：湿气主令',
      '主化生、运化',
      '易发：脾病、湿病'
    ],
    relationships: ['对应脾脏', '主运化', '与胃相表里']
  },
  {
    name: '阳明燥金',
    season: '五之气',
    color: '#9E9E9E',
    symbol: '燥',
    description: [
      '主气：燥金',
      '时间：秋分至小雪',
      '特点：燥气主令',
      '主收敛、肃降',
      '易发：肺病、燥病'
    ],
    relationships: ['对应肺脏', '主气司呼吸', '与大肠相表里']
  },
  {
    name: '太阳寒水',
    season: '终之气',
    color: '#2196F3',
    symbol: '寒',
    description: [
      '主气：寒水',
      '时间：小雪至大寒',
      '特点：寒气主令',
      '主封藏、凝滞',
      '易发：肾病、寒病'
    ],
    relationships: ['对应肾脏', '主水液', '与膀胱相表里']
  }
])

// 五脏六腑数据
const organs = ref([
  // 五脏（阴）
  {
    name: '肝',
    type: 'zang',
    color: '#4CAF50',
    luck: '木',
    x: centerX - 150,
    y: centerY - 100,
    description: [
      '功能：主疏泄，藏血',
      '开窍于目，在体合筋',
      '与胆相表里',
      '在志为怒，在液为泪',
      '肝为刚脏，体阴用阳'
    ],
    functions: ['疏泄气机', '贮藏血液', '调节情志', '促进消化']
  },
  {
    name: '心',
    type: 'zang',
    color: '#FF5722',
    luck: '火',
    x: centerX + 150,
    y: centerY - 100,
    description: [
      '功能：主血脉，藏神',
      '开窍于舌，在体合脉',
      '与小肠相表里',
      '在志为喜，在液为汗',
      '心为君主之官'
    ],
    functions: ['推动血液', '主司神明', '调节情绪', '主管生命活动']
  },
  {
    name: '脾',
    type: 'zang',
    color: '#FFC107',
    luck: '土',
    x: centerX,
    y: centerY - 200,
    description: [
      '功能：主运化，统血',
      '开窍于口，在体合肉',
      '与胃相表里',
      '在志为思，在液为涎',
      '脾为后天之本'
    ],
    functions: ['运化水谷', '统摄血液', '升清降浊', '营养全身']
  },
  {
    name: '肺',
    type: 'zang',
    color: '#9E9E9E',
    luck: '金',
    x: centerX - 150,
    y: centerY + 100,
    description: [
      '功能：主气司呼吸',
      '开窍于鼻，在体合皮',
      '与大肠相表里',
      '在志为悲，在液为涕',
      '肺为娇脏'
    ],
    functions: ['主呼吸', '宣发肃降', '通调水道', '朝百脉']
  },
  {
    name: '肾',
    type: 'zang',
    color: '#2196F3',
    luck: '水',
    x: centerX + 150,
    y: centerY + 100,
    description: [
      '功能：藏精，主水',
      '开窍于耳，在体合骨',
      '与膀胱相表里',
      '在志为恐，在液为唾',
      '肾为先天之本'
    ],
    functions: ['贮藏精气', '主生长发育', '主水液代谢', '纳气']
  },
  // 六腑（阳）
  {
    name: '胆',
    type: 'fu',
    color: '#8BC34A',
    luck: '木',
    x: centerX - 80,
    y: centerY - 30,
    description: [
      '功能：贮存和排泄胆汁',
      '主决断，为中正之官',
      '与肝相表里',
      '胆气主升，性喜宁谧'
    ],
    functions: ['贮藏胆汁', '帮助消化', '主决断', '调节情志']
  },
  {
    name: '小肠',
    type: 'fu',
    color: '#FF9800',
    luck: '火',
    x: centerX + 80,
    y: centerY - 30,
    description: [
      '功能：受盛化物，泌别清浊',
      '与心相表里',
      '小肠主液，吸收精微'
    ],
    functions: ['接受胃中水谷', '分别清浊', '吸收营养', '输送糟粕']
  },
  {
    name: '胃',
    type: 'fu',
    color: '#FFC107',
    luck: '土',
    x: centerX,
    y: centerY - 150,
    description: [
      '功能：受纳腐熟水谷',
      '胃主通降，以降为和',
      '与脾相表里',
      '胃为水谷之海'
    ],
    functions: ['接受食物', '初步消化', '通降浊气', '滋养全身']
  },
  {
    name: '大肠',
    type: 'fu',
    color: '#795548',
    luck: '金',
    x: centerX - 80,
    y: centerY + 30,
    description: [
      '功能：传导糟粕',
      '与肺相表里',
      '大肠主津，吸收水分',
      '传导变化，排出粪便'
    ],
    functions: ['传导糟粕', '吸收水分', '形成粪便', '排出体外']
  },
  {
    name: '膀胱',
    type: 'fu',
    color: '#2196F3',
    luck: '水',
    x: centerX + 80,
    y: centerY + 30,
    description: [
      '功能：贮存和排泄尿液',
      '与肾相表里',
      '膀胱主气化',
      '依赖肾阳温煦'
    ],
    functions: ['贮存尿液', '排泄尿液', '气化水液', '维持水平衡']
  },
  {
    name: '三焦',
    type: 'fu',
    color: '#9C27B0',
    luck: '火',
    x: centerX,
    y: centerY + 150,
    description: [
      '功能：通行元气，运行水液',
      '上焦如雾，中焦如沤，下焦如渎',
      '为原气之别使',
      '主持诸气，总司全身气化'
    ],
    functions: ['通行元气', '运行水液', '总司气化', '协调脏腑']
  }
])

// 计算连接路径
organs.value.forEach(organ => {
  const target = organs.value.find(o => {
    if (organ.name === '肝') return o.name === '胆'
    if (organ.name === '心') return o.name === '小肠'
    if (organ.name === '脾') return o.name === '胃'
    if (organ.name === '肺') return o.name === '大肠'
    if (organ.name === '肾') return o.name === '膀胱'
    if (organ.name === '胆') return o.name === '肝'
    if (organ.name === '小肠') return o.name === '心'
    if (organ.name === '胃') return o.name === '脾'
    if (organ.name === '大肠') return o.name === '肺'
    if (organ.name === '膀胱') return o.name === '肾'
    if (organ.name === '三焦') return o.name === '心包'
    return false
  })

  if (target) {
    const dx = target.x - organ.x
    const dy = target.y - organ.y
    const distance = Math.sqrt(dx * dx + dy * dy)
    const offset = 20

    organ.connection = `M ${organ.x} ${organ.y}
      C ${organ.x + dx * 0.3} ${organ.y + dy * 0.3 + offset}
        ${target.x - dx * 0.3} ${target.y - dy * 0.3 + offset}
        ${target.x} ${target.y}`
  }
})

// 计算位置
const getLuckPosition = (index) => {
  const angle = (index * 72 - 90) * Math.PI / 180
  return {
    x: centerX + orbitRadius * Math.cos(angle),
    y: centerY + orbitRadius * Math.sin(angle)
  }
}

const getQiPosition = (index) => {
  const angle = (index * 60 - 90) * Math.PI / 180
  const radius = orbitRadius * 0.7
  return {
    x: centerX + radius * Math.cos(angle),
    y: centerY + radius * Math.sin(angle)
  }
}

const getHexagonPoints = (size) => {
  const points = []
  for (let i = 0; i < 6; i++) {
    const angle = (i * 60) * Math.PI / 180
    const x = size * Math.cos(angle)
    const y = size * Math.sin(angle)
    points.push(`${x},${y}`)
  }
  return points.join(' ')
}

// 交互状态
const currentDetail = ref(null)
const detailPosition = ref({ x: 0, y: 0 })
const focusedElement = ref(null)
const showCycle = ref(true)
const activeCollapse = ref(['1', '2', '3', '4'])

// 计算详情面板高度
const detailHeight = computed(() => {
  if (!currentDetail.value) return 0
  let height = 80 // 基础高度
  height += currentDetail.value.description.length * 20
  if (currentDetail.value.relationships) {
    height += 30 + currentDetail.value.relationships.length * 20
  }
  return height
})

// 循环路径
const cyclePath = computed(() => {
  const points = []
  for (let i = 0; i < 5; i++) {
    const pos = getLuckPosition(i)
    points.push(`${i === 0 ? 'M' : 'L'} ${pos.x} ${pos.y}`)
  }
  points.push('Z')
  return points.join(' ')
})

// 交互方法
const showDetail = (type, data) => {
  if (!data && type === 'taiChi') {
    currentDetail.value = {
      title: '太极阴阳',
      color: '#333',
      description: [
        '太极是宇宙的本源，阴阳是事物的两种属性',
        '阴阳互根：相互依存，互为根本',
        '阴阳消长：此消彼长，动态平衡',
        '阴阳转化：物极必反，相互转化',
        '阴阳交感：相互作用，化生万物'
      ],
      relationships: ['阴中有阳，阳中有阴', '阴阳平衡则健康，失衡则疾病']
    }
  } else if (data) {
    currentDetail.value = {
      title: data.name + (type === 'luck' ? '运' : type === 'qi' ? '（六气）' : '（' + (data.type === 'zang' ? '脏' : '腑') + '）'),
      color: data.color,
      description: data.description || [],
      relationships: data.relationships || data.functions || []
    }
  }

  // 更新位置
  updateDetailPosition()
}

const hideDetail = () => {
  if (!focusedElement.value) {
    currentDetail.value = null
  }
}

const handleMouseMove = (event) => {
  if (currentDetail.value) {
    const svgRect = event.target.getBoundingClientRect()
    detailPosition.value = {
      x: event.clientX - svgRect.left + 20,
      y: event.clientY - svgRect.top + 20
    }

    // 防止超出边界
    if (detailPosition.value.x + 280 > svgWidth) {
      detailPosition.value.x = svgWidth - 300
    }
    if (detailPosition.value.y + detailHeight.value > svgHeight) {
      detailPosition.value.y = svgHeight - detailHeight.value - 20
    }
  }
}

const updateDetailPosition = () => {
  // 初始位置设置在右上角
  detailPosition.value = {
    x: svgWidth - 300,
    y: 20
  }
}

const focusOnLuck = (luck) => {
  focusedElement.value = { type: 'luck', data: luck }
  showDetail('luck', luck)
}

const focusOnQi = (qi) => {
  focusedElement.value = { type: 'qi', data: qi }
  showDetail('qi', qi)
}

const focusOnOrgan = (organ) => {
  focusedElement.value = { type: 'organ', data: organ }
  showDetail('organ', organ)
}

const toggleCycle = () => {
  showCycle.value = !showCycle.value
}

const resetView = () => {
  focusedElement.value = null
  currentDetail.value = null
  activeCollapse.value = ['1', '2', '3', '4']
}

// 初始化
onMounted(() => {
  // 初始显示太极图的说明
  setTimeout(() => {
    showDetail('taiChi')
  }, 1000)
})

onUnmounted(() => {
  // 清理
})
</script>

<style scoped>
.tcm-container {
  height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
}

.tcm-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  text-align: center;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.tcm-header h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 300;
  letter-spacing: 2px;
}

.tcm-header .subtitle {
  margin: 10px 0 0;
  font-size: 16px;
  opacity: 0.9;
  font-weight: 300;
}

.tcm-main {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  overflow: auto;
}

.svg-container {
  flex: 1;
  min-height: 600px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  border: 1px solid #e4e7ed;
}

.tcm-svg {
  width: 100%;
  height: 100%;
  cursor: default;
}

/* SVG 样式 */
.luck-node, .qi-node, .organ-node {
  cursor: pointer;
  transition: all 0.3s ease;
}

.luck-node:hover .luck-sphere,
.qi-node:hover .qi-hexagon,
.organ-node:hover .organ-circle,
.organ-node:hover .organ-rect {
  filter: drop-shadow(0 0 8px rgba(0, 0, 0, 0.3));
  transform: scale(1.1);
  transition: all 0.3s ease;
}

.luck-sphere.active,
.qi-hexagon.active,
.organ-circle.active,
.organ-rect.active {
  filter: drop-shadow(0 0 12px currentColor);
  stroke-width: 3;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { filter: drop-shadow(0 0 8px currentColor); }
  50% { filter: drop-shadow(0 0 16px currentColor); }
  100% { filter: drop-shadow(0 0 8px currentColor); }
}

.halo {
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.label {
  font-size: 12px;
  font-weight: 500;
  fill: #666;
  font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
}

.label-title {
  font-weight: bold;
  font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
}

.label-desc {
  font-size: 10px;
  opacity: 0.8;
  font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
}

.legend-title {
  font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
}

.detail-title {
  font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
  font-weight: bold;
}

.detail-text, .detail-relation {
  font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
}

.control-panel {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  padding: 15px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.hint {
  color: #909399;
  font-size: 14px;
  font-style: italic;
}

.description {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.description-card {
  border: none;
  box-shadow: none;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 5px;
}

.card-header h3 {
  margin: 0;
  color: #333;
  font-size: 20px;
  font-weight: 500;
}

.content {
  padding: 5px;
}

.content p {
  line-height: 1.8;
  color: #666;
  margin-bottom: 20px;
  text-indent: 2em;
}

.content ul {
  padding-left: 20px;
  margin: 10px 0;
}

.content li {
  margin: 8px 0;
  line-height: 1.6;
  color: #666;
}

.content strong {
  color: #409EFF;
  font-weight: 600;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .tcm-header h1 {
    font-size: 20px;
  }

  .tcm-header .subtitle {
    font-size: 14px;
  }

  .svg-container {
    min-height: 400px;
  }

  .control-panel {
    flex-direction: column;
    gap: 10px;
  }

  .card-header h3 {
    font-size: 18px;
  }
}
</style>