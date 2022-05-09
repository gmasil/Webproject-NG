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
  <div class="grid justify-items-center -m-4">
    <div class="text-left pt-20 text-lg w-full sm:w-sm p-4">
      <h1 class="text-2xl mb-4">Change Password</h1>
      <div class="grid grid-cols-2 gap-4">
        <div class="col-span-2">
          <label class="pl-2 block" for="inputCurrentPassword"
            >Current Password:</label
          >
          <input
            id="input-current-password"
            v-model="data.currentPassword"
            class="w-full p-2 rounded-lg"
            type="password"
            @keyup.enter="onChangeClick"
          />
        </div>
        <div class="col-span-2">
          <label class="pl-2 block" for="inputNewPassword">New Password:</label>
          <input
            id="input-new-password"
            v-model="data.newPassword"
            class="w-full p-2 rounded-lg"
            type="password"
            @keyup.enter="onChangeClick"
          />
        </div>
        <div class="col-span-2">
          <label class="pl-2 block" for="inputRepeatPassword"
            >Repeat Password:</label
          >
          <input
            id="input-repeat-password"
            v-model="data.repeatPassword"
            class="w-full p-2 rounded-lg"
            type="password"
            @keyup.enter="onChangeClick"
          />
        </div>
        <div class="col-span-2 justify-self-end">
          <button
            id="submit-change-password"
            class="px-4 py-1 rounded-lg cursor-pointer bg-theme-background-highlight"
            @click="onChangeClick"
          >
            Change Password
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import { userService } from "@/service/user";
import { ChangePasswordData } from "@/types";
import { ToastInterface, useToast } from "vue-toastification";
import { AxiosError } from "axios";

const toast: ToastInterface = useToast();

interface BaseComponentData {
  currentPassword: string;
  newPassword: string;
  repeatPassword: string;
}

const data: BaseComponentData = reactive({
  currentPassword: "",
  newPassword: "",
  repeatPassword: "",
});

function onChangeClick(): void {
  if (data.newPassword != data.repeatPassword) {
    toast.error("Passwords mismatch");
    return;
  }
  if (data.newPassword.length < 8) {
    toast.error("Passwords must have at least 8 characters");
    return;
  }
  userService
    .changePassword({
      currentPassword: data.currentPassword,
      newPassword: data.newPassword,
    } as ChangePasswordData)
    .then(() => {
      toast.success("Password changed successsfully");
      data.currentPassword = "";
      data.newPassword = "";
      data.repeatPassword = "";
    })
    .catch((error: AxiosError) => {
      toast.error("Error changing password: " + error.message);
    });
}

defineExpose({
  onChangeClick,
});
</script>
