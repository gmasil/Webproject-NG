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
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Themes",
  computed: {
    currentUser() {
      return this.$store.state.currentUser;
    }
  },
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
        "--theme-background-highlight",
        this.shadeColor(
          this.$data.backgroundColor,
          this.isLightColor(this.$data.backgroundColor) ? -20 : 20
        )
      );
    },
    changeTextColor() {
      document.documentElement.style.setProperty(
        "--theme-text",
        this.$data.textColor
      );
    },
    shadeColor(color, amount) {
      var r = parseInt(color.substring(1, 3), 16);
      var g = parseInt(color.substring(3, 5), 16);
      var b = parseInt(color.substring(5, 7), 16);
      r = parseInt(r + amount);
      g = parseInt(g + amount);
      b = parseInt(b + amount);
      r = r < 255 ? r : 255;
      g = g < 255 ? g : 255;
      b = b < 255 ? b : 255;
      var rr =
        r.toString(16).length == 1 ? "0" + r.toString(16) : r.toString(16);
      var gg =
        g.toString(16).length == 1 ? "0" + g.toString(16) : g.toString(16);
      var bb =
        b.toString(16).length == 1 ? "0" + b.toString(16) : b.toString(16);
      return "#" + rr + gg + bb;
    },
    isLightColor(color) {
      var r = parseInt(color.substring(1, 3), 16);
      var g = parseInt(color.substring(3, 5), 16);
      var b = parseInt(color.substring(5, 7), 16);
      var x = (r + g + b) / 3;
      return x > 128;
    }
  }
};
</script>
