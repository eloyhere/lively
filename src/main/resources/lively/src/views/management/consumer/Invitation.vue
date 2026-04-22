<template>
  <Modulize :service="invitationService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="scope">
      <ElFormItem prop="code" label="邀请码">
        <ElInput v-model="scope.search.code"/>
      </ElFormItem>
    </template>
    <template #column>
      <ElTableColumn label="邀请码" prop="code"></ElTableColumn>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {InvitationService} from "@/interaction/service.ts";
import {reactive, type Reactive} from "vue";
import type {Authority, Query, Invitation} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
const invitationService: InvitationService = new InvitationService();
const query: Reactive<Query<Invitation>> = reactive<Query<Invitation>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Invitation>> = reactive<Insert<Invitation>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Invitation>> = reactive<Update<Invitation>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Invitation>> = reactive<Array<Invitation>>([]);
</script>

<style scoped>

</style>