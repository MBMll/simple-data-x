package com.github.mbmll.datax.core.utils;


import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public interface ConcurrentUtil {
    /**
     * 开启一个异步回调任务
     *
     * @return
     *
     */
    static <T> FutureTask<T> runAsync(Callable<T> callable) {
        FutureTask<T> task = new FutureTask<>(callable);
        new Thread(task).start();
        return task;
    }
 }
