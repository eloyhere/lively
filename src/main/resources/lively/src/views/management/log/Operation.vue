<template>
  <Modulize :service="operationService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
    </template>
    <template #column>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {OperationService} from "../../../hooks/service";
import {reactive, type Reactive} from "vue";
import type {Query, Operation} from "@/declaration/entity";
import type {Insert, Update} from "@/declaration/modulize";
const operationService: OperationService = new OperationService();
const query: Reactive<Query<Operation>> = reactive<Query<Operation>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Operation>> = reactive<Insert<Operation>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Operation>> = reactive<Update<Operation>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Operation>> = reactive<Array<Operation>>([]);
</script>

<style scoped>

</style>