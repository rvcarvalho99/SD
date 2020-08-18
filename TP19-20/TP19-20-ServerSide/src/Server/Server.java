package Server;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import static java.io.DataInputStream.readUTF;
import static java.lang.Thread.sleep;


public class Server {



    public static void main(String[] args) throws IOException {
        ServerDB serverdb = new ServerDB();
        System.out.println("Spotify da Candonga");
        ServerSocket socket = new ServerSocket(12345);
        while (true){
            Socket conn = socket.accept();
            new Thread(new ClientHandler(conn,serverdb)).start();
            System.out.println("Client Connected");
        }
    }

    public static boolean login(DataOutputStream out, ServerDB serverdb,DataInputStream in){
        try {
            String welcomemenu = "Bem Vindo.\n 'login' \n 'registar'\n";
            out.writeUTF("nome");
            String nome = in.readUTF();
            out.writeUTF("pass");
            String pass = in.readUTF();
            Conta c = serverdb.checkuser(nome, pass);
            if (c == null) {
                out.writeInt(0);
                out.writeUTF("Conta inválida\n" + welcomemenu);
            } else {
                out.writeInt(1);
                return false;
            }
            return true;
        }
        catch (Exception e){return true;}
    }

    public static void registar(DataOutputStream out, ServerDB serverdb,DataInputStream in){
        try{
        String welcomemenu = "Bem Vindo.\n 'login' \n 'registar'\n";
        System.out.println("Efetuando registo");
        out.writeUTF("nome");
        String nome= in.readUTF();
        out.writeUTF("pass");
        String pass= in.readUTF();
        serverdb.novaConta(nome,pass);
        System.out.println("......");
        out.writeInt(0);
        out.writeUTF(welcomemenu);}
        catch (Exception e){}
    }

    public static void upload(DataOutputStream out, ServerDB serverdb,DataInputStream in){
        try{
        String path = "Insira a localização da musica";
        out.writeUTF(path);
        int size=in.readInt();
        out.writeUTF("Insira Nome da musica");
        String musicname = in.readUTF();
        out.writeUTF("Insira Autor da musica");
        String autor = in.readUTF();
        out.writeUTF("Insira Ano da musica");
        String ano = in.readUTF();
        out.writeUTF("Insira etiquetas da musica");
        ArrayList<String> etiquetas= new ArrayList<>();
        String etiqueta = in.readUTF();
        while (!etiqueta.equals("fim")){System.out.println("1");
            etiquetas.add(etiqueta);
            etiqueta = in.readUTF();
        }
        byte[] bytearray=new byte[size];
        in.readFully( bytearray);
        System.out.println("copying");
        int musicnumber = serverdb.novamusica(musicname,autor,ano,etiquetas,bytearray);
        System.out.println(bytearray.length);
        String done="Upload realizado com sucesso. ID da música: "+ musicnumber;
        out.writeUTF(done);
        System.out.println(done);}
        catch (Exception e){}
    }

    public static void download(DataOutputStream out, ServerDB serverdb,DataInputStream in){
        try{
            String path = "Insira o ID da musica";
            out.writeUTF(path);
            int id=in.readInt();
            if(id==-1){
               return;
            }
            File f = serverdb.download(id);
            byte [] array =Files.readAllBytes(f.toPath());
            out.writeInt(array.length);
            out.write(array);
            }
        catch (Exception e){}
    }

    public static void musica(DataOutputStream out, ServerDB serverdb,DataInputStream in){
        try{
            String path = "Insira o ID da musica";
            out.writeUTF(path);
            int id=in.readInt();
            if(id==-1){
                return;
            }
            out.writeUTF(serverdb.musicInfo(id));
        }
        catch (Exception e){}
    }


    public static class ClientHandler implements Runnable {
        Socket conn;
        private ServerDB serverdb;
        public ClientHandler(Socket c, ServerDB s){
            conn=c;
            serverdb=s;
        }
        public void run() {
            try {
                String welcomemenu = "Bem Vindo.\n 'login' \n 'registar'\n";
                String menu= "\nOpções:\nUpload\n";
                DataOutputStream out =new DataOutputStream(conn.getOutputStream());
                DataInputStream in = new DataInputStream( (conn.getInputStream()));
                ////////////////////////////////login && registar ///////////////////
                out.writeUTF(welcomemenu);

                Boolean logdone=true;
                while(logdone){
                    String response = in.readUTF();
                    if(response.equals("login")){
                        logdone= login(out,serverdb,in);
                    }
                    else{
                    registar(out,serverdb,in);
                    }


                }
                //////////////////////////////////////////////////////////////////////
                ///////////////////////////////////menu pós login ////////////////////

                out.writeUTF(menu);
                String r = readUTF(in);

                while (!r.equals("quit") && r.isEmpty()!=true  ) {
                    System.out.println("<"+r+">");
                    switch (r) {
                        case "upload":
                            upload(out,serverdb,in);
                            break;
                        case "download":
                            download(out,serverdb,in);
                            break;
                        case "musica":
                            musica(out,serverdb,in);
                            break;
                        default:
                            System.out.println("DEFAULT");
                            break;
                    }
                    r=readUTF(in);
                }
                out.writeUTF("Hasta");
                sleep(50);
                System.out.println("Fim da conexão");
                conn.close();
            }
            catch (Exception e){}
        }
    }
}
