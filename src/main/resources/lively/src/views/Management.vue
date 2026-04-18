<template>
  <ElContainer>
    <ElHeader class="header">
      <div>
        <ElButton icon="Cherry" @click="() => router.push({
           path: '/'
        })"></ElButton>
      </div>
      <div>
        <ElScrollbar always style="width: 100%">
          <ElSpace wrap>
            <ElTag v-for="(route, index) in history"
                   :type="current === route.path ? 'primary' : 'info'"
                   style="cursor: pointer"
                   @click="() => {
              router.replace({
                path: route.path
              });
            }"
            @close="()=>{
              if(current === route.path){
                if(history.size > 1){
                  let temp = Array.from(history);
                  let previous: RouteLocationNormalizedLoadedGeneric = temp[temp.length - 1] as unknown as RouteLocationNormalizedLoadedGeneric;
                  router.replace({
                    path: previous.path
                  });
                  history.delete(previous);
                }
              }else{
                history.delete(route);
              }
            }"
                   closable>
              {{route.meta.title}}
            </ElTag>
          </ElSpace>
        </ElScrollbar>
      </div>
      <div>
        <span>
          你好，{{nickname}}
        </span>
        <ElTooltip content="通知" placement="bottom" effect="light">
          <ElButton icon="Bell" circle/>
        </ElTooltip>
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
            <ElDropdownItem v-if="isAuthenticated" command="/authentication/logout">
              注销
            </ElDropdownItem>
            <ElDropdownItem v-if="!isAuthenticated" command="/authentication/account">
              登录/注册
            </ElDropdownItem>
          </template>
        </ElDropdown>
      </div>
    </ElHeader>
    <ElContainer>
      <ElAside width="180px" style="height: calc(100vh - 60px)">
        <ElScrollbar height="100%">
          <ElMenu :default-active="current" router style="user-select: none">
            <ElMenuItem index="/management">
              <ElIcon>
                <House/>
              </ElIcon>
              <span>管理</span>
            </ElMenuItem>
            <ElMenuItemGroup title="数据">
              <ElMenuItem index="/management/authority">
                <ElIcon>
                  <Lock/>
                </ElIcon>
                <span>权限管理</span>
              </ElMenuItem>
              <ElMenuItem index="/management/role">
                <ElIcon>
                  <User/>
                </ElIcon>
                <span>角色管理</span>
              </ElMenuItem>
              <ElMenuItem index="/management/consumer">
                <ElIcon>
                  <UserFilled/>
                </ElIcon>
                <span>用户管理</span>
              </ElMenuItem>
              <ElMenuItem index="/management/token">
                <ElIcon>
                  <Ticket/>
                </ElIcon>
                <span>凭据管理</span>
              </ElMenuItem>
              <ElMenuItem index="/management/invitation">
                <ElIcon>
                  <Present/>
                </ElIcon>
                <span>邀请管理</span>
              </ElMenuItem>
            </ElMenuItemGroup>
            <ElMenuItemGroup title="人文">
              <ElMenuItem index="/management/book">
                <ElIcon>
                  <Book/>
                </ElIcon>
                <span>书籍管理</span>
              </ElMenuItem>
              <ElMenuItem index="/management/chat">
                <ElIcon>
                  <Book/>
                </ElIcon>
                <span>聊天管理</span>
              </ElMenuItem>
            </ElMenuItemGroup>
            <ElMenuItemGroup title="日志">
              <ElMenuItem index="/management/operation">
                <ElIcon>
                  <Operation/>
                </ElIcon>
                <span>操作日志</span>
              </ElMenuItem>
              <ElMenuItem index="/management/online">
                <ElIcon>
                  <Coffee/>
                </ElIcon>
                <span>在线日志</span>
              </ElMenuItem>
            </ElMenuItemGroup>
            <ElMenuItemGroup title="监控">
              <ElMenuItem index="/management/performance">
                <ElIcon>
                  <PieChart/>
                </ElIcon>
                <span>性能用量</span>
              </ElMenuItem>
              <ElMenuItem index="/management/sequence">
                <ElIcon>
                  <List/>
                </ElIcon>
                <span>数据执行</span>
              </ElMenuItem>
            </ElMenuItemGroup>
          </ElMenu>
        </ElScrollbar>
      </ElAside>
      <ElMain>
        <RouterView/>
      </ElMain>
    </ElContainer>
  </ElContainer>
</template>

<script setup lang="ts">

import {type RouteLocationNormalizedLoadedGeneric, type Router, useRouter} from "vue-router";
import {computed, type ComputedRef, onMounted, reactive, watch} from "vue";
import {
  Cherry,
  Coffee,
  House,
  List,
  Lock,
  Operation,
  PieChart,
  Present,
  Ticket,
  User,
  UserFilled
} from "@element-plus/icons-vue";
import {type Consumer, type MaybeInvalid, validate} from "semantic-typescript";
import type {Authentication} from "@/interaction/entity.ts";
import {useAuthenticationStore} from "@/stores/authentication.ts";
import {useGet} from "@/hooks/network.ts";
import {ElMessage} from "element-plus";
import Book from "@/views/management/book/Book.vue";

const router: Router = useRouter();
const current: ComputedRef<string> = computed<string>((): string => {
  return router.currentRoute.value.path;
});
const history: Set<RouteLocationNormalizedLoadedGeneric> = reactive<Set<RouteLocationNormalizedLoadedGeneric>>(new Set<RouteLocationNormalizedLoadedGeneric>());
const authentication: ComputedRef<MaybeInvalid<Authentication>> = computed<MaybeInvalid<Authentication>>((): MaybeInvalid<Authentication> => {
  return useAuthenticationStore().authentication;
});
const nickname: ComputedRef<string> = computed<string>((): string => {
  return useAuthenticationStore().nickname.get("游客");
})
const isAuthenticated: ComputedRef<boolean> = computed<boolean>((): boolean => {
  return useAuthenticationStore().authenticated;
});
const avatar: ComputedRef<string> = computed<string>((): string => {
  if(validate(authentication.value)){
    if(validate(authentication.value.principal)){
      return authentication.value.principal.avatar;
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
      useGet("http://localhost/authentication/logout").then((response) => {
        if(response.status === 200){
          ElMessage({
            message: "操作成功，正在返回首页。",
            type: "success"
          });
          useAuthenticationStore().removeAuthentication();
        }
      });
      break;
    case "/management":
      router.push({
        path: "/management"
      });
      break;
  }
};
onMounted(() => {
  history.add(router.currentRoute.value);
  watch(router.currentRoute, (n, o) => {
    let temp = Array.from(history);
    if(!temp.some((route): boolean => route.path === n.path)){
      history.add(n);
    }
  });
})
</script>

<style scoped>
.header{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  user-select: none;
}

.header > div:first-child {
  flex: none;
  text-align: left;
}

.header > div:not(:first-child):not(:last-child) {
  text-align: center;
  flex: 1;
}

.header > div:last-child {
  flex: none;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
  gap: 10px;
}
</style>