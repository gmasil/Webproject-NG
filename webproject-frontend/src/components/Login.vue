<!--

    Webproject NG
    Copyright © 2021 Gmasil

    This file is part of Webproject NG.

    Webproject NG is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Webproject NG is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Webproject NG. If not, see <https://www.gnu.org/licenses/>.

-->
<template>
  <div>
    <h1 v-if="isAccessRestricted()">Restricted Access!</h1>
    <h1>Login</h1>
    <form method="post" action="/performlogin">
      <input type="text" name="username" />
      <input type="password" name="password" />
      <input id="login-remember" type="checkbox" name="rememberme" />
      <label for="login-remember">Stay logged in</label>
      <input type="submit" value="Login" />
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
