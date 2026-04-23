<template>
  <ElForm :model="formData"></ElForm>
</template>
<script setup lang="ts">

import {reactive, type Reactive} from "vue";

const formData: Reactive<Record<string, any>> = reactive<Record<string, any>>({

});
import {useGet} from "@/hooks/network.ts";
useGet("http://localhost:8080/generation/management?module=''").then
</script>


<style scoped>

</style>