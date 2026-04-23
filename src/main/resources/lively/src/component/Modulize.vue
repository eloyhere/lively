<template>
  <ElContainer v-loading="load" style="width: calc(100vw - 180px); height: calc(100vh - 100px); user-select: none;">
    <ElHeader style="height: 150px; width: calc(100vw - 180px); display: flex; flex-direction: column;">
      <slot name="header">
        <ElForm ref="queryForm" inline :model="query.target">
          <slot name="search" :search="query.target"></slot>
          <ElFormItem>
            <ElSpace wrap>
              <ElRadioGroup v-model="query.direction">
                <ElRadioButton value="ASC" border>升序</ElRadioButton>
                <ElRadioButton value="DESC" border>降序</ElRadioButton>
              </ElRadioGroup>
              <ElTooltip effect="light" placement="bottom" content="搜索">
                <ElButton icon="search" type="primary" @click="search()" circle plain/>
              </ElTooltip>
              <ElTooltip effect="light" placement="bottom" content="重置">
                <ElButton icon="refresh" type="info" @click="resetQueryForm()" circle plain/>
              </ElTooltip>
            </ElSpace>
          </ElFormItem>
        </ElForm>
        <ElSpace wrap>
          <ElTooltip effect="light" placement="bottom" content="新增">
            <ElButton icon="plus" type="primary" @click="insert.show = true" circle plain/>
          </ElTooltip>
          <ElTooltip effect="light" v-if="multiple" placement="bottom" content="单选">
            <ElButton icon="SemiSelect" v-if="multiple" type="info" @click="multiple = !multiple" circle plain/>
          </ElTooltip>
          <ElTooltip effect="light" v-if="!multiple" placement="bottom" content="多选">
            <ElButton icon="Select" v-if="!multiple" type="info" @click="multiple = !multiple" circle plain/>
          </ElTooltip>
          <ElTooltip effect="light" placement="bottom" content="删除">
            <ElButton icon="delete" type="danger" @click="multipleDelete()" circle plain/>
          </ElTooltip>
          <ElTooltip effect="light" placement="bottom" content="刷新">
            <ElButton icon="refresh" @click="search()" type="info" circle plain/>
          </ElTooltip>
        </ElSpace>
      </slot>
    </ElHeader>
    <ElContainer style="width: calc(100vw - 180px); height: calc(100vh - 300px);">
      <ElMain style="width: calc(100vw - 180px); height: calc(100vh - 400px);">
        <slot name="main">
          <ElScrollbar>
            <ElTable ref="table" :data="data" border style="width: calc(100vw - 180px); height: calc(100vh - 400px);" @selection-change="selectionChange">
              <ElTableColumn type="selection" width="50" align="center" v-if="multiple"></ElTableColumn>
              <ElTableColumn label="id" prop="id"></ElTableColumn>
              <slot name="column">
              </slot>
              <ElTableColumn label="锁定" width="75" align="center" prop="lock">
                <template #default="scope">
                  <ElIcon v-if="isLocked(scope.row)">
                    <Check/>
                  </ElIcon>
                  <ElIcon v-if="!isLocked(scope.row)">
                    <Close/>
                  </ElIcon>
                </template>
              </ElTableColumn>
              <ElTableColumn label="禁用" width="75" align="center" prop="ban">
                <template #default="scope">
                  <ElIcon v-if="isBanned(scope.row)">
                    <Check/>
                  </ElIcon>
                  <ElIcon v-if="!isBanned(scope.row)">
                    <Close/>
                  </ElIcon>
                </template>
              </ElTableColumn>
              <ElTableColumn label="修改时间" prop="edit" sortable>
                <template #default="scope">
                  {{useDateFormat(scope.row.edit)}}
                </template>
              </ElTableColumn>
              <ElTableColumn label="创建时间" prop="spawn" sortable>
                <template #default="scope">
                  {{useDateFormat(scope.row.spawn)}}
                </template>
              </ElTableColumn>
              <ElTableColumn fixed="right" label="操作">
                <template #default="scope">
                  <ElSpace wrap>
                    <ElTooltip effect="light" placement="bottom" content="修改">
                      <ElButton icon="edit" type="primary" @click="updateOperator.ready()" circle plain/>
                    </ElTooltip>
                    <ElTooltip v-if="!isLocked(scope.row)" effect="light" placement="bottom" content="锁定">
                      <ElButton v-if="!isLocked(scope.row)" icon="lock" type="warning" @click="lockOrUnlock(scope.row)" circle plain/>
                    </ElTooltip>
                    <ElTooltip v-if="isLocked(scope.row)" effect="light" placement="bottom" content="解锁">
                      <ElButton v-if="isLocked(scope.row)" icon="unlock" type="warning" @click="lockOrUnlock(scope.row)" circle plain/>
                    </ElTooltip>
                    <ElTooltip v-if="!isBanned(scope.row)" effect="light" placement="bottom" content="禁用">
                      <ElButton v-if="!isBanned(scope.row)" icon="hide" type="default" @click="disableOrEnable(scope.row)" circle plain/>
                    </ElTooltip>
                    <ElTooltip v-if="isBanned(scope.row)" effect="light" placement="bottom" content="启用">
                      <ElButton v-if="isBanned(scope.row)" icon="view" type="default" @click="disableOrEnable(scope.row)" circle plain/>
                    </ElTooltip>
                    <ElTooltip effect="light" placement="bottom" content="删除">
                      <ElButton icon="delete" type="danger" @click="deleteBy(scope.row)" circle plain/>
                    </ElTooltip>
                  </ElSpace>
                </template>
              </ElTableColumn>
            </ElTable>
          </ElScrollbar>
        </slot>
      </ElMain>
      <ElFooter style="width: calc(100vw - 180px); height: 100px;display: flex; flex-direction: column; justify-content: center; align-items: center">
        <slot name="footer">
          <ElPagination background :page-size="query.size" :total="query.total" @change="(value: number) => {
            query.page = Math.max(0, value);
            search();
          }"/>
        </slot>
        <ElDialog title="新增" v-model="insert.show">
          <ElForm ref="insertForm" :model="insert.target">
            <slot name="insert" :insert="insert.target"></slot>
          </ElForm>
          <template #footer>
            <ElButton type="primary" @click="insertOperator.perform()" plain>确定</ElButton>
            <ElButton type="warning" @click="insertOperator.reset()" plain>重置</ElButton>
            <ElButton type="info" @click="insertOperator.dismiss()" plain>取消</ElButton>
          </template>
        </ElDialog>
        <ElDialog title="修改" v-model="update.show">
          <ElForm ref="updateForm" :model="update.target">
            <slot name="update" :update="update.target"></slot>
          </ElForm>
          <template #footer>
            <ElButton type="primary" @click="updateOperator.perform()" plain>确定</ElButton>
            <ElButton type="warning" @click="updateOperator.reset()" plain>重置</ElButton>
            <ElButton type="info" @click="updateOperator.dismiss()" plain>取消</ElButton>
          </template>
        </ElDialog>
      </ElFooter>
    </ElContainer>

  </ElContainer>
</template>
<script generic="E extends BaseEntity" setup lang="ts">

import type {BaseEntity, Page, Query} from "@/declaration/entity.ts";
import {type ModelRef, onMounted, type Reactive, reactive, ref, type Ref,} from "vue";
import type {BaseService} from "@/interaction/service.ts";
import type {Insert, Operator, Update} from "@/declaration/modulize.ts";
import {type Consumer, type MaybeInvalid, type Predicate, type Runnable, validate} from "semantic-typescript";
import {ElMessage, ElMessageBox, type FormInstance} from "element-plus";
import {Check, Clock, Close, Lock, Unlock} from "@element-plus/icons-vue";
import {useDateFormat} from "@/hooks/datetime.ts";

interface Property<E extends BaseEntity>{
  service: BaseService<E>;
}
const property = defineProps<Property<E>>();

const query: ModelRef<Query<E>> = defineModel("query", {
  required: true
});

const queryForm: Ref<MaybeInvalid<FormInstance>> = ref<MaybeInvalid<FormInstance>>();
const search: Runnable = (): void => {
  load.value = true;
  if(validate(queryForm.value)){
    queryForm.value.validate().then((value) => {
      if(value){
        property.service.findAllPagedBy(query.value as unknown as Query<E>).then((page: Page<E>): void => {
          query.value.total = page.page.totalElements;
          query.value.page = page.page.number;
          query.value.size = Math.max(page.page.size, 10);
          data.value.length = 0;
          emit("search", query.value);
          page.content.forEach((entity) => data.value.push(entity as unknown as any));
          load.value = false;
        }, ()=> {
          load.value = false;
          ElMessage({
            message: "操作失败",
            type: "warning"
          });
        });
      }else{
        load.value = false;
      }
    });
  }
};
const resetQueryForm: Runnable = (): void => {
  if(validate(queryForm.value)){
    queryForm.value.resetFields();
    query.value = {
      total: 0,
      page: 0,
      size: 10,
      target: {},
      direction: "ASC"
    };
  }
};

const insert: ModelRef<Insert<E>> = defineModel("insert", {
  required: true
})
const insertForm: Ref<MaybeInvalid<FormInstance>> = ref<MaybeInvalid<FormInstance>>();
const insertOperator: Operator<E> = {
  ready: (): void => {
    insert.value.show = true;
  },
  dismiss: (): void => {
    insert.value.show = false;
  },
  reset: (): void => {
    if(validate(insertForm.value)){
      insertForm.value.resetFields();
      insert.value.target = {};
    }
  },
  validate: async (): Promise<void> => {
    return await new Promise<void>((resolve, reject): void => {
      if(validate(insertForm.value)){
        insertForm.value.validate().then((value: boolean): void => {
          emit("validate", value);
          if(value){
            resolve();
          }else{
            reject();
          }
        }, reject);
      }else{
        reject();
      }
    });
  },
  perform: async (): Promise<void> => {
    return await new Promise<void>((resolve, reject): void => {
      insertOperator.validate().then((): void => {
        property.service.insert(insert.value.target as unknown as E).then((): void => {
          emit("insert", update.value);
          search();
          resolve();
          ElMessage({
            message: "操作成功",
            type: "success"
          });
          insertOperator.dismiss();
        }, (): void => {
          reject();
          ElMessage({
            message: "操作失败",
            type: "warning"
          });
        });
      }, (): void => {
        reject();
        ElMessage({
          message: "操作失败",
          type: "warning"
        });
      });
    });
  }
};

const update: ModelRef<Update<E>> = defineModel("update", {
  required: true
});
const updateForm: Ref<MaybeInvalid<FormInstance>> = ref<MaybeInvalid<FormInstance>>();
const updateOperator: Operator<E> = {
  ready: (): void => {
    update.value.show = true;
  },
  dismiss: (): void => {
    update.value.show = false;
  },
  reset: async (): Promise<void> => {
    if(validate(updateForm.value)){
      updateForm.value.resetFields();
    }
  },
  validate: async (): Promise<void> => {
    return await new Promise<void>((resolve, reject): void => {
      if(validate(updateForm.value)){
        updateForm.value.validate().then((value: boolean): void => {
          emit("validate", value);
          if(value){
            resolve();
          }else{
            reject();
          }
        }, reject);
      }else{
        reject();
      }
    });
  },
  perform: async (): Promise<void> => {
    return await new Promise<void>((resolve, reject): void => {
      updateOperator.validate().then((): void => {
        property.service.update(update.value.target as unknown as E).then((): void => {
          emit("update", update.value);
          search();
          ElMessage({
            message: "操作成功",
            type: "success"
          });
          updateOperator.dismiss();
        }, (): void => {
          ElMessage({
            message: "操作失败",
            type: "warning"
          });
        })
      }, reject);
    });
  }
};
const isLocked: Predicate<E | Partial<E>> = (entity: E | Partial<E>): boolean => {
  if(validate(entity.lock)){
    let now: Date = new Date();
    return now.getTime() < entity.lock.getTime();
  }
  return false;
};
const isBanned: Predicate<E | Partial<E>> = (entity: E | Partial<E>): boolean => {
  if(validate(entity.ban)){
    let now: Date = new Date();
    return now.getTime() < entity.ban.getTime();
  }
  return false;
};
const lockOrUnlock: Consumer<E | Partial<E>> = (entity: E | Partial<E>): void => {
  let now = new Date();
  if(isLocked(entity)){
    ElMessageBox.confirm(`确定要解锁项目${entity.id}吗？`, "提示", {
      type: "warning",
      confirmButtonText: "确定",
      cancelButtonText: "取消"
    }).then((): void => {
      now.setTime(now.getTime() - 1000*60*5);
      entity.lock = now;
      property.service.update(entity).then((): void => {
        ElMessage({
          message: "操作成功，已解除锁定。",
          type: "success"
        });
      }, (): void => {
        ElMessage({
          message: "操作失败，无法解除锁定。",
          type: "warning"
        });
      });
    }, (): void => {
      ElMessage({
        message: "操作取消",
        type: "warning"
      });
    });
  }else{
    ElMessageBox.confirm(`确定要锁定项目${entity.id}吗？`, "提示", {
      type: "warning",
      confirmButtonText: "确定",
      cancelButtonText: "取消"
    }).then((): void => {
      now.setFullYear(now.getFullYear() + 100);
      entity.lock = now;
      property.service.update(entity).then((): void => {
        ElMessage({
          message: "操作成功，已锁定。",
          type: "success"
        });
      }, (): void => {
        ElMessage({
          message: "操作失败，无法锁定。",
          type: "warning"
        });
      });
    }, (): void => {
      ElMessage({
        message: "操作取消",
        type: "warning"
      });
    });
  }
};

const disableOrEnable: Consumer<E | Partial<E>> = (entity: E | Partial<E>): void => {
  let now = new Date();
  if(isBanned(entity)){
    ElMessageBox.confirm(`确定要启用项目${entity.id}吗？`, "提示", {
      type: "warning",
      confirmButtonText: "确定",
      cancelButtonText: "取消"
    }).then((): void => {
      now.setTime(now.getTime() - 1000*60*5);
      entity.ban = now;
      property.service.update(entity).then((): void => {
        ElMessage({
          message: "操作成功，已启用。",
          type: "success"
        });
      }, (): void => {
        ElMessage({
          message: "操作失败，无法解除禁用。",
          type: "warning"
        });
      });
    }, (): void => {
      ElMessage({
        message: "操作取消",
        type: "warning"
      });
    });
  }else{
    ElMessageBox.confirm(`确定要禁用项目${entity.id}吗？`, "提示", {
      type: "warning",
      confirmButtonText: "确定",
      cancelButtonText: "取消"
    }).then((): void => {
      now.setFullYear(now.getFullYear() + 100);
      entity.lock = now;
      property.service.update(entity).then((): void => {
        ElMessage({
          message: "操作成功，已禁用。",
          type: "success"
        });
      }, (): void => {
        ElMessage({
          message: "操作失败，无法禁用。",
          type: "warning"
        });
      });
    }, (): void => {
      ElMessage({
        message: "操作取消",
        type: "warning"
      });
    });
  }
};

const load: Ref<boolean> = ref<boolean>(true);
const data: ModelRef<Array<E>> = defineModel("data", {
  required: true
})
const multiple: Ref<boolean> = ref<boolean>(false);
const selection: Reactive<Array<E>> = reactive<Array<E>>([]);
const selectionChange: Consumer<Array<E>> = (value: Array<E>): void => {
  selection.length = 0;
  selection.push(...value as unknown as any);
};
const multipleDelete: Runnable = (): void => {
  if(selection.length > 0){
    ElMessageBox.confirm("确定要删除选定的多条数据吗？", "提示", {
      type: "warning",
      confirmButtonText: "确定",
      cancelButtonLoadingIcon: "取消"
    }).then((): void => {
      property.service.deleteAllByIdentifiers((selection as Array<E>).map((entity: E) => entity.id)).then((): void => {
        search();
        ElMessage({
          message: "操作成功",
          type: "success"
        });
      }, (): void => {
        ElMessage({
          message: "操作失败",
          type: "error"
        });
      });
    });
  }
};

interface Emit<E extends BaseEntity>{
  (event: "search", value: Query<E>): void;
  (event: "load", value: Page<E>): void;
  (event: "clear"): void;
  (event: "insert", value: Insert<E>): void;
  (event: "update", value: Update<E>): void;
  (event: "delete", value: E | Partial<E>): void;
  (event: "validate", value: boolean): void;
}
const emit = defineEmits<Emit<E>>();

const deleteBy: Consumer<E> = (entity: E) : void => {
  ElMessageBox.confirm(`确定要删除id为${entity.id}的项目吗？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then((): void => {
    property.service.deleteByIdentifier(entity.id).then((): void => {
      search();
      ElMessage({
        message: "操作成功",
        type: "success"
      });
    }, (): void => {
      search();
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


onMounted((): void => {
  search();
  setTimeout((): void => {
    load.value = false;
  }, 3000)
});
</script>

<style scoped>

</style>