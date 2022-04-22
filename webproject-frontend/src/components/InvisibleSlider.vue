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
  <div>
    <input
      v-model="value"
      class="m-0 p-0 absolute w-full h-full cursor-pointer"
      type="range"
      :min="min"
      :max="max"
      step="0.0001"
      @change="onChange"
      @input="onInput"
    />
  </div>
</template>

<script lang="ts" setup>
import { computed, reactive, watch, WritableComputedRef } from "vue";

interface Props {
  modelValue: number | string;
  min: number | string;
  max: number | string;
}

const props: Readonly<Props> = defineProps<Props>();

// eslint-disable-next-line @typescript-eslint/typedef
const emit = defineEmits<{
  (e: "update:modelValue", value: number | string): void;
  (e: "change", value: number | string): void;
  (e: "dragstart"): void;
  (e: "dragend"): void;
  (e: "move"): void;
}>();

watch(props, (): void => {
  emit("move");
});

const value: WritableComputedRef<number | string> = computed({
  get() {
    return props.modelValue;
  },
  set(val: number | string) {
    emit("update:modelValue", val);
  },
});

interface BaseComponentData {
  dragging: boolean;
}

const data: BaseComponentData = reactive({
  dragging: false,
} as BaseComponentData);

function onChange(): void {
  emit("change", props.modelValue);
  emit("dragend");
  data.dragging = false;
}

function onInput(): void {
  if (!data.dragging) {
    emit("dragstart");
    data.dragging = true;
  }
}

defineExpose({
  value,
  onChange,
  onInput,
});
</script>

<style scoped>
input[type="range"] {
  -webkit-appearance: none;
  width: 100%;
  background: transparent;
}

input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;
}

input[type="range"]:focus {
  outline: none;
}

/* chrome */
input[type="range"]::-webkit-slider-thumb {
  height: 0.75em;
  width: 5px;
  opacity: 1;
  background-color: var(--theme-text);
  cursor: pointer;
  box-shadow: none;
  border: none;
  border-radius: 0;
}

/* firefox */
input[type="range"]::-moz-range-thumb {
  height: 0.75em;
  width: 5px;
  opacity: 1;
  background-color: var(--theme-text);
  cursor: pointer;
  box-shadow: none;
  border: none;
  border-radius: 0;
}
</style>
