/*
 * Webproject NG
 * Copyright © 2021 Gmasil
 *
 * This file is part of Webproject NG.
 *
 * Webproject NG is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Webproject NG is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Webproject NG. If not, see <https://www.gnu.org/licenses/>.
 */
import Vue from "vue";
import Router from "vue-router";
import HelloWorld from "@/components/HelloWorld";
import VideoList from "@/components/VideoList";
import Themes from "@/components/Themes";
import Login from "@/components/Login";
import ChangePassword from "@/components/account/ChangePassword";
import Error from "@/components/Error";

Vue.use(Router);

import store from "@/vuex";

export const router = new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      name: "HelloWorld",
      component: HelloWorld
    },
    {
      path: "/videos",
      name: "VideoList",
      component: VideoList
    },
    {
      path: "/themes",
      name: "Themes",
      component: Themes,
      meta: { authorize: [] }
    },
    {
      path: "/changepassword",
      name: "ChangePassword",
      component: ChangePassword,
      meta: { authorize: [] }
    },
    {
      path: "/login",
      name: "Login",
      component: Login
    },
    {
      path: "*",
      component: Error
    }
  ]
});

function waitForInit(callback) {
  if (!store.state.initialized) {
    setTimeout(function() {
      waitForInit(callback);
    }, 50);
  } else {
    callback();
  }
}

router.beforeEach((to, from, next) => {
  const { authorize } = to.meta;
  if (authorize) {
    waitForInit(() => {
      if (!store.state.currentUser) {
        return next({ path: "/error" });
      } else {
        next();
      }
    });
  } else {
    next();
  }
});
