package input;

import entity.Estado;
import juegoplataforma.JuegoPlataforma;
import entity.Jugador;
import entity.Npc;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import juegoplataforma.EtapaJuego;
import juegoplataforma.EtapaNivel;
import juegoplataforma.Id;

/**
 * 
 * Clase que sirve de sensor de eventos de teclado del jugador.
 * <p>
 * Implementa la interface {@link java.awt.event.KeyListener}.
 * </p>
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class KeyInput implements KeyListener {
    
    
    public long timeRef=0;
    public static long combo=2;
    public boolean keys[]={false,false,false,false};
    
    /**
     * Sobreescritura del método {@link java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent) }.
     * @param e evento de teclado detectado
     */
    @Override
    public void keyTyped(KeyEvent e) {}
    /**
     * Sobreescritura del método {@link java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent) }.
     * <p>
     * Este método responde a la tecla presionada por el jugador, en caso de que esta se encuentre dentro de la 
     * lista de controles.
     * </p>
     * @param e evento de teclado detectado
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode(); 
        juegoplataforma.Handler handler = JuegoPlataforma.handler;
        Jugador jug = handler.jugador;
        switch(key){
            case KeyEvent.VK_M:
                JuegoPlataforma.musicOn=!JuegoPlataforma.musicOn;
        }
        if(JuegoPlataforma.etapa==EtapaJuego.menúPrinc){
            switch(key){
                case KeyEvent.VK_K:
                    JuegoPlataforma.etapa=EtapaJuego.menúJuego;
                    JuegoPlataforma.menúAreaX=-1;
                    JuegoPlataforma.menúAreaY=-1;
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
            }
        }else if(JuegoPlataforma.etapa==EtapaJuego.menúJuego){
            switch(key){
                case KeyEvent.VK_N:
                    if(handler.nivel!=null && !handler.seConfirmaOficialmenteUnaNuevaPartida()) return; //:v
                        handler.nuevaPartida();
                    break;
                case KeyEvent.VK_K:
                    if(handler.hayPartida()){
                        handler.backToLevel();
                        
                        JuegoPlataforma.menúAreaX=-1;
                        JuegoPlataforma.menúAreaY=-1;
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    JuegoPlataforma.etapa=EtapaJuego.menúPrinc;
                    JuegoPlataforma.menúAreaX=-1;
                    JuegoPlataforma.menúAreaY=-1;
            }
        }else if(JuegoPlataforma.etapa==EtapaJuego.ajustes){
            switch(key){
                case KeyEvent.VK_ESCAPE:
                    JuegoPlataforma.etapa=EtapaJuego.menúPrinc;
                    JuegoPlataforma.menúAreaX=-1;
                    JuegoPlataforma.menúAreaY=-1;
            }
        }else if(JuegoPlataforma.etapa==EtapaJuego.créditos){
            switch(key){
                case KeyEvent.VK_ESCAPE:
                    JuegoPlataforma.etapa=EtapaJuego.menúPrinc;
                    JuegoPlataforma.creditosIndex=0;
                    JuegoPlataforma.menúAreaX=-1;
                    JuegoPlataforma.menúAreaY=-1;
                    break;
                case KeyEvent.VK_ENTER:
                    if(++JuegoPlataforma.creditosIndex>1){
                        JuegoPlataforma.creditosIndex=0;
                    }
            }
        }else if(JuegoPlataforma.etapa==EtapaJuego.loading){
            switch(key){
                case KeyEvent.VK_ESCAPE:
                    
                    handler.nivel.jugadorPosX=jug.x;
                    handler.nivel.jugadorPosY=jug.y;
                    jug.velY=0;
                    jug.velX=0;
                    JuegoPlataforma.etapa=EtapaJuego.menúJuego;
                    JuegoPlataforma.restartMenuMusic();
                    //JuegoPlataforma.nivel=0;
                    JuegoPlataforma.pausado=false;
                    JuegoPlataforma.percent=0;
            }
        }
        else if(JuegoPlataforma.etapa==EtapaJuego.juego){
            switch(key){
                case KeyEvent.VK_P:
                    JuegoPlataforma.pausado=!(JuegoPlataforma.pausado);
            }
            if(handler.nivel.etapa==EtapaNivel.tranquiloDeLaVida){
                if(!JuegoPlataforma.pausado){
                    switch (key) {
                        case KeyEvent.VK_W:
                            //System.out.println("Ac:  "+jug.getAceleración());
                            //System.out.println("estado2: "+jug.estado);
                            if (jug.estado==Estado.onFloor) {
                                //en.moving=true;
                                jug.estado=Estado.jumping;
                                jug.aceleración = 10.0;
                                jug.agachado=false;
                            }
                            long time=(System.currentTimeMillis());
                            if (!keys[0] && (time-timeRef)>1000*combo || timeRef==0) {
                                //System.out.println("Vamos1!!");
                                keys[0]=true;
                                timeRef=System.currentTimeMillis();
                            }else if(!keys[1]){
                                //System.out.println("Vamos2!!");
                                if((time-timeRef)<1000*combo){
                                    keys[1]=true;
                                }
                                
                            }else restart();
                            timeRef=System.currentTimeMillis();
                            break;
                        case KeyEvent.VK_S:
                            if(jug.estado==Estado.jumping) jug.aceleración=0.0;
                            //else if(en.estado==Estado.onFloor)
                            //jug.agachado=!jug.agachado;
                            jug.agachado=true;
                            //en.moving=false;
                            if(jug.estado==Estado.onFloor) jug.setVelX(0);
                            if (keys[1] && !keys[2] && (System.currentTimeMillis()-timeRef)<1000*combo) {
                                //System.out.println("Vamos3!!");
                                keys[2]=true;
                                
                            }else restart();
                            timeRef=System.currentTimeMillis();
                            break;
                        case KeyEvent.VK_A:
                            //en.moving=true;
                            jug.setVelX(-5);
                            jug.agachado=false;
                            jug.facing = 0;
                            jug.setRandomSprite(26,37,85);
                            if (keys[2] && !keys[3] && (System.currentTimeMillis()-timeRef)<1000*combo) {
                                //System.out.println("Vamos4!!");
                                keys[3]=true;
                            }else restart();
                            timeRef=System.currentTimeMillis();
                            break;
                        case KeyEvent.VK_D:
                            //en.moving=true;
                            jug.setVelX(5);
                            jug.agachado=false;
                            jug.facing = 1;
                            jug.setRandomSprite(0,52,61);
                            break;
                        case KeyEvent.VK_H:
                            handler.nextLevel();
                            break;
                        case KeyEvent.VK_ENTER:
                            //jug.enRango(300, JuegoPlataforma.getCurrentLevel());
                            
                            if(jug.npcTalk!=null){
                                int f=0, s=26;
                                Npc n=jug.npcTalk;
                                if(n.x<jug.x){f=1; s=61;}
                                n.facing=f; n.spriteTemp=s;
                                if (n.haveAvailableConversations()) {
                                    n.setTalking(true);
                                    handler.nivel.etapa=EtapaNivel.conversación;
                                    jug.setVelX(0);
                                }
                            }
                            break;
                        case KeyEvent.VK_ESCAPE:
                            handler.nivel.jugadorPosX=jug.x;
                            handler.nivel.jugadorPosY=jug.y;
                            jug.velY=0;
                            jug.velX=0;
                            JuegoPlataforma.etapa=EtapaJuego.menúJuego;
                            JuegoPlataforma.restartMenuMusic();
                            //JuegoPlataforma.nivel=0;
                            JuegoPlataforma.pausado=false;
                            break;
                        case KeyEvent.VK_SPACE:
                            if (keys[3] && (System.currentTimeMillis()-timeRef)<1000*combo && JuegoPlataforma.nivel!=5) {
                                jug.hack=!jug.hack;
                                String s;
                                if(jug.hack)
                                    s="Hack desbloqueado: Volar";
                                else
                                    s="Hack desactivado: Volar";
                                javax.swing.JOptionPane.showMessageDialog(null,s);
                            }else restart();
                            timeRef=System.currentTimeMillis();
                            break;
                        case KeyEvent.VK_B:
                            for(objeto.Objeto o: handler.nivel.objetos)
                                if(o.id==Id.diamante)
                                    ((objeto.Diamante)o).setRecogido(true, jug);
                            
                            handler.nivel.actualizarNivel(null);
                            break;
                        default:
                            restart();
                            timeRef=System.currentTimeMillis();
                            //System.out.println(e);
                    }
                }else{
                    switch(key){
                        case KeyEvent.VK_ESCAPE:
                            JuegoPlataforma.pausado=false;
                    }
                }
            }else if(handler.nivel.etapa==EtapaNivel.cinematica){
                if(key==KeyEvent.VK_ENTER){
                    if (handler.nivel.cinematica.isAble()) {
                        handler.nivel.nextScene();
                    }
                }else if(key==KeyEvent.VK_ESCAPE){
                    handler.nivel.playLevel();
                }
            }else if(handler.nivel.etapa==EtapaNivel.conversación){
                if(!JuegoPlataforma.pausado){
                    Npc npc=jug.npcTalk;
                    interaction.Conversación con=npc.cours;
                    switch(key){
                        case KeyEvent.VK_ENTER:
                            if (npc.isTalking()) {
                                if(con.getDiálogo(con.dialogo).escribiendo){
                                    con.getDiálogo(con.dialogo).finish();
                                }else{
                                    npc.nextConversación();
                                }
                            }
                        break;
                        case KeyEvent.VK_ESCAPE:
                            handler.nivel.etapa=EtapaNivel.tranquiloDeLaVida;
                            npc.cours=null;
                            npc.setTalking(false);
                    }
                }else{
                    switch(key){
                        case KeyEvent.VK_ESCAPE:
                            JuegoPlataforma.pausado=false;
                    }
                }
            }
        }
    }
    /**
     * Sobreescritura del método {@link java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent) }.
     * <p>
     * Este método responde a la tecla liberada por el jugador, en caso de que esta se encuentre dentro de la 
     * lista de controles.
     * </p>
     * @param e evento de teclado detectado
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        Jugador jug = JuegoPlataforma.handler.jugador;
        //en.moving=false;
        if(JuegoPlataforma.etapa==EtapaJuego.juego){
            switch (key) {
                case KeyEvent.VK_W:
                    //jug.setVelY(0);
                    if((jug.estado==Estado.jumping || jug.estado==Estado.falling && jug.hack) 
                            && (jug.aceleración>5 || jug.hack)){
                        
                        jug.estado=Estado.jumping;
                        jug.aceleración=5.0;
                    }
                    break;
                case KeyEvent.VK_S:
                    jug.agachado=false;
                    jug.setVelY(0);
                    break;
                case KeyEvent.VK_A:
                    jug.setVelX(0);
                    break;
                case KeyEvent.VK_D:
                    jug.setVelX(0);
            }
        }
    }

    /**
     * Resetea el Truco, Lol
     * 
     */
    private void restart() {
        //System.out.println("UPS! Jeje");
        for (int i = 0; i < 4; i++) {
            keys[i]=false;
        }
        
    }
}