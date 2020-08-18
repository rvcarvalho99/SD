package Server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Musica {
    private String titulo;
    private String autor;
    private String ano;
    private ArrayList <String> etiquetas= new ArrayList<String>();
    private ReentrantLock lock = new ReentrantLock();
    private int id;
    private int descargas=0;

    public Musica(String t,String au, String an,ArrayList<String> et,byte[] bytearray,int i){
        try{
        File n = new File(t+".mp3");
        FileOutputStream fos = new FileOutputStream(n);
        fos.write(bytearray);
        fos.close();
        titulo = t;
        autor = au ;
        ano = an;
        etiquetas=et;
        id=i;
        }
        catch (Exception e){}
    }

    public  int getId(){return id;}

    public String musicInfo(){
        String info = "Título: "+ titulo + "\n Autor: "+ autor + "\n Ano: " + ano +"\n Etiquetas: ";
        String eti = etiquetas.get(0);
        int i=1;
        while(etiquetas.size()>i){
            eti=eti + "," + etiquetas.get(i);
            i++;
        }
        info=info+eti+"\n Descargas: " + descargas;
        System.out.println("->" + info);
        return info;
    }

    public File download(){
        lock.lock();
        descargas++;
        lock.unlock();
      return new File(titulo+".mp3");
    }
}
