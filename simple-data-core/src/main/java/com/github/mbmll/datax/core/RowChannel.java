package com.github.mbmll.datax.core;


import com.github.mbmll.datax.core.exceptions.ClosedException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @param <E> element type
 */
public class RowChannel<E> implements AutoCloseable {
    /**
     *
     */
    private final BlockingQueue<E> queue = new LinkedBlockingQueue<E>(100_000);
    /**
     *
     */
    private AtomicBoolean isRunning = new AtomicBoolean(true);
    /**
     * unit second
     */
    private int retryTime = 1;

    @Override
    public void close() throws Exception {
        isRunning.set(false);
    }

    /**
     * @param e
     *
     * @return
     */
    public boolean offer(E e) throws ClosedException {
        do {
            try {
                return queue.offer(e, retryTime, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            }
        } while (isRunning.get());
        throw new ClosedException();
    }

    /**
     *
     * @return element
     *
     * @throws ClosedException
     */
    public E poll() throws ClosedException {
        do {
            try {
                return queue.poll(retryTime, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            }
        } while (isRunning.get());
        throw new ClosedException();
    }
}
