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
  <div id="app" class="bg-theme-background min-h-screen text-theme-text">
    <ul>
      <li><router-link to="/">Home</router-link></li>
      <li><router-link to="/videos">Videos</router-link></li>
      <li><router-link to="/themes">Themes</router-link></li>
      <li v-if="!this.$store.state.authenticated">
        <router-link to="/login">Login</router-link>
      </li>
      <li v-if="this.$store.state.authenticated">
        <a href="/logout">Logout</a>
      </li>
    </ul>
    <p v-if="this.$store.state.currentUser">
      Logged in as
      {{ this.$store.state.currentUser.username }}
    </p>
    <router-view v-if="this.$store.state.initialized" />
  </div>
</template>

<script>
import store from "./vuex";

export default {
  name: "App",
  created() {
    this.$store.dispatch("loadTheme");
    this.$store.dispatch("loadCurrentUser");
  }
};
</script>

<style lang="scss">
@import "@/assets/styles/main.scss";
</style>
