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
    <ul>
      <li><router-link to="/">Home</router-link></li>
      <li><router-link to="/videos">Videos</router-link></li>
      <li v-if="authenticated">
        <router-link to="/themes">Themes</router-link>
      </li>
      <li v-if="!authenticated">
        <router-link to="/login">Login</router-link>
      </li>
      <li v-if="authenticated">
        <router-link to="/changepassword">Change Password</router-link>
      </li>
      <li v-if="authenticated">
        <a href="/logout">Logout</a>
      </li>
    </ul>
    <hr class="border-theme-text my-1" />
    <p v-if="authenticated">
      Logged in as
      {{ currentUser.username }}
    </p>
    <hr v-if="authenticated" class="border-theme-text my-2" />
  </div>
</template>

<script>
export default {
  name: "Navbar",
  computed: {
    authenticated() {
      return this.$store.state.currentUser != null;
    },
    currentUser() {
      return this.$store.state.currentUser;
    },
  },
};
</script>
