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
    <h1 v-if="isAccessRestricted()">Restricted Access!</h1>
    <h1>Login</h1>
    <form method="post" action="/performlogin">
      <input id="login-input-username" type="text" name="username" />
      <input id="login-input-password" type="password" name="password" />
      <input id="login-input-remember" type="checkbox" name="rememberme" />
      <label for="login-input-remember">Stay logged in</label>
      <input id="login-input-submit" type="submit" value="Login" />
    </form>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { mapGetters } from "vuex";

const Login = Vue.extend({
  name: "Login",
  methods: {
    ...mapGetters(["isAccessRestricted"]),
  },
});

export default Login;
</script>
