import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: "0.0.0.0",
    cors: true,
    port: 8991,
    open: true, //自动打开
    proxy: {
      '/server-api': {
        target: 'http://localhost:7980',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/server-api/, '')
      }
    },
  }
})
