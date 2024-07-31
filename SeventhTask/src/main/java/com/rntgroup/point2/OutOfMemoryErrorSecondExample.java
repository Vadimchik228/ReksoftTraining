package com.rntgroup.point2;

public class OutOfMemoryErrorSecondExample {
    private static class BigObject {
        private static int counter = 10;
        private long[] arr;

        public BigObject() {
            counter++;
            arr = new long[10 * 1024 * 1024 * counter];
        }
    }

    public static void main(String[] args) {
        try {
            while (true) {
                BigObject bigObject = new BigObject();
            }
        } catch (OutOfMemoryError e) {
            System.err.println("OutOfMemoryError occurred: " + e.getMessage());
        }
    }
}
