/*
 * Webproject NG
 * Copyright © 2021 - 2022 Gmasil
 *
 * This file is part of Webproject NG.
 *
 * Webproject NG is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International
 * Public License ("Public License").
 *
 * Webproject NG is non-free software: you can redistribute
 * it and/or modify it under the terms of the Public License.
 *
 * You should have received a copy of the Public License along
 * with Webproject NG. If not, see
 * https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt
 */
module.exports = {
  purge: ["./src/**/*.html", "./src/**/*.vue", "./src/**/*.jsx"],
  darkMode: "media", // or 'media' or 'class'
  theme: {
    extend: {
      colors: {
        "theme-background": "var(--theme-background)",
        "theme-background-highlight": "var(--theme-background-highlight)",
        "theme-primary": "var(--theme-primary)",
        "theme-secondary": "var(--theme-secondary)",
        "theme-text": "var(--theme-text)",
      },
      gridTemplateColumns: {
        fit: "fit-content(100%) fit-content(100%)",
      },
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
};
