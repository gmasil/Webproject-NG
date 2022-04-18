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

import { Store, useStore } from "@/store/pinia";
import axios, { AxiosError } from "axios";
import { Theme, Response, Resolve, Reject } from "@/types";

export const loadActiveTheme = (): Promise<Theme> => {
  return new Promise<Theme>((resolve: Resolve<Theme>, reject: Reject) => {
    axios
      .get<Theme>("/api/themes/active")
      .then((response: Response<Theme>) => {
        const theme: Theme = Theme.fromResponse(response);
        const store: Store = useStore();
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
  return new Promise<Theme[]>((resolve: Resolve<Theme[]>, reject: Reject) => {
    axios
      .get<Theme[]>("/api/themes/available")
      .then((response: Response<Theme[]>) => {
        resolve(Theme.fromResponseList(response));
      })
      .catch((error: AxiosError) => {
        reject(error);
      });
  });
};

export const saveTheme = (theme: Theme): Promise<Theme> => {
  // eslint-disable-next-line @typescript-eslint/typedef
  const config = { headers: { "Content-Type": "application/json" } };
  return new Promise<Theme>((resolve: Resolve<Theme>, reject: Reject) => {
    if (theme.id != null) {
      // update theme
      axios
        .put<Theme>(`/api/themes/${theme.id}`, theme, config)
        .then((response: Response<Theme>) => {
          resolve(Theme.fromResponse(response));
        })
        .catch((error: AxiosError) => {
          reject(error);
        });
    } else {
      // save new theme
      axios
        .post<Theme>("/api/themes", theme, config)
        .then((response: Response<Theme>) => {
          resolve(Theme.fromResponse(response));
        })
        .catch((error: AxiosError) => {
          reject(error);
        });
    }
  });
};

export const activateTheme = (themeId: number): Promise<void> => {
  return new Promise<void>((resolve: Resolve<void>, reject: Reject) => {
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

// eslint-disable-next-line @typescript-eslint/typedef
export const themeService = {
  loadActiveTheme,
  loadAvailableThemes,
  saveTheme,
  activateTheme,
};

export default themeService;
