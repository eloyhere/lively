<template>
  <ElContainer class="home-container" direction="vertical">
    <ElHeader class="sticky-header" height="60px">
      <Toolbar/>
    </ElHeader>
    
    <ElScrollbar 
      ref="scrollbarRef"
      class="custom-scrollbar"
      :wrap-class="'scrollbar-wrapper'"
      :view-class="'scrollbar-view'"
      @scroll="handleScroll"
    >
      <div class="scroll-content">
        <!-- Hero 区域 -->
        <section class="hero-section">
          <div class="hero-content">
            <h1 class="hero-title">
              <span class="line line-1">传承</span>
              <span class="line line-2">中医智慧</span>
              <span class="line line-3">守护</span>
              <span class="line line-4">健康传承</span>
            </h1>
          </div>
          
          <!-- 滚动指示器 -->
          <div class="scroll-indicator" @click="scrollToContent">
            <span class="scroll-text">滚动探索</span>
            <div class="arrow">↓</div>
          </div>
        </section>
        
        <!-- 粘性文本展示区域 -->
        <div class="sticky-text-container">
          <!-- 中间文本内容 -->
          <div class="text-content">
            <!-- 固定文本 -->
            <div class="fixed-text" :class="{ 'sticky-active': isFixedTextActive }">
              <h2>中医之道</h2>
              <p>传承千年的智慧</p>
            </div>
            
            <!-- 滚动文本区 -->
            <div class="scrolling-text">
              <!-- 中医基础理论 -->
              <div class="text-block block-1" :class="{ 'active': activeBlock >= 1 }">
                <h3>天人合一</h3>
                <p>中医认为人体是一个小宇宙，与自然界的大宇宙相互感应。四季更替、昼夜变化、地理环境都会影响人体的生理病理。顺应自然规律，与天地相应，是中医养生的基本原则。</p>
              </div>
              
              <div class="text-block block-2" :class="{ 'active': activeBlock >= 2 }">
                <h3>阴阳学说</h3>
                <p>阴阳是中医理论的核心，代表自然界中相互对立又相互依存的两个方面。阳主动、主热、主表；阴主静、主寒、主里。阴阳平衡则健康，阴阳失调则疾病，治疗的目的在于恢复阴阳的平衡状态。</p>
              </div>
              
              <div class="text-block block-3" :class="{ 'active': activeBlock >= 3 }">
                <h3>五行理论</h3>
                <p>木、火、土、金、水五行不仅代表五种物质，更象征五种功能属性。木主生发，火主温热，土主运化，金主肃降，水主润下。五行相生相克，维系着人体脏腑的正常功能，形成了完整的生理病理系统。</p>
              </div>
              
              <!-- 脏腑经络 -->
              <div class="text-block block-4" :class="{ 'active': activeBlock >= 4 }">
                <h3>脏腑学说</h3>
                <p>中医将人体内脏分为五脏（心、肝、脾、肺、肾）和六腑（胆、胃、大肠、小肠、膀胱、三焦）。五脏主藏精，六腑主传化，脏腑之间通过经络相连，功能协调，共同维持生命活动。</p>
              </div>
              
              <div class="text-block block-5" :class="{ 'active': activeBlock >= 5 }">
                <h3>经络系统</h3>
                <p>经络是人体气血运行的通道，包括十二正经、奇经八脉、十五络脉等。经络内连脏腑，外络肢节，沟通内外，运行气血。针灸、推拿等疗法正是通过刺激经络穴位来调节脏腑功能。</p>
              </div>
              
              <!-- 诊断方法 -->
              <div class="text-block block-6" :class="{ 'active': activeBlock >= 6 }">
                <h3>四诊合参</h3>
                <p>中医诊断通过望、闻、问、切四诊收集病情资料。望诊观神色形态，闻诊听声音嗅气味，问诊询病史症状，切诊按脉象触肌体。四诊信息相互印证，全面了解病情，为辨证论治提供依据。</p>
              </div>
              
              <div class="text-block block-7" :class="{ 'active': activeBlock >= 7 }">
                <h3>辨证论治</h3>
                <p>辨证是分析辨别证候，论治是根据证候确定治法。中医辨证有八纲辨证、脏腑辨证、六经辨证等多种方法。同病异治、异病同治体现了中医治疗的灵活性和个体化特点。</p>
              </div>
              
              <!-- 治疗养生 -->
              <div class="text-block block-8" :class="{ 'active': activeBlock >= 8 }">
                <h3>治未病</h3>
                <p>中医强调预防为主，治未病是最高明的医术。"未病先防"指养生保健，增强体质；"既病防变"指及早治疗，防止恶化；"瘥后防复"指病后调理，防止复发。</p>
              </div>
              
              <div class="text-block block-9" :class="{ 'active': activeBlock >= 9 }">
                <h3>中药学</h3>
                <p>中药讲究性味归经。四气有寒热温凉，五味有辛甘酸苦咸，归经指药物作用的部位。药物配伍有君臣佐使，方剂组成讲究配伍禁忌。中药治疗强调整体调节，标本兼治。</p>
              </div>
              
              <div class="text-block block-10" :class="{ 'active': activeBlock >= 10 }">
                <h3>针灸疗法</h3>
                <p>针灸通过刺激特定穴位，调节经络气血，平衡阴阳。针法有补泻手法，灸法有温通作用。现代研究表明针灸能调节神经系统、内分泌系统和免疫系统，具有广泛的治疗作用。</p>
              </div>
              
              <!-- 养生保健 -->
              <div class="text-block block-11" :class="{ 'active': activeBlock >= 11 }">
                <h3>四时养生</h3>
                <p>春季养肝，宜舒展情志；夏季养心，宜清热解暑；长夏养脾，宜健脾祛湿；秋季养肺，宜滋阴润燥；冬季养肾，宜温补藏精。顺应四时变化，调整饮食起居，是养生的重要原则。</p>
              </div>
              
              <div class="text-block block-12" :class="{ 'active': activeBlock >= 12 }">
                <h3>饮食养生</h3>
                <p>药食同源，食物也有四性五味。寒性食物清热，热性食物温补。饮食要五味调和，不可偏嗜。早餐宜好，午餐宜饱，晚餐宜少。食疗是中医养生的重要组成部分。</p>
              </div>
              
              <div class="text-block block-13" :class="{ 'active': activeBlock >= 13 }">
                <h3>情志养生</h3>
                <p>七情（喜怒忧思悲恐惊）过度都会损伤脏腑。怒伤肝，喜伤心，思伤脾，忧伤肺，恐伤肾。保持心态平和，情绪稳定，避免过激情绪，是养生的重要方面。</p>
              </div>
              
              <!-- 运动养生 -->
              <div class="text-block block-14" :class="{ 'active': activeBlock >= 14 }">
                <h3>导引养生</h3>
                <p>导引包括太极拳、八段锦、五禽戏等传统养生功法。通过舒缓的动作配合呼吸，疏通经络，调和气血，强身健体。现代研究证实这些功法能改善心肺功能，提高免疫力。</p>
              </div>
              
              <!-- 经典著作 -->
              <div class="text-block block-15" :class="{ 'active': activeBlock >= 15 }">
                <h3>经典医籍</h3>
                <p>《黄帝内经》奠定理论基础，《伤寒杂病论》确立辨证体系，《神农本草经》记载药物知识，《千金方》汇集诊疗经验。这些经典著作是中医学术的宝贵财富，至今仍指导临床实践。</p>
              </div>
              
              <!-- 现代发展 -->
              <div class="text-block block-16" :class="{ 'active': activeBlock >= 16 }">
                <h3>中西医结合</h3>
                <p>现代中医在继承传统的基础上，结合现代医学技术，形成了中西医结合的诊疗模式。中医的整体观念、辨证论治与西医的精确诊断、靶向治疗相互补充，为患者提供更优质的医疗服务。</p>
              </div>
              
              <!-- 未来展望 -->
              <div class="text-block block-17" :class="{ 'active': activeBlock >= 17 }">
                <h3>传承创新</h3>
                <p>中医发展既要传承经典，保持特色，又要与时俱进，创新发展。加强科学研究，探索作用机制，规范诊疗标准，培养专业人才，让中医智慧更好地造福人类健康。</p>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 结束语 -->
        <div class="end-section" :class="{ 'visible': showEndSection }">
          <div class="end-content">
            <h2>探索中医世界</h2>
            <p>每一段文字都是一个故事<br>每一次滚动都是一次发现</p>
            <div class="scroll-to-top">
              <ElSpace>
                <ElLink href="/chat">聊天</ElLink>
                <ElLink href="/management">管理</ElLink>
              </ElSpace>
            </div>
          </div>
        </div>
      </div>
    </ElScrollbar>
  </ElContainer>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { ElScrollbar } from 'element-plus'

// 响应式状态
const isFixedTextActive = ref(false)
const activeBlock = ref(0)
const showEndSection = ref(false)
const scrollbarRef = ref<InstanceType<typeof ElScrollbar>>()

// 滚动监听处理
const handleScroll = (e: { scrollTop: number }) => {
  const scrollTop = e.scrollTop
  const wrapper = document.querySelector('.scrollbar-wrapper') as HTMLElement
  
  if (!wrapper) return
  
  const wrapperHeight = wrapper.clientHeight
  const contentHeight = wrapper.scrollHeight
  
  // 控制固定文本粘性
  const heroSection = document.querySelector('.hero-section') as HTMLElement
  if (heroSection) {
    const heroSectionHeight = heroSection.clientHeight
    isFixedTextActive.value = scrollTop > heroSectionHeight * 0.7
  }
  
  // 控制段落激活状态
  const textBlocks = document.querySelectorAll('.text-block')
  if (textBlocks.length > 0) {
    let newActiveBlock = 0
    
    // 计算每个文本块的位置
    textBlocks.forEach((block, index) => {
      const blockElement = block as HTMLElement
      const blockTop = blockElement.offsetTop
      const blockHeight = blockElement.clientHeight
      
      // 当文本块进入视窗中心区域时激活
      if (scrollTop >= blockTop - wrapperHeight * 0.7 && 
          scrollTop <= blockTop + blockHeight - wrapperHeight * 0.3) {
        newActiveBlock = index + 1
      }
    })
    
    activeBlock.value = newActiveBlock
  }
  
  // 控制结束语显示
  const endSection = document.querySelector('.end-section') as HTMLElement
  if (endSection) {
    const endSectionTop = endSection.offsetTop
    showEndSection.value = scrollTop + wrapperHeight * 0.8 >= endSectionTop
  }
}

// 滚动到内容区域
const scrollToContent = () => {
  const heroSection = document.querySelector('.hero-section') as HTMLElement
  if (heroSection && scrollbarRef.value?.wrapRef) {
    const scrollDistance = heroSection.clientHeight * 0.9
    scrollbarRef.value.scrollTo({ top: scrollDistance, behavior: 'smooth' })
  }
}

// 滚动到顶部
const scrollToTop = () => {
  if (scrollbarRef.value?.wrapRef) {
    scrollbarRef.value.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

// 初始化
onMounted(() => {
  nextTick(() => {
    // 初始化滚动计算
    if (scrollbarRef.value?.wrapRef) {
      const scrollTop = scrollbarRef.value.wrapRef.scrollTop
      handleScroll({ scrollTop })
    }
  })
})
</script>

<style scoped>
.home-container {
  height: 100vh;
  background: #0a0a0a;
  color: #fff;
  font-family: 'Microsoft YaHei', sans-serif;
  position: relative;
  overflow: hidden;
}

.home-container:before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 30%, rgba(76, 0, 255, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 70%, rgba(255, 0, 128, 0.1) 0%, transparent 50%);
  z-index: 0;
  pointer-events: none;
}

/* 自定义滚动条样式 */
:deep(.custom-scrollbar) {
  height: calc(100vh - 60px);
  position: relative;
  z-index: 1;
}

:deep(.scrollbar-wrapper) {
  overflow-x: hidden !important;
  scroll-behavior: smooth;
  overscroll-behavior-y: contain;
}

:deep(.scrollbar-view) {
  min-height: 100%;
  display: flex;
  flex-direction: column;
}

:deep(.el-scrollbar__thumb) {
  background-color: rgba(102, 126, 234, 0.6) !important;
  transition: background-color 0.3s;
}

:deep(.el-scrollbar__thumb:hover) {
  background-color: rgba(102, 126, 234, 0.8) !important;
}

:deep(.el-scrollbar__bar.is-vertical) {
  width: 8px;
  right: 2px;
}

/* 滚动内容容器 */
.scroll-content {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 1;
}

/* 粘性头部 */
.sticky-header {
  position: sticky;
  top: 0;
  z-index: 1000;
  background: rgba(10, 10, 10, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 20px;
}

/* Hero 区域 */
.hero-section {
  width: 100%;
  height: 100vh;
  min-height: 600px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  padding: 0 20px;
  text-align: center;
  overflow: hidden;
}

.hero-content {
  position: relative;
  z-index: 2;
}

.hero-title {
  font-size: clamp(2.5rem, 8vw, 5rem);
  font-weight: 900;
  line-height: 1.2;
  margin: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
}

.hero-title .line {
  display: inline-block;
  overflow: hidden;
  position: relative;
  opacity: 0;
  transform: translateY(30px);
  animation: fadeInUp 1s ease forwards;
}

.hero-title .line:before {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #667eea, #764ba2);
  animation: underline 2s ease forwards 1s;
}

.hero-title .line-1 {
  font-size: clamp(2rem, 6vw, 4rem);
  color: #667eea;
  animation-delay: 0.2s;
}

.hero-title .line-2 {
  font-size: clamp(2.5rem, 8vw, 5rem);
  color: #fff;
  animation-delay: 0.4s;
}

.hero-title .line-3 {
  font-size: clamp(2rem, 6vw, 4rem);
  color: #764ba2;
  animation-delay: 0.6s;
}

.hero-title .line-4 {
  font-size: clamp(2.5rem, 8vw, 5rem);
  color: #fff;
  background: linear-gradient(90deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation-delay: 0.8s;
}

/* 滚动指示器 */
.scroll-indicator {
  position: absolute;
  bottom: 40px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  color: rgba(255, 255, 255, 0.6);
  animation: bounce 2s infinite;
  cursor: pointer;
  z-index: 2;
  transition: all 0.3s ease;
  padding: 10px;
  user-select: none;
}

.scroll-indicator:hover {
  color: #fff;
  transform: translateX(-50%) translateY(-5px);
}

.scroll-text {
  font-size: 0.875rem;
  letter-spacing: 2px;
  font-weight: 300;
  margin-bottom: 10px;
  text-transform: uppercase;
}

.scroll-indicator .arrow {
  font-size: 1.5rem;
  animation: arrowBounce 1.5s infinite;
}

/* 粘性文本容器 */
.sticky-text-container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  padding: 0 20px;
  z-index: 1;
  display: flex;
  justify-content: center;
}

/* 文本内容区域 */
.text-content {
  width: 100%;
  max-width: 800px;
  position: relative;
  padding: 2rem 0;
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 固定标题 */
.fixed-text {
  position: sticky;
  top: 50vh;
  left: 0;
  width: 100%;
  transform: translateY(-50%);
  text-align: center;
  margin-bottom: 50vh;
  opacity: 1;
  transition: all 0.5s ease;
  z-index: 10;
  pointer-events: none;
  padding: 2rem 0;
  background: transparent;
  backdrop-filter: none;
}

.fixed-text.sticky-active {
  opacity: 0.2;
  transform: translateY(-50%) scale(0.9);
}

.fixed-text h2 {
  font-size: clamp(2.5rem, 6vw, 4rem);
  font-weight: 700;
  margin: 0 0 1rem 0;
  background: linear-gradient(90deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  line-height: 1;
  transition: all 0.3s ease;
}

.fixed-text p {
  font-size: clamp(1rem, 2vw, 1.2rem);
  color: rgba(255, 255, 255, 0.7);
  font-weight: 300;
  letter-spacing: 2px;
  text-transform: uppercase;
  margin: 0;
  transition: all 0.3s ease;
}

/* 滚动文本区 */
.scrolling-text {
  position: relative;
  z-index: 5;
  padding: 0 1rem;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 文本块 */
.text-block {
  width: 100%;
  max-width: 700px;
  height: 100vh;
  min-height: 600px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  opacity: 0.1;
  transform: translateY(30px);
  transition: all 0.8s cubic-bezier(0.4, 0, 0.2, 1);
  padding: 0 2rem;
  position: relative;
  will-change: opacity, transform;
  scroll-snap-align: start;
  scroll-margin-top: 20px;
}

.text-block:before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 0;
  background: linear-gradient(to bottom, #667eea, #764ba2);
  border-radius: 2px;
  transition: height 0.8s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 1;
}

.text-block.active {
  opacity: 1;
  transform: translateY(0);
}

.text-block.active:before {
  height: 80%;
}

.text-block h3 {
  font-size: clamp(1.8rem, 4vw, 2.5rem);
  font-weight: 600;
  margin: 0 0 1.5rem 0;
  color: rgba(255, 255, 255, 0.6);
  padding-left: 2rem;
  position: relative;
  transition: all 0.5s ease;
  z-index: 2;
}

.text-block.active h3 {
  color: #667eea;
  transform: translateX(20px);
}

.text-block h3:before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 10px;
  height: 10px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.5s ease 0.3s;
  z-index: 3;
}

.text-block.active h3:before {
  opacity: 1;
}

.text-block p {
  font-size: clamp(1rem, 2vw, 1.2rem);
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.6);
  margin: 0;
  padding-left: 2rem;
  max-width: 600px;
  transition: all 0.5s ease 0.2s;
  opacity: 0.7;
  z-index: 2;
}

.text-block.active p {
  opacity: 1;
  transform: translateX(10px);
  color: rgba(255, 255, 255, 0.9);
}

/* 结束语 */
.end-section {
  width: 100%;
  height: 100vh;
  min-height: 600px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 0 20px;
  opacity: 0;
  transform: translateY(50px);
  transition: all 1s ease;
  position: relative;
  z-index: 2;
  pointer-events: none;
  scroll-snap-align: start;
}

.end-section.visible {
  opacity: 1;
  transform: translateY(0);
  pointer-events: all;
}

.end-content {
  max-width: 600px;
  padding: 2rem;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.end-content h2 {
  font-size: clamp(2rem, 5vw, 3rem);
  font-weight: 700;
  margin: 0 0 2rem 0;
  background: linear-gradient(90deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  line-height: 1.2;
}

.end-content p {
  font-size: clamp(1rem, 2vw, 1.2rem);
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.7);
  margin: 0 0 3rem 0;
}

.scroll-to-top {
  display: inline-block;
  padding: 1rem 2rem;
  background: linear-gradient(90deg, #667eea, #764ba2);
  color: white;
  border: none;
  border-radius: 30px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  text-transform: uppercase;
  letter-spacing: 1px;
  user-select: none;
}

.scroll-to-top:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
}

/* 关键帧动画 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes underline {
  from {
    width: 0;
  }
  to {
    width: 100%;
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0) translateX(-50%);
  }
  40% {
    transform: translateY(-10px) translateX(-50%);
  }
  60% {
    transform: translateY(-5px) translateX(-50%);
  }
}

@keyframes arrowBounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(10px);
  }
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .sticky-text-container {
    padding: 0 1rem;
  }
  
  .text-content {
    padding: 1rem 0;
  }
  
  .fixed-text {
    padding: 1rem 0;
  }
  
  .scrolling-text {
    padding: 0 0.5rem;
  }
  
  .text-block {
    padding: 0 1rem;
  }
  
  .text-block:before {
    width: 3px;
  }
  
  .text-block h3 {
    padding-left: 1.5rem;
  }
  
  .text-block p {
    padding-left: 1.5rem;
  }
}

@media (max-width: 768px) {
  .hero-section {
    min-height: 500px;
  }
  
  .text-block {
    min-height: 500px;
    padding: 0 1rem;
  }
  
  .text-block:before {
    width: 2px;
  }
  
  .text-block h3 {
    padding-left: 1.2rem;
    font-size: 1.6rem;
  }
  
  .text-block p {
    padding-left: 1.2rem;
    font-size: 1rem;
  }
  
  .end-content {
    padding: 1.5rem;
  }
  
  .sticky-header {
    padding: 0 10px;
  }
}

@media (max-width: 480px) {
  .hero-section {
    min-height: 400px;
  }
  
  .hero-title .line-1,
  .hero-title .line-3 {
    font-size: 1.8rem;
  }
  
  .hero-title .line-2,
  .hero-title .line-4 {
    font-size: 2.2rem;
  }
  
  .text-block {
    min-height: 400px;
    padding: 0 0.8rem;
  }
  
  .text-block h3 {
    padding-left: 1rem;
    font-size: 1.4rem;
    margin-bottom: 1rem;
  }
  
  .text-block p {
    padding-left: 1rem;
    font-size: 0.95rem;
    line-height: 1.6;
  }
  
  .scroll-to-top {
    padding: 0.8rem 1.5rem;
    font-size: 0.9rem;
  }
  
  .sticky-header {
    padding: 0 8px;
  }
}
</style>