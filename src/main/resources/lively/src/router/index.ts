import {createRouter, createWebHistory, type Router} from 'vue-router'
import Home from "@/views/Home.vue";
const router: Router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      name: "Home",
      path: "/",
      meta: {
        title: "首页"
      },
      component: Home
    },
    {
      name: "Management",
      path: "/management",
      meta: {
        title: "管理"
      },
      component: () => import("../views/Management.vue"),
      children: [
        {
          name: "Authority",
          path: "/management/authority",
          meta: {
            title: "权限管理"
          },
          component: () => import("../views/management/consumer/Authority.vue"),
        },
        {
          name: "Consumer",
          path: "/management/consumer",
          meta: {
            title: "用户管理"
          },
          component: () => import("../views/management/consumer/Consumer.vue"),
        },
        {
          name: "Invitation",
          path: "/management/invitation",
          meta: {
            title: "邀请管理"
          },
          component: () => import("../views/management/consumer/Invitation.vue"),
        },
        {
          name: "Role",
          path: "/management/role",
          meta: {
            title: "角色管理"
          },
          component: () => import("../views/management/consumer/Role.vue"),
        },
        {
          name: "Token",
          path: "/management/token",
          meta: {
            title: "凭据管理"
          },
          component: () => import("../views/management/consumer/Token.vue"),
        },
      ]
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
        },
        {
          name: "Profile",
          path: "/authentication/profile",
          component: () => import("../views/authentication/Profile.vue")
        }
      ]
    }
  ],
})

export default router
