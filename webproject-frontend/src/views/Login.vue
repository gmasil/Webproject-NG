<!--

    Webproject NG
    Copyright © 2021 - 2022 Gmasil

    This file is part of Webproject NG.

    Webproject NG is licensed under the Creative Commons
    Attribution-NonCommercial-ShareAlike 4.0 International
    Public License ("Public License").

    Webproject NG is non-free software: you can redistribute
    it and/or modify it under the terms of the Public License.

    You should have received a copy of the Public License along
    with Webproject NG. If not, see
    https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt

-->
<template>
  <div class="grid justify-items-center -m-4">
    <div class="text-center pt-20 text-lg w-full sm:w-sm p-4">
      <div v-if="store.isAccessRestricted" class="mb-12">
        <p class="text-3xl">Restricted Access!</p>
        <p>You have to login to access this site!</p>
      </div>
      <form method="post" action="/performlogin">
        <div class="grid grid-cols-2 gap-4">
          <div class="col-span-2">
            <p class="text-left pl-2">Username:</p>
            <input
              id="login-input-username"
              ref="inputUsername"
              class="w-full p-2 rounded-lg"
              type="text"
              name="username"
              :value="getUsername"
              autofocus
            />
          </div>
          <div class="col-span-2">
            <p class="text-left pl-2">Password:</p>
            <input
              id="login-input-password"
              ref="inputPassword"
              class="w-full p-2 rounded-lg"
              type="password"
              name="password"
            />
          </div>
          <div class="justify-self-start pl-2">
            <input
              id="login-input-remember"
              class="cursor-pointer"
              type="checkbox"
              name="rememberme"
              :checked="isRememberMe"
            />
            <label class="w-full ml-2 cursor-pointer" for="login-input-remember"
              >Stay logged in</label
            >
          </div>
          <div class="justify-self-end">
            <input
              id="login-input-submit"
              class="px-4 py-1 rounded-lg cursor-pointer bg-theme-background-highlight"
              type="submit"
              value="Login"
            />
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useStore } from "@/store/pinia";
import type { Store } from "@/store/pinia";
import { computed, onMounted, ref } from "@vue/runtime-core";
import type { Ref, ComputedRef } from "@vue/runtime-core";
import { useRoute } from "vue-router";
import type { RouteLocationNormalizedLoaded } from "vue-router";
import { useToast } from "vue-toastification";
import type { ToastInterface } from "vue-toastification";

const store: Store = useStore();
const route: RouteLocationNormalizedLoaded = useRoute();
const toast: ToastInterface = useToast();

const inputUsername: Ref<HTMLInputElement | undefined> =
  ref<HTMLInputElement>();
const inputPassword: Ref<HTMLInputElement | undefined> =
  ref<HTMLInputElement>();

const isLoginFailed: ComputedRef<boolean> = computed(() => {
  return route.query.error !== undefined;
});

const isLogout: ComputedRef<boolean> = computed(() => {
  return route.query.logout !== undefined;
});

const isRememberMe: ComputedRef<boolean> = computed(() => {
  return route.query.rememberme !== undefined && route.query.rememberme == "on";
});

const getUsername: ComputedRef<string> = computed(() => {
  if (route.query.username && route.query.username.length != 0) {
    return route.query.username as string;
  }
  return "";
});

onMounted(() => {
  if (inputUsername.value && inputPassword.value) {
    if (getUsername.value.length == 0) {
      inputUsername.value.focus();
    } else {
      inputPassword.value.focus();
    }
  }
  if (isLoginFailed.value) {
    toast.error("Wrong username or password");
  }
  if (isLogout.value) {
    toast.success("You have been logged out");
  }
});

defineExpose({
  store,
  isLoginFailed,
  isLogout,
  getUsername,
  isRememberMe,
});
</script>
