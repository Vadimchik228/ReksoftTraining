package com.rntgroup.point1;

import java.util.ArrayList;
import java.util.List;

public class OutOfMemoryErrorFirstExample {
    public static void main(String[] args) {
        List<Object> objects = new ArrayList<>();
        try {
            while (true) {
                objects.add(new byte[10 * 1024 * 1024]);
            }
        } catch (OutOfMemoryError e) {
            System.err.println("OutOfMemoryError occurred: " + e.getMessage());
        }
    }
}
