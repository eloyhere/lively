package pers.eloyhere.lively.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@RestController("generationController")
@RequestMapping("/generation")
class GenerationController {

    @GetMapping(value = "management")
    public ResponseEntity<String> entity(String module){
        String upperModule = StringUtils.capitalize(module.toLowerCase(Locale.ROOT));
        String lowerModule = module.toLowerCase(Locale.ROOT);

        String template = """
            <template>
              <Modulize :service="%sService" v-model:query="query" v-model:data="data" v-model:update="update" v-model:insert="insert">
                <template #search="{search}">
                </template>
                <template #column>
                </template>
              </Modulize>
            </template>
            <script setup lang="ts">
            
            import Modulize from "@/component/Modulize.vue";
            import {%sService} from "@/interaction/service.ts";
            import {reactive, type Reactive} from "vue";
            import type {Query, %s} from "@/declaration/entity.ts";
            import type {Insert, Update} from "@/declaration/modulize.ts";
            const %sService: %sService = new %sService();
            const query: Reactive<Query<%s>> = reactive<Query<%s>>({
              page: 0,
              total: 0,
              size: 10,
              direction: "ASC",
              target: {}
            });
            const insert: Reactive<Insert<%s>> = reactive<Insert<%s>>({
              show: false,
              target: {}
            });
            const update: Reactive<Update<%s>> = reactive<Update<%s>>({
              show: false,
              target: {}
            });
            const data: Reactive<Array<%s>> = reactive<Array<%s>>([]);
            </script>
            
            <style scoped>
            
            </style>
            """.replace(" ", "");
        String result = String.format(template,
            lowerModule,
            upperModule,
            upperModule,
            lowerModule,
            upperModule,
            upperModule,
            upperModule,
            upperModule,
            upperModule,
            upperModule,
            upperModule,
            upperModule
        );

        return ResponseEntity.ok(result);
    }
}
