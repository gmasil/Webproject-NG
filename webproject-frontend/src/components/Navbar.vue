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
      <router-link v-if="isAuthenticated()" class="inline-block" to="/themes"
        >Themes</router-link
      >
      |
      <router-link v-if="!isAuthenticated()" to="/login">Login</router-link>
      <span v-if="!isAuthenticated()"> |</span>
      <router-link v-if="isAuthenticated()" to="/changepassword"
        >Account: {{ getUsername }}</router-link
      >
      |
      <a v-if="isAuthenticated()" href="/logout">Logout</a>
    </div>
    <hr v-if="isAuthenticated()" class="border-theme-text my-2" />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapGetters } from "vuex";
import { User } from "@/types";

export default defineComponent({
  name: "Navbar",
  computed: {
    getUsername: function (): string {
      return (this.getCurrentUser() as User).username;
    },
  },
  methods: {
    ...mapGetters(["isAuthenticated", "getCurrentUser"]),
  },
});
</script>
