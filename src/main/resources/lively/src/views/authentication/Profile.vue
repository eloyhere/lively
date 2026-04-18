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

  </ElDescriptions>
</template>

<script setup lang="ts">

import {useAuthenticationStore} from "@/stores/authentication.ts";
import {computed, type ComputedRef} from "vue";
import {isPrimitive, type Primitive} from "semantic-typescript";
import type {Authentication, Consumer} from "@/interaction/entity.ts";
import {useGet} from "@/hooks/network.ts";
import {useSerializer} from "@/hooks/entity.ts";

const profile: ComputedRef<Map<keyof Consumer, Primitive>> = computed(() => {
  let result: Map<keyof Consumer, Primitive> = new Map<keyof Consumer, Primitive>();
  useAuthenticationStore().principal.ifPresent((consumer): void => {
    let key: keyof Consumer;
    for(key in consumer){
      let value: unknown = consumer[key];
      if(isPrimitive(value)){
        result.set(key, value);
      }
    }
  })
  return result;
});
</script>

<style scoped>

</style>