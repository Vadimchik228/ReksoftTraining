package com.rntgroup;

import java.util.ArrayList;
import java.util.List;

public class ApplicationRunner {
    public static void main(String[] args) throws InterruptedException {
        List<Object> objectPool = new ArrayList<>();
        while (true) {
            objectPool.add(new byte[10 * 1024 * 1024]);
            Thread.sleep(1000);
        }
    }
}
