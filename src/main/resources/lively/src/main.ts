import {type App, createApp} from "vue"
import {createPinia, type Pinia} from "pinia"
import Application from "./App.vue"
import router from "./router"
import "./style.css"
import installElementPlus from "./plugins/element.ts";
import piniaPluginPersistedState from "pinia-plugin-persistedstate"
import authority from "./directives/authority.ts"
const application: App<Element> = createApp(Application);
installElementPlus(application);
const pinia: Pinia = createPinia();
pinia.use(piniaPluginPersistedState);
application.use(pinia);
application.use(router);
authority(application);
router.isReady().then(():void => {
    application.mount("#app");
});

