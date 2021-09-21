<!--

    Webproject NG
    Copyright © 2021 Gmasil

    This file is part of Webproject NG.

    Webproject NG is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Webproject NG is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Webproject NG. If not, see <https://www.gnu.org/licenses/>.

-->
<template>
  <div class="grid xl:grid-cols-5 lg:grid-cols-3 md:grid-cols-2 gap-5 p-5">
    <div
      class="h-16 w-full flex bg-theme-background-light rounded-lg"
      v-for="video in videos"
      :key="video.id"
    >
      <div
        class="w-28 h-16 bg-cover bg-center rounded-lg flex-initial"
        :style="'background-image: url(\'' + video.thumbnail + '\')'"
      ></div>
      <p class="self-center text-theme-text pl-4 px-0 mx-0 flex-1 test">
        {{ video.title }}
      </p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "VideoList",
  data() {
    return {
      videos: []
    };
  },
  created() {
    axios
      .get("/api/videos?size=12")
      .then(response => (this.videos = response.data._embedded.videos));
  }
};
</script>
