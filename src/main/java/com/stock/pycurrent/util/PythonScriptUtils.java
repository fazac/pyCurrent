package com.stock.pycurrent.util;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.lang.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.*;
import java.util.function.Supplier;

@CommonsLog
public class PythonScriptUtils {
    private PythonScriptUtils() {
        throw new IllegalStateException("PythonScriptUtils class");
    }


    private static void execPythonFile(String fileName, String params) {
        Runtime r = Runtime.getRuntime();
        String pyScript = "C:/Users/fa/Desktop/py/" + fileName;
        File f = new File(pyScript);
        if (f.exists() && !f.isDirectory()) {
            try {
                String[] args = new String[]{"python", pyScript};
                args = ArrayUtils.concat(args, params.split(" "));
                log.warn("args" + JSONUtils.toJson(args));
                Process p = r.exec(args);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception ex) {
                log.error("execPython Exception", ex);
            }
        } else {
            log.warn("UnExist file! " + pyScript);
        }
    }


    /**
     * 纯执行脚本
     *
     * @param fileName 脚本名
     * @param params   参数
     */
    public static void execThreadPY(String fileName, String params) {
        execThreadPY(fileName, params, null);
    }

    /**
     * 单线程执行py脚本
     *
     * @param fileName 脚本名
     * @param params   参数
     * @param supplier 处理函数
     * @param <T>      返回值泛型
     * @return 返回值
     */
    public static <T> T execThreadPY(String fileName, String params, @Nullable Supplier<T> supplier) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorUtils.SINGLE_TASK_POOL.submit(() -> {
            log.info("PY-ENTER ");
            log.info("PY-" + fileName);
            execPythonFile(fileName, params);
            countDownLatch.countDown();
            log.info("PY-OVER ");
        });
        try {
            countDownLatch.await();
            return supplier == null ? null : supplier.get();
        } catch (InterruptedException e) {
            log.error(e);
            Thread.currentThread().interrupt();
        }
        return null;
    }

}
