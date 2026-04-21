<template>
  <div class="toolbar-container">
    <div>
      <slot name="prefix">
        <ElButton icon="cherry" @click="goHome()"/>
      </slot>
    </div>
    <div>
      <slot name="default">
        <ElSpace wrap>
          <ElLink v-for="link in visible" :disabled="link.disabled" :href="link.href" :icon="link.icon" :hidden="link.hidden" :target="link.target">
            {{link.text}}
          </ElLink>
        </ElSpace>
      </slot>
    </div>
    <div>
      <slot name="suffix">
        <AvatarCard/>
      </slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, type ComputedRef, reactive, useCssModule} from "vue";
import AvatarCard from "@/component/AvatarCard.vue";
import type {Link} from "@/declaration/component.ts"
import {type Router, useRouter} from "vue-router";
import type {Runnable} from "semantic-typescript";
interface Property{
  fixed?: boolean;
  shadow?: boolean;
  bordered?: boolean;
  transparent?: boolean;
  backgroundColor?: string;
}

const properties = withDefaults(defineProps<Property>(), {
  fixed: false,
  backgroundColor: "#ffffff",
  shadow: true,
  bordered: true,
  transparent: false,
});

const router: Router = useRouter();

const model = defineModel<Array<Link>>({
  default: ()=> []
});
const visible: ComputedRef<Array<Link>> = computed<Array<Link>>((): Array<Link> => {
  return model.value.filter((link) => !link.hidden && !link.disabled);
});

const goHome: Runnable = (): void => {
  router.replace({
    path: "/"
  });
};
</script>


<style scoped>

.toolbar-container{
  width: 100vw;
  height: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}

.toolbar-container > div:first-child {
  flex: none;
  text-align: left;
  padding-left: 20px;
}

.toolbar-container > div:not(:first-child):not(:last-child) {
  text-align: center;
  flex: 1;
}

.toolbar-container > div:last-child {
  flex: none;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
  padding-right: 20px;
}
</style>