package com.stock.pycurrent.util;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.apachecommons.CommonsLog;

import java.util.concurrent.*;


@CommonsLog
public class ExecutorUtils {

    private ExecutorUtils() {
        throw new IllegalStateException("ExecutorUtils class");
    }

    public static final Integer CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService SINGLE_TASK_POOL = Executors.newSingleThreadExecutor();

    public static final ExecutorService MULTIPLE_TASK_POOL = new ThreadPoolExecutor(CORE_POOL_SIZE * 100, CORE_POOL_SIZE * 300, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    public static final ListeningExecutorService GUAVA_EXECUTOR = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(CORE_POOL_SIZE));

    public static void addGuavaComplexTask(Runnable task) {
        GUAVA_EXECUTOR.submit(task);
    }


}
