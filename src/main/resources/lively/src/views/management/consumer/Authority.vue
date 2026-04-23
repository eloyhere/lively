<template>
  <Modulize :service="authorityService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
      <ElFormItem label="id" prop="id" :rules="[
          {
            pattern: /^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$/,
            message: '请输入有效的uuid',
            trigger: 'blur'
          }
      ]">
        <ElInput placeholder="请输入id" v-model="search.id" clearable/>
      </ElFormItem>
      <ElFormItem label="权限" prop="authority" :rules="[
          {
            max: 32,
            min: 5,
            message: '长度在5-32个字符',
            trigger: 'blur'
          }
      ]">
        <ElInput placeholder="请输入权限" v-model="search.authority" clearable/>
      </ElFormItem>
    </template>
    <template #column>
      <ElTableColumn label="权限" prop="authority" sortable></ElTableColumn>
    </template>
    <template #insert="scope">
      <ElFormItem label="权限">
        <ElInput v-model="scope.insert.authority"/>
      </ElFormItem>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {AuthorityService} from "@/interaction/service.ts";
import type {Authority, Query} from "@/declaration/entity.ts";
import {reactive, type Reactive} from "vue";
import type {Insert, Update} from "@/declaration/modulize.ts";
const authorityService: AuthorityService = new AuthorityService();

const query: Reactive<Query<Authority>> = reactive<Query<Authority>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Authority>> = reactive<Insert<Authority>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Authority>> = reactive<Update<Authority>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Authority>> = reactive<Array<Authority>>([]);
</script>



<style scoped>

</style>