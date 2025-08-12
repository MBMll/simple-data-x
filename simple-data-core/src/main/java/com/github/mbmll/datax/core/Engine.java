package com.github.mbmll.datax.core;


import com.github.mbmll.datax.core.utils.ConcurrentUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Engine<E> {
    List<FutureTask<E>> tasks;
    private AtomicBoolean isRunning = new AtomicBoolean(true);


    private void offer(E e, BlockingQueue<E> queue) {
        while (isRunning.get()) {
            try {
                queue.offer(e, 1, TimeUnit.SECONDS);
                break;
            } catch (InterruptedException ignored) {
            }
        }
    }

    private E poll(BlockingQueue<E> q) {
        E e = null;
        while (isRunning.get() && e == null) {
            try {
                e = q.poll(1, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            }
        }
        return e;
    }

    /**
     *
     * @param readers
     * @return
     */
    public void run(List<Reader<E>> readers,
                    List<Transformer<E>> transformers,
                    List<Writer<E>> writers) {
        BlockingQueue<E> queue = new ArrayBlockingQueue<>(100_000);
        for (Reader<E> reader : readers) {
            tasks.add(ConcurrentUtil.runAsync(() -> {
                reader.read((E e) -> {
                    offer(e, queue);
                });
                return null;
            }));
        }
        List<BlockingQueue<E>> queues = new ArrayList<>(writers.size());
        tasks.add(ConcurrentUtil.runAsync(() -> {
            while (isRunning.get()) {
                E e = poll(queue);
                for (Transformer<E> transformer : transformers) {
                    e = transformer.transform(e);
                }
                for (BlockingQueue<E> q : queues) {
                    offer(e, q);
                }
            }
            return null;
        }));
        for (int i = 0; i < writers.size(); i++) {
            Writer<E> writer = writers.get(i);
            BlockingQueue<E> q = queues.get(i);
            tasks.add(ConcurrentUtil.runAsync(() -> {
                E e = poll(q);
                writer.write(e);
                return null;
            }));
        }
        for (FutureTask<E> task : tasks) {
            try {
                task.get();
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }
    }
}
