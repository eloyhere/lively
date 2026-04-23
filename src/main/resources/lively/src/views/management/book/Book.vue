<template>
  <Modulize :service="bookService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
    </template>
    <template #column>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {BookService} from "@/interaction/service.ts";
import {reactive, type Reactive} from "vue";
import type {Query, Book} from "@/declaration/entity.ts";
import type {Insert, Update} from "@/declaration/modulize.ts";
const bookService: BookService = new BookService();
const query: Reactive<Query<Book>> = reactive<Query<Book>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Book>> = reactive<Insert<Book>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Book>> = reactive<Update<Book>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Book>> = reactive<Array<Book>>([]);
</script>

<style scoped>

</style>