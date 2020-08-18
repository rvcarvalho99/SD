package Server;



import java.util.concurrent.locks.ReentrantLock;

public class Conta
{
    private String nome;
    private String pass;
    private ReentrantLock lockConta = new ReentrantLock();

    public Conta(String n, String p)
    {
        nome=n;
        pass=p;
    }

    public boolean checkuserinfo(String n,String p){
        if(nome.equals(n) && p.equals(pass))
            return true;
            return false;
    }
    public void lock(){
        this.lockConta.lock();
    }

    public void unlock(){
        this.lockConta.unlock();
    }
}
