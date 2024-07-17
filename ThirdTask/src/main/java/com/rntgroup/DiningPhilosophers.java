package com.rntgroup;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {

    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[5];
        Lock[] forks = new ReentrantLock[5];

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new ReentrantLock();
        }

        for (int i = 0; i < philosophers.length; i++) {
            Lock rightFork = forks[i];
            Lock leftFork = forks[(i + 1) % forks.length];

            philosophers[i] = new Philosopher(leftFork, rightFork);

            Thread t = new Thread(philosophers[i]);
            t.start();
        }
    }

}
