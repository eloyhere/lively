<template>
  <ElContainer direction="vertical" style="width: calc(100vw - 180px); height: calc(100vh - 100px)">
    <slot name="default">
      <ElHeader style="height: 200px; width: calc(100vw - 180px); display: flex; flex-direction: column;">
        <slot name="header">
          <ElForm ref="queryForm" inline :model="query.target">
            <slot name="search"></slot>
            <ElFormItem>
              <ElTooltip effect="light" placement="bottom" content="搜索">
                <ElButton icon="search" type="primary" circle plain/>
              </ElTooltip>
              <ElTooltip effect="light" placement="bottom" content="重置">
                <ElButton icon="refresh" type="info" circle plain/>
              </ElTooltip>
            </ElFormItem>
          </ElForm>
          <ElSpace wrap>
            <ElTooltip effect="light" placement="bottom" content="新增">
              <ElButton icon="plus" type="primary" @click="insert.show = true" circle plain/>
            </ElTooltip>
            <ElTooltip effect="light" placement="bottom" content="删除">
              <ElButton icon="delete" type="danger" circle plain/>
            </ElTooltip>
            <ElTooltip effect="light" placement="bottom" content="刷新">
              <ElButton icon="refresh" type="info" circle plain/>
            </ElTooltip>
          </ElSpace>
        </slot>
      </ElHeader>
      <ElContainer direction="vertical" style="width: calc(100vw - 180px); height: calc(100vh - 300px)">
        <ElMain style="height: calc(100vh - 360px); width: calc(100vw - 180px)">
          <slot name="main">
            <ElTable :data="data" style="height: calc(100vh - 360px); width: calc(100vw - 180px)">
              <slot name="column">

              </slot>
              <ElTableColumn fixed="right" label="操作">
                <template #default="scope">
                  <ElSpace wrap>
                    <ElTooltip effect="light" placement="bottom" content="修改">
                      <ElButton icon="edit" type="primary" circle plain/>
                    </ElTooltip>
                    <ElTooltip effect="light" placement="bottom" content="锁定">
                      <ElButton icon="lock" type="warning" circle plain/>
                    </ElTooltip>
                    <ElTooltip effect="light" placement="bottom" content="禁用">
                      <ElButton icon="hide" type="default" circle plain/>
                    </ElTooltip>
                    <ElTooltip effect="light" placement="bottom" content="删除">
                      <ElButton icon="delete" type="danger" @click="deleteBy(scope.row)" circle plain/>
                    </ElTooltip>
                  </ElSpace>
                </template>
              </ElTableColumn>
            </ElTable>
          </slot>
        </ElMain>
        <ElFooter>
          <slot name="footer">
            <ElPagination v-model:page-size="query.size" v-model:current-page="query.page" v-model:total="query.total"/>
          </slot>
        </ElFooter>
      </ElContainer>
      <ElDialog title="新增" v-model="insert.show">
        <ElForm ref="insertForm" :model="insert.target">
          <slot name="insert"></slot>
        </ElForm>
        <template #footer>
          <ElButton type="primary" plain>确定</ElButton>
          <ElButton type="warning" plain>重置</ElButton>
          <ElButton type="info" @click="insert.show = false" plain>取消</ElButton>
        </template>
      </ElDialog>
      <ElDialog title="修改" v-model="update.show">
        <ElForm ref="updateForm" :model="update.target">
          <slot name="insert"></slot>
        </ElForm>
        <template #footer>
          <ElButton type="primary" plain>确定</ElButton>
          <ElButton type="warning" plain>重置</ElButton>
          <ElButton type="info" @click="update.show = false" plain>取消</ElButton>
        </template>
      </ElDialog>
    </slot>
  </ElContainer>
</template>
<script generic="E extends BaseEntity" setup lang="ts">

import type {BaseEntity, Page, Query} from "@/declaration/entity.ts";
import {type Reactive, reactive, ref, type Ref} from "vue";
import type {BaseService} from "@/interaction/service.ts";
import type {Insert, Operator, Update} from "@/declaration/modulize.ts";
import type {Consumer, MaybeInvalid} from "semantic-typescript";
import {ElMessage, ElMessageBox} from "element-plus";

interface Property<E extends BaseEntity>{
  service: BaseService<E>;
}
const property = defineProps<Property<E>>();

const query: Reactive<Query<E>> = reactive<Query<E>>({
  page: 0,
  total: 0,
  size: 10,
  target: {}
});
const queryForm: Ref<MaybeInvalid<HTMLElement>> = ref<MaybeInvalid<HTMLElement>>();

const insert: Reactive<Insert<E>> = reactive<Insert<E>>({
  show: false,
  target: {}
});
const insertForm: Ref<MaybeInvalid<HTMLElement>> = ref<MaybeInvalid<HTMLElement>>();

const update: Reactive<Update<E>> = reactive<Update<E>>({
  show: false,
  target: {}
});
const updateForm: Ref<MaybeInvalid<HTMLElement>> = ref<MaybeInvalid<HTMLElement>>();

const data: Reactive<Array<E>> = reactive<Array<E>>([
  {} as unknown as E
]);

interface Emit{
  (event: "search", value: E | Partial<E>): void;
  (event: "load", value: Page<E>): void;
  (event: "clear"): void;
  (event: "insert", value: E | Partial<E>): void;
  (event: "update", value: E | Partial<E>): void;
  (event: "delete", value: E | Partial<E>): void;
}
const emit = defineEmits<Emit>();

const insertOperator: Operator<E> = {
  ready: (): void => {

  },
  dismiss: async (): Promise<void> => {

  },
  perform: async (): Promise<void> => {

  }
};

const updateOperator: Operator<E> = {
  ready: (): void => {

  },
  dismiss: async (): Promise<void> => {

  },
  perform: async (): Promise<void> => {

  }
};

const deleteBy: Consumer<E> = (entity: E) : void => {
  ElMessageBox.confirm(`确定要删除id为${entity.id}的项目吗？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then((): void => {
    property.service.deleteByIdentifier(entity.id).then((): void => {
      ElMessage({
        message: "操作成功",
        type: "success"
      });
    }, (): void => {
      ElMessage({
        message: "操作失败",
        type: "success"
      });
    });
  }, (): void => {
    ElMessage({
      message: "操作取消",
      type: "info"
    });
  })
};
</script>

<style scoped>

</style>