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
  <div>
    <div class="text-center">
      <router-link to="/">Home</router-link>
      |
      <router-link to="/videos">Videos</router-link>
      |
      <router-link
        v-if="store.isAuthenticated"
        class="inline-block"
        to="/themes"
        >Themes</router-link
      >
      <span v-if="store.isAuthenticated"> | </span>
      <router-link v-if="!store.isAuthenticated" to="/login">Login</router-link>
      <span v-if="!store.isAuthenticated && store.isAccessRestricted"> |</span>
      <router-link
        v-if="store.isAuthenticated"
        id="navbar-username"
        to="/changepassword"
        >Account: {{ store.getUsername }}</router-link
      >
      <span v-if="store.isAuthenticated"> | </span>
      <a v-if="store.isAuthenticated" href="/logout">Logout</a>
    </div>
    <hr class="border-theme-text my-2" />
  </div>
</template>

<script setup lang="ts">
import { useStore } from "@/store/pinia";
import type { Store } from "@/store/pinia";

const store: Store = useStore();

defineExpose({
  store,
});
</script>
