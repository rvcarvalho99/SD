package com.company;

public class Reader implements Runnable{
    private RWLock rwlock;

    public Reader(RWLock rw) {
        this.rwlock = rw;
    }

    @Override
    public void run() {
        this.rwlock.readLock();
        System.out.println("<----------- Carro");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {}
        System.out.println("Carro <-----------");
        this.rwlock.readUnlock();

    }
}
