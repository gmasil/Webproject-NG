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
    <label for="inputCurrentPassword">Current Password:</label>
    <input
      id="inputCurrentPassword"
      v-model="currentPassword"
      class="block"
      type="password"
    />
    <label for="inputNewPassword">Current Password:</label>
    <input
      id="inputNewPassword"
      v-model="newPassword"
      class="block"
      type="password"
    />
    <label for="inputRepeatPassword">Current Password:</label>
    <input
      id="inputRepeatPassword"
      v-model="repeatPassword"
      class="block"
      type="password"
    />
    <button
      class="border-2 border-theme-text bg-theme-background px-2 py-1"
      @click="onChangeClick"
    >
      Change Password
    </button>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { mapActions } from "vuex";
import { ChangePasswordData } from "@/types";

const ChangePassword = Vue.extend({
  name: "ChangePassword",
  data() {
    return {
      currentPassword: "",
      newPassword: "",
      repeatPassword: "",
    };
  },
  methods: {
    ...mapActions(["changePassword"]),
    onChangeClick() {
      if (this.newPassword != this.repeatPassword) {
        this.$toast.error("Passwords mismatch");
        return;
      }
      if (this.newPassword.length < 8) {
        this.$toast.error("Passwords must have at least 8 characters");
        return;
      }
      this.changePassword({
        currentPassword: this.currentPassword,
        newPassword: this.newPassword,
      } as ChangePasswordData)
        .then(() => {
          this.$toast.success("Password changed successsfully");
        })
        .catch((error: Error) => {
          this.$toast.error("Error changing password: " + error.message);
        });
    },
  },
});

export default ChangePassword;
</script>
