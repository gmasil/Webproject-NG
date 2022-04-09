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
  <input
    type="text"
    class="colorisify"
    :value="modelValue"
    data-coloris
    @input="$emit('update:modelValue', $event.target.value)"
  />
</template>

<script lang="ts">
import { defineComponent } from "vue";
import Coloris from "@melloware/coloris";

export default defineComponent({
  name: "Themes",
  props: {
    modelValue: {
      type: String,
      default: "",
      required: true,
    },
  },
  emits: ["update:modelValue"],
  watch: {
    modelValue() {
      // manually set the correct color for little preview box
      // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
      this.$el.parentElement.style.color = this.modelValue;
    },
  },
  mounted(): void {
    Coloris({
      el: ".colorisify",
      alpha: false,
      swatches: [
        "#264653",
        "#2a9d8f",
        "#e9c46a",
        "rgb(244,162,97)",
        "#e76f51",
        "#d62828",
        "navy",
        "#07b",
        "#0096c7",
        "#00b4d8",
        "rgba(0,119,182)",
      ],
    });
  },
});
</script>
