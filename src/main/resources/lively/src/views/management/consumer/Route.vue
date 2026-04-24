<template>
  <Modulize :service="routeService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
    </template>
    <template #column>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {RouteService} from "../../../hooks/service.ts";
import {reactive, type Reactive} from "vue";
import type {Query, Route} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
const routeService: RouteService = new RouteService();
const query: Reactive<Query<Route>> = reactive<Query<Route>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Route>> = reactive<Insert<Route>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Route>> = reactive<Update<Route>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Route>> = reactive<Array<Route>>([]);
</script>

<style scoped>

</style>