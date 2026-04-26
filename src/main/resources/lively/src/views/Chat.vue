<template>
<ElContainer style="width: 100vw; height: 100vh;">
  <ElHeader>

  </ElHeader>
  <ElMain style="height: calc(100vh - 60px); width: 100vw">
    <ElTabs v-model="tab" type="card" editable>
      <ElTabPane v-for="chat in chats" :label="chat.description" :name="chat.name" style="height: calc(100vh - 100px); width: 100vw">

      </ElTabPane>
    </ElTabs>
  </ElMain>
</ElContainer>
</template>

<script setup lang="ts">

import {onMounted, reactive, type Reactive, ref, type Ref} from "vue";
import type {Chat} from "@/declaration/entity";
import {ChatService} from "@/hooks/service";


const tab: Ref<string> = ref<string>("");
const chats: Reactive<Array<Chat>> = reactive<Array<Chat>>([]);

const chatService: ChatService = new ChatService();

onMounted((): void =>{
  chatService.myChats().then((value: Array<Chat>) => {
    chats.length = 0;
    chats.push(...value);
  });
})
</script>

<style scoped>


</style>