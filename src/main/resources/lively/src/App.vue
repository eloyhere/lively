<template>
  <RouterView/>
</template>

<script setup lang="ts">

import {ElLoading, ElMessage} from "element-plus";
import {onMounted} from "vue";
import {useAuthenticationStore} from "@/stores/authentication.ts";
import {useGet} from "@/hooks/network.ts";

const load = ElLoading.service({
  lock: true,
  text: 'Loading',
  background: 'rgba(0, 0, 0, 0.7)',
});

onMounted((): void => {
  useAuthenticationStore().auto();
  useGet("http://localhost/consumer/countBy")
      .then((response: Response) => response.json())
      .then(BigInt)
      .then(console.log);
  load.close();
})
</script>

<style scoped>

</style>