package Server;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ServerDB {
    private ArrayList<Conta> contas = new ArrayList<Conta>();
    private ArrayList<Musica> musicas = new ArrayList<Musica>();
    private ReentrantLock lock = new ReentrantLock();

    private Condition contascondition;
    private Condition musicascondition;

    public ServerDB(){
        contascondition = lock.newCondition();
        musicascondition = lock.newCondition();
    }

    public void novaConta(String nome,String pass){

        Conta c = new Conta(nome,pass);
        lock.lock();
        contas.add(c);
        lock.unlock();
    }

    public int novamusica(String t,String au, String an,ArrayList<String> et,byte[] bytearray){
        lock.lock();
        musicas.size();
        Musica c = new Musica(t,au,an,et,bytearray,musicas.size()+1);

        musicas.add(c);
        lock.unlock();
        return c.getId();
    }

    public String musicInfo(int x){
        Musica m = musicas.get(x-1);
        return m.musicInfo();
    }

    public File download(int x){
        Musica m = musicas.get(x-1);
        return m.download();
    }

    public Conta checkuser(String nome, String pass){
        lock.lock();
        for(Conta c : contas)
        {
            if(c.checkuserinfo(nome,pass)){
                lock.unlock();
                return c;
            }
        }
        lock.unlock();
        return null;
    }
}
