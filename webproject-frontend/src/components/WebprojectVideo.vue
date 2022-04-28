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
    class="relative overflow-hidden select-none"
    @mousemove="onMouseMove"
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
      @volumechange="updateVolume"
    >
      <source :src="data.video.files[0].name" type="video/mp4" />
    </video>
    <div
      :data-show="
        data.paused ||
        data.mouseOnControls ||
        data.mouseOnVideo ||
        data.videoSliderDragging
      "
      class="videoControls absolute bottom-0 left-0 right-0"
      @mouseover="data.mouseOnControls = true"
      @mouseleave="data.mouseOnControls = false"
    >
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
          v-show="
            data.video?.seekPreviewFile &&
            (data.scrolling || data.videoSliderDragging)
          "
          ref="videoScrollPreview"
          class="absolute"
          :src="videoPreviewImage"
          alt="scrollpreview"
        />
        <invisible-slider
          v-model="data.videoSliderTime"
          class="absolute w-full h-2 z-40"
          min="0"
          :max="data.video.duration"
          @change="onSliderChange"
          @dragstart="data.videoSliderDragging = true"
          @dragend="data.videoSliderDragging = false"
          @move="onVideoSliderMove"
        ></invisible-slider>
        <div
          ref="videoTime"
          class="absolute left-0 h-2 z-30 bg-theme-text border-y border-theme-background-highlight"
        ></div>
        <div
          ref="videoBuffer"
          class="absolute left-0 h-2 z-20 bg-theme-text border-y border-theme-background-highlight opacity-50"
        ></div>
      </div>
      <vue-feather
        v-if="data.paused"
        type="play"
        class="cursor-pointer relative"
        @click="togglePlay"
      ></vue-feather>
      <vue-feather
        v-if="!data.paused"
        type="pause"
        class="cursor-pointer relative"
        @click="togglePlay"
      ></vue-feather>
      <span class="videoTime relative"
        >{{ data.currentTime }} / {{ data.videoDuration }}</span
      >

      <vue-feather
        :type="`${data.muted ? 'volume-x' : 'volume-2'}`"
        class="cursor-pointer relative"
        @click="toggleMute"
      ></vue-feather>

      <vue-feather
        :type="`${data.fullscreen ? 'minimize' : 'maximize'}`"
        class="cursor-pointer absolute right-0"
        @click="toggleFullscreen"
      ></vue-feather>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, watch, onMounted, ComputedRef } from "vue";
import type { Ref } from "vue";
import { VideoFull } from "@/types";
import { ToastInterface, useToast } from "vue-toastification";

const toast: ToastInterface = useToast();

const videoContainer: Ref<HTMLElement | undefined> = ref();
const videoElement: Ref<HTMLVideoElement | undefined> = ref();
const videoScroller: Ref<HTMLElement | undefined> = ref();
const videoScrollPreview: Ref<HTMLImageElement | undefined> = ref();
const videoTime: Ref<HTMLElement | undefined> = ref();
const videoBuffer: Ref<HTMLElement | undefined> = ref();

interface Props {
  modelValue: VideoFull;
}

const props: Readonly<Props> = defineProps<Props>();

function onSliderChange(value: number): void {
  refreshControlsHideTimer();
  if (videoElement.value) {
    videoElement.value.currentTime = value;
  }
}

interface BaseComponentData {
  video: VideoFull | null;
  paused: boolean;
  fullscreen: boolean;
  scrolling: boolean;
  mouseOnVideo: boolean;
  mouseOnControls: boolean;
  hideControlsTimer: number;
  currentTime: string;
  videoDuration: string;
  videoSliderDragging: boolean;
  videoSliderTime: number;
  muted: boolean;
}

const data: BaseComponentData = reactive({
  video: null,
  paused: true,
  fullscreen: false,
  scrolling: false,
  mouseOnVideo: false,
  mouseOnControls: false,
  hideControlsTimer: NaN,
  currentTime: formatTime(0),
  videoDuration: formatTime(0),
  videoSliderDragging: false,
  videoSliderTime: 0,
  muted: false,
} as BaseComponentData);

watch(props, updateData);
onMounted(() => {
  updateData();
  document.addEventListener("fullscreenchange", updateFullscreenEvent);
});

function updateData(): void {
  data.video = props.modelValue;
  if (data.video.duration) {
    data.videoDuration = formatTime(data.video.duration);
  }
}

const videoPreviewImage: ComputedRef<string | undefined> = computed(
  getVideoThumbnailImage
);

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
  onMouseMove,
  onSliderChange,
  onVideoSliderMove,
  updateVolume,
  toggleMute,
});

function updateVolume(): void {
  if (videoElement.value) {
    data.muted = videoElement.value.muted;
  }
}

function toggleMute(): void {
  if (videoElement.value) {
    videoElement.value.muted = !videoElement.value.muted;
    updateVolume();
  }
}

function updatePaused(): void {
  if (videoElement.value) {
    data.paused = videoElement.value.paused;
  }
}

function onMouseMove(): void {
  refreshControlsHideTimer();
}

function refreshControlsHideTimer(): void {
  data.mouseOnVideo = true;
  if (!isNaN(data.hideControlsTimer)) {
    clearTimeout(data.hideControlsTimer);
  }
  data.hideControlsTimer = setTimeout(() => {
    data.mouseOnVideo = false;
  }, 2000);
}

function updateTime(): void {
  if (videoElement.value) {
    const currentTime: number = videoElement.value.currentTime;
    data.currentTime = formatTime(currentTime);
    if (!data.videoSliderDragging) {
      data.videoSliderTime = currentTime;
    }
    if (
      videoTime.value &&
      videoBuffer.value &&
      data.video &&
      data.video.duration &&
      videoScroller.value
    ) {
      const width: number = (currentTime * 100) / data.video.duration;
      videoTime.value.style.width = `${width}%`;
      for (let i: number = 0; i < videoElement.value.buffered.length; i++) {
        const start: number = videoElement.value.buffered.start(i);
        const end: number = videoElement.value.buffered.end(i);
        if (start < currentTime && currentTime < end) {
          const bufferWidth: number = (end * 100) / data.video.duration;
          videoBuffer.value.style.width = `${bufferWidth}%`;
        }
      }
    }
  }
}

function togglePlay(): void {
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
    data.mouseOnControls = false;
  }
}

function toggleFullscreen(): void {
  if (videoContainer.value) {
    if (!document.fullscreenElement) {
      // request fullscreen
      videoContainer.value.requestFullscreen().catch((error: Error) => {
        toast.error(error.message);
      });
    } else {
      // exit fullscreen
      document.exitFullscreen().catch((error: Error) => {
        toast.error(error.message);
      });
    }
    data.mouseOnControls = false;
  }
}

function updateFullscreenEvent(event: Event): void {
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

function updateFullscreen(): void {
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

function onVideoSliderMove(): void {
  if (data.videoSliderDragging && data.video && data.video.duration) {
    const percentage: number = data.videoSliderTime / data.video.duration;
    showPreview(percentage);
  }
}

function onScroll(event: MouseEvent): void {
  if (videoScroller.value) {
    const rect: DOMRect = videoScroller.value.getBoundingClientRect();
    const pos: number = event.clientX - rect.left;
    const percentage: number = pos / rect.width;
    showPreview(percentage);
  }
}

function showPreview(percentage: number): void {
  if (
    videoScrollPreview.value &&
    videoScroller.value &&
    data.video &&
    data.video.seekPreviewFile
  ) {
    const previewWidth: number = data.video.seekPreviewFile.width;
    const previewHeigth: number = data.video.seekPreviewFile.height;
    const previewFrames: number = data.video.seekPreviewFile.frames;
    const rect: DOMRect = videoScroller.value.getBoundingClientRect();
    const img: number = Math.floor(percentage * previewFrames);
    videoScrollPreview.value.style.clip = `rect(${
      img * previewHeigth
    }px, ${previewWidth}px, ${img * previewHeigth + previewHeigth}px, 0)`;
    const top: number = Math.floor(0 - previewHeigth - img * previewHeigth);
    let left: number = Math.floor(percentage * rect.width - previewWidth / 2);
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
  if (videoScroller.value && data.video && data.video.duration) {
    const previewFrames: number = 600;
    const rect: DOMRect = videoScroller.value.getBoundingClientRect();
    const x: number = event.clientX - rect.left;
    const img: number = Math.floor((x / rect.width) * previewFrames);
    const time: number = (img / previewFrames) * data.video.duration;
    jumpVideoTo(time);
  }
}

function getVideoThumbnailImage(): string {
  if (data.video && data.video.thumbnail) {
    const base: string = data.video.thumbnail.substring(
      0,
      data.video.thumbnail.lastIndexOf("/")
    );
    return `${base}/videopreview.jpg`;
  }
  return "";
}

function jumpVideoTo(time: number): void {
  if (videoElement.value) {
    videoElement.value.currentTime = time;
  }
}

function formatTime(time: number): string {
  const hrs: number = Math.floor(time / 3600);
  const mins: number = Math.floor((time % 3600) / 60);
  const secs: number = Math.floor(time % 60);
  let ret: string = "";
  if (hrs > 0) {
    ret += `${hrs}:`;
  }
  ret += `${mins < 10 ? "0" : ""}${mins}:`;
  ret += `${secs < 10 ? "0" : ""}${secs}`;
  return ret;
}
</script>

<script lang="ts">
export interface IWebprojectVideo {
  jumpVideoTo(time: number): void;
}
export default {};
</script>

<style scoped>
.vue-feather {
  margin: 0 0.5em;
  padding: 0.5em 0 0 0;
}

.videoTime {
  top: -0.4em;
}

.videoControls {
  opacity: 1;
  transition: opacity 0.1s linear;
}

.videoControls[data-show="false"] {
  opacity: 0;
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
