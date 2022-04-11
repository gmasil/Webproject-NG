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
import { mapState, mapStores } from "pinia";
import { useStore } from "@/store/pinia";
import { useToast } from "vue-toastification";
import Coloris from "@melloware/coloris";
import { loadActiveTheme } from "@/service/theme";
import { loadAppProperties } from "@/service/app";
import { loadCurrentUser } from "@/service/user";

const toast = useToast();

export default defineComponent({
  name: "App",
  computed: {
    ...mapStores(useStore),
    ...mapState(useStore, ["isInitialized", "isAccessRestricted"]),
  },
  created(): void {
    loadAppProperties().catch(() =>
      toast.error("Error while loading application properties")
    );
    loadCurrentUser().catch(() =>
      toast.error("Error while loading current user")
    );
    loadActiveTheme().catch(() =>
      toast.error("Error while loading active theme")
    );
  },
  mounted(): void {
    Coloris.init();
  },
});
</script>

<style lang="scss">
@import "vue-select/src/scss/vue-select.scss";
@import "@melloware/coloris/dist/coloris.css";
@import "@/assets/styles/main.scss";
</style>
