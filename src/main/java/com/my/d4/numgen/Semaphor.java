package com.my.d4.numgen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Created by dumin on 5/12/17.
 */
public class Semaphor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Semaphor.class);
    public final static int THREAD_QUATN = 100;

    static Semaphor instance;

    static Semaphore semaphore = new Semaphore(10);

    public void printStatistic() throws InterruptedException {
        semaphore.acquire();
        TimeUnit.MILLISECONDS.sleep(Utils.getDelay() * 100);
        LOGGER.info("Thread name-{}, queue {}", Thread.currentThread().getName(), semaphore.getQueueLength());
        semaphore.release();
    }


    public static Semaphor getInstance() {

        synchronized (Semaphor.class) {
            if (instance == null) {
                instance = new Semaphor();
            }
        }
        return instance;
    }

    public static void main(String... s) throws InterruptedException, ExecutionException {

        List<Callable<Integer>> tasks = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(100);

        IntStream.range(0, THREAD_QUATN).forEach(i -> {
            executor.submit(()->{
                try {
                    TimeUnit.MILLISECONDS.sleep(Utils.getDelay() * 1000);
                    Semaphor.getInstance().printStatistic();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });


        executor.shutdown();


    }

}
