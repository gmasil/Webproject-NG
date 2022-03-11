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

import Vue, { VNode } from "vue";
import App from "@/App.vue";
import router from "@/router";
import store from "@/store";
import Toast from "vue-toastification";
import vSelect from "vue-select";
import { ColorPicker, ColorPanel } from "one-colorpicker";
import "tailwindcss/tailwind.css";
import "vue-toastification/dist/index.css";

Vue.config.productionTip = false;

Vue.use(Toast, {
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

// eslint-disable-next-line @typescript-eslint/no-unsafe-argument
Vue.use(ColorPanel);
// eslint-disable-next-line @typescript-eslint/no-unsafe-argument
Vue.use(ColorPicker);
Vue.component("VSelect", vSelect);

new Vue({
  router,
  store,
  render: (h): VNode => h(App),
}).$mount("#app");
