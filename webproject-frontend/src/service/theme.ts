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

import { useStore } from "@/store/pinia";
import axios, { AxiosError } from "axios";
import { Theme } from "@/types";

export const loadActiveTheme = (): Promise<Theme> => {
  return new Promise<Theme>((resolve, reject) => {
    axios
      .get<Theme>("/api/themes/active")
      .then((response) => {
        const theme: Theme = Theme.fromResponse(response);
        const store = useStore();
        store.activeTheme = theme;
        if (theme != null) {
          theme.applyTheme();
        }
        resolve(theme);
      })
      .catch((error: AxiosError) => {
        reject(error);
      });
  });
};

export const loadAvailableThemes = (): Promise<Theme[]> => {
  return new Promise<Theme[]>((resolve, reject) => {
    axios
      .get<Theme[]>("/api/themes/available")
      .then((response) => {
        resolve(Theme.fromResponseList(response));
      })
      .catch((error: AxiosError) => {
        reject(error);
      });
  });
};

export const saveTheme = (theme: Theme): Promise<Theme> => {
  const config = { headers: { "Content-Type": "application/json" } };
  return new Promise<Theme>((resolve, reject) => {
    if (theme.id != null) {
      // update theme
      axios
        .put<Theme>(`/api/themes/${theme.id}`, theme, config)
        .then((response) => {
          resolve(Theme.fromResponse(response));
        })
        .catch((error: AxiosError) => {
          reject(error);
        });
    } else {
      // save new theme
      axios
        .post<Theme>("/api/themes", theme, config)
        .then((response) => {
          resolve(Theme.fromResponse(response));
        })
        .catch((error: AxiosError) => {
          reject(error);
        });
    }
  });
};

export const activateTheme = (themeId: number): Promise<void> => {
  return new Promise<void>((resolve, reject) => {
    axios
      .put(`/api/themes/activate/${themeId}`)
      .then(() => {
        resolve();
      })
      .catch((error: AxiosError) => {
        reject(error);
      });
  });
};

export const themeService = {
  loadActiveTheme,
  loadAvailableThemes,
  saveTheme,
  activateTheme,
};

export default themeService;
