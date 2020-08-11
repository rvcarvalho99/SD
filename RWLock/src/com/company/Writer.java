package com.company;

public class Writer implements Runnable{
    private RWLock rwlock;

    public Writer(RWLock rw) {
        this.rwlock = rw;
    }

    @Override
    public void run() {
        this.rwlock.writeLock();
        System.out.println("Camião ----------->");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {}
        System.out.println("-----------> Camião");
        this.rwlock.writeUnlock();
    }
}
