package com.github.mbmll.datax.core;


import com.github.mbmll.datax.core.concepts.Reader;
import com.github.mbmll.datax.core.concepts.Transformer;
import com.github.mbmll.datax.core.concepts.Writer;
import com.github.mbmll.datax.core.exceptions.ClosedException;
import com.github.mbmll.datax.core.utils.ConcurrentUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Engine<E> {
    List<FutureTask<E>> tasks;


    /**
     *
     * @param readers
     * @param transformers
     * @param writers
     */
    public void run(List<Reader<E>> readers,
                    List<Transformer<E>> transformers,
                    List<Writer<E>> writers) {
        RowChannel<E> channel = new RowChannel<E>(100_000);
        for (Reader<E> reader : readers) {
            tasks.add(ConcurrentUtil.runAsync(() -> {
                try {
                    reader.read(channel);
                    return null;
                    // 不 catch ClosedException, 因为这里不应该抛出这个异常
                } finally {
                    channel.close();
                }
            }));
        }
        //  创建多个队列，每个队列对应一个writer
        List<RowChannel<E>> queues = new ArrayList<>(writers.size());
        for (Writer<E> writer : writers) {
            queues.add(new RowChannel<>());
        }
        // 执行 transformer, 输入为channel，输出为queues
        tasks.add(ConcurrentUtil.runAsync(() -> {
            try {
                E e;
                do {
                    e = channel.poll();
                    for (Transformer<E> transformer : transformers) {
                        e = transformer.transform(e);
                    }
                    for (RowChannel<E> q : queues) {
                        q.offer(e);
                    }
                } while (e != null);
            } finally {
                for (RowChannel<E> queue : queues) {
                    queue.close();
                }
            }
            return null;
        }));
        // 创建多个线程，每个线程对应一个writer
        for (int i = 0; i < writers.size(); i++) {
            Writer<E> writer = writers.get(i);
            RowChannel<E> q = queues.get(i);
            tasks.add(ConcurrentUtil.runAsync(() -> {
                try {
                    writer.write(q);
                } catch (ClosedException ignored) {
                }
                return null;
            }));
        }
        // 等待所有任务完成
        for (FutureTask<E> task : tasks) {
            try {
                task.get();
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }
    }
}
