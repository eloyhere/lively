<template>
  <Modulize :service="announcementService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
    <template #search="{search}">
    </template>
    <template #column>
    </template>
  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {AnnouncementService} from "../../../hooks/service";
import {reactive, type Reactive} from "vue";
import type {Query, Announcement} from "@/declaration/entity";
import type {Insert, Update} from "@/declaration/modulize";
const announcementService: AnnouncementService = new AnnouncementService();
const query: Reactive<Query<Announcement>> = reactive<Query<Announcement>>({
  page: 0,
  total: 0,
  size: 10,
  direction: "ASC",
  target: {}
});
const insert: Reactive<Insert<Announcement>> = reactive<Insert<Announcement>>({
  show: false,
  target: {}
});
const update: Reactive<Update<Announcement>> = reactive<Update<Announcement>>({
  show: false,
  target: {}
});
const data: Reactive<Array<Announcement>> = reactive<Array<Announcement>>([]);
</script>

<style scoped>

</style>