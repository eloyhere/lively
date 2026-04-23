<template>
  <Modulize :service="chatService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
    </template>
    <template #column>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {ChatService} from "@/interaction/service.ts";
import {reactive, type Reactive} from "vue";
import type {Query, Chat} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
const chatService: ChatService = new ChatService();
const query: Reactive<Query<Chat>> = reactive<Query<Chat>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Chat>> = reactive<Insert<Chat>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Chat>> = reactive<Update<Chat>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Chat>> = reactive<Array<Chat>>([]);
</script>

<style scoped>

</style>