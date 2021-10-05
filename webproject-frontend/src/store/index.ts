///
/// Webproject NG
/// Copyright © 2021 Gmasil
///
/// This file is part of Webproject NG.
///
/// Webproject NG is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// Webproject NG is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with Webproject NG. If not, see <https://www.gnu.org/licenses/>.
///

import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";

Vue.use(Vuex);

class User {}

class Theme {
  backgroundColor: string;
  backgroundHighlightColor: string;
  primaryColor: string;
  secondaryColor: string;
  textColor: string;

  constructor(
    backgroundColor: string,
    backgroundHighlightColor: string,
    primaryColor: string,
    secondaryColor: string,
    textColor: string
  ) {
    this.backgroundColor = backgroundColor;
    this.backgroundHighlightColor = backgroundHighlightColor;
    this.primaryColor = primaryColor;
    this.secondaryColor = secondaryColor;
    this.textColor = textColor;
  }

  applyTheme() {
    document.documentElement.style.setProperty(
      "--theme-background",
      this.backgroundColor
    );
    document.documentElement.style.setProperty(
      "--theme-background-highlight",
      this.backgroundHighlightColor
    );
    document.documentElement.style.setProperty(
      "--theme-primary",
      this.primaryColor
    );
    document.documentElement.style.setProperty(
      "--theme-secondary",
      this.secondaryColor
    );
    document.documentElement.style.setProperty("--theme-text", this.textColor);
  }
}

class State {
  initialized = false;
  currentUser: User | null = null;
  theme: Theme | null = null;
}

export default new Vuex.Store({
  state: new State(),
  actions: {
    loadCurrentUser({ commit }) {
      axios
        .get("/api/users/current")
        .then((response) => {
          commit("setCurrentUser", response.data);
          commit("initialized");
        })
        .catch(() => {
          commit("initialized");
        });
    },
    loadTheme({ commit }) {
      axios
        .get("/api/themes/active")
        .then((response) => {
          commit("setTheme", response.data);
        })
        .catch((error) => {
          console.log("Error while loading theme: " + error);
        });
    },
  },
  mutations: {
    setCurrentUser(state, data) {
      Vue.set(state, "currentUser", data);
    },
    initialized(state) {
      Vue.set(state, "initialized", true);
    },
    setTheme(state, data) {
      Vue.set(state, "theme", data);
      if (state.theme) {
        state.theme.applyTheme();
      }
    },
  },
});
