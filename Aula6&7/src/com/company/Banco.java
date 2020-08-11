package com.company;

import com.company.Exceptions.ContaInvalida;
import com.company.Exceptions.SaldoInsuficiente;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class Banco {
    private HashMap<Integer,Conta> contas = new HashMap<Integer,Conta>();
    private int clients=0;
    private ReentrantLock lockBanco = new ReentrantLock();

    public Banco() {
     }

     public int criarConta(double saldoInicial){
        this.lockBanco.lock();
         clients++;
         Conta c = new Conta(saldoInicial);
         contas.put(clients-1,c);
         this.lockBanco.unlock();
        return clients-1;

     }

    public double fecharConta(int id) throws ContaInvalida {
        this.lockBanco.lock();
        if (!this.contas.containsKey(id)){
            this.lockBanco.unlock();
            throw new ContaInvalida();
        }
        Conta c = this.contas.remove(id);
        this.lockBanco.unlock();
        return c.getSaldo();
    }

    public double consultar(int conta) throws ContaInvalida {
        this.lockBanco.lock();
        if (!this.contas.containsKey(conta)){
            this.lockBanco.unlock();
            throw new ContaInvalida();
        }
        this.contas.get(conta).lock();
        this.lockBanco.unlock();
        double saldo = this.contas.get(conta).getSaldo();
        this.contas.get(conta).unlock();
        return saldo;
    }

    public void depositar(int conta, double valor) throws ContaInvalida {
        this.lockBanco.lock();
        if (!this.contas.containsKey(conta)){
            this.lockBanco.unlock();
            throw new ContaInvalida();
        }
        this.contas.get(conta).lock();
        this.lockBanco.unlock();
        this.contas.get(conta).depositarSaldo(valor);
        this.contas.get(conta).unlock();

    }

    public void levantar(int conta, double valor) throws SaldoInsuficiente, ContaInvalida {
        this.lockBanco.lock();
        if (!this.contas.containsKey(conta)){
            this.lockBanco.unlock();
            throw new ContaInvalida();
        }
        this.contas.get(conta).lock();
        this.lockBanco.unlock();
        this.contas.get(conta).levantarSaldo(valor);
        this.contas.get(conta).unlock();
    }

    public void transferir(int nrConta1, int nrConta2, double valor) throws ContaInvalida, SaldoInsuficiente {
        if(nrConta1 > nrConta2)
        {
            synchronized(this.contas.get(nrConta1))
            {
                synchronized(this.contas.get(nrConta2))
                {
                    levantar(nrConta1, valor);
                    depositar(nrConta2, valor);
                }
            }
        }
        else
        {
            synchronized(this.contas.get(nrConta2))
            {
                synchronized(this.contas.get(nrConta1))
                {
                    levantar(nrConta1, valor);
                    depositar(nrConta2, valor);
                }
            }
        }
    }
}