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
  <div v-if="isAllLoaded">
    <div class="grid xl:grid-cols-4 lg:grid-cols-3 md:grid-cols-2 gap-6">
      <div
        class="md:col-start-1 md:col-end-3 md:row-span-2 lg:self-stretch relative lg:flex lg:justify-center rounded-lg"
      >
        <div
          class="z-20 xl:w-11/12 self-center rounded-lg bg-theme-background-highlight"
        >
          <router-link class="w-full" :to="`/videos/${data.featuredVideo.id}`">
            <h1 class="text-2xl text-center">Watch Now!</h1>
            <img
              class="w-full"
              :src="data.featuredVideo.thumbnail"
              :alt="data.featuredVideo.title"
            />
            <div class="w-full py-2">
              <p class="text-center py-0 px-4 text-theme-text line-clamp-2">
                {{ data.featuredVideo.title }}
              </p>
            </div>
          </router-link>
        </div>
      </div>
      <div
        v-for="video in data.page.content"
        :key="video.id"
        class="w-full flex rounded-lg bg-theme-background-highlight"
      >
        <router-link class="w-full" :to="`/videos/${video.id}`">
          <img
            class="w-full rounded-t-lg"
            :src="video.thumbnail"
            :alt="video.title"
          />
          <div class="w-full py-2">
            <p class="text-center py-0 px-4 text-theme-text line-clamp-2">
              {{ video.title }}
            </p>
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ComputedRef, onMounted, reactive } from "vue";
import { Page, PageResponse, Video } from "@/types";
import { ToastInterface, useToast } from "vue-toastification";
import { videoService } from "@/service/video";
import { AxiosError } from "axios";

const toast: ToastInterface = useToast();

interface BaseComponentData {
  featuredVideo: Video | null;
  page: PageResponse<Video> | null;
}

const data: BaseComponentData = reactive({
  featuredVideo: null,
  page: null,
} as BaseComponentData);

const isAllLoaded: ComputedRef<boolean> = computed(() => {
  return data.featuredVideo != null && data.page != null;
});

onMounted(() => {
  videoService
    .loadFeaturedVideo()
    .then((video: Video) => {
      data.featuredVideo = video;
      loadVideoPage();
    })
    .catch((error: AxiosError) => {
      toast.error("Error while loading featured video: " + error.message);
    });
});

function loadVideoPage(): void {
  videoService
    .loadVideos({
      size: 20,
      page: 0,
      sort: "releaseDate,DESC",
    } as Page)
    .then((page: PageResponse<Video>): void => {
      data.page = page;
    })
    .catch((error: AxiosError): void => {
      toast.error("Error while loading videos: " + error.message);
    });
}

defineExpose({
  isAllLoaded,
});
</script>
