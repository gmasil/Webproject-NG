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
  mounted(): void {
    Coloris({
      el: ".colorisify",
    });
  },
});
</script>
