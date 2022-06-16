package juegoplataforma;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Cinematica {
    public ArrayList<BufferedImage> imgs;
    public BufferedImage transparencia;
    public BufferedImage curso;
    static final int DELAY_INIT=90;
    public int delay,img;
    boolean estado;
    
    public Cinematica(File file){
        imgs=new ArrayList();
        delay=DELAY_INIT;
        img=0;
        estado=true;
        System.out.println(file.getAbsolutePath());
        try {
            transparencia=ImageIO.read(new File("src\\res\\Fondos\\transparencia.png"));
            if(file.exists())
                for (File img : file.listFiles()) {
                    BufferedImage imag=ImageIO.read(img);
                    imgs.add(imag);
                }
            if(!imgs.isEmpty()) curso=imgs.get(0);
        } catch (IOException ex) {
            Logger.getLogger(Cinematica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    synchronized boolean render(Graphics g){
        //System.out.println("Entro");
        if(curso==null) return false;
        //System.out.println("Entro");
        g.drawImage(curso, 0, 0, JuegoPlataforma.getFrameWidth()+60, JuegoPlataforma.getFrameHeight()+60, null);
        
        for (int i = 0; i < delay/2; i++) {
            g.drawImage(transparencia, 0, 0, JuegoPlataforma.getFrameWidth()+60, JuegoPlataforma.getFrameHeight()+60, null);
        }
        
        return true;
    }
    
    synchronized void tick(){
        if(estado){if(delay>0)delay--;}
        else if(delay<DELAY_INIT)delay++;
        else avanzar();
    }
    
    public void continuar(){
        estado=false;
    }
    
    private void avanzar(){
        if (++img<imgs.size()) {
            estado=true;
            curso=imgs.get(img);
        }else curso=null;
    }
    
    public boolean isAble(){
        return delay==0;
    }

    void recharge() {
        delay=DELAY_INIT;
        img=0;
        estado=true;
        if (imgs.isEmpty())curso=null;else curso=imgs.get(0);
    }
    
}