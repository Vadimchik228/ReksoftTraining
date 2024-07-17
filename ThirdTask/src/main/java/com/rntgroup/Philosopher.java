package com.rntgroup;

import java.util.concurrent.locks.Lock;

class Philosopher implements Runnable {
    private static int generator = 1;
    private final Lock leftFork;
    private final Lock rightFork;
    private final int id;

    public Philosopher(Lock leftFork, Lock rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        id = generator++;
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();
                eat();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " thinking");
        Thread.sleep((long) (Math.random() * 1000));
    }

    private void eat() throws InterruptedException {
        while (true) {
            boolean leftForkLocked = leftFork.tryLock();
            boolean rightForkLocked = rightFork.tryLock();

            if (leftForkLocked && rightForkLocked) {
                try {
                    System.out.println("Philosopher " + id + " eating");
                    Thread.sleep((long) (Math.random() * 1000));
                } finally {
                    leftFork.unlock();
                    rightFork.unlock();
                }
                break;
            }

            if (leftForkLocked) {
                leftFork.unlock();
            }
            if (rightForkLocked) {
                rightFork.unlock();
            }

            // Даем другим философам шанс захватить вилки
            Thread.sleep((long) (Math.random() * 100));
        }
    }
}
