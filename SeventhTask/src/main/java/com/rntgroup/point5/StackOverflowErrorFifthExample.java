package com.rntgroup.point5;

public class StackOverflowErrorFifthExample {
    StackOverflowErrorFifthExample object = new StackOverflowErrorFifthExample();

    public static void main(String[] args) {
        try {
            new StackOverflowErrorFifthExample();
        } catch (StackOverflowError e) {
            System.err.println("StackOverflowError occurred: " + e.getMessage());
        }
    }
}
