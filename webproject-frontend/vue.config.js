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
const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  assetsDir: "static",
  transpileDependencies: true,
  devServer: {
    proxy: {
      "^/api": {
        target: "http://localhost:6900",
        changeOrigin: true,
        headers: { Connection: "keep-alive" },
      },
      "^/actuator": {
        target: "http://localhost:6900",
        changeOrigin: true,
        headers: { Connection: "keep-alive" },
      },
      "^/performlogin": {
        target: "http://localhost:6900",
        changeOrigin: true,
        headers: { Connection: "keep-alive" },
      },
      "^/logout": {
        target: "http://localhost:6900",
        changeOrigin: true,
        headers: { Connection: "keep-alive" },
      },
    },
  },
  pwa: {
    name: "Webproject",
    themeColor: "#000000",
    iconPaths: {
      faviconSVG: "static/img/icons/favicon.svg",
      favicon32: "static/img/icons/favicon-32x32.png",
      favicon16: "static/img/icons/favicon-16x16.png",
      appleTouchIcon: "static/img/icons/apple-touch-icon-152x152.png",
      maskIcon: "static/img/icons/safari-pinned-tab.svg",
      msTileImage: "static/img/icons/msapplication-icon-144x144.png",
    },
    manifestOptions: {
      display: "standalone",
      icons: [
        {
          src: "/static/img/icons/android-chrome-192x192.png",
          sizes: "192x192",
          type: "image/png",
        },
        {
          src: "/static/img/icons/android-chrome-512x512.png",
          sizes: "512x512",
          type: "image/png",
        },
        {
          src: "/static/img/icons/android-chrome-maskable-192x192.png",
          sizes: "192x192",
          type: "image/png",
          purpose: "maskable",
        },
        {
          src: "/static/img/icons/android-chrome-maskable-512x512.png",
          sizes: "512x512",
          type: "image/png",
          purpose: "maskable",
        },
        {
          src: "/static/img/icons/apple-touch-icon-60x60.png",
          sizes: "60x60",
          type: "image/png",
        },
        {
          src: "/static/img/icons/apple-touch-icon-76x76.png",
          sizes: "76x76",
          type: "image/png",
        },
        {
          src: "/static/img/icons/apple-touch-icon-120x120.png",
          sizes: "120x120",
          type: "image/png",
        },
        {
          src: "/static/img/icons/apple-touch-icon-152x152.png",
          sizes: "152x152",
          type: "image/png",
        },
        {
          src: "/static/img/icons/apple-touch-icon-180x180.png",
          sizes: "180x180",
          type: "image/png",
        },
        {
          src: "/static/img/icons/apple-touch-icon.png",
          sizes: "180x180",
          type: "image/png",
        },
        {
          src: "/static/img/icons/favicon-16x16.png",
          sizes: "16x16",
          type: "image/png",
        },
        {
          src: "/static/img/icons/favicon-32x32.png",
          sizes: "32x32",
          type: "image/png",
        },
        {
          src: "/static/img/icons/msapplication-icon-144x144.png",
          sizes: "144x144",
          type: "image/png",
        },
        {
          src: "/static/img/icons/mstile-150x150.png",
          sizes: "150x150",
          type: "image/png",
        },
      ],
    },
  },
});
