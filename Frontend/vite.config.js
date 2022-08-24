import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  base: '/at2/',
  plugins: [vue()],
  server: {
    proxy: {
      '/api': {
        target: 'http://10.4.56.82:8080',
        changeOrigin: true,
        secure: false,
        // rewrite: (path) => path.replace(/^\/api/,'')
      }
    },
    port : 80
  }
})

// 'http://10.4.56.82:8080/api/'