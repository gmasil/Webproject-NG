///
/// Webproject NG
/// Copyright © 2022 Gmasil
///
/// This file is part of Webproject NG.
///
/// Webproject NG is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// Webproject NG is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with Webproject NG. If not, see <https://www.gnu.org/licenses/>.
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

Vue.use(ColorPanel);
Vue.use(ColorPicker);
Vue.component("VSelect", vSelect);

new Vue({
  router,
  store,
  render: (h): VNode => h(App),
}).$mount("#app");
