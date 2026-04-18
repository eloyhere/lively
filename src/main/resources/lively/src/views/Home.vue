<template>
  <ElContainer>
    <ElHeader class="header">
      <div>1</div>
      <div>1</div>
      <div>
        <ElSpace wrap>
          <span>
          你好，{{nickname}}
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
              <ElDropdownItem command="/notification">
                <ElBadge :value="105" :max="99">
                  通知
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
    </ElHeader>
    <ElMain>

    </ElMain>
  </ElContainer>
</template>

<script setup lang="ts">
import {computed, type ComputedRef, onMounted} from "vue";
import {type Consumer, type MaybeInvalid, type Supplier, validate} from "semantic-typescript";
import type {Authentication} from "@/interaction/entity.ts";
import {useAuthenticationStore} from "@/stores/authentication.ts";
import {User} from "@element-plus/icons-vue";
import {type Router, useRouter} from "vue-router";
import {ElMessage} from "element-plus";
import {useGet} from "@/hooks/network.ts";
import {type Serializer, useSerializer} from "@/hooks/entity.ts";

const router:Router = useRouter();

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
        if(response.status === 203){
          window.document.cookie = "";
          ElMessage({
            message: "注销成功。",
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