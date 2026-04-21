<template>
  <Modulize :service="bookService">

  </Modulize>
</template>
<script setup lang="ts">

import Modulize from "@/component/Modulize.vue";
import {BookService} from "@/interaction/service.ts";
const bookService: BookService = new BookService();

</script>



<style scoped>

</style>