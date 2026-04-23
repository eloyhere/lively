<template>
  <Modulize :service="messageService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
    </template>
    <template #column>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {MessageService} from "@/interaction/service.ts";
import {reactive, type Reactive} from "vue";
import type {Query, Message} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
const messageService: MessageService = new MessageService();
const query: Reactive<Query<Message>> = reactive<Query<Message>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Message>> = reactive<Insert<Message>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Message>> = reactive<Update<Message>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Message>> = reactive<Array<Message>>([]);
</script>

<style scoped>

</style>