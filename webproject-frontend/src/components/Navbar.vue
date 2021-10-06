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
      <li v-if="this.isAuthenticated()">
        <router-link to="/themes">Themes</router-link>
      </li>
      <li v-if="!this.isAuthenticated()">
        <router-link to="/login">Login</router-link>
      </li>
      <li v-if="this.isAuthenticated()">
        <router-link to="/changepassword">Change Password</router-link>
      </li>
      <li v-if="this.isAuthenticated()">
        <a href="/logout">Logout</a>
      </li>
    </ul>
    <hr class="border-theme-text my-1" />
    <p v-if="this.isAuthenticated()">
      Logged in as
      {{ this.getUsername }}
    </p>
    <hr v-if="this.isAuthenticated()" class="border-theme-text my-2" />
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { mapGetters } from "vuex";

const Navbar = Vue.extend({
  name: "Navbar",
  methods: {
    ...mapGetters(["isAuthenticated", "getCurrentUser"]),
  },
  computed: {
    getUsername: function (): string {
      return this.getCurrentUser().username;
    },
  },
});

export default Navbar;
</script>
