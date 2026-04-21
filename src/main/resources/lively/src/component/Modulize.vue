<template>
  <ElContainer style="width: calc(100vw - 180px); height: calc(100vh - 100px); user-select: none;">
    <ElHeader style="height: 150px; width: calc(100vw - 180px); display: flex; flex-direction: column;">
      <slot name="header">
        <ElForm ref="queryForm" inline :model="query.target">
          <slot name="search" :search="query.target"></slot>
          <ElFormItem>
            <ElTooltip effect="light" placement="bottom" content="搜索">
              <ElButton icon="search" type="primary" @click="search()" circle plain/>
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
            <ElButton icon="refresh" @click="obtain()" type="info" circle plain/>
          </ElTooltip>
        </ElSpace>
      </slot>
    </ElHeader>
    <ElContainer style="width: calc(100vw - 180px); height: calc(100vh - 300px);">
      <ElMain style="width: calc(100vw - 180px); height: calc(100vh - 420px);">
        <slot name="main">
          <ElScrollbar>
            <ElTable v-loading="load" :data="data" style="width: calc(100vw - 180px); height: calc(100vh - 420px)">
              <ElTableColumn label="id" prop="id"></ElTableColumn>
              <slot name="column">
              </slot>
              <ElTableColumn label="权限" prop="authority"></ElTableColumn>
              <ElTableColumn label="锁定" prop="lock"></ElTableColumn>
              <ElTableColumn label="禁用" prop="lock"></ElTableColumn>
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
      <ElFooter style="width: calc(100vw - 180px); height: 120px;display: flex; flex-direction: column; justify-content: center; align-items: center">
        <slot name="footer">
          <ElPagination background :page-size="query.size" :page-count="query.total" @change="(value: number) => query.page = value"/>
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
import {onMounted, type Reactive, reactive, ref, type Ref} from "vue";
import type {BaseService} from "@/interaction/service.ts";
import type {Insert, Operator, Update} from "@/declaration/modulize.ts";
import {type Consumer, type MaybeInvalid, type Predicate, type Runnable, validate} from "semantic-typescript";
import {ElMessage, ElMessageBox, type FormInstance} from "element-plus";

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
const queryForm: Ref<MaybeInvalid<FormInstance>> = ref<MaybeInvalid<FormInstance>>();
const search: Runnable = (): void => {
  property.service.findAllPagedBy(query as unknown as Query<E>).then((page: Page<E>): void => {
    query.total = page.page.totalPages;
    query.page = page.page.number;
    query.size = Math.max(page.page.size, 10)
    console.log(page, query)
  }, ()=> {
    ElMessage({
      message: "操作失败",
      type: "warning"
    });
  });
};

const insert: Reactive<Insert<E>> = reactive<Insert<E>>({
  show: false,
  target: {}
});
const insertForm: Ref<MaybeInvalid<FormInstance>> = ref<MaybeInvalid<FormInstance>>();
const insertOperator: Operator<E> = {
  ready: (): void => {
    insert.show = true;
  },
  dismiss: (): void => {
    insert.show = false;
  },
  reset: (): void => {
    if(validate(insertForm.value)){
      insertForm.value.resetFields();
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
        property.service.insert(insert.target as unknown as E).then((): void => {
          emit("insert", update.target as unknown as E);
          obtain();
          ElMessage({
            message: "操作成功",
            type: "success"
          });
          insertOperator.dismiss();
        }, (): void => {
          ElMessage({
            message: "操作失败",
            type: "warning"
          });
        });
      }, (): void => {
        ElMessage({
          message: "操作失败",
          type: "warning"
        });
      });
    });
  }
};

const update: Reactive<Update<E>> = reactive<Update<E>>({
  show: false,
  target: {}
});
const updateForm: Ref<MaybeInvalid<FormInstance>> = ref<MaybeInvalid<FormInstance>>();
const updateOperator: Operator<E> = {
  ready: (): void => {
    update.show = true;
  },
  dismiss: (): void => {
    update.show = false;
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
        property.service.update(update.target as unknown as E).then((): void => {
          emit("update", update.target as unknown as E);
          obtain();
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
  (event: "validate", value: boolean): void;
}
const emit = defineEmits<Emit>();

const deleteBy: Consumer<E> = (entity: E) : void => {
  ElMessageBox.confirm(`确定要删除id为${entity.id}的项目吗？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then((): void => {
    property.service.deleteByIdentifier(entity.id).then((): void => {
      obtain();
      ElMessage({
        message: "操作成功",
        type: "success"
      });
    }, (): void => {
      obtain();
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
const load: Ref<boolean> = ref<boolean>(true);
const obtain: Runnable = (): void => {
  load.value = true;
  property.service.findAllPagedBy(query as unknown as Query<E>).then((page): void => {
    data.length = 0;
    data.push(...page.content as unknown as any);
    emit("load", page);
    load.value = false;
  }, (): void => {
    ElMessage({
      type: "warning",
      message: "加载失败"
    });
  });
  setTimeout((): void => {
    load.value = false;
  }, 3000)
}

onMounted((): void => {
  obtain();
});
</script>

<style scoped>

</style>