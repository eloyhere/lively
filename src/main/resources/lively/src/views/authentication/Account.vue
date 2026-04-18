<template>
  <ElContainer class="container" :style="{
    background: 'url(http://localhost/background.jpeg)',
    backgroundRepeat: 'no-repeat',
    backgroundAttachment: 'fixed',
    backgroundSize: '100% 100%'
  }">
    <div>
      <div class="cartoon">
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
      <el-tabs v-model="title" class="panel" style="padding: 16px">
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
            <ElFormItem label="记住我">
              <el-switch v-model="loginFormData.remember" />
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
            <ElFormItem label="认证码">
              <ElInput
                  placeholder="请输入认证码"
                  clearable show-password
                  @focus="toCloseEyes()"
                  @blur="toSmile()"
                  v-model="loginFormData.password"
              ></ElInput>
            </ElFormItem>
            <ElFormItem label="记住我">
              <el-switch v-model="loginFormData.remember" />
            </ElFormItem>
            <ElFormItem>
              <ElButton plain type="primary" icon="checked">登录</ElButton>
              <ElButton plain type="info" icon="refresh">重置</ElButton>
            </ElFormItem>
          </ElForm>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <ElForm v-model="registerFormData">
            <ElFormItem label="用户名">
              <ElInput
                  placeholder="请输入用户名"
                  clearable
                  v-model="registerFormData.username"></ElInput>
            </ElFormItem>
            <ElFormItem label="密码">
              <ElInput
                  placeholder="请输入密码"
                  clearable show-password
                  @focus="toCloseEyes()"
                  @blur="toSmile()"
                  v-model="registerFormData.password"
              ></ElInput>
            </ElFormItem>
            <ElFormItem label="昵称">
              <ElInput
                  placeholder="请输入昵称"
                  clearable
                  v-model="registerFormData.nickname"></ElInput>
            </ElFormItem>
            <ElFormItem label="头像">
              <input type="file" id="filePicker" @change="pick()" hidden="hidden"/>
              <ElAvatar :src="registerFormData.avatar" @click="prepareToPick()" style="cursor: pointer"/>
            </ElFormItem>
          </ElForm>
        </el-tab-pane>
      </el-tabs>
    </div>
  </ElContainer>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref, type Ref} from "vue";
import { Picture } from "@element-plus/icons-vue";
import { fetchPicture } from "@/hooks/picture.ts";
import {useGet, usePost} from "@/hooks/network.ts";
import {ElMessage} from "element-plus";
import {useAuthenticationStore} from "@/stores/authentication.ts";
import {useSerializer} from "@/hooks/entity.ts";
import type {Authentication, Consumer} from "@/interaction/entity.ts";
import {type Router, useRouter} from "vue-router";
import type {MaybeInvalid, Runnable,} from "semantic-typescript";
import {Optional} from "semantic-typescript";
import {useDataUrl} from "@/hooks/url.ts";

const router: Router = useRouter();

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
  remember: boolean;
}
const loginFormData: LoginFormData = reactive<LoginFormData>({
  username: "",
  password: "",
  remember: false
});
const performLogin: () => void = (): void => {
  let parameters: URLSearchParams = new URLSearchParams();
  parameters.append("username", loginFormData.username);
  parameters.append("password", loginFormData.password);
  parameters.append("remember", String(loginFormData.remember));
  usePost("http://localhost/authentication/login", parameters)
      .then((response: Response): void => {
        if(response.status === 200){
          response.text().then((text) => {
            let resolver = useSerializer<Authentication>();
            let cookie: string = response.headers.get("Set-Cookie") || "";
            console.log(cookie);
            window.document.cookie += cookie;
            useAuthenticationStore().setAuthentication(resolver.deserialize(text));
          }).then(() => {
            useGet("http://localhost/authentication/identity").then((response) => response.text()).then(console.log);
          });
          ElMessage({
            message: "登录成功",
            type: "success"
          });
          router.push({
            path: "/",
            replace: true
          });
        }else{
          ElMessage({
            message: "登录失败",
            type: "info"
          });
        }
      });
};

interface RegisterFormData extends Record<string, string | number | boolean>{
  username: string;
  password: string;
  nickname: string;
  avatar: string;
}
const registerFormData: RegisterFormData = reactive<RegisterFormData>({
  username: "",
  password: "",
  nickname: "",
  avatar: ""
});
const prepareToPick: Runnable = ()=> {
  window.document.getElementById("filePicker")!.click();
}
const pick: Runnable = () => {
  let filePicker: Optional<HTMLInputElement> = Optional.of<HTMLInputElement>(document.getElementById("filePicker") as HTMLInputElement);
  filePicker.ifPresent((picker) => {
    let files: FileList = picker.files!;
    let file: File = files.item(0)!;
    useDataUrl(file).then((data) => {
      registerFormData.avatar = data;
    });
  })
}
onMounted(() => {
  fetchPicture("http://localhost/smile.jpeg").then((value) => {
    smile.value = value;
    src.value = value;
  });
  fetchPicture("http://localhost/Close Eyes.png").then((value) => {
    closeEyes.value = value;
  })
});
</script>

<style scoped>
.panel{
  width: 500px;
  height: 300px;
  position: relative;
  background: white;
}

.container{
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.cartoon{
  display: grid;
  width: 200px;
  height: 160px;
  position: relative;
  top: 0;
  left: -20%;
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