<template>
  <ElContainer class="home-container">
    <ElHeader>
      <Toolbar v-model="links"/>
    </ElHeader>
    <ElMain>

    </ElMain>
  </ElContainer>
</template>

<script setup lang="ts">
import {computed, type ComputedRef, onMounted, reactive} from "vue";
import Toolbar from "@/component/Toolbar.vue";
import type {Link} from "@/declaration/component.ts";

const links: Array<Link> = reactive<Array<Link>>([
  { text: "首页", icon: "house", href: "/" },
  { text: "中医药知识", icon: "cherry", href: "/knowledge" },
  { text: "方剂查询", icon: "document", href: "/prescription" },
  { text: "在线咨询", icon: "chat-dot-round", href: "/consultation" },
  { text: "关于我们", icon: "info-filled", href: "/about" }
]);
onMounted((): void => {
})
</script>

<style scoped>

.home-container{
  background: url("http://localhost:8080/background.jpeg") no-repeat fixed;
  background-size: 100% 100%;
  width: 100%;
  height: 100%
}
.home-container :deep(.el-header) {
  --el-header-padding: 0;
  padding: var(--el-header-padding);
}


</style>