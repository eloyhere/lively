<template>
  <div class="tcm-layout">
    <div class="tcm-layout__container">
      <RouterView />
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.tcm-layout {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  background: linear-gradient(180deg, #faf6f1 0%, #f5ebe0 50%, #fef9f5 100%);
  display: flex;
  justify-content: center;
}
.tcm-layout__container {
  width: 100%;
  max-width: 1200px;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  background: #fffcf8;
  box-shadow: 0 0 80px rgba(139, 90, 43, 0.06);
}
</style>
