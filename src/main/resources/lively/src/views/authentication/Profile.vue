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
        <ElDescriptionsItem label="权限">
          <ElTag v-for="authority in profile.roles.flatMap((role) => role.authorities)">
            {{authority}}
          </ElTag>
        </ElDescriptionsItem>
        <ElDescriptionsItem label="注册时间">
          {{profile.spawn}}
        </ElDescriptionsItem>
      </ElDescriptions>
    </ElMain>
  </ElContainer>
</template>

<script setup lang="ts">

import {useAuthenticationStore} from "@/stores/authentication.ts";
import {computed, type ComputedRef} from "vue";
import type { Consumer } from "@/declaration/entity";
import Toolbar from "@/component/Toolbar.vue";

const profile: ComputedRef<Consumer> = computed<Consumer>((): Consumer => {
  return useAuthenticationStore().value?.principal!;
});
</script>

<style scoped>

</style>