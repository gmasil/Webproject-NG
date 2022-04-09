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
    <v-select
      v-model="selectedTheme"
      class="bg-theme-background-highlight text-theme-text"
      :options="themes"
      label="name"
      @input="onThemeSelectionChange"
    ></v-select>

    <button @click="onActivateClick">Activate</button>
    <button @click="onDuplicateClick">Duplicate</button>

    <div v-if="selectedTheme && !selectedTheme.preset">
      <div class="grid grid-cols-fit gap-x-4 gap-y-1 justify-items-start">
        <span class="inline-block">Name</span>
        <input v-model="selectedThemeCopy.name" type="text" />
        <span class="inline-block">Background color</span>
        <input
          v-model="selectedThemeCopy.backgroundColor"
          @input="onBackgroundColorChange"
        />
        <span>Text color</span>
        <input v-model="selectedThemeCopy.textColor" />
      </div>

      <button @click="onSaveActivateClick">Save and Activate</button>
      <button @click="onSaveClick">Save</button>
      <button @click="onResetClick">Reset</button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapActions, mapGetters } from "vuex";
import { Theme } from "@/types";
import { useToast } from "vue-toastification";

const toast = useToast();

declare interface BaseComponentData {
  themes: Theme[];
  selectedTheme: Theme | null;
  selectedThemeCopy: Theme | null;
}

export default defineComponent({
  name: "Themes",
  data(): BaseComponentData {
    return {
      themes: [],
      selectedTheme: null,
      selectedThemeCopy: null,
    };
  },
  created(): void {
    this.loadAvailableThemes()
      .then((themes: Theme[]) => {
        this.themes = themes;
        const activeTheme: Theme = this.getActiveTheme() as Theme;
        if (this.themes.length > 0) {
          for (let theme of themes) {
            if (theme.id == activeTheme.id) {
              this.selectedTheme = theme;
              this.selectedThemeCopy = JSON.parse(
                JSON.stringify(this.selectedTheme)
              ) as Theme;
              return;
            }
          }
        }
      })
      .catch((error: Error) => {
        toast.error("Error while loading available themes: " + error.message);
      });
  },
  methods: {
    ...mapActions([
      "loadActiveTheme",
      "loadAvailableThemes",
      "saveTheme",
      "activateTheme",
    ]),
    ...mapGetters(["getCurrentUser", "getActiveTheme"]),
    onThemeSelectionChange(): void {
      this.onResetClick();
    },
    onSaveClick(): void {
      if (this.selectedThemeCopy != null && this.selectedTheme != null) {
        this.patchTheme(this.selectedThemeCopy, this.selectedTheme);
        this.persistSelectedTheme(false);
      }
    },
    onSaveActivateClick(): void {
      if (this.selectedThemeCopy != null && this.selectedTheme != null) {
        this.patchTheme(this.selectedThemeCopy, this.selectedTheme);
        this.persistSelectedTheme(true);
      }
    },
    onResetClick(): void {
      this.selectedThemeCopy = JSON.parse(
        JSON.stringify(this.selectedTheme)
      ) as Theme;
    },
    onDuplicateClick(): void {
      let newTheme: Theme = JSON.parse(
        JSON.stringify(this.selectedTheme)
      ) as Theme;
      newTheme.id = null;
      newTheme.name += " Copy";
      newTheme.preset = false;
      this.themes.push(newTheme);
      this.selectedTheme = newTheme;
      this.selectedThemeCopy = JSON.parse(
        JSON.stringify(this.selectedTheme)
      ) as Theme;
    },
    patchTheme(source: Theme, target: Theme): void {
      target.name = source.name;
      target.backgroundColor = source.backgroundColor;
      target.backgroundHighlightColor = source.backgroundHighlightColor;
      target.primaryColor = source.primaryColor;
      target.secondaryColor = source.secondaryColor;
      target.textColor = source.textColor;
    },
    persistSelectedTheme(activate: boolean): void {
      if (this.selectedTheme?.id != null) {
        // update theme
        this.saveTheme(this.selectedTheme)
          .then(() => {
            toast.success("Theme saved successfully");
            if (activate) {
              this.onActivateClick();
            }
          })
          .catch((error: Error) => {
            toast.error("Error while saving theme: " + error.message);
          });
      } else {
        // create new theme
        this.saveTheme(this.selectedTheme)
          .then((savedTheme: Theme) => {
            let index: number | null = this.findIndexOfSelectedTheme();
            if (index == null) {
              toast.error("Error while saving new theme, cannot find index");
              return;
            }
            this.themes[index] = savedTheme;
            this.selectedTheme = this.themes[index];
            this.selectedThemeCopy = JSON.parse(
              JSON.stringify(this.selectedTheme)
            ) as Theme;
            toast.success("Theme saved successfully");
            if (activate) {
              this.onActivateClick();
            }
          })
          .catch((error: Error) => {
            toast.error("Error saving theme: " + error.message);
          });
      }
    },
    findIndexOfSelectedTheme(): number | null {
      for (let index = 0; index < this.themes.length; ++index) {
        if (this.themes[index] == this.selectedTheme) {
          return index;
        }
      }
      return null;
    },
    onActivateClick(): void {
      if (this.selectedTheme?.id != null) {
        this.activateTheme(this.selectedTheme.id)
          .then(() => {
            this.loadActiveTheme().catch(() =>
              toast.error("Error while loading active theme")
            );
          })
          .catch((error: Error) => {
            toast.error("Error while activating theme: " + error.message);
          });
      }
    },
    onBackgroundColorChange(): void {
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
    shadeColor(color: string, amount: number): string {
      let r: number = parseInt(color.substring(1, 3), 16);
      let g: number = parseInt(color.substring(3, 5), 16);
      let b: number = parseInt(color.substring(5, 7), 16);
      r += amount;
      g += amount;
      b += amount;
      r = r < 255 ? r : 255;
      g = g < 255 ? g : 255;
      b = b < 255 ? b : 255;
      r = r > 0 ? r : 0;
      g = g > 0 ? g : 0;
      b = b > 0 ? b : 0;
      var rr =
        r.toString(16).length == 1 ? "0" + r.toString(16) : r.toString(16);
      var gg =
        g.toString(16).length == 1 ? "0" + g.toString(16) : g.toString(16);
      var bb =
        b.toString(16).length == 1 ? "0" + b.toString(16) : b.toString(16);
      return "#" + rr + gg + bb;
    },
    isLightColor(color: string): boolean {
      var r = parseInt(color.substring(1, 3), 16);
      var g = parseInt(color.substring(3, 5), 16);
      var b = parseInt(color.substring(5, 7), 16);
      var x = (r + g + b) / 3;
      return x > 128;
    },
  },
});
</script>
