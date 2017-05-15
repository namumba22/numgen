package com.my.d4.numgen;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by dumin on 5/12/17.
 */
public class Consumer {

    private static Consumer consumer;
    private Integer counter = 0;
    private final Map<Integer, String> buffer = new HashMap<>();
    ReadWriteLock lock = new ReentrantReadWriteLock();

    private Consumer() {
    }

    @Benchmark @BenchmarkMode(Mode.Throughput)
    public void handle(int i) {
        lock.writeLock().lock();
        try {
            buffer.put(i, String.format("%04d - number was handled", i));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Benchmark  @BenchmarkMode(Mode.AverageTime)
    public void writeIntoFile() {
        lock.writeLock().lock();
        try {
            if (buffer.containsKey(counter)) {
                System.out.println(buffer.get(counter));
                buffer.remove(counter);
                counter++;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static Consumer getInstance() {

        synchronized (Consumer.class) {
            if (consumer == null) {
                consumer = new Consumer();
            }
        }
        return consumer;
    }

}
