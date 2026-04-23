import {createRouter, createWebHistory, type RouteLocationNormalizedLoadedGeneric, type Router} from 'vue-router'
import Home from "@/views/Home.vue";
import {useAuthenticationStore} from "@/stores/authentication.ts";
import {ElMessage} from "element-plus";
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
          name: "ManagementIndex",
          path: "/management",
          meta: {
            title: "管理后台"
          },
          component: () => import("../views/management/Index.vue"),
        },
        {
          name: "ManagementAuthority",
          path: "/management/authority",
          meta: {
            title: "权限管理"
          },
          component: () => import("../views/management/consumer/Authority.vue"),
        },
        {
          name: "ManagementConsumer",
          path: "/management/consumer",
          meta: {
            title: "用户管理"
          },
          component: () => import("../views/management/consumer/Consumer.vue"),
        },
        {
          name: "ManagementInvitation",
          path: "/management/invitation",
          meta: {
            title: "邀请管理"
          },
          component: () => import("../views/management/consumer/Invitation.vue"),
        },
        {
          name: "ManagementMenu",
          path: "/management/menu",
          meta: {
            title: "菜单管理"
          },
          component: () => import("../views/management/consumer/Invitation.vue"),
        },
        {
          name: "ManagementRole",
          path: "/management/role",
          meta: {
            title: "角色管理"
          },
          component: () => import("../views/management/consumer/Role.vue"),
        },
        {
          name: "ManagementRoute",
          path: "/management/route",
          meta: {
            title: "路由管理"
          },
          component: () => import("../views/management/consumer/Invitation.vue"),
        },
        {
          name: "ManagementToken",
          path: "/management/token",
          meta: {
            title: "凭据管理"
          },
          component: () => import("../views/management/consumer/Token.vue"),
        },
        {
          name: "ManagementBook",
          path: "/management/book",
          meta: {
            title: "书籍管理"
          },
          component: () => import("../views/management/book/Book.vue"),
        },
        {
          name: "ManagementChapter",
          path: "/management/book/chapter",
          meta: {
            title: "章节管理"
          },
          component: () => import("../views/management/book/Chapter.vue"),
        },
        {
          name: "ManagementChat",
          path: "/management/chat",
          meta: {
            title: "聊天管理"
          },
          component: () => import("../views/management/chat/Chat.vue"),
        },
        {
          name: "ManagementMessage",
          path: "/management/chat/message",
          meta: {
            title: "消息管理"
          },
          component: () => import("../views/management/chat/Message.vue"),
        },
        {
          name: "ManagementAnnouncement",
          path: "/management/announcement",
          meta: {
            title: "消息管理"
          },
          component: () => import("../views/management/Announcement.vue"),
        },
        {
          name: "ManagementOnline",
          path: "/management/online",
          meta: {
            title: "在线日志"
          },
          component: () => import("../views/management/Online.vue"),
        },
        {
          name: "ManagementOperation",
          path: "/management/operation",
          meta: {
            title: "操作日志"
          },
          component: () => import("../views/management/Operation.vue"),
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

router.beforeEach((to: RouteLocationNormalizedLoadedGeneric, from) => {
  let white = new Set(["/","/authentication/account"]);
  if(useAuthenticationStore().authenticated){
    if(to.path === "/authentication/account"){
      return "/";
    }
    return true;
  }else{
    if(white.has(to.path)){
      return true;
    }else{
      ElMessage({
        message: "身份认证无效，请登录。",
        type: "info"
      });
      return "/authentication/account";
    }
  }
});
export default router;
