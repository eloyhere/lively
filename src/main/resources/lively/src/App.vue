<template>
  <RouterView/>
</template>

<script setup lang="ts">

import {ElLoading} from "element-plus";
import {onMounted} from "vue";

const load = ElLoading.service({
  lock: true,
  text: 'Loading',
  background: 'rgba(0, 0, 0, 0.7)',
});

onMounted((): void => {
  load.close();
})
</script>

<style scoped>

</style>