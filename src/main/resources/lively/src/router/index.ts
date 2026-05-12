import { createRouter, createWebHistory, type RouteLocationNormalizedLoadedGeneric, type Router } from 'vue-router'
import Home from "@/views/Home.vue";
import Splash from "@/views/Splash.vue";
import { authenticationStore } from "@/stores/authentication.ts";
const router: Router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      name: "欢迎",
      path: "/",
      meta: {
        title: "首页",
        roles: ["guest", "consumer", "administrator"]
      },
      component: Splash
    },
    {
      name: "Home",
      path: "/home",
      meta: {
        title: "首页",
        roles: ["guest", "consumer", "administrator"]
      },
      component: Home,
      children: [

      ],
    },
    {
      name: "Practises",
      path: "/practises",
      meta: {
        title: "中医题库",
        roles: ["consumer", "administrator"]
      },
      component: () => import("../views/Practises.vue"),
      children: [
        {
          name: "PractisesHome",
          path: "",
          meta: {
            title: "中医刷题",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/tcm/TcmHome.vue")
        },
        {
          name: "PractisesQuiz",
          path: "quiz",
          meta: {
            title: "答题中",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/tcm/TcmQuiz.vue")
        },
        {
          name: "PractisesResult",
          path: "result",
          meta: {
            title: "答题结果",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/tcm/TcmResult.vue")
        },
        {
          name: "PractisesWrongBook",
          path: "wrong-book",
          meta: {
            title: "错题本",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/tcm/TcmWrongBook.vue")
        },
        {
          name: "PractisesFavorites",
          path: "favorites",
          meta: {
            title: "收藏夹",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/tcm/TcmFavorites.vue")
        },
        {
          name: "PractisesStats",
          path: "stats",
          meta: {
            title: "学习统计",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/tcm/TcmStats.vue")
        }
      ]
    },
    {
      path: "/chat",
      name: "Chat",
      meta: {
        title: "聊天",
        roles: ["consumer", "administrator"]
      },
      component: () => import("../views/Chat.vue")
    },
    {
      name: "Management",
      path: "/management",
      meta: {
        title: "管理",
        roles: ["consumer", "administrator"]
      },
      component: () => import("../views/Management.vue"),
      children: [
        {
          name: "ManagementIndex",
          path: "/management",
          meta: {
            title: "管理后台",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/Index.vue"),
        },
        {
          name: "ManagementAuthority",
          path: "/management/authority",
          meta: {
            title: "权限管理",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/consumer/Authority.vue"),
        },
        {
          name: "ManagementConsumer",
          path: "/management/consumer",
          meta: {
            title: "用户管理",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/consumer/Consumer.vue"),
        },
        {
          name: "ManagementInvitation",
          path: "/management/invitation",
          meta: {
            title: "邀请管理",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/consumer/Invitation.vue"),
        },
        {
          name: "ManagementRole",
          path: "/management/role",
          meta: {
            title: "角色管理",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/consumer/Role.vue"),
        },
        {
          name: "ManagementToken",
          path: "/management/token",
          meta: {
            title: "凭据管理",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/consumer/Token.vue"),
        },
        {
          name: "ManagementBook",
          path: "/management/book",
          meta: {
            title: "书籍管理",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/book/Book.vue"),
        },
        {
          name: "ManagementChapter",
          path: "/management/book/chapter",
          meta: {
            title: "章节管理",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/book/Chapter.vue"),
        },
        {
          name: "ManagementChat",
          path: "/management/chat",
          meta: {
            title: "聊天管理",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/chat/Chat.vue"),
        },
        {
          name: "ManagementQuestion",
          path: "/management/question",
          meta: {
            title: "问答题管理",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/question/Question.vue"),
        },
        {
          name: "ManagementChoice",
          path: "/management/choice",
          meta: {
            title: "选择题管理",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/question/Choice.vue"),
        },
        {
          name: "ManagementMessage",
          path: "/management/chat/message",
          meta: {
            title: "消息管理",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/chat/Message.vue"),
        },
        {
          name: "ManagementAnnouncement",
          path: "/management/announcement",
          meta: {
            title: "公告管理",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/Announcement.vue"),
        },
        {
          name: "ManagementOperation",
          path: "/management/operation",
          meta: {
            title: "操作日志",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/log/Operation.vue"),
        },
        {
          name: "ManagementVisit",
          path: "/management/visit",
          meta: {
            title: "访问日志",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/log/Visit.vue"),
        },
        {
          name: "ManagementLevel",
          path: "/management/level",
          meta: {
            title: "访问日志",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/management/game/Level.vue"),
        },
        {
          name: "ManagementTcmDashboard",
          path: "/management/tcm/dashboard",
          meta: {
            title: "题库统计",
            roles: ["administrator"]
          },
          component: () => import("../views/management/tcm/TcmDashboard.vue"),
        },
        {
          name: "ManagementTcmQuestion",
          path: "/management/tcm/question",
          meta: {
            title: "中医题库管理",
            roles: ["administrator"]
          },
          component: () => import("../views/management/tcm/TcmQuestion.vue"),
        }
      ],
    },
    {
      name: "Authentication",
      path: "/authentication",
      component: () => import("../views/Authentication.vue"),
      children: [
        {
          name: "Account",
          path: "/authentication/account",
          meta: {
            title: "账户",
            roles: ["guest"]
          },
          component: () => import("../views/authentication/Account.vue")
        },
        {
          name: "Profile",
          path: "/authentication/profile",
          meta: {
            title: "资料",
            roles: ["consumer", "administrator"]
          },
          component: () => import("../views/authentication/Profile.vue")
        }
      ]
    }
  ]
})

router.beforeEach((to: RouteLocationNormalizedLoadedGeneric, from) => {
  if(to.path === "/"){
    return true;
  }
  let roles: Array<string> = (Array.isArray(to.meta.roles) ? to.meta.roles: []);
  if(roles.some((name: string) => authenticationStore().roles.some((role) => name === role.name))){
    return true;
  }
  if(roles.includes("guest")){
    return true;
  }
  return {
    path: from.path || "/",
    replace: true
  };
});


export default router;