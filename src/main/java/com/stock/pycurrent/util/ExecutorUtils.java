package com.stock.pycurrent.util;

import com.stock.pycurrent.entity.emum.PyFuncEnum;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.lang.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@CommonsLog
public class ExecutorUtils {

    private ExecutorUtils() {
        throw new IllegalStateException("ExecutorUtils class");
    }

    public static final ExecutorService SINGLE_TASK_POOL = Executors.newSingleThreadExecutor();

    public static <T> T execThreadPY(@Nullable Supplier<T> supplier) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorUtils.SINGLE_TASK_POOL.submit(() -> {
            execPythonFile(PyFuncEnum.EM_CURRENT.toString());
            countDownLatch.countDown();
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

    private static void execPythonFile(String params) {
        Runtime r = Runtime.getRuntime();
        String pyScript = "C:/Users/fa/Desktop/akshare_stock.py";
        File f = new File(pyScript);
        if (f.exists() && !f.isDirectory()) {
            try {
                String[] args = new String[]{"python", pyScript};
                args = concat(args, params.split(" "));
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

    @SafeVarargs
    private static <T> T[] concat(T[] first, T[]... rest) {
        int totalLength = first.length;

        for (T[] array : rest) {
            totalLength += array.length;
        }

        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;

        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }


}
