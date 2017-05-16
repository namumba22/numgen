package com.my.d4.numgen;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by dumin on 5/12/17.
 */
public class Utils {
    public static int getDelay(){
        return ThreadLocalRandom.current().nextInt(5)+1;
    }
}
