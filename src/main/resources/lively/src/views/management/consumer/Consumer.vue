<template>
  <ElContainer style="user-select: none">
    <ElHeader>
      <ElForm inline :model="query.target">
        <ElFormItem label="id" prop="id">
          <ElInput placeholder="请输入id" clearable/>
        </ElFormItem>
        <ElFormItem label="权限" prop="authority">
          <ElInput placeholder="请输入权限" clearable/>
        </ElFormItem>
        <ElFormItem label="描述" prop="description">
          <ElInput placeholder="请输入描述" clearable/>
        </ElFormItem>
        <ElFormItem>
          <ElTooltip effect="light" placement="top" content="搜索">
            <ElButton icon="search" circle plain type="primary"/>
          </ElTooltip>
          <ElTooltip effect="light" placement="top" content="重置">
            <ElButton icon="refresh" circle plain type="info"/>
          </ElTooltip>
        </ElFormItem>
      </ElForm>
    </ElHeader>
    <ElContainer>
      <ElMain style="display: flex; flex-direction: column; justify-content: center; align-items: flex-start; gap: 10px">
        <ElSpace wrap style="margin-left: 10px">
          <ElTooltip effect="light" placement="top" content="新增">
            <ElButton icon="plus" circle plain type="primary"/>
          </ElTooltip>
          <ElTooltip effect="light" placement="top" content="删除">
            <ElButton icon="delete" circle plain type="danger"/>
          </ElTooltip>
          <ElTooltip effect="light" placement="top" content="刷新">
            <ElButton icon="refresh" circle plain type="info"/>
          </ElTooltip>
        </ElSpace>
        <ElTable :data="data" v-loading="load" border>
          <ElTableColumn label="id" prop="id"></ElTableColumn>
          <ElTableColumn label="权限" prop="authority"></ElTableColumn>
          <ElTableColumn label="描述" prop="description"></ElTableColumn>
          <ElTableColumn label="锁定" prop="lock"></ElTableColumn>
          <ElTableColumn label="禁用" prop="ban"></ElTableColumn>
          <ElTableColumn label="创建时间" prop="spawn"></ElTableColumn>
          <ElTableColumn label="修改时间" prop="spawn"></ElTableColumn>
          <ElTableColumn fixed="right" label="操作">
            <template #default="scope">
              <ElTooltip effect="light" placement="top" content="修改">
                <ElButton size="small" type="info" plain icon="edit" circle />
              </ElTooltip>
              <ElTooltip effect="light" placement="top" content="删除">
                <ElButton size="small" type="danger" plain icon="delete" circle/>
              </ElTooltip>
              <ElTooltip effect="light" placement="top" content="锁定">
                <ElButton size="small" type="info" plain icon="lock" circle/>
              </ElTooltip>
              <ElTooltip effect="light" placement="top" content="禁用">
                <ElButton size="small" type="warning" plain icon="hide" circle/>
              </ElTooltip>
            </template>
          </ElTableColumn>
        </ElTable>
        <ElPagination v-model:current-page="query.page" v-model:page-size="query.size" :total="query.total"/>
      </ElMain>
      <ElFooter>

      </ElFooter>
    </ElContainer>
  </ElContainer>
</template>
<script setup lang="ts">

import type {Authority, Page, Query} from "@/declaration/entity.ts";
import {onMounted, reactive, ref, type Ref} from "vue";
import {AuthorityService} from "@/interaction/service.ts";

const query: Query<Authority> = reactive<Query<Authority>>({
  page: 0,
  size: 10,
  total: 0,
  target: {}
});

const load: Ref<boolean> = ref<boolean>(true);
const data: Array<Authority> = reactive<Array<Authority>>([]);

const authorityService: AuthorityService = new AuthorityService();

onMounted((): void => {
  authorityService.findAllPagedBy(query).then((page: Page<Authority>): void => {
    query.page = page.number;
    query.size = page.size;
    query.total = page.totalElements;
    load.value = false;
  }, () => {
    load.value = false;
  });
});
</script>



<style scoped>

</style>