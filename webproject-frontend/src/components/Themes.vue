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
  <div>
    <v-select
      class="bg-theme-background-highlight text-theme-text"
      v-model="selectedTheme"
      :options="themes"
      label="name"
      @input="onThemeSelectionChange"
    ></v-select>
    <!--
    <select v-model="selectedTheme" @change="onThemeSelectionChange">
      <option v-for="theme in themes" v-bind:value="theme" :key="theme.id">
        {{ theme.name }}
      </option>
    </select>
    -->

    <button @click="onActivateClick">Activate</button>
    <button @click="onDuplicateClick">Duplicate</button>

    <div v-if="selectedTheme && !selectedTheme.preset">
      <div class="grid grid-cols-fit gap-x-4 gap-y-1 justify-items-start">
        <span class="inline-block">Name</span>
        <input type="text" v-model="selectedThemeCopy.name" />
        <span class="inline-block">Background color</span>
        <color-picker
          class="border border-theme-text"
          v-model="selectedThemeCopy.backgroundColor"
          @change="onBackgroundColorChange"
          disabled="true"
        ></color-picker>
        <span>Text color</span>
        <color-picker
          class="border border-theme-text"
          v-model="selectedThemeCopy.textColor"
        ></color-picker>
      </div>

      <button @click="onSaveActivateClick">Save and Activate</button>
      <button @click="onSaveClick">Save</button>
      <button @click="onResetClick">Reset</button>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { Theme } from "@/types";
import axios from "axios";

declare interface BaseComponentData {
  themes: Theme[];
  selectedTheme: Theme | null;
  selectedThemeCopy: Theme | null;
}

const Themes = Vue.extend({
  name: "Themes",
  computed: {
    currentUser() {
      return this.$store.state.currentUser;
    },
    globalTheme() {
      return this.$store.state.theme;
    },
  },
  data(): BaseComponentData {
    return {
      themes: [],
      selectedTheme: null,
      selectedThemeCopy: null,
    };
  },
  created() {
    axios.get("/api/themes/available").then((response) => {
      this.themes = response.data;
      if (this.themes.length > 0) {
        this.themes.forEach((theme: Theme) => {
          if (theme.id == this.globalTheme.id) {
            this.selectedTheme = theme;
            this.selectedThemeCopy = JSON.parse(
              JSON.stringify(this.selectedTheme)
            );
            return;
          }
        });
      }
    });
  },
  methods: {
    onThemeSelectionChange() {
      this.selectedThemeCopy = JSON.parse(JSON.stringify(this.selectedTheme));
    },
    onSaveClick() {
      if (this.selectedThemeCopy != null && this.selectedTheme != null) {
        this.patchTheme(this.selectedThemeCopy, this.selectedTheme);
        this.persistSelectedTheme(false);
      }
    },
    onSaveActivateClick() {
      if (this.selectedThemeCopy != null && this.selectedTheme != null) {
        this.patchTheme(this.selectedThemeCopy, this.selectedTheme);
        this.persistSelectedTheme(true);
      }
    },
    onResetClick() {
      this.selectedThemeCopy = JSON.parse(JSON.stringify(this.selectedTheme));
    },
    onDuplicateClick() {
      let newTheme = JSON.parse(JSON.stringify(this.selectedTheme));
      newTheme.id = null;
      newTheme.name += " Copy";
      newTheme.preset = false;
      this.themes.push(newTheme);
      this.selectedTheme = newTheme;
      this.selectedThemeCopy = JSON.parse(JSON.stringify(this.selectedTheme));
    },
    patchTheme(source: Theme, target: Theme) {
      target.name = source.name;
      target.backgroundColor = source.backgroundColor;
      target.backgroundHighlightColor = source.backgroundHighlightColor;
      target.primaryColor = source.primaryColor;
      target.secondaryColor = source.secondaryColor;
      target.textColor = source.textColor;
    },
    persistSelectedTheme(activate: boolean) {
      const config = { headers: { "Content-Type": "application/json" } };
      if (this.selectedTheme?.id != null) {
        // update theme
        axios
          .put(
            "/api/themes/" + this.selectedTheme.id,
            this.selectedTheme,
            config
          )
          .then(() => {
            console.log("Theme saved successfully");
            if (activate) {
              this.onActivateClick();
            }
          })
          .catch((error) => {
            console.log("Error saving theme: " + error);
          });
      } else {
        // create new theme
        axios
          .post("/api/themes", this.selectedTheme, config)
          .then((response) => {
            let index = this.findIndexOfSelectedTheme();
            if (index == null) {
              alert("find a better solution!");
              return;
            }
            this.themes[index] = response.data as Theme;
            this.selectedTheme = this.themes[index];
            this.selectedThemeCopy = JSON.parse(
              JSON.stringify(this.selectedTheme)
            );
            console.log("Theme saved successfully");
            if (activate) {
              this.onActivateClick();
            }
          })
          .catch((error) => {
            console.log("Error saving theme: " + error);
          });
      }
    },
    findIndexOfSelectedTheme() {
      for (let index = 0; index < this.themes.length; ++index) {
        if (this.themes[index] == this.selectedTheme) {
          return index;
        }
      }
      return null;
    },
    onActivateClick() {
      if (this.selectedTheme != null) {
        axios.put("/api/themes/activate/" + this.selectedTheme.id).then(() => {
          this.$store.dispatch("loadTheme");
        });
      }
    },
    onBackgroundColorChange() {
      if (
        this.selectedThemeCopy != null &&
        this.selectedThemeCopy.backgroundColor != null
      ) {
        this.selectedThemeCopy.backgroundHighlightColor = this.shadeColor(
          this.selectedThemeCopy.backgroundColor,
          this.isLightColor(this.selectedThemeCopy.backgroundColor) ? -20 : 20
        );
      }
    },
    shadeColor(color: string, amount: number) {
      let r: number = parseInt(color.substring(1, 3), 16);
      let g: number = parseInt(color.substring(3, 5), 16);
      let b: number = parseInt(color.substring(5, 7), 16);
      r += amount;
      g += amount;
      b += amount;
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
    isLightColor(color: string) {
      var r = parseInt(color.substring(1, 3), 16);
      var g = parseInt(color.substring(3, 5), 16);
      var b = parseInt(color.substring(5, 7), 16);
      var x = (r + g + b) / 3;
      return x > 128;
    },
  },
});

export default Themes;
</script>
