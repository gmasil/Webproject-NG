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
const path = require("path");

module.exports = {
  outputDir: path.resolve(__dirname, "dist"),
  assetsDir: "static",
  devServer: {
    proxy: {
      "^/api": {
        target: "http://localhost:6900",
        changeOrigin: true,
        headers: { Connection: "keep-alive" },
      },
      "^/swagger-ui": {
        target: "http://localhost:6900",
        changeOrigin: true,
        headers: { Connection: "keep-alive" },
      },
      "^/v3/api-docs": {
        target: "http://localhost:6900",
        changeOrigin: true,
        headers: { Connection: "keep-alive" },
      },
      "^/swagger-resources": {
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
};
