<template>
  <ElContainer>
    <ElHeader class="header">
      <Toolbar v-model="links"/>
    </ElHeader>
    <ElContainer style="height: calc(100vh - 60px); width: 100vw">
      <ElAside width="180px" style="height: calc(100vh - 60px)">
        <ElScrollbar height="100%">
          <ElMenu :default-active="current.path" router style="user-select: none; height: calc(100vh - 60px)">
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
      <ElMain style="height: calc(100vh - 60px); width: calc(100vw - 180px) ">
        <ElTabs type="card" v-model="active" closable @tab-remove="removeTab" style="height: calc(100vh - 60px); width: calc(100vw - 180px)">
          <ElTabPane
              v-for="route in history"
              :name="route.path"
              :key="route.path"
              :label="route.meta.title">
            <RouterView/>
          </ElTabPane>
        </ElTabs>
      </ElMain>
    </ElContainer>
  </ElContainer>
</template>

<script setup lang="ts">

import {type RouteLocationNormalizedLoadedGeneric, type Router, useRouter} from "vue-router";
import {computed, type ComputedRef, onMounted, reactive, ref, type Ref, watch} from "vue";
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
import Book from "@/views/management/book/Book.vue";
import TagToolbar from "@/component/TagToolbar.vue";
import type {Link, Tag} from "@/declaration/component.ts";
import Toolbar from "@/component/Toolbar.vue";
import type {Consumer} from "semantic-typescript";

const router: Router = useRouter();
const current: ComputedRef<RouteLocationNormalizedLoadedGeneric> = computed<RouteLocationNormalizedLoadedGeneric>((): RouteLocationNormalizedLoadedGeneric => {
  return router.currentRoute.value;
});
const path: ComputedRef<string> = computed<string>((): string => {
  return current.value.path;
})
const links: Array<Link> = reactive<Array<Link>>([
  { text: '首页', icon: 'house', href: '/' },
  { text: '中医药知识', icon: 'cherry', href: '/knowledge' },
  { text: '方剂查询', icon: 'document', href: '/prescription' },
  { text: '在线咨询', icon: 'chat-dot-round', href: '/consultation' },
  { text: '关于我们', icon: 'info-filled', href: '/about' }
]);
const active: Ref<string> = ref<string>(path.value);
const history: Array<RouteLocationNormalizedLoadedGeneric> = reactive<Array<RouteLocationNormalizedLoadedGeneric>>([]);
const removeTab: Consumer<string> = (name: string): void => {
  if(history.length === 1){
    router.replace({
      path: "/management"
    });
    history.length = 0;
  }else{
    let temporary: Array<RouteLocationNormalizedLoadedGeneric> = [...history.filter((route) => route.path !== name)];
    history.length = 0;
    history.push(...temporary);
  }
};
onMounted(():void => {
  watch(current, (n) => {
    if(n.path !== "/management"){
      if(!history.some((route) => route.path === n.path)){
        history.push(n);
      }
    }
    active.value = n.path;
  });
  watch(active, (n) => {
    router.replace({
      path: n
    });
  });
});
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

:deep(.el-main) {
  --el-main-padding: 0;
  padding: var(--el-main-padding);
}
</style>