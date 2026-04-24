<template>
  <ElContainer>
    <ElHeader>
      <Toolbar/>
    </ElHeader>
    <ElMain>
      <ElDescriptions border style="user-select: none">
        <ElDescriptionsItem label="头像" :rowspan="2" align="center">
          <ElAvatar :src="profile.avatar"/>
        </ElDescriptionsItem>
        <ElDescriptionsItem label="id">
          {{profile.id}}
        </ElDescriptionsItem>
        <ElDescriptionsItem label="用户名">
          {{profile.username}}
        </ElDescriptionsItem>
        <ElDescriptionsItem label="昵称">
          {{profile.nickname}}
        </ElDescriptionsItem>
        <ElDescriptionsItem label="角色">
          <ElTag v-for="role in profile.roles">
            {{role.name}}
          </ElTag>
        </ElDescriptionsItem>
        <ElDescriptionsItem label="注册时间">
          {{profile.spawn}}
        </ElDescriptionsItem>
        <ElDescriptionsItem label="权限">
          <ElSpace wrap>
            <ElTag v-for="authority in profile.roles.flatMap((role: Role) => {
            return [...role.authorities];
          })">
              {{authority.authority}}
            </ElTag>
          </ElSpace>
        </ElDescriptionsItem>
      </ElDescriptions>
    </ElMain>
  </ElContainer>
</template>

<script setup lang="ts">

import {authenticationStore} from "@/stores/authentication.ts";
import {computed, type ComputedRef} from "vue";
import type {Consumer, Role} from "@/declaration/entity";
import Toolbar from "@/component/Toolbar.vue";

const profile: ComputedRef<Consumer> = computed<Consumer>((): Consumer => {
  return authenticationStore().value?.principal!;
});
</script>

<style scoped>

</style>