<template>
  <Modulize :service="tokenService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
    </template>
    <template #column>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {TokenService} from "../../../hooks/service.ts";
import {reactive, type Reactive} from "vue";
import type {Query, Token} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
const tokenService: TokenService = new TokenService();
const query: Reactive<Query<Token>> = reactive<Query<Token>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Token>> = reactive<Insert<Token>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Token>> = reactive<Update<Token>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Token>> = reactive<Array<Token>>([]);
</script>

<style scoped>

</style>