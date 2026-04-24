<template>
  <Modulize :service="choiceService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
    </template>
    <template #column>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {ChoiceService} from "../../../hooks/service.ts";
import {reactive, type Reactive} from "vue";
import type {Query, Choice} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
const choiceService: ChoiceService = new ChoiceService();
const query: Reactive<Query<Choice>> = reactive<Query<Choice>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Choice>> = reactive<Insert<Choice>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Choice>> = reactive<Update<Choice>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Choice>> = reactive<Array<Choice>>([]);
</script>

<style scoped>

</style>