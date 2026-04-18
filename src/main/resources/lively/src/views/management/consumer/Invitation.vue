<template>
  <ElContainer>
    <ElHeader>
      <ElTooltip effect="light" content="新增">
        <ElButton circle icon="plus" plain type="primary"></ElButton>
      </ElTooltip>
      <ElTooltip effect="light" content="删除">
        <ElButton circle icon="delete" plain type="danger"></ElButton>
      </ElTooltip>
      <ElTooltip effect="light" content="刷新">
        <ElButton circle icon="refresh" plain type="info"></ElButton>
      </ElTooltip>
    </ElHeader>
    <ElMain>
      <div style="width: 100%; height: 100%;display: flex; flex-direction: column; align-items: center; gap: 10px">
        <ElTable :data="data" border>
          <el-table-column type="selection" width="55" />
          <ElTableColumn label="id">

          </ElTableColumn>
          <ElTableColumn label="邀请码">

          </ElTableColumn>
          <ElTableColumn label="锁定时间">

          </ElTableColumn>
          <ElTableColumn label="禁用时间">

          </ElTableColumn>
          <ElTableColumn label="修改时间">

          </ElTableColumn>
          <ElTableColumn label="生成时间">

          </ElTableColumn>
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
        <ElPagination background :total="data.length"/>
      </div>
    </ElMain>
  </ElContainer>
</template>
<script setup lang="ts">

import type {Invitation} from "@/interaction/entity.ts";
import {reactive} from "vue";

const data: Array<Invitation> = reactive<Array<Invitation>>([{
  edit: new Date(),
  id: "",
  lock: new Date(),
  code: "",
  spawn: new Date(),
  version: 5n
}]);

</script>

<style scoped>

</style>