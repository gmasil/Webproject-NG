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

export class Theme {
  id?: number;
  name?: string;
  backgroundColor?: string;
  backgroundHighlightColor?: string;
  primaryColor?: string;
  secondaryColor?: string;
  textColor?: string;
  /*
  constructor(
    name: string,
    backgroundColor: string,
    backgroundHighlightColor: string,
    primaryColor: string,
    secondaryColor: string,
    textColor: string
  ) {
    this.name = name;
    this.backgroundColor = backgroundColor;
    this.backgroundHighlightColor = backgroundHighlightColor;
    this.primaryColor = primaryColor;
    this.secondaryColor = secondaryColor;
    this.textColor = textColor;
  }
*/

  applyTheme(): void {
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
