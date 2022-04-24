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
  <div v-if="page != null">
    <div class="text-center select-none">
      <a
        class="bg-theme-background-highlight px-4 py-2 rounded-lg cursor-pointer"
        @click="openPreviousPage()"
        >Prev</a
      >
      <span class="mx-4">Page {{ page.number + 1 }}</span>
      <a
        class="bg-theme-background-highlight px-4 py-2 rounded-lg cursor-pointer"
        @click="openNextPage()"
        >Next</a
      >
    </div>
    <div
      class="grid xl:grid-cols-5 lg:grid-cols-3 md:grid-cols-2 grid-cols-1 gap-5 p-5"
    >
      <div
        v-for="video in page.content"
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
    <div class="text-center select-none">
      <a
        class="bg-theme-background-highlight px-4 py-2 rounded-lg cursor-pointer"
        @click="openPreviousPage()"
        >Prev</a
      >
      <span class="mx-4">Page {{ page.number + 1 }}</span>
      <a
        class="bg-theme-background-highlight px-4 py-2 rounded-lg cursor-pointer"
        @click="openNextPage()"
        >Next</a
      >
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { Video, Page, PageResponse } from "@/types";
import { ToastInterface, useToast } from "vue-toastification";
import { videoService } from "@/service/video";
import { AxiosError } from "axios";

const toast: ToastInterface = useToast();

interface BaseComponentData {
  page: PageResponse<Video> | null;
}

export default defineComponent({
  name: "VideoList",
  data(): BaseComponentData {
    return {
      page: null,
    };
  },
  created(): void {
    this.openPage(0);
  },
  methods: {
    openPage(pageNumber: number): void {
      videoService
        .loadVideos({
          size: 10,
          page: pageNumber,
          sort: "releaseDate,DESC",
        } as Page)
        .then((page: PageResponse<Video>): void => {
          this.page = page;
        })
        .catch((error: AxiosError): void => {
          toast.error("Error while loading videos: " + error.message);
        });
    },
    openPreviousPage(): void {
      if (this.page != null && !this.page.first) {
        this.openPage(this.page.number - 1);
      }
    },
    openNextPage(): void {
      if (this.page != null && !this.page.last) {
        this.openPage(this.page.number + 1);
      }
    },
  },
});
</script>
