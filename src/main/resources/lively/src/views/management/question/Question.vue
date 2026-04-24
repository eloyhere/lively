<template>
  <Modulize :service="questionService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
    </template>
    <template #column>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {QuestionService} from "../../../hooks/service.ts";
import {reactive, type Reactive} from "vue";
import type {Query, Question} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
const questionService: QuestionService = new QuestionService();
const query: Reactive<Query<Question>> = reactive<Query<Question>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Question>> = reactive<Insert<Question>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Question>> = reactive<Update<Question>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Question>> = reactive<Array<Question>>([]);
</script>

<style scoped>

</style>