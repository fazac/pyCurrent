package com.stock.pycurrent.util;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

/**
 * @author fzc
 * @date 2023/11/16 12:01
 * @description
 */

public class MessageUtil {
    private MessageUtil() {
        throw new IllegalStateException("MessageUtil class");
    }

    public static void sendMessage(String text) {
        if (Platform.isWindows()) {
            User32.INSTANCE.MessageBoxA(null, text, "Java Notification", User32.MB_OK | User32.MB_ICON_INFORMATION);
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


}
