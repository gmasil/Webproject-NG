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
  <!-- overflow-hidden -->
  <div
    v-if="data.video"
    id="videoContainer"
    ref="videoContainer"
    class="relative"
  >
    <video
      ref="videoElement"
      class="-z-20"
      :poster="data.video.thumbnail"
      @click="togglePlay"
      @dblclick="toggleFullscreen"
      @canplay="updatePaused"
      @playing="updatePaused"
      @pause="updatePaused"
      @timeupdate="updateTime"
    >
      <source :src="data.video.files[0].name" type="video/mp4" />
    </video>
    <div ref="videoControls" class="absolute bottom-0 left-0 right-0">
      <div
        class="absolute top-0 bottom-0 left-0 right-0 bg-theme-background-highlight opacity-80"
      ></div>
      <div
        ref="videoScroller"
        class="relative w-full h-2 z-30 bg-theme-background-highlight"
        @mouseover="data.scrolling = true"
        @mouseleave="data.scrolling = false"
        @mousemove="onScroll"
        @click="onScrollClick"
      >
        <img
          v-if="data.scrolling"
          ref="videoScrollPreview"
          class="absolute"
          :src="videoPreviewImage"
          alt="scrollpreview"
        />
        <div
          ref="videoTime"
          class="absolute left-0 h-2 z-30 bg-theme-text border-y border-theme-background-highlight"
        ></div>
      </div>
      <vue-feather
        v-if="data.paused"
        ref="playButton"
        type="play"
        class="cursor-pointer relative"
        @click="togglePlay"
      ></vue-feather>
      <vue-feather
        v-if="!data.paused"
        ref="pauseButton"
        type="pause"
        class="cursor-pointer relative"
        @click="togglePlay"
      ></vue-feather>
      <vue-feather
        v-if="!data.fullscreen"
        type="maximize"
        class="cursor-pointer absolute right-0"
        @click="toggleFullscreen"
      ></vue-feather>
      <vue-feather
        v-if="data.fullscreen"
        type="minimize"
        class="cursor-pointer absolute right-0"
        @click="toggleFullscreen"
      ></vue-feather>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, computed, watch, onMounted } from "vue";
import type { Ref } from "vue";
import { VideoFull, DOMEvent } from "@/types";
import { useToast } from "vue-toastification";

const toast = useToast();

const videoContainer: Ref<HTMLElement | undefined> = ref();
const videoElement: Ref<HTMLVideoElement | undefined> = ref();
const videoScroller: Ref<HTMLElement | undefined> = ref();
const videoScrollPreview: Ref<HTMLImageElement | undefined> = ref();
const videoControls: Ref<HTMLElement | undefined> = ref();
const videoTime: Ref<HTMLElement | undefined> = ref();

interface Props {
  modelValue: VideoFull;
}

const props = defineProps<Props>();

declare interface BaseComponentData {
  video: VideoFull | null;
  paused: boolean;
  fullscreen: boolean;
  scrolling: boolean;
}

const data: BaseComponentData = reactive({
  video: null,
  paused: true,
  fullscreen: false,
  scrolling: false,
} as BaseComponentData);

watch(props, updateData);
onMounted(() => {
  updateData();
  document.addEventListener("fullscreenchange", updateFullscreenEvent);
});

function updateData() {
  data.video = props.modelValue;
}

function updatePaused(event: DOMEvent<HTMLVideoElement>) {
  if (event.target) {
    data.paused = event.target.paused;
  }
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
  togglePlay,
  toggleFullscreen,
  updatePaused,
  updateTime,
});

function updateTime() {
  if (
    videoTime.value &&
    videoElement.value &&
    data.video &&
    data.video.length &&
    videoScroller.value
  ) {
    const width: number =
      (videoElement.value.currentTime * 100) / data.video.length;
    videoTime.value.style.width = `${width}%`;
  }
}

function togglePlay() {
  if (videoElement.value) {
    if (videoElement.value.paused) {
      videoElement.value
        .play()
        .then(() => {
          data.paused = false;
        })
        .catch((error: Error) => {
          toast.error(error.message);
        });
    } else {
      videoElement.value.pause();
      data.paused = true;
    }
  }
}

function toggleFullscreen() {
  if (videoContainer.value) {
    if (!document.fullscreenElement) {
      // request fullscreen
      videoContainer.value
        .requestFullscreen()
        .catch((error: Error) => {
          toast.error(error.message);
        });
    } else {
      // exit fullscreen
      document
        .exitFullscreen()
        .catch((error: Error) => {
          toast.error(error.message);
        });
    }
  }
}

function updateFullscreenEvent(event: Event) {
  if (
    videoElement.value &&
    event &&
    event.target &&
    event.target == videoElement.value &&
    document.fullscreenElement
  ) {
    // native fullscreen called:
    // exit fullscreen and open correct fullscreen mode
    document
      .exitFullscreen()
      .then(() => {
        toggleFullscreen();
      })
      .catch((error: Error) => {
        toast.error(error.message);
      });
  } else {
    updateFullscreen();
  }
}

function updateFullscreen() {
  if (document.fullscreenElement) {
    // fullscreen
    if (videoElement.value && videoContainer.value) {
      videoContainer.value.style.height = `calc(100vw * ${videoElement.value.videoHeight} / ${videoElement.value.videoWidth})`;
      videoContainer.value.className = `${videoContainer.value.className} fullscreen`;
      data.fullscreen = true;
    }
  } else {
    // no fullscreen
    if (videoElement.value && videoContainer.value) {
      videoContainer.value.style.height = "auto";
      videoContainer.value.className = videoContainer.value.className
        .replaceAll("fullscreen", "")
        .trim();
      data.fullscreen = false;
    }
  }
}

function onScroll(event: MouseEvent): void {
  if (videoScrollPreview.value && videoScroller.value && videoContainer.value) {
    const previewWidth = 192;
    const previewHeigth = 108;
    const previewFrames = 600;
    const rect: DOMRect = videoScroller.value.getBoundingClientRect();
    const x: number = event.clientX - rect.left;
    const img = Math.floor((x / rect.width) * previewFrames);
    videoScrollPreview.value.style.clip = `rect(${
      img * previewHeigth
    }px, ${previewWidth}px, ${img * previewHeigth + previewHeigth}px, 0)`;
    const top: number = Math.floor(0 - previewHeigth - img * previewHeigth);
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
    const rect: DOMRect = videoScroller.value.getBoundingClientRect();
    const x: number = event.clientX - rect.left;
    const img = Math.floor((x / rect.width) * previewFrames);
    const time = (img / previewFrames) * data.video.length;
    jumpVideoTo(time);
  }
}

function getVideoThumbnailImage() {
  if (data.video && data.video.thumbnail) {
    const base: string = data.video.thumbnail.substring(
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

<style scoped>
.vue-feather {
  margin: 0 0.5em;
  padding: 0.5em 0 0 0;
}

#videoContainer {
  width: 100%;
  height: auto;
}

#videoContainer.fullscreen {
  width: 100%;
  height: calc(100vw * 9 / 16);
}

#videoContainer.fullscreen video {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
}
</style>
