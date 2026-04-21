<template>
  <Modulize :service="authorityService">
    <template #search="scope">
      <ElFormItem label="id" prop="id" :rules="[
          {
            pattern: /^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$/,
            message: '请输入有效的uuid',
            trigger: 'blur'
          }
      ]">
        <ElInput placeholder="请输入id" v-model="scope.search.id" clearable/>
      </ElFormItem>
      <ElFormItem label="权限" prop="authority" :rules="[
          {
            max: 32,
            min: 5
          }
      ]">
        <ElInput placeholder="请输入权限" v-model="scope.search.authority" clearable/>
      </ElFormItem>
    </template>
    <template #column>
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
const authorityService: AuthorityService = new AuthorityService();
</script>



<style scoped>

</style>