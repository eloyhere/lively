import { fileURLToPath, URL } from "node:url"

import { defineConfig } from "vite"
import vue from "@vitejs/plugin-vue"
import vueDevTools from "vite-plugin-vue-devtools"

export default defineConfig({
  base: "/",
  server: {
    port: 80,
    proxy: {
      '/tcm': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/consumer': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/administrator': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url))
    },
  },
  build: {
    outDir: "../static/",
  }
})
