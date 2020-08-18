package Client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.io.DataInputStream.readUTF;
import static java.lang.Thread.sleep;


public class Client {

    public static void upload(DataInputStream in, DataOutputStream out, BufferedReader inUser, String r, byte[] b){
        try{
            out.writeUTF(r);
            r=readUTF(in);
            System.out.println(r);
            r=inUser.readLine();

            byte [] array = Files.readAllBytes(Paths.get("C:\\Users\\rvcar\\Downloads\\a.mp3"));

            out.writeInt(array.length);
            int i=0;
            while(!r.equals("fim") || i<5 ) {
                i++;
                if(i<5){
                r = readUTF(in);
                System.out.println(r);}
                r = inUser.readLine();
                out.writeUTF(r);
            }

            out.write(array);

            r= readUTF(in);
            System.out.println(r);
        }
        catch (Exception e){System.out.println("Caminho inválido");}
    }

    public static void download(DataInputStream in, DataOutputStream out, BufferedReader inUser, String r, byte[] b){
        try{
        out.writeUTF(r);
        r=readUTF(in);
        System.out.println(r);
        r=inUser.readLine();
        out.writeInt(Integer.parseInt(r));
        int size = in.readInt();
        byte [] array = new byte[size];
        in.readFully(array);
        System.out.println("Nome para a musica?");
        r= inUser.readLine();
            File n = new File("C:\\\\Users\\\\rvcar\\\\OneDrive\\\\Desktop\\\\"+r+".mp3");
            FileOutputStream fos = new FileOutputStream(n);
            fos.write(array);
            fos.close();
        }
        catch (Exception e){try{out.writeInt(-1);}catch(Exception es){}}
    }

    public static void musica(DataInputStream in, DataOutputStream out, BufferedReader inUser, String r, byte[] b) {
        try {
            out.writeUTF(r);
            r = readUTF(in);
            System.out.println(r);
            r = inUser.readLine();
            out.writeInt(Integer.parseInt(r));
            System.out.println(in.readUTF());
        } catch (Exception e){try{out.writeInt(-1);}catch(Exception es){}}
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        DataInputStream in = new DataInputStream((socket.getInputStream()));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        BufferedReader inUser = new BufferedReader(new InputStreamReader(System.in));
        byte[] b = new byte [2000];
        String r;
        r=readUTF(in);
        System.out.println(r);
        boolean logdone=true;
        while(logdone){
            String request= inUser.readLine();
            if(request.equals("login") || request.equals("registar")) {
                out.writeUTF(request);
                r=readUTF(in);
                System.out.println(r);
                out.writeUTF(inUser.readLine());
                r=readUTF(in);
                System.out.println(r);
                out.writeUTF(inUser.readLine());
            }
            int v= in.readInt();
            if(v==0) {
            System.out.println(readUTF(in));
            }
            else logdone=false;

        }

        r=readUTF(in);
        System.out.println(r);
        while ((r = inUser.readLine()) != null && !r.equals("quit")){
             switch (r) {
                case "upload":
                    upload(in,out,inUser,r,b);
                    break;
                case "download":
                    download(in,out,inUser,r,b);
                case "musica":
                    musica(in,out,inUser,r,b);
            }
        }

        out.writeUTF(r);
        System.out.println(in.readUTF());
        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();
    }

}

