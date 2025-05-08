const { defineConfig } = require("@vue/cli-service");
const webpack = require("webpack");
const path = require("path");
module.exports = defineConfig({
  devServer: {
    static: {
      // point at the Windows folder
      directory: path.resolve(__dirname, "C:/ProjIntegrationImages"),
      // mount it at /external-images
      publicPath: "/external-images",
    },
    // auto-reload on file changes
    watchFiles: ["C:/ProjIntegrationImages/**/*"],
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
