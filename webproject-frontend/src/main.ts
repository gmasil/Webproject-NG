///
/// Webproject NG
/// Copyright © 2021 Gmasil
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

import Vue from "vue";
import App from "@/App.vue";
import router from "@/router";
import store from "@/store";
import vSelect from "vue-select";
import { ColorPicker, ColorPanel } from "one-colorpicker";
import "tailwindcss/tailwind.css";

Vue.config.productionTip = false;

Vue.use(ColorPanel);
Vue.use(ColorPicker);
Vue.component("v-select", vSelect);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
