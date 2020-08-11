package com.company;

import com.company.Exceptions.ContaInvalida;
import com.company.Exceptions.SaldoInsuficiente;

public class Cliente implements Runnable{
    private Banco banco;
    private int nOps;
    private int conta;
    private double valor;

    public void run(){
        for (int i = 0; i < this.nOps; i++) {
            if(this.valor>0) {
                try {
                    this.banco.depositar(this.conta, this.valor);
                } catch (ContaInvalida contaInvalida) {
                    System.out.println(contaInvalida.getMessage());
                }
            }
            else {
                try {
                    this.banco.levantar(this.conta, - this.valor);
                } catch (SaldoInsuficiente | ContaInvalida saldoInsuficiente) {
                    System.out.println(saldoInsuficiente.getMessage());
                }
            }
        }
    }

    public Cliente(Banco banco, int nOps, int conta, double valor) {
        this.banco = banco; this.nOps = nOps; this.conta = conta; this.valor = valor;
    }

    public static void main(String [] args) throws ContaInvalida {
        Banco b = new Banco();

        Cliente cli1 = new Cliente(b, 100000, 0, 5);
        Cliente cli2 = new Cliente(b, 100000, 0, -5);

        Thread t1 = new Thread(cli1);
        Thread t2 = new Thread(cli2);
        b.criarConta(1);
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ignored) {}

        System.out.println(b.consultar(0));
        System.out.println("FIM");
    }


}
