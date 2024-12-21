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
    public static final ScheduledExecutorService SINGLE_MSG_POOL = Executors.newScheduledThreadPool(1);

    public static final ExecutorService MULTIPLE_TASK_POOL = new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE * 2, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    public static final ListeningExecutorService GUAVA_EXECUTOR = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(CORE_POOL_SIZE));

    public static final CompletionService<Void> COMPLETION_SERVICE = new ExecutorCompletionService<>(MULTIPLE_TASK_POOL);

    public static void addGuavaComplexTask(Runnable task) {
        GUAVA_EXECUTOR.submit(task);
    }

    public static void addCsComplexTask(Callable<Void> task) {
        COMPLETION_SERVICE.submit(task);
    }


}
