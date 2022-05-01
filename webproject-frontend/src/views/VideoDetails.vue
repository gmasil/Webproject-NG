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
    <div v-if="!data.loading">
      <div v-if="data.loadingError">
        <h1 class="text-center text-2xl">{{ data.loadingError }}</h1>
      </div>
      <div v-else>
        <!--normal video page-->
        <div class="w-full md:w-10/12 lg:w-10/12 mx-auto pb-4">
          <webproject-video
            ref="videoElement"
            v-model="data.video"
          ></webproject-video>
          <h1 class="text-center text-2xl">
            {{ data.video.title }}
          </h1>
          <h1 class="text-center">
            {{ releaseDate }}
          </h1>
          <div
            v-if="data.video.artists && data.video.artists.length > 0"
            class="pt-4"
          >
            <p class="text-xl pb-2">Artists:</p>
            <div class="flex flex-wrap gap-2">
              <div
                v-for="artist in data.video.artists"
                :key="artist.id"
                class="w-max inline-block select-none"
              >
                <span
                  class="text-lg bg-theme-background-highlight rounded-lg px-2 py-1"
                >
                  {{ artist.name }}
                </span>
              </div>
            </div>
          </div>
          <div
            v-if="data.video.categories && data.video.categories.length > 0"
            class="pt-4"
          >
            <p class="text-xl pb-2">Categories:</p>
            <div class="flex flex-wrap gap-2">
              <div
                v-for="category in data.video.categories"
                :key="category"
                class="w-max inline-block select-none"
              >
                <span
                  class="text-lg bg-theme-background-highlight rounded-lg px-2 py-1"
                >
                  {{ category }}
                </span>
              </div>
            </div>
          </div>
          <div
            v-if="data.video.scenes && data.video.scenes.length > 0"
            class="pt-4"
          >
            <p class="text-xl pb-2">Scenes:</p>
            <div class="flex flex-wrap gap-2">
              <div
                v-for="scene in data.video.scenes"
                :key="scene.id"
                class="w-max inline-block select-none"
              >
                <div
                  class="text-lg bg-theme-background-highlight rounded-lg px-2 py-1 cursor-pointer"
                  @click="jumpVideoTo(scene.time)"
                >
                  <div class="">{{ scene.name }}</div>
                  <div class="text-center text-xs opacity-70 -mt-1">
                    {{ formatSceneTime(scene.time) }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-if="data.loading">
      <h1 class="text-center text-2xl">Loading...</h1>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, onMounted, ref, computed, ComputedRef } from "vue";
import type { Ref } from "vue";
import { RouteLocationNormalizedLoaded, useRoute } from "vue-router";
import { VideoFull } from "@/types";
import { useToast } from "vue-toastification";
import type { ToastInterface } from "vue-toastification";
import { videoService } from "@/service/video";
import { AxiosError } from "axios";
import type { IWebprojectVideo } from "@/components/WebprojectVideo.vue";
import dateFormat from "dateformat";

const toast: ToastInterface = useToast();
const route: RouteLocationNormalizedLoaded = useRoute();

const videoElement: Ref<IWebprojectVideo | undefined> = ref();

interface BaseComponentData {
  id: string;
  video: VideoFull | null;
  loading: boolean;
  loadingError: string | null;
}

const data: BaseComponentData = reactive({
  id: route.params.id as string,
  video: null,
  loading: true,
  loadingError: null,
});

const releaseDate: ComputedRef<string> = computed(() => {
  if (data.video && data.video.releaseDate) {
    return dateFormat(data.video.releaseDate, "dd.mm.yyyy hh:MM:ss");
  }
  return "";
});

onMounted(() => {
  videoService
    .loadVideo(data.id)
    .then((video: VideoFull) => {
      data.video = video;
      data.loading = false;
    })
    .catch((error: AxiosError) => {
      if (error.response?.status === 404) {
        toast.error("Video does not exist");
        data.loadingError = "Video not found";
      } else {
        toast.error("Error while loading video: " + error.message);
        data.loadingError = "Error while loading video";
      }
      data.loading = false;
    });
});

defineExpose({
  formatSceneTime,
  jumpVideoTo,
  releaseDate,
});

function jumpVideoTo(time: number): void {
  if (videoElement.value) {
    videoElement.value.jumpVideoTo(time);
    window.scrollTo(0, 0);
  }
}

function formatSceneTime(time: number): string {
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
