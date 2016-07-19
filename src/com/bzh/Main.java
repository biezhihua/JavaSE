package com.bzh;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random random = new Random(47);
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            int key = random.nextInt(Integer.MAX_VALUE);
            map.put(key, key);
            map.put(key, key);
            System.out.println(Integer.toBinaryString(hash(key)));
        }


    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
