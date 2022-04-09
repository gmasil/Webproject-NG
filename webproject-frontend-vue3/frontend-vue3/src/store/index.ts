import { InjectionKey } from "vue";
import { useStore as baseUseStore, createStore, Store } from "vuex";
import axios from "axios";
import {
  AppProperties,
  Page,
  PageResponse,
  Theme,
  User,
  Video,
  VideoFull,
  ChangePasswordData,
} from "@/types";

export class State {
  initializedUser = false;
  currentUser: User | null = null;
  appProperties: AppProperties | null = null;
  activeTheme: Theme | null = null;
  videos: PageResponse<Video> | null = null;
}

export const key: InjectionKey<Store<State>> = Symbol();

export const store = createStore<State>({
  state: new State(),
  getters: {
    isInitialized(state): boolean {
      return state.initializedUser && state.appProperties != null;
    },
    isAccessRestricted(state): boolean {
      if (state.appProperties == null) {
        return true;
      }
      if (state.appProperties.publicAccess) {
        return false;
      } else {
        return state.currentUser == null;
      }
    },
    getCurrentUser(state): User | null {
      return state.currentUser;
    },
    getAppProperties(state): AppProperties | null {
      return state.appProperties;
    },
    getActiveTheme(state): Theme | null {
      return state.activeTheme;
    },
    isAuthenticated(state): boolean {
      return state.currentUser != null;
    },
    getVideos(state): PageResponse<Video> | null {
      return state.videos;
    },
  },
  mutations: {
    setAppProperties(state, data: AppProperties): void {
      state.appProperties = data;
    },
    setCurrentUser(state, data: User): void {
      state.currentUser = data;
      state.initializedUser = true;
    },
    setActiveTheme(state, data: Theme): void {
      state.activeTheme = data;
      if (state.activeTheme != null) {
        state.activeTheme.applyTheme();
      }
    },
    setVideos(state, data: PageResponse<Video>): void {
      state.videos = data;
    },
    dummy(state): void {
      if (state != null) {
        // workaround for: 'commit' is defined but never used
        // if commit is not defined the function is not assignable to type 'Action<State, State>'.
      }
    },
  },
  actions: {
    loadAppProperties({ commit }): Promise<AppProperties> {
      return new Promise<AppProperties>((resolve, reject) => {
        axios
          .get("/api/app/config")
          .then((response) => {
            const props: AppProperties = response.data as AppProperties;
            commit("setAppProperties", props);
            resolve(props);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    loadCurrentUser({ commit }): Promise<User | null> {
      return new Promise<User | null>((resolve, reject) => {
        axios
          .get("/api/users/current")
          .then((response) => {
            if (response.data !== "") {
              const user: User = response.data as User;
              commit("setCurrentUser", user);
              resolve(user);
            } else {
              commit("setCurrentUser", null);
              resolve(null);
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    loadActiveTheme({ commit }): Promise<Theme> {
      return new Promise<Theme>((resolve, reject) => {
        axios
          .get("/api/themes/active")
          .then((response) => {
            const theme: Theme = Theme.fromResponse(response);
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
              resolve(Theme.fromResponse(response));
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
              resolve(Theme.fromResponse(response));
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
    loadVideos({ commit, state }, page: Page): Promise<PageResponse<Video>> {
      return new Promise<PageResponse<Video>>((resolve, reject) => {
        if (
          state.videos != null &&
          state.videos.number == page.page &&
          state.videos.size == page.size
        ) {
          resolve(state.videos);
        } else {
          axios
            .get(
              `/api/videos?size=${page.size}&page=${page.page}&sort=${page.sort}`
            )
            .then((response) => {
              const pageResponse: PageResponse<Video> =
                response.data as PageResponse<Video>;
              commit("setVideos", pageResponse);
              resolve(pageResponse);
            })
            .catch((error) => {
              commit("dummy");
              reject(error);
            });
        }
      });
    },
    loadVideo({ commit }, id: number): Promise<VideoFull> {
      return new Promise<VideoFull>((resolve, reject) => {
        axios
          .get(`/api/videos/${id}`)
          .then((response) => {
            resolve(VideoFull.fromResponse(response));
          })
          .catch((error) => {
            commit("dummy");
            reject(error);
          });
      });
    },
  },
  modules: {},
});

export function useStore(): Store<State> {
  return baseUseStore(key);
}
