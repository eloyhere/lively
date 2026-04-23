<template>
  <Modulize :service="roleService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
    </template>
    <template #column>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {RoleService} from "@/interaction/service.ts";
import {reactive, type Reactive} from "vue";
import type {Query, Role} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
const roleService: RoleService = new RoleService();
const query: Reactive<Query<Role>> = reactive<Query<Role>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Role>> = reactive<Insert<Role>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Role>> = reactive<Update<Role>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Role>> = reactive<Array<Role>>([]);
</script>

<style scoped>

</style>