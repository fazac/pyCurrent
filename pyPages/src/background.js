const {app, protocol, BrowserWindow, Menu, MenuItem, ipcMain, Notification, Tray, nativeImage} = require('electron')
// 需在当前文件内开头引入 Node.js 的 'path' 模块
const path = require('path')

app.commandLine.appendSwitch('--ignore-certificate-errors', 'true')
// Scheme must be registered before the app is ready
protocol.registerSchemesAsPrivileged([
    {scheme: 'app', privileges: {secure: true, standard: true}}
])

const msgArr = [];

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
    mainWindow.on('blur', () => {
        if (!mainWindow.isDestroyed()) {
            mainWindow.minimize();
        }
    });

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

    //msg切换icon
    let count = 0;

    //发送消息
    ipcMain.on('send-msg', (event, title, body) => {
        showNotification(title, body);
        let timer = setInterval(function () {
            count++;
            if (count % 2 === 0) {
                iconTray.setImage(path.join(__dirname, '../dist/icon/icon.ico'));
            } else {
                iconTray.setImage(path.join(__dirname, '../dist/icon/empty.png'));
            }
        }, 500);
        msgArr.push(timer);
    });

    //创建系统托盘
    var iconTray = new Tray(path.join(__dirname, '../dist/icon/icon.ico'));

    iconTray.setToolTip('CPY');

    // 配置右键菜单
    var trayMenu = Menu.buildFromTemplate([
        {
            label: '退出',
            click: function () {
                if (process.platform !== 'darwin') {
                    app.quit();
                }
            }
        }
    ]);
// 绑定右键菜单到托盘
    iconTray.setContextMenu(trayMenu);


    setTimeout(function () {
        var win = BrowserWindow.getFocusedWindow();
        // 点击关闭按钮让应用保存在托盘
        win.on('close', (e) => {
            if (!win.isFocused()) {
                win = null;
            } else {
                // 阻止窗口的关闭事件
                e.preventDefault();
                win.hide();
            }
        });
    })


// 任务栏图标双击托盘打开应用
    iconTray.on('double-click', function () {
        mainWindow.show();
        if (!!msgArr) {
            msgArr.forEach(item => {
                clearInterval(item);
            })
            msgArr.length = 0;
        }
    });


}

function showNotification(title, body) {
    new Notification({title: title, body: body}).show()
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
    role: 'reload',
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
