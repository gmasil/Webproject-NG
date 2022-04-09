/* eslint-disable */
import { ComponentCustomProperties } from "vue";
import { Store, State } from "@/store";

declare module "@vue/runtime-core" {
  interface ComponentCustomProperties {
    $store: Store<State>;
  }
}
