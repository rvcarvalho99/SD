package com.company;

public class Counter {
    public int count;

    public Counter(int i) {count=i;}

    public synchronized void inc(){this.count++;}

    public int get(){return this.count;}
}