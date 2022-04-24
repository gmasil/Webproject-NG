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
    v-if="isInitialized"
    id="app"
    class="bg-theme-background min-h-screen text-theme-text"
  >
    <navbar v-if="!isAccessRestricted" />
    <router-view />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapState } from "pinia";
import { useStore } from "@/store/pinia";
import { ToastInterface, useToast } from "vue-toastification";
import Coloris from "@melloware/coloris";
import { themeService } from "@/service/theme";
import { appService } from "@/service/app";
import { userService } from "@/service/user";

const toast: ToastInterface = useToast();

export default defineComponent({
  name: "App",
  computed: {
    ...mapState(useStore, ["isInitialized", "isAccessRestricted"]),
  },
  created(): void {
    appService
      .loadAppProperties()
      .catch(() => toast.error("Error while loading application properties"));
    userService
      .loadCurrentUser()
      .catch(() => toast.error("Error while loading current user"));
    themeService
      .loadActiveTheme()
      .catch(() => toast.error("Error while loading active theme"));
  },
  mounted(): void {
    Coloris.init();
  },
});
</script>
