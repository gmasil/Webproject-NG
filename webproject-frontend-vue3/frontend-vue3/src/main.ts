import { createApp } from "vue";
import App from "./App.vue";
import { store, key } from "./store";
import router from "./router";
import Toast from "vue-toastification";
import vSelect from "vue-select";
import "vue-toastification/dist/index.css";
import "tailwindcss/tailwind.css";
import "vue-select/dist/vue-select.css";
import Navbar from "@/components/Navbar.vue";

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
