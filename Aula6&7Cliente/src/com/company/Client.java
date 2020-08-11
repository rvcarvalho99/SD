package com.company;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //new Thread(new Listener(in)).start();
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        BufferedReader inUser = new BufferedReader(new InputStreamReader(System.in));

        //System.out.println(in.readLine());
        String r="";
        r= in.readLine();
        while ((r.isEmpty()!=true)) {
            System.out.println(r);
            r= in.readLine();
        }

        while ((r = inUser.readLine()) != null && !r.equals("quit")) {
            out.println(r);
            r="";
            r= in.readLine();
            while ((r.isEmpty()!=true)) {
                System.out.println(r);
                r= in.readLine();
            }
        }
        out.println(r);
        r= in.readLine();
        System.out.println(r);
        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();
    }
    public static class Listener implements Runnable {
        BufferedReader conn;

        public Listener(BufferedReader c) {
            conn = c;
        }

        public void run() {
            try {

                String r="";
                r= conn.readLine();
                while ((r.isEmpty()!=true)) {
                    System.out.println(r);
                    r= conn.readLine();
                }
            }
            catch (Exception e){}
        }
    }

}

