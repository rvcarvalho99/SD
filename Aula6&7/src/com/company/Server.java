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
        System.out.println("BPI Turning on");
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
                out.println("'create' to create an account\n'close' to close an account\n'consult' to consult an account\n'deposit' to deposit money in an account\n'withdraw' to withdraw money from an account\n'transfer' to transfer money (duh)\n");

                String r=in.readLine();
                while (!r.equals("quit") && r.isEmpty()!=true  ) {
                    try {
                        switch (r) {
                            case "create":
                                int x = bpi.criarConta(0);
                                out.println("Your account number: " + x);
                                break;
                            case "consult":
                                try {
                                    out.println("Insert your account number\n");
                                    r = in.readLine();
                                    double saldo = bpi.consultar(Integer.parseInt(r));
                                    out.println("Your account balance: " + saldo);
                                } catch (ContaInvalida contaInvalida) {
                                    out.println("Invalid Account");
                                }
                                break;
                            case "close":
                                try {
                                    out.println("Insert your account number\n");
                                    r = in.readLine();
                                    double saldo = bpi.fecharConta(Integer.parseInt(r));
                                    out.println("Your account balance: " + saldo + ". Account closed");
                                } catch (ContaInvalida contaInvalida) {
                                    out.println("Invalid Account");
                                }
                                break;
                            case "deposit":
                                try {
                                    out.println("Insert your account number\n");
                                    r = in.readLine();
                                    out.println("Insert the deposit value\n");
                                    String c = in.readLine();
                                    bpi.depositar(Integer.parseInt(r), Double.parseDouble(c));

                                } catch (ContaInvalida contaInvalida) {
                                    out.println("Invalid Account");
                                }
                                break;
                            case "withdraw":
                                try {
                                    out.println("Insert your account number\n");
                                    r = in.readLine();
                                    out.println("Insert the withdraw value\n");
                                    String c = in.readLine();
                                    bpi.levantar(Integer.parseInt(r), Double.parseDouble(c));

                                } catch (ContaInvalida contaInvalida) {
                                    out.println("Invalid Account");
                                } catch (SaldoInsuficiente saldoInsuficiente) {
                                    out.println("Not enough money, you're broke");
                                }
                                break;
                            case "transfer":
                                try {
                                    out.println("Insert your account number\n");
                                    r = in.readLine();

                                    out.println("Insert the other account number\n");
                                    String acc2 = in.readLine();
                                    out.println("Insert the amount to be transfered\n");
                                    String c = in.readLine();
                                    bpi.transferir(Integer.parseInt(r), Integer.parseInt(acc2), Double.parseDouble(c));

                                } catch (ContaInvalida contaInvalida) {
                                    out.println("Invalid Accounts");
                                } catch (SaldoInsuficiente saldoInsuficiente) {
                                    out.println("Not enough money, you're broke");
                                }
                                break;
                            case "":
                            default:
                                System.out.println("Invalid request");
                                out.println("Invalid");
                                break;
                        }
                    }
                    catch (Exception e){out.println("invalid input");}
                    out.println("'create' to create an account\n'close' to close an account\n'consult' to consult an account\n'deposit' to deposit money in an account\n'withdraw' to withdraw money from an account\n'transfer' to transfer money\n");
                    r=in.readLine();
                }
                out.println("BPI shuting down");
                conn.close();
            }
            catch (Exception e){}
        }
    }
}
