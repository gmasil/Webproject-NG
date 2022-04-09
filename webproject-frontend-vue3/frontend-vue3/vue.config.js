const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
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
});
