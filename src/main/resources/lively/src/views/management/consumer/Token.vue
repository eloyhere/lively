<template>
  <Modulize :service="tokenService"></Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {TokenService} from "@/interaction/service.ts";
const tokenService: TokenService = new TokenService();
</script>



<style scoped>

</style>