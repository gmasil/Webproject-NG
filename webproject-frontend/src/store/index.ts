///
/// Webproject NG
/// Copyright © 2021 - 2022 Gmasil
///
/// This file is part of Webproject NG.
///
/// Webproject NG is licensed under the Creative Commons
/// Attribution-NonCommercial-ShareAlike 4.0 International
/// Public License ("Public License").
///
/// Webproject NG is non-free software: you can redistribute
/// it and/or modify it under the terms of the Public License.
///
/// You should have received a copy of the Public License along
/// with Webproject NG. If not, see
/// https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt
///

import { InjectionKey } from "vue";
import { useStore as baseUseStore, createStore, Store } from "vuex";
import { AppProperties, Theme, User } from "@/types";

export class State {
  initializedUser = false;
  currentUser: User | null = null;
  appProperties: AppProperties | null = null;
  activeTheme: Theme | null = null;
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
  },
  mutations: {
    setCurrentUser(state, data: User): void {
      state.currentUser = data;
      state.initializedUser = true;
    },
    setAppProperties(state, data: AppProperties): void {
      state.appProperties = data;
    },
    setActiveTheme(state, data: Theme): void {
      state.activeTheme = data;
      if (state.activeTheme != null) {
        state.activeTheme.applyTheme();
      }
    },
  },
});

export function useStore(): Store<State> {
  return baseUseStore(key);
}
