<template>
  <Modulize :service="authorityService"/>
</template>
<script setup lang="ts">

import type {Authority, Insert, Page, Query, Update} from "@/declaration/entity.ts";
import {onMounted, reactive, ref, type Ref} from "vue";
import {AuthorityService} from "@/interaction/service.ts";
import type {Consumer, Runnable} from "semantic-typescript";
import {ElMessage, ElMessageBox} from "element-plus";
import QueryForm from "@/component/QueryForm.vue";
import Modulize from "@/component/Modulize.vue";

const query: Query<Authority> = reactive<Query<Authority>>({
  page: 0,
  size: 10,
  total: 0,
  target: {}
});

const load: Ref<boolean> = ref<boolean>(true);
const data: Array<Authority> = reactive<Array<Authority>>([]);

const authorityService: AuthorityService = new AuthorityService();

const insert: Insert<Authority> = reactive<Insert<Authority>>({
  show: false,
  target: {}
});

const update: Update<Authority> = reactive<Insert<Authority>>({
  show: false,
  target: {}
});

const readyForInsert: Runnable = (): void => {
  insert.show = true;
};
const performInsert: Runnable = (): void => {
  authorityService.insert(update.target).then((): void => {
    ElMessage({
      message: "操作成功",
      type: "success"
    });
    insert.show = false;
  }, (): void => {
    ElMessage({
      message: "操作失败",
      type: "error"
    });
  });
};

const deleteBy: Consumer<Authority> = (authority: Authority): void => {
  ElMessageBox.confirm(`是否删除权限${authority.authority}？`, "提示", {
    type: "warning",
    confirmButtonText: "确定",
    cancelButtonText: "取消"
  }).then((): void => {
    authorityService.deleteByIdentifier(authority.id).then((): void => {
      ElMessage({
        message: "操作成功",
        type: "success"
      });
    })
  }, (): void => {
    ElMessage({
      message: "操作取消",
      type: "info"
    });
  });
};

const readyForUpdate: Consumer<Authority> = (authority: Authority): void => {
  authorityService.findByIdentifier(authority.id).then((authority): void => {
    update.target = authority;
    update.show = true;
  }, (): void => {
    ElMessage({
      message: "加载失败",
      type: "error"
    });
  });
};
const performUpdate: Consumer<Insert<Authority>> = (): void => {
  authorityService.update(update.target).then((): void => {
    ElMessage({
      message: "操作成功",
      type: "success"
    });
    update.show = false;
  }, () => {
    ElMessage({
      message: "操作失败",
      type: "error"
    });
    update.show = false;
  });
};

onMounted((): void => {
  authorityService.findAllPagedBy(query).then((page: Page<Authority>): void => {
    query.page = page.number;
    query.size = page.size;
    query.total = page.totalElements;
    load.value = false;
  }, () => {
    load.value = false;
  });
});
</script>



<style scoped>

</style>