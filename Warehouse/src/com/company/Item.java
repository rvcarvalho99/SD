package com.company;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Item {
    private ReentrantLock lock;
    private Condition notEmpty;
    private Condition notFull;
    private int quantity;
    private int maxQuantity;

    public Item(int maxQuantity, int quantity) {
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
        this.quantity = quantity;
        this.maxQuantity = maxQuantity;
    }
    public void supply(int q){
        lock.lock();
        try {
            while (q>0) {
            while (quantity==maxQuantity){System.out.println("FULL - w8ing");
                notFull.await();}

            int cadd = Math.min(q, (this.maxQuantity - this.quantity));
            System.out.println("Had: " + this.quantity + "/" + this.maxQuantity + " Added: " + cadd);
            this.quantity += cadd;
            q -= cadd;
            this.notEmpty.signal();
            }
        }

        catch (InterruptedException ignored){}
        finally {
            lock.unlock();
        }
    }
    public void consume(){
        lock.lock();
        try {
            while (this.quantity == 0){System.out.println("EMPTY - w8ing");
                this.notEmpty.await();}
            this.quantity--;
            this.notFull.signal();
        } catch (InterruptedException ignored){}
        finally {
            lock.unlock();
        }
    }

    public int getQuantity() throws InterruptedException{
        lock.lock();
        try {
            return this.quantity;
        } finally {
            lock.unlock();
        }
    }

}
