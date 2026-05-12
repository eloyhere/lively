<template>
  <Modulize :service="choiceService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <!-- 搜索条件 -->
    <template #search="{search}">
      <ElFormItem label="问题" prop="question">
        <ElInput v-model="search.question" placeholder="搜索问题内容" clearable style="width: 200px;" />
      </ElFormItem>
    </template>

    <!-- 表格列 -->
    <template #column>
      <ElTableColumn label="问题内容" prop="question" min-width="300" show-overflow-tooltip />
      <ElTableColumn label="选项数量" width="100" align="center">
        <template #default="{row}">
          {{ row.answers ? row.answers.length : 0 }}
        </template>
      </ElTableColumn>
      <ElTableColumn label="正确答案" width="200" show-overflow-tooltip>
        <template #default="{row}">
          <template v-if="row.answers && row.answers.length > 0">
            <ElTag v-for="ans in row.answers.filter((a: any) => a.proper)" :key="ans.id" size="small" style="margin-right: 4px;">{{ ans.content }}</ElTag>
          </template>
        </template>
      </ElTableColumn>
    </template>

    <!-- 新增表单 -->
    <template #insert="{insert}">
      <ElFormItem label="问题" prop="question" required>
        <ElInput v-model="insert.question" type="textarea" :rows="3" placeholder="输入问题内容" />
      </ElFormItem>
      <ElFormItem label="选项" prop="answers" required>
        <div v-for="(ans, idx) in insert.answers" :key="idx" style="margin-bottom: 8px; display: flex; align-items: center;">
          <ElInput v-model="ans.content" style="width: 400px;" :placeholder="'选项 ' + (idx + 1)">
            <template #prepend>{{ idx + 1 }}</template>
          </ElInput>
          <ElSwitch v-model="ans.proper" active-text="正确" inactive-text="" style="margin-left: 8px;" />
          <ElButton type="danger" :icon="Delete" circle size="small" @click="removeAnswer(insert, idx)" style="margin-left: 8px;" />
        </div>
        <ElButton type="primary" :icon="Plus" size="small" @click="addAnswer(insert)">添加选项</ElButton>
      </ElFormItem>
    </template>

    <!-- 修改表单 -->
    <template #update="{update}">
      <ElFormItem label="问题" prop="question" required>
        <ElInput v-model="update.question" type="textarea" :rows="3" placeholder="输入问题内容" />
      </ElFormItem>
      <ElFormItem label="选项" prop="answers" required>
        <div v-for="(ans, idx) in update.answers" :key="idx" style="margin-bottom: 8px; display: flex; align-items: center;">
          <ElInput v-model="ans.content" style="width: 400px;" :placeholder="'选项 ' + (idx + 1)">
            <template #prepend>{{ idx + 1 }}</template>
          </ElInput>
          <ElSwitch v-model="ans.proper" active-text="正确" inactive-text="" style="margin-left: 8px;" />
          <ElButton type="danger" :icon="Delete" circle size="small" @click="removeAnswer(update, idx)" style="margin-left: 8px;" />
        </div>
        <ElButton type="primary" :icon="Plus" size="small" @click="addAnswer(update)">添加选项</ElButton>
      </ElFormItem>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {ChoiceService} from "@/hooks/service.ts";
import {reactive, type Reactive} from "vue";
import type {Query, Choice} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
import {Delete, Plus} from '@element-plus/icons-vue';
import { ElMessage } from "element-plus";

const choiceService: ChoiceService = new ChoiceService();
const query: Reactive<Query<Choice>> = reactive<Query<Choice>>({
  page: 1,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Choice>> = reactive<Insert<Choice>>({
  show: false,
  target: {
    question: "",
    answers: [{content: "", proper: false}, {content: "", proper: false}, {content: "", proper: false}, {content: "", proper: false}]
  } as Partial<Choice>
});
const update: Reactive<Update<Choice>> = reactive<Update<Choice>>({
  show: false,
  target: {
    question: "",
    answers: [{content: "", proper: false}, {content: "", proper: false}, {content: "", proper: false}, {content: "", proper: false}]
  } as Partial<Choice>
});
const data: Reactive<Array<Choice>> = reactive<Array<Choice>>([]);

function addAnswer(target: Partial<Choice>) {
  if (target.answers) {
    if (target.answers.length >= 8) {
      ElMessage.warning("最多支持 8 个选项");
      return;
    }
    target.answers.push({content: "", proper: false});
  }
}

function removeAnswer(target: Partial<Choice>, idx: number) {
  if (target.answers) {
    if (target.answers.length <= 2) {
      ElMessage.warning("至少需要保留 2 个选项");
      return;
    }
    const removing = target.answers[idx];
    target.answers.splice(idx, 1);
    // 如果删除的是正确答案，提醒用户
    if (removing?.proper) {
      const hasCorrect = target.answers.some(a => a.proper);
      if (!hasCorrect) {
        ElMessage.warning("已删除最后一个正确答案，请重新设置");
      }
    }
  }
}
</script>

<style scoped>

</style>
