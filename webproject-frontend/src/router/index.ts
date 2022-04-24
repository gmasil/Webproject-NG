///
/// Webproject NG
/// Copyright © 2021 - 2022 Gmasil
///
/// This file is part of Webproject NG.
///
/// Webproject NG is licensed under the Creative Commons
/// Attribution-NonCommercial-ShareAlike 4.0 International
/// Public License ("Public License").
///
/// Webproject NG is non-free software: you can redistribute
/// it and/or modify it under the terms of the Public License.
///
/// You should have received a copy of the Public License along
/// with Webproject NG. If not, see
/// https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt
///

import {
  createRouter,
  createWebHistory,
  RouteRecordRaw,
  RouteLocationNormalized,
  NavigationGuardNext,
  Router,
} from "vue-router";
import { CallbackFunction } from "@/types";
import { Store, useStore } from "@/store/pinia";
import HomeView from "@/views/HomeView.vue";
import VideoList from "@/views/VideoList.vue";
import VideoDetails from "@/views/VideoDetails.vue";
import Themes from "@/views/Themes.vue";
import ChangePassword from "@/views/account/ChangePassword.vue";
import Login from "@/views/Login.vue";

const routes: RouteRecordRaw[] = [
  {
    path: "/",
    name: "HomeView",
    component: HomeView,
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

const router: Router = createRouter({
  // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
  history: createWebHistory(process.env.BASE_URL as string),
  routes,
});

function waitForInit(callback: CallbackFunction): void {
  if (!useStore().isInitialized) {
    setTimeout(() => {
      waitForInit(callback);
    }, 50);
  } else {
    callback();
  }
}

router.beforeEach(
  (
    to: RouteLocationNormalized,
    _from: RouteLocationNormalized,
    next: NavigationGuardNext
  ): void => {
    if (to.path == "/login" || to.path == "/logout" || to.path == "/error") {
      next();
    }
    const store: Store = useStore();
    waitForInit(() => {
      if (store.isAccessRestricted) {
        return next({ path: "/login" });
      }
      const authorize: boolean = to.meta?.authorize as boolean;
      if (authorize) {
        if (!store.isAuthenticated) {
          return next({ path: "/error" });
        } else {
          next();
        }
      } else {
        next();
      }
    });
  }
);

export default router;
