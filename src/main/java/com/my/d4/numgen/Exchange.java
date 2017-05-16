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
public class Exchange {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exchange.class);
    public final static int THREAD_QUATN = 100;

    static Exchange instance;

    public static Exchange getInstance() {

        synchronized (Exchange.class) {
            if (instance == null) {
                instance = new Exchange();
            }
        }
        return instance;
    }



    public static void main(String... s) throws InterruptedException, ExecutionException {

        Exchanger<String> exchanger = new Exchanger<>();

        List<Callable<Integer>> tasks = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(100);

        IntStream.range(0, THREAD_QUATN).forEach(i -> {
            executor.submit(()->{
                try {
                    int delay = Utils.getDelay();
                    System.out.println(Thread.currentThread().getName() + " message: " + delay);
                    System.out.println(Thread.currentThread().getName() + " message: " + exchanger.exchange(Thread.currentThread().getName()+" "+delay));
                } catch (Exception e) {
                }


            });
        });


        executor.shutdown();


    }

}
