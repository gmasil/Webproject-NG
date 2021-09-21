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
module.exports = {
  purge: ["./src/**/*.html", "./src/**/*.vue", "./src/**/*.jsx"],
  darkMode: "media", // or 'media' or 'class'
  theme: {
    extend: {
      colors: {
        "theme-background": {
          light: "var(--theme-background-light)",
          DEFAULT: "var(--theme-background)",
          dark: "var(--theme-background-dark)"
        },
        "theme-primary": {
          DEFAULT: "var(--theme-primary)"
        },
        "theme-secondary": {
          DEFAULT: "var(--theme-secondary)"
        },
        "theme-text": {
          light: "var(--theme-text-light)",
          DEFAULT: "var(--theme-text)",
          dark: "var(--theme-text-dark)"
        },
        "theme-text-highlight": {
          DEFAULT: "var(--theme-text-highlight)"
        }
      },
      gridTemplateColumns: {
        fit: "fit-content(100%) fit-content(100%)"
      }
    }
  },
  variants: {
    extend: {}
  },
  plugins: []
};
