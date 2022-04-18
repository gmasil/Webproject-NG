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
import { defineComponent } from "vue";
import { userService } from "@/service/user";
import { ChangePasswordData } from "@/types";
import { ToastInterface, useToast } from "vue-toastification";
import { AxiosError } from "axios";

const toast: ToastInterface = useToast();

export default defineComponent({
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
        toast.error("Passwords mismatch");
        return;
      }
      if (this.newPassword.length < 8) {
        toast.error("Passwords must have at least 8 characters");
        return;
      }
      userService
        .changePassword({
          currentPassword: this.currentPassword,
          newPassword: this.newPassword,
        } as ChangePasswordData)
        .then(() => {
          toast.success("Password changed successsfully");
        })
        .catch((error: AxiosError) => {
          toast.error("Error changing password: " + error.message);
        });
    },
  },
});
</script>
