package com.stock.pycurrent.util;

import com.stock.pycurrent.entity.emum.SSEMsgEnum;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;

import java.awt.*;
import java.util.Arrays;

/**
 * @author fzc
 * @date 2023/11/16 12:01
 * @description
 */
@CommonsLog
public class MessageUtil {
    private MessageUtil() {
        throw new IllegalStateException("MessageUtil class");
    }

    public static void sendMessage(String text) {
        if (Platform.isWindows()) {
            ExecutorUtils.MULTIPLE_TASK_POOL.submit(() -> User32.INSTANCE.MessageBoxA(null, text, "Java Notification", User32.MB_OK | User32.MB_ICON_INFORMATION));
        } else {
            System.out.println("This feature is only available on Windows.");
        }
    }

    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = Native.load("user32", User32.class);

        int MB_OK = 0x00000000;
        int MB_ICON_INFORMATION = 0x00000040;

        void MessageBoxA(Pointer hWnd, String text, String caption, int options);
    }

    @SneakyThrows
    public static void sendNotificationMsg(String title, String code) {
        if (PARAMS.BAK_MODE) {
            sendMessage("New Message");
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("System tray icon demo");
            tray.add(trayIcon);
            trayIcon.displayMessage(title, code.substring(2, 6), TrayIcon.MessageType.INFO);
        } else {
            MySseEmitterUtil.sendMsgToClient(Arrays.asList(title, code), SSEMsgEnum.RT_NOTIFICATION);
        }
        log.warn(title + " " + code);
    }


}
