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

    <div v-if="selectedTheme && !selectedTheme.preset">
      <div class="grid grid-cols-fit gap-x-4 gap-y-1 justify-items-start">
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
      themes: [],
      selectedTheme: null,
      selectedThemeCopy: null,
      backgroundColor: getComputedStyle(
        document.documentElement
      ).getPropertyValue("--theme-background"),
      textColor: getComputedStyle(document.documentElement).getPropertyValue(
        "--theme-text"
      )
    };
  },
  created() {
    axios
      .get("/api/themes/search/available")
      .then(response => {
        this.themes = response.data._embedded.themes;
        if (this.themes.length > 0) {
          this.themes.forEach(theme => {
            if (theme.id == this.globalTheme.id) {
              this.selectedTheme = theme;
              this.selectedThemeCopy = JSON.parse(
                JSON.stringify(this.selectedTheme)
              );
              return;
            }
          });
        }
      })
      .catch(error => {});
  },
  methods: {
    onThemeSelectionChange() {
      this.selectedThemeCopy = JSON.parse(JSON.stringify(this.selectedTheme));
    },
    onSaveClick() {
      this.patchTheme(this.selectedThemeCopy, this.selectedTheme);
      this.persistSelectedTheme(false);
    },
    onSaveActivateClick() {
      this.patchTheme(this.selectedThemeCopy, this.selectedTheme);
      this.persistSelectedTheme(true);
    },
    onResetClick() {
      this.selectedThemeCopy = JSON.parse(JSON.stringify(this.selectedTheme));
    },
    patchTheme(source, target) {
      target.backgroundColor = source.backgroundColor;
      target.backgroundHighlightColor = source.backgroundHighlightColor;
      target.primaryColor = source.primaryColor;
      target.secondaryColor = source.secondaryColor;
      target.textColor = source.textColor;
    },
    persistSelectedTheme(activate) {
      const config = { headers: { "Content-Type": "application/json" } };
      axios
        .put(
          "/api/themes/update/" + this.selectedTheme.id,
          this.selectedTheme,
          config
        )
        .then(response => {
          console.log("Theme saved successfully");
          if (activate) {
            this.onActivateClick();
          }
        })
        .catch(error => {
          console.log("Error saving theme: " + error);
        });
    },
    onActivateClick() {
      axios
        .put("/api/themes/activate/" + this.selectedTheme.id)
        .then(response => {
          this.$store.dispatch("loadTheme");
        })
        .catch(error => {});
    },
    onBackgroundColorChange() {
      this.selectedThemeCopy.backgroundHighlightColor = this.shadeColor(
        this.selectedThemeCopy.backgroundColor,
        this.isLightColor(this.selectedThemeCopy.backgroundColor) ? -20 : 20
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
  },
  computed: {
    globalTheme() {
      return this.$store.state.theme;
    }
  }
};
</script>
