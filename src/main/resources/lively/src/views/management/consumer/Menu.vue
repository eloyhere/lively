<template>
  <Modulize :service="menuService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
    </template>
    <template #column>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {MenuService} from "../../../hooks/service.ts";
import {reactive, type Reactive} from "vue";
import type {Query, Menu} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
const menuService: MenuService = new MenuService();
const query: Reactive<Query<Menu>> = reactive<Query<Menu>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Menu>> = reactive<Insert<Menu>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Menu>> = reactive<Update<Menu>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Menu>> = reactive<Array<Menu>>([]);
</script>

<style scoped>

</style>