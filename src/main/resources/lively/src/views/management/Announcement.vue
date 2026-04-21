<template>
  <Modulize :service="announcementService">

  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {AnnouncementService} from "@/interaction/service.ts";
const announcementService: AnnouncementService = new AnnouncementService();

</script>



<style scoped>

</style>