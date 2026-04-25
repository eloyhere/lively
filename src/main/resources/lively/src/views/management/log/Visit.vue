<template>
  <Modulize :service="visitService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
    </template>
    <template #column>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {VisitService} from "../../../hooks/service";
import {reactive, type Reactive} from "vue";
import type {Query, Visit} from "@/declaration/entity";
import type {Insert, Update} from "@/declaration/modulize";
const visitService: VisitService = new VisitService();
const query: Reactive<Query<Visit>> = reactive<Query<Visit>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Visit>> = reactive<Insert<Visit>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Visit>> = reactive<Update<Visit>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Visit>> = reactive<Array<Visit>>([]);
</script>

<style scoped>

</style>