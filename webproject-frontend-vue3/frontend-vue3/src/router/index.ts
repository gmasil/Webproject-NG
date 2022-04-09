import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import { AppProperties, User, CallbackFunction } from "@/types";
import { store } from "@/store";
import HelloWorld from "@/components/HelloWorld.vue";
import VideoList from "@/components/VideoList.vue";
import VideoDetails from "@/components/VideoDetails.vue";
import Themes from "@/components/Themes.vue";
import ChangePassword from "@/components/account/ChangePassword.vue";
import Login from "@/components/Login.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "HelloWorld",
    component: HelloWorld,
  },
  {
    path: "/videos",
    name: "VideoList",
    component: VideoList,
  },
  {
    path: "/videos/:id",
    name: "VideoDetails",
    component: VideoDetails,
  },
  {
    path: "/themes",
    name: "Themes",
    component: Themes,
    meta: { authorize: [] },
  },
  {
    path: "/changepassword",
    name: "ChangePassword",
    component: ChangePassword,
    meta: { authorize: [] },
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
  },
  {
    path: "/:pathMatch(.*)*",
    component: Error,
  },
  //{
  // path: "/about",
  // name: "about",
  // route level code-splitting
  // this generates a separate chunk (about.[hash].js) for this route
  // which is lazy-loaded when the route is visited.
  //  component: () =>
  //    import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  //},
];

const router = createRouter({
  // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
  history: createWebHistory(process.env.BASE_URL as string),
  routes,
});

function isInitialized(): boolean {
  // eslint-disable-next-line @typescript-eslint/no-unsafe-return, @typescript-eslint/no-unsafe-member-access
  return store.getters["isInitialized"];
}

function isPublicAccess(): boolean {
  // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment, @typescript-eslint/no-unsafe-member-access
  const props: AppProperties = store.getters["getAppProperties"];
  return props.publicAccess;
}

function getCurrentUser(): User | null {
  // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment, @typescript-eslint/no-unsafe-member-access
  return store.getters["getCurrentUser"] as User | null;
}

function waitForInit(callback: CallbackFunction): void {
  if (!isInitialized()) {
    setTimeout(function () {
      waitForInit(callback);
    }, 50);
  } else {
    callback();
  }
}

router.beforeEach((to, from, next) => {
  if (to.path == "/login" || to.path == "/logout" || to.path == "/error") {
    next();
  }
  waitForInit(() => {
    if (!isPublicAccess() && getCurrentUser() == null) {
      return next({ path: "/login" });
    }
    const authorize: boolean = to.meta?.authorize as boolean;
    if (authorize) {
      if (getCurrentUser() == null) {
        return next({ path: "/error" });
      } else {
        next();
      }
    } else {
      next();
    }
  });
});

export default router;
