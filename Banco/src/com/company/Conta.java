package com.company;

import com.company.Exceptions.ContaInvalida;
import com.company.Exceptions.SaldoInsuficiente;

import java.util.concurrent.locks.ReentrantLock;

public class Conta
{
    public double saldo;
    private ReentrantLock lockConta = new ReentrantLock();

    public Conta()
    {
        saldo=0;
    }

    public Conta(double saldoInicial)
    {
        saldo=saldoInicial;
    }

    public double getSaldo()
    {
        return this.saldo;
    }

    public void depositarSaldo(double c)
    {
        this.saldo+=c;
    }

    public void levantarSaldo(double d) throws SaldoInsuficiente {
        if(this.saldo>=d)
        this.saldo-=d;
        else{this.unlock(); throw new SaldoInsuficiente();}
    }
    public void lock(){
        this.lockConta.lock();
    }

    public void unlock(){
        this.lockConta.unlock();
    }
}
