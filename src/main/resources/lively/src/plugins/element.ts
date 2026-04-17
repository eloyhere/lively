import type { App } from "vue"
import ElementPlus from "element-plus"
import * as icons from "@element-plus/icons-vue"
import "element-plus/theme-chalk/index.css"
import locale from "element-plus/es/locale/lang/zh-cn"

export default (app: App<Element>) => {
    app.use(ElementPlus, { locale })
    Object.keys(icons).forEach((key: string) => {
        app.component(key, (icons as any)[key]);
    });
}
