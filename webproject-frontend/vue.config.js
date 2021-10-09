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
