// 所有的 Node.js API接口 都可以在 preload 进程中被调用.
// 它拥有与Chrome扩展一样的沙盒。

const {contextBridge, ipcRenderer} = require('electron/renderer')

contextBridge.exposeInMainWorld('electronAPI', {
    openUrl: (url) => ipcRenderer.send('open-url', url),
    sendMsg: (title, body) => ipcRenderer.send('send-msg', title, body)
})

window.addEventListener('DOMContentLoaded', () => {
    const replaceText = (selector, text) => {
        const element = document.getElementById(selector)
        if (element) element.innerText = text
    }

    for (const dependency of ['chrome', 'node', 'electron']) {
        replaceText(`${dependency}-version`, process.versions[dependency])
    }
})
