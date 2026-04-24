<template>
  <div style="padding-right: 20px">
    <ElSpace wrap>
      <span style="user-select: none">
          {{nickname}}
      </span>
      <ElDropdown @command="handle">
        <ElAvatar :src="avatar">
          <ElIcon>
            <User/>
          </ElIcon>
        </ElAvatar>
        <template #dropdown>
          <ElDropdownItem v-if="isAuthenticated" command="/authentication/profile">
            个人资料
          </ElDropdownItem>
          <ElDropdownItem command="/management">
            管理
          </ElDropdownItem>
          <ElDropdownItem command="/announcement">
            <ElBadge :value="announcements.length" :hidden="announcements.length === 0" :max="99">
              公告
            </ElBadge>
          </ElDropdownItem>
          <ElDropdownItem v-if="isAuthenticated" command="/authentication/logout">
            注销
          </ElDropdownItem>
          <ElDropdownItem v-if="!isAuthenticated" command="/authentication/account">
            登录/注册
          </ElDropdownItem>
        </template>
      </ElDropdown>
    </ElSpace>
  </div>
</template>
<script setup lang="ts">

import {User} from "@element-plus/icons-vue";
import {type Router, useRouter} from "vue-router";
import {computed, type ComputedRef, onMounted, reactive} from "vue";
import {type Consumer, type MaybeInvalid, validate} from "semantic-typescript";
import type {Announcement, Authentication} from "@/declaration/entity.ts";
import {authenticationStore} from "@/stores/authentication.ts";
import {ElMessage, ElMessageBox, ElNotification} from "element-plus";
import {AnnouncementService, AuthenticationService, ConsumerService} from "../hooks/service.ts";

const router:Router = useRouter();

const authenticationWebsocket: WebSocket = new WebSocket("ws://localhost:8080/websocket/authentication");
authenticationWebsocket.addEventListener("open", () => {
  authenticationWebsocket.send("1");
});
authenticationWebsocket.addEventListener("message", (event: MessageEvent) => {
  
});
const authenticationService: AuthenticationService = new AuthenticationService();
const nickname: ComputedRef<string> = computed<string>((): string => {
  return authenticationStore().nickname.get("游客");
})
const isAuthenticated: ComputedRef<boolean> = computed<boolean>((): boolean => {
  return authenticationStore().authenticated;
});
const avatar: ComputedRef<string> = computed<string>((): string => {
  let authentication = authenticationStore().authentication;
  if(validate(authentication)){
    if(validate(authentication.principal)){
      return authentication.principal.avatar;
    }
  }
  return "";
});
const handle: Consumer<string> = (command: string): void => {
  switch (command){
    case "/authentication/profile":
      router.push({
        path: command
      });
      break;
    case "/authentication/account":
      router.push({
        path: command
      });
      break;
    case "/authentication/logout":
      ElMessageBox.confirm("确定要注销登录吗？", "提示", {
        type: "warning",
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then((): void => {
        authenticationService.logout().then((): void =>{
          router.push({
            path: "/"
          });
          ElMessage({
            message: "操作成功，正在返回首页",
            type: "success"
          });
        }, (): void => {
          ElMessage({
            message: "操作失败",
            type: "warning"
          });
        });
      });
      break;
    case "/management":
      router.push({
        path: "/management"
      });
      break;
    case "/announcement":
      announcements.forEach((announcement): void => {
        ElNotification({
          type: "primary",
          title: announcement.title,
          message: announcement.content
        })
      })
      break;
  }
};
const announcementService: AnnouncementService = new AnnouncementService();
const announcements: Array<Announcement> = reactive<Array<Announcement>>([]);
onMounted((): void => {
  announcementService.findAllPagedBy({
    page: 0,
    size: 10,
    total: 10,
    direction: "ASC",
    target: {}
  }).then((value): void => {
    announcements.length = 0;
    announcements.push(...value.content);
    announcements.push({
      ban: new Date(),
      content: "",
      edit: new Date(),
      id: "",
      lock: new Date(),
      spawn: new Date(),
      title: "",
      version: 0n
    });
  }, (): void => {
    ElMessage({
      message: "加载公告失败",
      type: "info"
    });
  });
})
</script>
<style scoped>

</style>