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

import Vue from "vue";
import Router from "vue-router";
import HelloWorld from "@/components/HelloWorld.vue";
import VideoList from "@/components/VideoList.vue";
import Themes from "@/components/Themes.vue";
import Login from "@/components/Login.vue";
import ChangePassword from "@/components/account/ChangePassword.vue";
import Error from "@/components/Error.vue";
import { AppProperties, CallbackFunction, User } from "@/types";

Vue.use(Router);

import store from "@/store";

const router = new Router({
  mode: "history",
  routes: [
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
      path: "*",
      component: Error,
    },
  ],
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
    if (!isPublicAccess()) {
      if (getCurrentUser() == null) {
        return next({ path: "/login" });
      }
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
