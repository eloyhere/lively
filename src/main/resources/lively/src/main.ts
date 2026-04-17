import {type App, createApp} from "vue"
import { createPinia } from "pinia"

import Application from "./App.vue"
import router from "./router"
import "./style.css"
import installElementPlus from "./plugins/element.ts";
const application: App<Element> = createApp(Application);
installElementPlus(application);
application.use(createPinia());
application.use(router);
application.mount("#app");
