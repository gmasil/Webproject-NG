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

import { AxiosResponse } from "axios";

export class Theme {
  id?: number | null;
  name?: string;
  backgroundColor?: string;
  backgroundHighlightColor?: string;
  primaryColor?: string;
  secondaryColor?: string;
  textColor?: string;
  preset?: boolean;

  public static copy(theme: Theme): Theme {
    return Object.assign(new Theme(), theme);
  }

  public static copyList(themes: Theme[]): Theme[] {
    for (let index: number = 0; index < themes.length; index++) {
      themes[index] = Theme.copy(themes[index]);
    }
    return themes;
  }

  public static fromResponse(response: AxiosResponse<Theme, unknown>): Theme {
    return Theme.copy(response.data);
  }

  public static fromResponseList(
    response: AxiosResponse<Theme[], unknown>
  ): Theme[] {
    return Theme.copyList(response.data);
  }

  public applyTheme(): void {
    if (this.name == null) {
      return;
    }
    if (this.backgroundColor == null) {
      return;
    }
    if (this.backgroundHighlightColor == null) {
      return;
    }
    if (this.primaryColor == null) {
      return;
    }
    if (this.secondaryColor == null) {
      return;
    }
    if (this.textColor == null) {
      return;
    }
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
