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
import { Page, Theme, User, Video, ChangePasswordData } from "@/types";

export class State {
  initialized = false;
  currentUser: User | null = null;
  activeTheme: Theme | null = null;
}

Vue.use(Vuex);

export default new Vuex.Store({
  state: new State(),
  getters: {
    isInitialized(state): boolean {
      return state.initialized;
    },
    getCurrentUser(state): User | null {
      return state.currentUser;
    },
    getActiveTheme(state): Theme | null {
      return state.activeTheme;
    },
    isAuthenticated(state): boolean {
      return state.currentUser != null;
    },
  },
  actions: {
    loadCurrentUser({ commit }): Promise<User> {
      return new Promise<User>((resolve, reject) => {
        axios
          .get("/api/users/current")
          .then((response) => {
            const user: User = response.data;
            commit("setCurrentUser", user);
            commit("initialized");
            resolve(user);
          })
          .catch((error) => {
            commit("initialized");
            reject(error);
          });
      });
    },
    loadActiveTheme({ commit }): Promise<Theme> {
      return new Promise<Theme>((resolve, reject) => {
        axios
          .get("/api/themes/active")
          .then((response) => {
            const theme: Theme = Object.assign(new Theme(), response.data);
            commit("setActiveTheme", theme);
            resolve(theme);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    loadAvailableThemes({ commit }): Promise<Theme[]> {
      return new Promise<Theme[]>((resolve, reject) => {
        axios
          .get("/api/themes/available")
          .then((response) => {
            const themes: Theme[] = response.data as Theme[];
            // repackage Theme to make all class functions available
            for (let index = 0; index < themes.length; ++index) {
              themes[index] = Object.assign(new Theme(), themes[index]);
            }
            resolve(themes);
          })
          .catch((error) => {
            commit("dummy");
            reject(error);
          });
      });
    },
    saveTheme({ commit }, theme: Theme): Promise<Theme> {
      const config = { headers: { "Content-Type": "application/json" } };
      return new Promise<Theme>((resolve, reject) => {
        if (theme.id != null) {
          // update theme
          axios
            .put(`/api/themes/${theme.id}`, theme, config)
            .then((response) => {
              const savedTheme: Theme = Object.assign(
                new Theme(),
                response.data
              );
              resolve(savedTheme);
            })
            .catch((error: Error) => {
              commit("dummy");
              reject(error);
            });
        } else {
          // save new theme
          axios
            .post("/api/themes", theme, config)
            .then((response) => {
              const savedTheme: Theme = Object.assign(
                new Theme(),
                response.data
              );
              resolve(savedTheme);
            })
            .catch((error: Error) => {
              commit("dummy");
              reject(error);
            });
        }
      });
    },
    activateTheme({ commit }, themeId: number): Promise<void> {
      return new Promise<void>((resolve, reject) => {
        axios
          .put(`/api/themes/activate/${themeId}`)
          .then(() => {
            resolve();
          })
          .catch((error: Error) => {
            commit("dummy");
            reject(error);
          });
      });
    },
    changePassword({ commit }, data: ChangePasswordData): Promise<void> {
      const config = { headers: { "Content-Type": "application/json" } };
      return new Promise<void>((resolve, reject) => {
        axios
          .put("/api/users/updatepassword", data, config)
          .then(() => {
            resolve();
          })
          .catch((error: Error) => {
            commit("dummy");
            reject(error);
          });
      });
    },
    loadVideos({ commit }, page: Page): Promise<Video[]> {
      return new Promise<Video[]>((resolve, reject) => {
        axios
          .get(
            `/api/videos?size=${page.size}&page=${page.page}&sort=${page.sort}`
          )
          .then((response) => {
            const videos: Video[] = response.data["content"] as Video[];
            resolve(videos);
          })
          .catch((error) => {
            commit("dummy");
            reject(error);
          });
      });
    },
  },
  mutations: {
    setCurrentUser(state, data): void {
      Vue.set(state, "currentUser", data);
    },
    initialized(state): void {
      Vue.set(state, "initialized", true);
    },
    setActiveTheme(state, data: Theme): void {
      Vue.set(state, "activeTheme", data);
      if (state.activeTheme != null) {
        state.activeTheme.applyTheme();
      }
    },
    dummy(state): void {
      if (state != null) {
        // workaround for: 'commit' is defined but never used
        // if commit is not defined the function is not assignable to type 'Action<State, State>'.
      }
    },
  },
});
