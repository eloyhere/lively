<template>
  <ElContainer class="container">
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
      <svg width="400" height="200" xmlns="http://www.w3.org/2000/svg" class="cloud1">
        <defs>
          <linearGradient id="cloudGradient" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" style="stop-color:#ffffff;stop-opacity:1" />
            <stop offset="100%" style="stop-color:#e8f4fc;stop-opacity:1" />
          </linearGradient>
          <radialGradient id="cloudHighlight" cx="30%" cy="30%" r="70%">
            <stop offset="0%" style="stop-color:#ffffff;stop-opacity:0.8" />
            <stop offset="100%" style="stop-color:#ffffff;stop-opacity:0" />
          </radialGradient>
        </defs>
        <g>
          <circle cx="40" cy="60" r="35" fill="url(#cloudGradient)" />
          <circle cx="85" cy="45" r="40" fill="url(#cloudGradient)" />
          <circle cx="100" cy="70" r="45" fill="url(#cloudGradient)" />
          <circle cx="150" cy="60" r="35" fill="url(#cloudGradient)" />
          <circle cx="130" cy="40" r="25" fill="url(#cloudGradient)" />
          <circle cx="60" cy="40" r="25" fill="url(#cloudGradient)" />
          <ellipse cx="90" cy="35" rx="25" ry="15" fill="url(#cloudHighlight)" opacity="0.6" />
        </g>
      </svg>
      <svg width="400" height="200" xmlns="http://www.w3.org/2000/svg" class="cloud2">
        <defs>
          <linearGradient id="cloudGradient" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" style="stop-color:#ffffff;stop-opacity:1" />
            <stop offset="100%" style="stop-color:#e8f4fc;stop-opacity:1" />
          </linearGradient>
          <radialGradient id="cloudHighlight" cx="30%" cy="30%" r="70%">
            <stop offset="0%" style="stop-color:#ffffff;stop-opacity:0.8" />
            <stop offset="100%" style="stop-color:#ffffff;stop-opacity:0" />
          </radialGradient>
        </defs>
        <g>
          <circle cx="40" cy="60" r="35" fill="url(#cloudGradient)" />
          <circle cx="85" cy="45" r="40" fill="url(#cloudGradient)" />
          <circle cx="100" cy="70" r="45" fill="url(#cloudGradient)" />
          <circle cx="150" cy="60" r="35" fill="url(#cloudGradient)" />
          <circle cx="130" cy="40" r="25" fill="url(#cloudGradient)" />
          <circle cx="60" cy="40" r="25" fill="url(#cloudGradient)" />
          <ellipse cx="90" cy="35" rx="25" ry="15" fill="url(#cloudHighlight)" opacity="0.6" />
        </g>
      </svg>
      <svg width="400" height="200" xmlns="http://www.w3.org/2000/svg" class="cloud3">
        <defs>
          <linearGradient id="cloudGradient" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" style="stop-color:#ffffff;stop-opacity:1" />
            <stop offset="100%" style="stop-color:#e8f4fc;stop-opacity:1" />
          </linearGradient>
          <radialGradient id="cloudHighlight" cx="30%" cy="30%" r="70%">
            <stop offset="0%" style="stop-color:#ffffff;stop-opacity:0.8" />
            <stop offset="100%" style="stop-color:#ffffff;stop-opacity:0" />
          </radialGradient>
        </defs>
        <g>
          <circle cx="40" cy="60" r="35" fill="url(#cloudGradient)" />
          <circle cx="85" cy="45" r="40" fill="url(#cloudGradient)" />
          <circle cx="100" cy="70" r="45" fill="url(#cloudGradient)" />
          <circle cx="150" cy="60" r="35" fill="url(#cloudGradient)" />
          <circle cx="130" cy="40" r="25" fill="url(#cloudGradient)" />
          <circle cx="60" cy="40" r="25" fill="url(#cloudGradient)" />
          <ellipse cx="90" cy="35" rx="25" ry="15" fill="url(#cloudHighlight)" opacity="0.6" />
        </g>
      </svg>
      <svg width="400" height="200" xmlns="http://www.w3.org/2000/svg" class="cloud4">
        <defs>
          <linearGradient id="cloudGradient" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" style="stop-color:#ffffff;stop-opacity:1" />
            <stop offset="100%" style="stop-color:#e8f4fc;stop-opacity:1" />
          </linearGradient>
          <radialGradient id="cloudHighlight" cx="30%" cy="30%" r="70%">
            <stop offset="0%" style="stop-color:#ffffff;stop-opacity:0.8" />
            <stop offset="100%" style="stop-color:#ffffff;stop-opacity:0" />
          </radialGradient>
        </defs>
        <g>
          <circle cx="40" cy="60" r="35" fill="url(#cloudGradient)" />
          <circle cx="85" cy="45" r="40" fill="url(#cloudGradient)" />
          <circle cx="100" cy="70" r="45" fill="url(#cloudGradient)" />
          <circle cx="150" cy="60" r="35" fill="url(#cloudGradient)" />
          <circle cx="130" cy="40" r="25" fill="url(#cloudGradient)" />
          <circle cx="60" cy="40" r="25" fill="url(#cloudGradient)" />
          <ellipse cx="90" cy="35" rx="25" ry="15" fill="url(#cloudHighlight)" opacity="0.6" />
        </g>
      </svg>
      <svg width="400" height="200" xmlns="http://www.w3.org/2000/svg" class="cloud5">
        <defs>
          <linearGradient id="cloudGradient" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" style="stop-color:#ffffff;stop-opacity:1" />
            <stop offset="100%" style="stop-color:#e8f4fc;stop-opacity:1" />
          </linearGradient>
          <radialGradient id="cloudHighlight" cx="30%" cy="30%" r="70%">
            <stop offset="0%" style="stop-color:#ffffff;stop-opacity:0.8" />
            <stop offset="100%" style="stop-color:#ffffff;stop-opacity:0" />
          </radialGradient>
        </defs>
        <g>
          <circle cx="40" cy="60" r="35" fill="url(#cloudGradient)" />
          <circle cx="85" cy="45" r="40" fill="url(#cloudGradient)" />
          <circle cx="100" cy="70" r="45" fill="url(#cloudGradient)" />
          <circle cx="150" cy="60" r="35" fill="url(#cloudGradient)" />
          <circle cx="130" cy="40" r="25" fill="url(#cloudGradient)" />
          <circle cx="60" cy="40" r="25" fill="url(#cloudGradient)" />
          <ellipse cx="90" cy="35" rx="25" ry="15" fill="url(#cloudHighlight)" opacity="0.6" />
        </g>
      </svg>
      <el-tabs v-model="title" class="panel" style="padding: 16px">
        <el-tab-pane label="用户名密码登录" name="UsernameAndPasswordLogin">
          <ElForm ref="usernamePasswordLoginForm" :model="usernamePasswordLoginFormData">
            <ElFormItem label="用户名" prop="username" :rules="[
                {
                  required: true,
                  message: '请输入用户名',
                  trigger: 'blur'
                },
                {
                  min: 3,
                  max: 20,
                  message: '3-20个字符',
                  trigger: 'blur'
                },
            ]">
              <ElInput
                  placeholder="请输入用户名"
                  clearable
                  v-model="usernamePasswordLoginFormData.username"></ElInput>
            </ElFormItem>
            <ElFormItem label="密码" prop="password" :rules="[
                {
                  required: true,
                  message: '请输入密码',
                  trigger: 'blur'
                },
                {
                  min: 5,
                  max: 20,
                  message: '5-20个字符',
                  trigger: 'blur'
                },
            ]">
              <ElInput
                  placeholder="请输入密码"
                  clearable show-password
                  @focus="toCloseEyes()"
                  @blur="toSmile()"
                  v-model="usernamePasswordLoginFormData.password"
              ></ElInput>
            </ElFormItem>
            <ElFormItem label="记住我" prop="remember">
              <el-switch v-model="usernamePasswordLoginFormData.remember" />
            </ElFormItem>
            <ElFormItem>
              <ElButton plain type="primary" icon="checked" @click="performUsernamePasswordLogin()">登录</ElButton>
              <ElButton plain type="info" icon="refresh" @click="resetUsernamePasswordLoginForm()">重置</ElButton>
            </ElFormItem>
          </ElForm>
        </el-tab-pane>
        <el-tab-pane label="认证码登录" name="CheckCodeLogin">
          <ElForm ref="checkCodeLoginForm" :model="checkCodeLoginFormData">
            <ElFormItem label="用户名" prop="username" :rules="[
                {
                  required: true,
                  message: '请输入用户名',
                  trigger: 'blur'
                },
                {
                  min: 3,
                  max: 20,
                  message: '3-20个字符',
                  trigger: 'blur'
                },
            ]">
              <ElInput
                  placeholder="请输入用户名"
                  clearable
                  v-model="checkCodeLoginFormData.username"></ElInput>
            </ElFormItem>
            <ElFormItem label="认证码" prop="code" :rules="[
                {
                  required: true,
                  message: '请输入认证码',
                  trigger: 'blur'
                },
                {
                  min: 5,
                  max: 20,
                  message: '5-20个字符',
                  trigger: 'blur'
                },
            ]">
              <ElInput
                  placeholder="请输入认证码"
                  clearable show-password
                  @focus="toCloseEyes()"
                  @blur="toSmile()"
                  v-model="checkCodeLoginFormData.code"
              ></ElInput>
            </ElFormItem>
            <ElFormItem label="记住我" prop="remember">
              <el-switch v-model="checkCodeLoginFormData.remember" />
            </ElFormItem>
            <ElFormItem>
              <ElButton plain type="primary" icon="checked" @click="performCheckCodeLogin()">登录</ElButton>
              <ElButton plain type="info" icon="refresh" @click="resetCheckCodeLoginForm()">重置</ElButton>
            </ElFormItem>
          </ElForm>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <ElForm :model="registerFormData">
            <ElFormItem label="用户名" prop="username" :rules="[
                {
                  required: true,
                  message: '请输入用户名',
                  trigger: 'blur'
                },
                {
                  min: 3,
                  max: 20,
                  message: '3-20个字符',
                  trigger: 'blur'
                },
            ]">
              <ElInput
                  placeholder="请输入用户名"
                  clearable
                  v-model="registerFormData.username"></ElInput>
            </ElFormItem>
            <ElFormItem label="密码" prop="password" :rules="[
                {
                  required: true,
                  message: '请输入密码',
                  trigger: 'blur'
                },
                {
                  min: 3,
                  max: 20,
                  message: '3-20个字符',
                  trigger: 'blur'
                },
            ]">
              <ElInput
                  placeholder="请输入密码"
                  clearable show-password
                  @focus="toCloseEyes()"
                  @blur="toSmile()"
                  v-model="registerFormData.password"
              ></ElInput>
            </ElFormItem>
            <ElFormItem label="昵称" prop="nickname" :rules="[
                {
                  required: true,
                  message: '请输入昵称',
                  trigger: 'blur'
                },
                {
                  min: 5,
                  max: 20,
                  message: '5-20个字符',
                  trigger: 'blur'
                },
            ]">
              <ElInput
                  placeholder="请输入昵称"
                  clearable
                  v-model="registerFormData.nickname"></ElInput>
            </ElFormItem>
            <ElFormItem label="头像" prop="avatar" :rules="[
                {
                  required: true,
                  message: '请选择头像',
                  trigger: 'blur'
                }
            ]">
              <input type="file" id="filePicker" @change="pick()" hidden="hidden"/>
              <ElAvatar :src="registerFormData.avatar" @click="pick()" style="cursor: pointer">
                <template #default>
                  <ElIcon>
                    <Picture/>
                  </ElIcon>
                </template>
              </ElAvatar>
            </ElFormItem>
            <ElFormItem label="邀请码" prop="invitation" :rules="[
                {
                  required: true,
                  message: '请输入邀请码'
                }
            ]">
              <ElInput placeholder="请输入邀请码" clearable v-model="registerFormData.invitation"/>
            </ElFormItem>
            <ElFormItem>
              <ElButton plain type="primary" icon="checked">注册</ElButton>
              <ElButton plain type="info" icon="refresh">重置</ElButton>
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
import {ElMessage, type FormInstance} from "element-plus";
import {useAuthenticationStore} from "@/stores/authentication.ts";
import {useSerializer} from "@/hooks/entity.ts";
import type {Authentication, Consumer} from "@/interaction/entity.ts";
import {type Router, useRouter} from "vue-router";
import {type MaybeInvalid, type Runnable, validate,} from "semantic-typescript";
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

const usernamePasswordLoginForm: Ref<MaybeInvalid<FormInstance>> = ref();
interface LoginFormData extends Record<string, string | number | boolean>{
  username: string;
  password: string;
  remember: boolean;
}
const usernamePasswordLoginFormData: LoginFormData = reactive<LoginFormData>({
  username: "",
  password: "",
  remember: false
});
const performUsernamePasswordLogin: () => void = async (): Promise<void> => {
  if(validate(usernamePasswordLoginForm.value)){
    await usernamePasswordLoginForm.value.validate((valid: boolean) => {
      if(valid){
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("username", usernamePasswordLoginFormData.username);
        parameters.append("password", usernamePasswordLoginFormData.password);
        parameters.append("remember", String(usernamePasswordLoginFormData.remember));
        usePost("http://localhost/authentication/login", parameters)
            .then((response: Response): void => {
              if(response.status === 200){
                response.text().then((text) => {
                  let resolver = useSerializer<Authentication>();
                  useAuthenticationStore().setAuthentication(resolver.deserialize(text));
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
      }else{
        ElMessage({
          message: "请完善信息",
          type: "info"
        });
      }
    });
  }
};
const resetUsernamePasswordLoginForm: Runnable = () => {
  if(validate(usernamePasswordLoginForm.value)){
    usernamePasswordLoginForm.value.resetFields();
  }
};

const checkCodeLoginForm: Ref<MaybeInvalid<FormInstance>> = ref();
interface CheckCodeLoginFormData extends Record<string, string | number | boolean>{
  username: string;
  code: string;
  remember: boolean;
}
const checkCodeLoginFormData: CheckCodeLoginFormData = reactive<CheckCodeLoginFormData>({
  username: "",
  code: "",
  remember: false
});
const performCheckCodeLogin: () => void = async (): Promise<void> => {
  if(validate(checkCodeLoginForm.value)){
    await checkCodeLoginForm.value.validate((valid: boolean) => {
      if(valid){
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("username", checkCodeLoginFormData.username);
        parameters.append("code", checkCodeLoginFormData.code);
        parameters.append("remember", String(checkCodeLoginFormData.remember));
        usePost("http://localhost/authentication/code", parameters)
            .then((response: Response): void => {
              if(response.status === 200){
                response.text().then((text) => {
                  let resolver = useSerializer<Authentication>();
                  useAuthenticationStore().setAuthentication(resolver.deserialize(text));
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
      }else{
        ElMessage({
          message: "请完善信息",
          type: "info"
        });
      }
    });
  }
};
const resetCheckCodeLoginForm: Runnable = () => {
  if(validate(checkCodeLoginForm.value)){
    checkCodeLoginForm.value.resetFields();
  }
};

const registerForm: Ref<MaybeInvalid<FormInstance>> = ref();
interface RegisterFormData extends Record<string, string | number | boolean>{
  username: string;
  password: string;
  nickname: string;
  avatar: string;
  invitation: string;
}
const registerFormData: RegisterFormData = reactive<RegisterFormData>({
  username: "",
  password: "",
  nickname: "",
  avatar: "",
  invitation: ""
});
const pick: Runnable = () => {
  let filePicker: Optional<HTMLInputElement> = Optional.of<HTMLInputElement>(document.getElementById("filePicker") as HTMLInputElement);
  filePicker.ifPresent((picker) => {
    picker.addEventListener("change", ()=> {
      if(validate(picker.files)){
        let files: FileList = picker.files;
        if(files.length > 0){
          let file: File = files.item(0) as File;
          useDataUrl(file).then((data) => {
            registerFormData.avatar = data;
          });
        }
      }
    });
    picker.click();
  })
};
const performRegister: () => void = async (): Promise<void> => {
  if(validate(registerForm.value)){
    await registerForm.value.validate((valid: boolean) => {
      if(valid){
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("username", registerFormData.username);
        parameters.append("password", registerFormData.password);
        parameters.append("nickname", registerFormData.nickname);
        parameters.append("invitation", registerFormData.invitation);
        parameters.append("avatar", registerFormData.avatar);
        usePost("http://localhost/authentication/register", parameters)
            .then((response: Response): void => {
              if(response.status === 200){
                response.text().then((text) => {
                  let resolver = useSerializer<Authentication>();
                  useAuthenticationStore().setAuthentication(resolver.deserialize(text));
                });
                ElMessage({
                  message: "注册成功",
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
      }else{
        ElMessage({
          message: "请完善信息",
          type: "info"
        });
      }
    });
  }
};
const resetRegisterForm: Runnable = () => {
  if(validate(checkCodeLoginForm.value)){
    checkCodeLoginForm.value.resetFields();
  }
};
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
  width: 600px;
  height: 360px;
  background: white;
  z-index: 10;
}

.container{
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  background: url(http://localhost/background.jpeg) no-repeat fixed;
  background-size: 100% 100%;
}

.cartoon{
  display: grid;
  width: 200px;
  height: 160px;
  position: absolute;
  margin-top: -160px;
  z-index: 1;
  pointer-events: none;
}

.cloud1{
  position: absolute;
  margin-top: -280px;
  margin-left: -75px;
  z-index: 2;
  transform: rotate(-40deg);
  pointer-events: none;
}

.cloud2{
  position: absolute;
  margin-top: -130px;
  margin-left: 40px;
  z-index: 2;
  transform: rotate(45deg);
  pointer-events: none;
}

.cloud3{
  position: absolute;
  margin-top: -20px;
  margin-left: -120px;
  z-index: 2;
  transform: rotate(40deg);
  pointer-events: none;
}

.cloud4{
  position: absolute;
  margin-top: -160px;
  margin-left: 80px;
  z-index: 2;
  transform: rotate(-40deg);
  pointer-events: none;
}

.cloud5{
  position: absolute;
  margin-top: -250px;
  margin-left: 30px;
  z-index: 2;
  pointer-events: none;
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