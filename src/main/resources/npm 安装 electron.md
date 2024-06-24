### 设置NPM配置
打开.npmrc 文件（Windows 用户目录下），写入如下配置即可；

```
registry=https://registry.npmmirror.com/
electron_mirror=https://npmmirror.com/mirrors/electron/
electron_builder_binaries_mirror=https://npmmirror.com/mirrors/electron-builder-binaries/
sqlite3_binary_host_mirror=https://npmmirror.com/mirrors/sqlite3/
chromedriver_cdnurl=https://npmmirror.com/mirrors/chromedriver/
operadriver_cdnurl=https://npmmirror.com/mirrors/operadriver/
fse_binary_host_mirror=https://npmmirror.com/mirrors/fsevents/
```

### NPM 相关操作

####  清除缓存
`npm cache verify`
#### 查看镜像
`npm config get registry`
#### 设置镜像
`npm config set registry https://registry.npmmirror.com`

### 快速开始
```
git clone https://github.com/electron/electron-quick-start
cd electron-quick-start
npm install
npm start
```

#### 使用npm开始
` npm install electron --save-dev` 

#### npm 下载卡着-- 设置NPM的设置之后使用就不会卡了
```
npm install -g cnpm --registry=https://registry.npmmirror.com
cnpm install --save-dev electron
```


### 打包应用
```
npm install @electron-forge/cli --save-dev 
npx electron-forge import
npm run make
```

原文链接：https://blog.csdn.net/weixin_45729594/article/details/139186691