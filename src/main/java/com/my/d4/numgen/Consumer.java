package com.my.d4.numgen;

import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by dumin on 5/12/17.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class Consumer {

    private static Consumer consumer;
    private Integer counter = 0;
    private final Map<Integer, String> buffer = new HashMap<>();
    ReadWriteLock lock = new ReentrantReadWriteLock();

//    private Consumer() {
//    }

    public void handle(int i) {
        lock.writeLock().lock();
        try {
            buffer.put(i, String.format("%04d - number was handled", i));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Benchmark
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
