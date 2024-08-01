package com.rntgroup.point4;

public class StackOverflowErrorFourthExample {
    public static void main(String[] args) {
        try {
            recursiveMethod();
        } catch (StackOverflowError e) {
            System.err.println("StackOverflowError occurred: " + e.getMessage());
        }
    }

    public static void recursiveMethod() {
        recursiveMethod();
    }
}
