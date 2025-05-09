const { defineConfig } = require("@vue/cli-service");
const webpack = require("webpack");
const path = require("path");
module.exports = defineConfig({
  devServer: {
    static: {},
    watchFiles: {
      paths: ["src/**/*", "public/**/*"],
      options: {
        ignored: /C:\\DumpStack\.log\.tmp/,
      },
    },
  },
  transpileDependencies: true,
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: "false",
        __VUE_OPTIONS_API__: "false",
        __VUE_PROD_DEVTOOLS__: "false",
      }),
    ],
  },
});
