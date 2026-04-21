<template>
  <Modulize :service="consumerService">
    <template #search>

    </template>
    <template #column>

    </template>
  </Modulize>
</template>

<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {ConsumerService} from "@/interaction/service.ts";
const consumerService: ConsumerService = new ConsumerService();
</script>

<style scoped></style>