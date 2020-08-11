package com.company.Exceptions;

public class SaldoInsuficiente extends Exception {
    public SaldoInsuficiente(){
        super("Fundos insuficientes");
    }
    public SaldoInsuficiente(String message){
        super(message);
    }
}
