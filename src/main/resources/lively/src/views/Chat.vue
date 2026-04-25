<template>
  <ElContainer class="home-container">
    <ElHeader>
      <Toolbar/>
    </ElHeader>
    <ElMain>

    </ElMain>
  </ElContainer>
</template>

<script setup lang="ts">
import {computed, type ComputedRef, onMounted, reactive} from "vue";
import Toolbar from "@/component/Toolbar.vue";
import type {Link} from "@/declaration/component.ts";

onMounted((): void => {
})
</script>

<style scoped>

.home-container{
  background: url("/background.jpeg") no-repeat fixed;
  background-size: 100% 100%;
  width: 100%;
  height: 100%
}
.home-container :deep(.el-header) {
  --el-header-padding: 0;
  padding: var(--el-header-padding);
}


</style>