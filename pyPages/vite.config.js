import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import copy from 'rollup-plugin-copy'

// https://vitejs.dev/config/
export default defineConfig({
    base: "./",
    plugins: [
        vue(),
    ],
    build: {
        terserOptions: {
            compress: {
                drop_console: true, // 生产环境移除console
            },
        },
        emptyOutDir: false, // 将此配置项设为false即可
        rollupOptions: {
            plugins: [
                copy({
                    targets: [
                        {src: 'src/assets/icon', dest: 'dist'} // 将src/static目录下的文件复制到dist目录
                    ],
                    flatten: true,
                    verbose: true
                }),
            ],
        },
        outDir: 'dist',
        assetsDir: 'assets',
    },
    server: {
        port: 10213,
    },
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
})
