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
  <div v-if="video != null">
    <h1 class="text-theme-text">
      {{ video.title }}
    </h1>
    <video width="70%" :poster="video.thumbnail" controls>
      <source :src="video.files[0].name" type="video/mp4" />
    </video>
    <div>
      <table>
        <tr v-for="scene in video.scenes" :key="scene.id">
          <td class="pr-4">{{ scene.name }}</td>
          <td>{{ formatSceneTime(scene.time) }}</td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { mapActions } from "vuex";
import { VideoFull } from "@/types";

declare interface BaseComponentData {
  id: string;
  video: VideoFull | null;
}

const VideoDetails = Vue.extend({
  name: "VideoDetails",
  data(): BaseComponentData {
    return {
      id: this.$route.params.id,
      video: null,
    };
  },
  created(): void {
    this.loadVideo(this.id)
      .then((video: VideoFull) => {
        this.video = video;
      })
      .catch((error: Error) => {
        this.$toast.error("Error while loading video: " + error.message);
      });
  },
  methods: {
    ...mapActions(["loadVideo"]),
    formatSceneTime(time: number): string {
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
    },
  },
});

export default VideoDetails;
</script>
