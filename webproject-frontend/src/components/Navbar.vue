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
    <ul>
      <li><router-link to="/">Home</router-link></li>
      <li><router-link to="/videos">Videos</router-link></li>
      <li v-if="isAuthenticated()">
        <router-link to="/themes">Themes</router-link>
      </li>
      <li v-if="!isAuthenticated()">
        <router-link to="/login">Login</router-link>
      </li>
      <li v-if="isAuthenticated()">
        <router-link to="/changepassword">Change Password</router-link>
      </li>
      <li v-if="isAuthenticated()">
        <a href="/logout">Logout</a>
      </li>
    </ul>
    <hr class="border-theme-text my-1" />
    <p v-if="isAuthenticated()">
      Logged in as
      {{ getUsername }}
    </p>
    <hr v-if="isAuthenticated()" class="border-theme-text my-2" />
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { mapGetters } from "vuex";
import { User } from "@/types";

const Navbar = Vue.extend({
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

export default Navbar;
</script>
