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
      class="block"
      type="password"
      v-model="currentPassword"
      id="inputCurrentPassword"
    />
    <label for="inputNewPassword">Current Password:</label>
    <input
      class="block"
      type="password"
      v-model="newPassword"
      id="inputNewPassword"
    />
    <label for="inputRepeatPassword">Current Password:</label>
    <input
      class="block"
      type="password"
      v-model="repeatPassword"
      id="inputRepeatPassword"
    />
    <button
      class="border-2 border-theme-text bg-theme-background px-2 py-1"
      @click="onChangeClick"
    >
      Change Password
    </button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ChangePassword",
  data() {
    return {
      currentPassword: "",
      newPassword: "",
      repeatPassword: "",
    };
  },
  methods: {
    onChangeClick() {
      if (this.newPassword != this.repeatPassword) {
        alert("Passwords mismatch");
        return;
      }
      if (this.newPassword.length < 8) {
        alert("Passwords must have at least 8 characters");
        return;
      }
      this.updatePassword();
    },
    updatePassword() {
      const config = { headers: { "Content-Type": "application/json" } };
      axios
        .put(
          "/api/users/updatepassword",
          {
            currentPassword: this.currentPassword,
            newPassword: this.newPassword,
          },
          config
        )
        .then((response) => {
          console.log("Password changed");
        })
        .catch((error) => {
          console.log("Error changing password: " + error);
        });
    },
  },
};
</script>
