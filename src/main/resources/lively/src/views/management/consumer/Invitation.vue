<template>
  <Modulize :service="invitationService">
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
const invitationService: InvitationService = new InvitationService();
</script>

<style scoped>

</style>