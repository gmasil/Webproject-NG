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

import { defineStore } from "pinia";
import { AppProperties, Theme, User } from "@/types";

export class State {
  initializedUser = false;
  currentUser: User | null = null;
  appProperties: AppProperties | null = null;
  activeTheme: Theme | null = null;
}

export const useStore = defineStore("main", {
  state: () => {
    return new State();
  },
  getters: {
    isInitialized: (state) => {
      return state.initializedUser && state.appProperties != null;
    },
    isAccessRestricted: (state) => {
      if (state.appProperties == null) {
        return true;
      }
      if (state.appProperties.publicAccess) {
        return false;
      } else {
        return state.currentUser == null;
      }
    },
    isAuthenticated(state): boolean {
      return state.currentUser != null;
    },
    getUsername(state): string {
      if (state.currentUser != null) {
        return state.currentUser.username;
      }
      return "Unauthorized";
    },
  },
});
