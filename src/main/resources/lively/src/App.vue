<template>
  <RouterView/>
</template>

<script setup lang="ts">

import {ElLoading, ElMessage} from "element-plus";
import {onMounted} from "vue";
import {useAuthenticationStore} from "@/stores/authentication.ts";

const load = ElLoading.service({
  lock: true,
  text: "加载中...",
  background: 'rgba(0, 0, 0, 0.7)',
});

onMounted((): void => {
  window.addEventListener("load", () => {
    useAuthenticationStore().auto();
  })
  load.close();
})
</script>

<style scoped>

</style>