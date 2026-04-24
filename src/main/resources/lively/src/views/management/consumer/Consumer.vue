<template>
  <Modulize :service="consumerService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search>
      <ElFormItem label="用户名" prop="username">
        <ElInput placeholder="请输入用户名" clearable/>
      </ElFormItem>
      <ElFormItem label="昵称" prop="nickname">
        <ElInput placeholder="请输入昵称" clearable/>
      </ElFormItem>
    </template>
    <template #column>
      <ElTableColumn label="头像" prop="avatar">
        <template #default="scope">
          <ElImage :src="scope.row.avatar">
            <template #error>
              <ElIcon>
                <Picture/>
              </ElIcon>
            </template>
          </ElImage>
        </template>
      </ElTableColumn>
      <ElTableColumn label="用户名" prop="username" sortable></ElTableColumn>
      <ElTableColumn label="昵称" prop="nickname" sortable></ElTableColumn>
      <ElTableColumn label="角色" prop="authorities">
        <template #default="scope">
          <ElTag v-for="role in scope.row.roles">
            {{role.name}}
          </ElTag>
        </template>
      </ElTableColumn>
    </template>
  </Modulize>
</template>

<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {ConsumerService} from "../../../hooks/service.ts";
import {reactive, type Reactive} from "vue";
import type {Consumer, Query} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
import {Picture} from "@element-plus/icons-vue";
const consumerService: ConsumerService = new ConsumerService();

const query: Reactive<Query<Consumer>> = reactive<Query<Consumer>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Consumer>> = reactive<Insert<Consumer>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Consumer>> = reactive<Update<Consumer>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Consumer>> = reactive<Array<Consumer>>([]);
</script>

<style scoped></style>