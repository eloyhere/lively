<template>
  <ElContainer class="container">
    <div style="display: grid; width: 200px; height: 160px;">
      <div style="grid-area: 1/1;">
        <transition name="fade" mode="out-in">
          <ElImage :src="smile" v-if="src === smile" fit="fill" style="width: 200px; height: 160px">
            <template #error>
              <ElIcon>
                <Picture/>
              </ElIcon>
            </template>
          </ElImage>
        </transition>
      </div>
      <div style="grid-area: 1/1;">
        <transition name="fade" mode="out-in">
          <ElImage :src="closeEyes" v-if="src === closeEyes" fit="fill" style="width: 200px; height: 160px">
            <template #error>
              <ElIcon>
                <Picture/>
              </ElIcon>
            </template>
          </ElImage>
        </transition>
      </div>
    </div>

    <el-tabs v-model="title" class="panel">
      <el-tab-pane label="用户名密码登录" name="UsernameAndPasswordLogin">
        <ElForm v-model="loginFormData">
          <ElFormItem label="用户名">
            <ElInput
                placeholder="请输入用户名"
                clearable
                v-model="loginFormData.username"></ElInput>
          </ElFormItem>
        </ElForm>
        <ElForm>
          <ElFormItem label="密码">
            <ElInput
                placeholder="请输入密码"
                clearable show-password
                @focus="toCloseEyes()"
                @blur="toSmile()"
                v-model="loginFormData.password"
            ></ElInput>
          </ElFormItem>
          <ElFormItem>
            <ElButton plain type="primary" icon="checked" @click="performLogin()">登录</ElButton>
            <ElButton plain type="info" icon="refresh">重置</ElButton>
          </ElFormItem>
        </ElForm>
      </el-tab-pane>
      <el-tab-pane label="认证码登录" name="CheckCodeLogin">
        <ElForm v-model="loginFormData">
          <ElFormItem label="用户名">
            <ElInput
                placeholder="请输入用户名"
                clearable
                v-model="loginFormData.username"></ElInput>
          </ElFormItem>
        </ElForm>
        <ElForm>
          <ElFormItem label="密码">
            <ElInput
                placeholder="请输入认证码"
                clearable show-password
                @focus="toCloseEyes()"
                @blur="toSmile()"
                v-model="loginFormData.password"
            ></ElInput>
          </ElFormItem>
          <ElFormItem>
            <ElButton plain type="primary" icon="checked">登录</ElButton>
            <ElButton plain type="info" icon="refresh">重置</ElButton>
          </ElFormItem>
        </ElForm>
      </el-tab-pane>
      <el-tab-pane label="注册" name="register">

      </el-tab-pane>
    </el-tabs>
  </ElContainer>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref, type Ref} from "vue";
import { Picture } from "@element-plus/icons-vue";
import { fetchPicture } from "@/hooks/picture.ts";
import {usePost} from "@/hooks/network.ts";
import type {Primitive} from "semantic-typescript";

type Title = "UsernameAndPasswordLogin" | "CheckCodeLogin" | "QRLogin" | "register";
const title: Ref<Title> = ref("UsernameAndPasswordLogin");

const smile: Ref<string> = ref("");
const closeEyes: Ref<string> = ref("");

const src: Ref<string> = ref("");

const toSmile: () => void = (): void => {
  src.value = smile.value;
};
const toCloseEyes: () => void = (): void => {
  src.value = closeEyes.value;
};

interface LoginFormData extends Record<string, string | number | boolean>{
  username: string;
  password: string;
}
const loginFormData: LoginFormData = reactive<LoginFormData>({
  username: "",
  password: ""
});
const performLogin: () => void = (): void => {
  let parameters: URLSearchParams = new URLSearchParams();
  parameters.append("username", loginFormData.username);
  parameters.append("password", loginFormData.password);
  usePost("http://localhost/authentication/login", parameters)
      .then((response: Response): object => response.json())
      .then(console.log);
};

interface RegisterFormData extends Record<string, string | number | boolean>{
  username: string;
  password: string;
}
const registerForm: RegisterFormData = reactive<RegisterFormData>({
  username: "",
  password: ""
});
onMounted(() => {
  fetchPicture("../../../public/smile.jpeg").then((value) => {
    smile.value = value;
    src.value = value;
  });
  fetchPicture("../../../public/Close Eyes.png").then((value) => {
    closeEyes.value = value;
  })
})
</script>

<style scoped>
.panel{
  width: 500px;
  height: 300px;
}

.container{
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>