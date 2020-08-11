package com.company;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new Listener(in,socket)).start();
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        BufferedReader inUser = new BufferedReader(new InputStreamReader(System.in));

        String r="";


        while ((r = inUser.readLine()) != null && !r.equals("quit"))
            out.println(r);

        out.println(r);
        r= in.readLine();
        System.out.println(r);
        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();
    }


    public static class Listener implements Runnable {
        BufferedReader conn;
        Socket socket;
        public Listener(BufferedReader c,Socket s) {
            conn = c;
            socket = s;
        }

        public void run() {
            try {
                String r;
                while(socket.isBound()) {
                    r = conn.readLine();

                    while ((r.isEmpty() != true)) {
                        System.out.println(r);
                        r = conn.readLine();
                    }
                }
            }
            catch (Exception e){}
        }
    }

}

