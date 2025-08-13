package com.github.mbmll.datax.core;


import com.github.mbmll.datax.core.exceptions.ClosedException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @param <E> element type
 */
public class RowChannel<E> implements AutoCloseable {
    /**
     *
     */
    private final BlockingQueue<E> queue;
    /**
     *
     */
    private final AtomicInteger runnings;
    /**
     * unit second
     */
    private final int retryTime = 1;

    /**
     *
     */
    public RowChannel(int offers) {
        this(100_000, offers);
    }

    public RowChannel(int capacity, int offers) {
        queue = new LinkedBlockingQueue<>(capacity);
        runnings = new AtomicInteger(offers);
    }

    @Override
    public void close() throws Exception {
        runnings.decrementAndGet();
    }

    public boolean isRunning() {
        return runnings.get() > 0;
    }

    /**
     * offer and check thread status
     * @param e  element
     * @throws ClosedException if offer thread is closed
     */
    public void offer(E e) throws ClosedException {
        do {
            try {
                queue.offer(e, retryTime, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            }
        } while (runnings.get() > 0);
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
        } while (runnings.get() > 0);
        throw new ClosedException();
    }
}
