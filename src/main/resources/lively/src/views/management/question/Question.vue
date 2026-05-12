<template>
  <Modulize :service="questionService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <!-- 搜索条件 -->
    <template #search="{search}">
      <ElFormItem label="问题" prop="question">
        <ElInput v-model="search.question" placeholder="搜索问题内容" clearable style="width: 200px;" />
      </ElFormItem>
    </template>

    <!-- 表格列 -->
    <template #column>
      <ElTableColumn label="问题内容" prop="question" min-width="300" show-overflow-tooltip />
      <ElTableColumn label="答案内容" prop="answer" min-width="300" show-overflow-tooltip />
    </template>

    <!-- 新增表单 -->
    <template #insert="{insert}">
      <ElFormItem label="问题内容" prop="question" required>
        <ElInput v-model="insert.question" type="textarea" :rows="3" placeholder="输入问题内容" />
      </ElFormItem>
      <ElFormItem label="答案内容" prop="answer" required>
        <ElInput v-model="insert.answer" type="textarea" :rows="3" placeholder="输入答案内容" />
      </ElFormItem>
    </template>

    <!-- 修改表单 -->
    <template #update="{update}">
      <ElFormItem label="问题内容" prop="question" required>
        <ElInput v-model="update.question" type="textarea" :rows="3" placeholder="输入问题内容" />
      </ElFormItem>
      <ElFormItem label="答案内容" prop="answer" required>
        <ElInput v-model="update.answer" type="textarea" :rows="3" placeholder="输入答案内容" />
      </ElFormItem>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {QuestionService} from "../../../hooks/service.ts";
import {reactive, type Reactive} from "vue";
import type {Query, Question} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
const questionService: QuestionService = new QuestionService();
const query: Reactive<Query<Question>> = reactive<Query<Question>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Question>> = reactive<Insert<Question>>({
  show: false,
  target: {
    question: "",
    answer: ""
  }
});
const update: Reactive<Update<Question>> = reactive<Update<Question>>({
  show: false,
  target: {
    question: "",
    answer: ""
  }
});
const data: Reactive<Array<Question>> = reactive<Array<Question>>([]);
</script>

<style scoped>

</style>
