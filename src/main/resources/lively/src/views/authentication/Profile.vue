<template>
  <ElDescriptions border>
    <ElDescriptionsItem label="头像" :rowspan="2" align="center">
      <ElAvatar :src="profile.get('avatar')"/>
    </ElDescriptionsItem>
    <ElDescriptionsItem label="id">
      {{profile.get("id")}}
    </ElDescriptionsItem>
    <ElDescriptionsItem label="用户名">
      {{profile.get("username")}}
    </ElDescriptionsItem>
    <ElDescriptionsItem label="昵称">
      {{profile.get("nickname")}}
    </ElDescriptionsItem>
    <ElDescriptionsItem label="拥有角色">
      <span v-for="role in profile."></span>
    </ElDescriptionsItem>
  </ElDescriptions>
</template>

<script setup lang="ts">

import {useAuthenticationStore} from "@/stores/authentication.ts";
import {computed, type ComputedRef} from "vue";
import {isPrimitive, type MaybeInvalid, type Primitive, validate} from "semantic-typescript";
import type {Authentication, Consumer, Role} from "@/declaration/entity";
import {useGet} from "@/hooks/network.ts";
import {useSerialization} from "@/hooks/serialization.ts";

const profile: ComputedRef<Consumer> = computed<Consumer>((): Consumer => {
  let authentication: MaybeInvalid<Authentication> = useAuthenticationStore().at;

});
</script>

<style scoped>

</style>