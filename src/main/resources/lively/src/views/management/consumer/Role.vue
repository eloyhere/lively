<template>
  <Modulize :service="roleServive">
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {RoleService} from "@/interaction/service.ts";

const roleServive : RoleService = new RoleService();
</script>



<style scoped>

</style>