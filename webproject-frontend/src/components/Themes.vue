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
  <div class="grid grid-cols-fit gap-x-4 gap-y-1 justify-items-start">
    <span class="inline-block">Background color</span>
    <color-picker
      class="border border-theme-text"
      v-model="backgroundColor"
      @change="changeBackgroundColor"
    ></color-picker>
    <span>Text color</span>
    <color-picker
      class="border border-theme-text"
      v-model="textColor"
      @change="changeTextColor"
    ></color-picker>
    <i></i>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Themes",
  data() {
    return {
      backgroundColor: getComputedStyle(
        document.documentElement
      ).getPropertyValue("--theme-background"),
      textColor: getComputedStyle(document.documentElement).getPropertyValue(
        "--theme-text"
      )
    };
  },
  created() {},
  methods: {
    changeBackgroundColor() {
      document.documentElement.style.setProperty(
        "--theme-background",
        this.$data.backgroundColor
      );
      document.documentElement.style.setProperty(
        "--theme-background-light",
        this.shadeColor(this.$data.backgroundColor, 40)
      );
      document.documentElement.style.setProperty(
        "--theme-background-dark",
        this.shadeColor(this.$data.backgroundColor, -40)
      );
    },
    changeTextColor() {
      document.documentElement.style.setProperty(
        "--theme-text",
        this.$data.textColor
      );
    },
    shadeColor(color, percent) {
      var R = parseInt(color.substring(1, 3), 16);
      var G = parseInt(color.substring(3, 5), 16);
      var B = parseInt(color.substring(5, 7), 16);
      R = parseInt((R * (100 + percent)) / 100);
      G = parseInt((G * (100 + percent)) / 100);
      B = parseInt((B * (100 + percent)) / 100);
      R = R < 255 ? R : 255;
      G = G < 255 ? G : 255;
      B = B < 255 ? B : 255;
      var RR =
        R.toString(16).length == 1 ? "0" + R.toString(16) : R.toString(16);
      var GG =
        G.toString(16).length == 1 ? "0" + G.toString(16) : G.toString(16);
      var BB =
        B.toString(16).length == 1 ? "0" + B.toString(16) : B.toString(16);
      return "#" + RR + GG + BB;
    }
  }
};
</script>
