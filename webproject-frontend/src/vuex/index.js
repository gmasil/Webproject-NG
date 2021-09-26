/*
 * Webproject NG
 * Copyright © 2021 Gmasil
 *
 * This file is part of Webproject NG.
 *
 * Webproject NG is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Webproject NG is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Webproject NG. If not, see <https://www.gnu.org/licenses/>.
 */
import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    initialized: false,
    currentUser: null,
    authenticated: false,
    theme: null
  },
  actions: {
    loadCurrentUser({ commit }) {
      axios
        .get("/api/users/current")
        .then(response => {
          commit("setCurrentUser", response.data);
        })
        .catch(error => {
          commit("initialized");
        });
    },
    loadTheme({ commit }) {
      axios
        .get("/api/themes/active")
        .then(response => {
          commit("setTheme", response.data);
        })
        .catch(error => {
          console.log("Error while loading theme: " + error);
        });
    }
  },
  mutations: {
    setCurrentUser(state, data) {
      Vue.set(state, "currentUser", data);
      Vue.set(state, "authenticated", true);
      Vue.set(state, "initialized", true);
    },
    initialized(state) {
      Vue.set(state, "initialized", true);
    },
    setTheme(state, data) {
      Vue.set(state, "theme", data);
      document.documentElement.style.setProperty(
        "--theme-background",
        state.theme.backgroundColor
      );
      document.documentElement.style.setProperty(
        "--theme-background-highlight",
        state.theme.backgroundHighlightColor
      );
      document.documentElement.style.setProperty(
        "--theme-primary",
        state.theme.primaryColor
      );
      document.documentElement.style.setProperty(
        "--theme-secondary",
        state.theme.secondaryColor
      );
      document.documentElement.style.setProperty(
        "--theme-text",
        state.theme.textColor
      );
    }
  }
});
