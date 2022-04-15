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
  <div v-if="data.video" ref="videoContainer" class="relative overflow-hidden">
    <video
      ref="videoElement"
      :poster="data.video.thumbnail"
      @click="onVideoClick"
      controls
    >
      <source :src="data.video.files[0].name" type="video/mp4" />
    </video>
    <div ref="videoControls">
      <div
        ref="videoScroller"
        class="w-100 h-4 bg-theme-background-highlight"
        @mouseover="data.scrolling = true"
        @mouseleave="data.scrolling = false"
        @mousemove="onScroll"
        @click="onScrollClick"
      ></div>
      <img
        ref="videoScrollPreview"
        :class="data.scrolling ? 'visible' : 'hidden'"
        class="absolute"
        :src="videoPreviewImage"
        alt="scrollpreview"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, computed, watch, onMounted } from "vue";
import type { Ref } from "vue";
import { VideoFull } from "@/types";

const videoContainer: Ref<HTMLElement | undefined> = ref();
const videoElement: Ref<HTMLVideoElement | undefined> = ref();
const videoScroller: Ref<HTMLElement | undefined> = ref();
const videoScrollPreview: Ref<HTMLImageElement | undefined> = ref();
const videoControls: Ref<HTMLElement | undefined> = ref();

interface Props {
  modelValue: VideoFull;
}

const props = defineProps<Props>();

declare interface BaseComponentData {
  video: VideoFull | null;
  scrolling: boolean;
}

const data: BaseComponentData = reactive({
  video: null,
  scrolling: false,
} as BaseComponentData);

watch(props, updateData);
onMounted(updateData);

function updateData() {
  data.video = props.modelValue;
}

const videoPreviewImage = computed(getVideoThumbnailImage);

defineExpose({
  props,
  formatTime,
  jumpVideoTo,
  videoElement,
  videoPreviewImage,
  onScroll,
  onScrollClick,
  onVideoClick,
});

function onVideoClick() {
  if (videoElement.value) {
    if (videoElement.value.paused) {
      void videoElement.value.play();
    } else {
      void videoElement.value.pause();
    }
  }
}

function onScroll(event: MouseEvent): void {
  if (videoScrollPreview.value && videoScroller.value && videoContainer.value) {
    const previewWidth = 192;
    const previewHeigth = 108;
    const previewFrames = 600;
    let rect: DOMRect = videoScroller.value.getBoundingClientRect();
    const x: number = event.clientX - rect.left;
    const img = Math.floor((x / rect.width) * previewFrames);
    videoScrollPreview.value.style.clip = `rect(${
      img * previewHeigth
    }px, ${previewWidth}px, ${img * previewHeigth + previewHeigth}px, 0)`;
    const top: number = Math.floor(
      videoContainer.value.offsetHeight -
        previewHeigth -
        videoScroller.value.offsetHeight -
        img * previewHeigth
    );
    let left: number = Math.floor(x - previewWidth / 2);
    if (left < 0) {
      left = 0;
    } else if (left > rect.width - previewWidth) {
      left = rect.width - previewWidth;
    }
    videoScrollPreview.value.style.top = `${top}px`;
    videoScrollPreview.value.style.left = `${left}px`;
  }
}

function onScrollClick(event: MouseEvent): void {
  if (
    videoScrollPreview.value &&
    videoScroller.value &&
    data.video &&
    data.video.length
  ) {
    const previewFrames = 600;
    let rect: DOMRect = videoScroller.value.getBoundingClientRect();
    const x: number = event.clientX - rect.left;
    const img = Math.floor((x / rect.width) * previewFrames);
    const time = (img / previewFrames) * data.video.length;
    jumpVideoTo(time);
  }
}

function getVideoThumbnailImage() {
  if (data.video && data.video.thumbnail) {
    let base: string = data.video.thumbnail.substring(
      0,
      data.video.thumbnail.lastIndexOf("/")
    );
    return `${base}/videopreview.jpg`;
  }
}

function jumpVideoTo(time: number) {
  if (videoElement.value) {
    videoElement.value.currentTime = time;
  }
}

function formatTime(time: number): string {
  let hrs = Math.floor(time / 3600);
  let mins = Math.floor((time % 3600) / 60);
  let secs = Math.floor(time % 60);
  let ret = "";
  if (hrs > 0) {
    ret += `${hrs}:`;
  }
  ret += `${mins < 10 ? "0" : ""}${mins}:`;
  ret += `${secs < 10 ? "0" : ""}${secs}`;
  return ret;
}
</script>
