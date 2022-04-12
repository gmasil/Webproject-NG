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
    <div v-if="data.video != null">
      <video
        ref="videoElement"
        class="md:w-6/12 w-full mx-auto"
        :poster="data.video.thumbnail"
        controls
      >
        <source :src="data.video.files[0].name" type="video/mp4" />
      </video>
      <h1 class="text-center text-2xl">
        {{ data.video.title }}
      </h1>
      <div>
        <table>
          <tr v-for="scene in data.video.scenes" :key="scene.id">
            <td class="pr-4">{{ scene.name }}</td>
            <td>
              <a @click="jumpVideoTo(scene.time)">
                {{ formatSceneTime(scene.time) }}
              </a>
            </td>
          </tr>
        </table>
      </div>
    </div>
    <div v-if="data.loadingError != null">
      <h1 class="text-center text-2xl">{{ data.loadingError }}</h1>
    </div>
    <div v-if="data.video == null && data.loadingError == null">
      <h1 class="text-center text-2xl">Loading...</h1>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, Ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { VideoFull } from "@/types";
import { useToast } from "vue-toastification";
import { videoService } from "@/service/video";
import { AxiosError } from "axios";

const toast = useToast();
const route = useRoute();
const videoElement: Ref<HTMLVideoElement | undefined> = ref();

declare interface BaseComponentData {
  id: string;
  video: VideoFull | null;
  loadingError: string | null;
}

const data = reactive({
  id: route.params.id as string,
  video: null,
  loadingError: null,
}) as BaseComponentData;

onMounted(() => {
  videoService
    .loadVideo(data.id)
    .then((video: VideoFull) => {
      data.video = video;
    })
    .catch((error: AxiosError) => {
      if (error.response?.status === 404) {
        toast.error("Video does not exist");
        data.loadingError = "Video not found";
      } else {
        toast.error("Error while loading video: " + error.message);
        data.loadingError = "Error while loading video";
      }
    });
});

defineExpose({
  formatSceneTime,
  jumpVideoTo,
  videoElement,
});

function jumpVideoTo(time: number) {
  if (videoElement.value) {
    videoElement.value.currentTime = time;
  }
}

function formatSceneTime(time: number): string {
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
