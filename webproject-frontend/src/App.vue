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
  <div
    id="app"
    v-if="initialized"
    class="bg-theme-background min-h-screen text-theme-text"
  >
    <navbar />
    <router-view />
  </div>
</template>

<script>
import Navbar from "@/components/Navbar";

export default {
  name: "App",
  components: {
    Navbar
  },
  created() {
    this.$store.dispatch("loadCurrentUser");
    this.$store.dispatch("loadTheme");
  },
  computed: {
    initialized() {
      return this.$store.state.initialized;
    }
  }
};
</script>

<style lang="scss">
@import "vue-select/src/scss/vue-select.scss";
@import "@/assets/styles/main.scss";
</style>
