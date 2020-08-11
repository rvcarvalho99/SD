package com.company;

import com.company.Exceptions.ContaInvalida;
import com.company.Exceptions.SaldoInsuficiente;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Server {





    public static void main(String[] args) throws IOException {

        ServerSocket socket = new ServerSocket(12345);
        Banco bpi = new Banco();
        while (true){
            Socket conn = socket.accept();
            new Thread(new ClientHandler(conn,bpi)).start();
            System.out.println("Client Connected");
        }
    }
    public static class ClientHandler implements Runnable {
        Socket conn;
        Banco bpi;
        public ClientHandler(Socket c,Banco b){
            conn=c; bpi=b;
        }
        public void run() {
            try {

                PrintWriter out =
                        new PrintWriter(conn.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                out.println("Digite:->'create' to create an account\n->'close' to close an account\n'consult' to consult an account\n'deposit' to deposit money in an account\n'withdraw' to withdraw money from an account\n'transfer' to transfer money\n");

                String r;
                while ((r= in.readLine()) != "quit" && r.isEmpty()!=true  ) {
                    System.out.println("here + " + r);
                    switch(r)
                    {
                        case "create":
                            int x= bpi.criarConta(0);
                            out.println("Your account number: "+ x );
                            break;
                        case "consult":
                            try {
                                out.println("Insert your account number\n");
                                r= in.readLine();
                                double saldo = bpi.consultar(Integer.parseInt(r));
                                out.println("O seu saldo é: " + saldo);
                            }catch (ContaInvalida contaInvalida) {
                                out.println("Invalid Account");
                            }
                            break;
                        case "close":
                            System.out.println("three");
                            break;
                        case "deposit":
                            try{
                            out.println("Insert your account number\n");
                            r= in.readLine();
                            out.println("Insert the deposit value\n");
                            String c= in.readLine();
                            bpi.depositar(Integer.parseInt(r),Double.parseDouble(c));
                            break;
                            }
                            catch (ContaInvalida contaInvalida){
                                out.println("Invalid Account");}
                        case "withdraw":
                            System.out.println("five");
                            break;
                        case "transfer":
                            System.out.println("five");
                            break;
                        case "":
                        default:
                            System.out.println("Invalid request");
                            out.println("Invalid");
                            break;
                    }
                    out.println("Digite:->'create' to create an account\n->'close' to close an account\n'consult' to consult an account\n'deposit' to deposit money in an account\n'withdraw' to withdraw money from an account\n'transfer' to transfer money\n");

                }
                System.out.println("BPI shuting down");

                conn.shutdownOutput();
                conn.shutdownInput();
                conn.close();
            }
            catch (Exception e){}
        }
    }
}
