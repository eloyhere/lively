import {createRouter, createWebHistory, type Router} from 'vue-router'
import Home from "@/views/Home.vue";
const router: Router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      name: "Home",
      path: "/",
      component: Home
    },
    {
      name: "Authentication",
      path: "/authentication",
      component: () => import("../views/Authentication.vue"),
      children: [
        {
          name: "Account",
          path: "/authentication/account",
          component: () => import("../views/authentication/Account.vue")
        }
      ]
    }
  ],
})

export default router
