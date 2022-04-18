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
    ref="colorInput"
    type="text"
    class="colorisify"
    :value="modelValue"
    data-coloris
    @input="$emit('update:modelValue', $event.target.value)"
  />
</template>

<script lang="ts" setup>
import { ref, watch, onMounted } from "vue";
import type { Ref } from "vue";
import Coloris from "@melloware/coloris";

const colorInput: Ref<HTMLElement | undefined> = ref();

interface Props {
  modelValue: string;
}

const props: Readonly<Props> = defineProps<Props>();

// eslint-disable-next-line @typescript-eslint/typedef
const emit = defineEmits<{
  (e: "update:modelValue", value: string): void;
}>();

watch(props, (newProps: Props) => {
  // manually set the correct color for little preview box
  if (colorInput.value && colorInput.value.parentElement) {
    colorInput.value.parentElement.style.color = newProps.modelValue;
  }
});

onMounted(() => {
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
});

defineExpose({
  emit,
});
</script>
