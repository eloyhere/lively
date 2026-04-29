<template>
  <RouterView />
</template>

<script setup lang="ts">

import { ElLoading, ElMessage } from "element-plus";
import {onMounted, shallowRef} from "vue";
import { eventStore } from "./stores/event";
import type { Authentication } from "./declaration/entity";
import { useRouter } from "vue-router";
import { authenticationStore } from "./stores/authentication";

const router = useRouter();
const load = ElLoading.service({
  lock: true,
  text: "加载中...",
  background: "rgba(0, 0, 0, 0.7)",
});
onMounted((): void => {
  eventStore().subscribe("Register", (authentication: unknown) => {
    ElMessage({
      message: "注册成功",
      type: "success",
      plain: true,
      grouping: true
    });
    authenticationStore().setAuthentication(authentication as Authentication);
    router.replace({
      path: "/home"
    });
  });
  eventStore().subscribe("Login", (authenticaiton: unknown) => {
    ElMessage({
      message: "登录成功",
      type: "success",
      plain: true,
      grouping: true
    });
    authenticationStore().setAuthentication(authenticaiton as Authentication);
    router.replace({
      path: "/home"
    });
  });
  eventStore().subscribe("Logout", () => {
    ElMessage({
      message: "已退出登录，正在返回首页",
      type: "info",
      plain: true,
      grouping: true
    });
    authenticationStore().removeAuthentication();
    router.replace({
      path: "/"
    });
  });
  eventStore().subscribe("Expire", () => {
    ElMessage({
      message: "登录失效，正在返回首页",
      type: "info",
      plain: true,
      grouping: true
    });
    authenticationStore().expire();
    router.replace({
      path: "/home"
    });
  });
  load.close();

});

</script>

<style scoped></style>