const {app, protocol, BrowserWindow, Menu, MenuItem, ipcMain} = require('electron')
// 需在当前文件内开头引入 Node.js 的 'path' 模块
const path = require('path')

app.commandLine.appendSwitch('--ignore-certificate-errors', 'true')
// Scheme must be registered before the app is ready
protocol.registerSchemesAsPrivileged([
    {scheme: 'app', privileges: {secure: true, standard: true}}
])

const createWindow = () => {
    const mainWindow = new BrowserWindow({
        minWidth: 1280,
        minHeight: 860,
        width: 1280,
        height: 860,
        autoHideMenuBar: true,
        //窗口是否在屏幕居中. 默认值为 false
        center: true,
        //设置为 false 时可以创建一个无边框窗口 默认值为 true。
        frame: true,
        //窗口是否在创建时显示。 默认值为 true。
        show: true,
        webPreferences: {
            nodeIntegration: true,
            nodeIntegrationInWorker: true,
            preload: path.join(__dirname, 'preload.js'),
            // webSecurity: false, false 是 控制台会报警告, 不太喜欢, 就设置为了 true
            webSecurity: false
        }
    })
    if (app.isPackaged) {
        mainWindow.loadURL(`file://${path.join(__dirname, '../dist/index.html')}`)
    } else {
        mainWindow.loadURL('http://localhost:10213')
    }
    //打开标签页
    ipcMain.on('open-url', (event, url) => {
        const webContents = mainWindow.webContents;
        console.log(url);
        if (webContents.getURL() !== url) {
            event.preventDefault();
            if (!app.isPackaged) {
                webContents.loadURL(`http://localhost:10213/#` + url);
            } else {
                webContents.loadFile("../dist/index.html#", {
                    hash: url
                });
            }
        }
    });

}

const menu = new Menu()
menu.append(new MenuItem({
    id: 'console',
    label: 'console',
    accelerator: 'F12',
    click: () => {
        BrowserWindow.getAllWindows()[0].webContents.openDevTools()
    },
}))
menu.append(new MenuItem({
    id: 'reload',
    label: 'reload',
    accelerator: 'F5',
    click: () => {
        BrowserWindow.getAllWindows()[0].webContents.loadURL('http://localhost:5173/')
    },
}))

Menu.setApplicationMenu(menu)

app.whenReady().then(() => {
    createWindow()
    app.on('activate', () => {
        if (BrowserWindow.getAllWindows().length === 0) createWindow()
    })
})

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') app.quit()
})
