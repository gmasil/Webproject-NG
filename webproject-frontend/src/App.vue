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
  <div
    v-if="store.isInitialized"
    id="app"
    class="bg-theme-background min-h-screen text-theme-text relative"
  >
    <navbar v-if="!store.isAccessRestricted" />
    <div class="grid justify-items-center pb-14">
      <div class="p-4 w-full sm:w-sm md:w-md lg:w-lg xl:w-xl 2xl:w-2xl">
        <router-view />
      </div>
    </div>
    <div
      v-if="store.appProperties.git.commit"
      class="bg-theme-background-highlight text-center p-2 absolute bottom-0 w-full border-t-2 border-theme-text"
    >
      Version: {{ store.appProperties?.git.build.version }} ({{
        store.appProperties.git.commit.describeShort
      }})
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { useStore } from "@/store/pinia";
import type { Store } from "@/store/pinia";
import { ToastInterface, useToast } from "vue-toastification";
import Coloris from "@melloware/coloris";
import { themeService } from "@/service/theme";
import { appService } from "@/service/app";
import { userService } from "@/service/user";

const store: Store = useStore();

const toast: ToastInterface = useToast();

onMounted(() => Coloris.init());

appService
  .loadAppProperties()
  .catch(() => toast.error("Error while loading application properties"));

userService
  .loadCurrentUser()
  .catch(() => toast.error("Error while loading current user"));

themeService
  .loadActiveTheme()
  .catch(() => toast.error("Error while loading active theme"));

defineExpose({
  store,
});
</script>
