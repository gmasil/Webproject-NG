///
/// Webproject NG
/// Copyright © 2021 - 2022 Gmasil
///
/// This file is part of Webproject NG.
///
/// Webproject NG is licensed under the Creative Commons
/// Attribution-NonCommercial-ShareAlike 4.0 International
/// Public License ("Public License").
///
/// Webproject NG is non-free software: you can redistribute
/// it and/or modify it under the terms of the Public License.
///
/// You should have received a copy of the Public License along
/// with Webproject NG. If not, see
/// https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt
///

import { createApp } from "vue";
import App from "@/App.vue";
import { store, key } from "@/store";
import router from "@/router";
import Toast from "vue-toastification";
import vSelect from "vue-select";
import Navbar from "@/components/Navbar.vue";

import "vue-toastification/dist/index.css";
import "tailwindcss/tailwind.css";
import "vue-select/dist/vue-select.css";
import "@melloware/coloris/dist/coloris.css";

const app = createApp(App).use(store, key).use(router);

// eslint-disable-next-line @typescript-eslint/no-unsafe-argument
app.component("VSelect", vSelect);
app.component("Navbar", Navbar);

app.use(Toast, {
  transition: "Vue-Toastification__bounce",
  maxToasts: 20,
  newestOnTop: true,
  position: "top-right",
  timeout: 7000,
  closeOnClick: true,
  pauseOnFocusLoss: true,
  pauseOnHover: false,
  draggable: true,
  draggablePercent: 0.6,
  showCloseButtonOnHover: false,
  hideProgressBar: false,
  closeButton: "button",
  icon: true,
  rtl: false,
});

app.mount("#app");
