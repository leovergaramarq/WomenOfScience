package juegoplataforma;

import entity.Entidad;
import entity.Jugador;
import entity.Npc;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import objeto.*;

/**
 * 
 * Clase manejadora de los niveles y del personaje principal.
 * @see java.awt.Canvas
 * @see javax.swing.JFrame
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class Handler {
    public Nivel nivel;
    public Jugador jugador;
    //public Npc[] npc;
    /**
     * Inicializa al personaje principal y a los NPCs.
     */
    public void setEntidades(){
        jugador=new Jugador(JuegoPlataforma.playerModel, 64, 64);
        /*
        npc=new Npc[5];
        npc[0]=new Npc(JuegoPlataforma.entityModels[0], 64, 64, false,Id.aventurero);
        npc[1]=new Npc(JuegoPlataforma.entityModels[1], 64, 64, false,Id.aventurera);
        npc[2]=new Npc(JuegoPlataforma.entityModels[2], 64, 64, false,Id.man);
        npc[3]=new Npc(JuegoPlataforma.entityModels[3], 64, 64, false,Id.zombie);
        npc[4]=new Npc(JuegoPlataforma.entityModels[4], 64, 64, false,Id.robot);
        */
    }
    /**
     * Calcula la distancia en pixeles entre cualesquiera dos entidades.
     * @param en1 entidad 1
     * @param en2 entidad 2
     * @return distancia entre las dos entidades.
     */
    public double distanciaEntre(Entidad en1, Entidad en2){
        return Math.sqrt((en1.x-en2.x)*(en1.x-en2.x)+(en1.y-en2.y)*(en1.y-en2.y));
    }
    /**
     * Lleva a cabo la renderización de los niveles y del personaje principal.
     * <p>
     * Invoca al método {@link entity.Jugador#render(java.awt.Graphics)} y a 
     * {@link Nivel#render(java.awt.Graphics, java.awt.Graphics) } para llevar a cabo la renderización.
     * </p>
     * @param g gráficos empleados para dibujar el nivel actual del juego
     * @param g2 gráficos empleados para dibujar elementos tales como información
     * @see JuegoPlataforma#getCurrentLevel() 
     * 
     */
    public synchronized void render(Graphics g,Graphics g2) {
        if(JuegoPlataforma.nivel>0){
            nivel.render(g, g2);
            if(nivel.etapa!=EtapaNivel.cinematica)jugador.render(g);
        }
    }
    /**
     * Produce el movimiento del personaje principal y de los elementos del nivel actual.
     * <p>
     * Llama al método {@link entity.Jugador#tick() } para el movimiento del personaje principal, 
     * y a {@link Nivel#tick() } para generar el movimiento de los elementos pertenecientes al nivel actual 
     * (NPCs, objetos, etc.).
     * </p>
     * 
     */
    public synchronized void tick() {
        if(JuegoPlataforma.etapa==EtapaJuego.juego){
            nivel.tick();
            if(nivel.etapa!=EtapaNivel.cinematica)jugador.tick();
        }
    }
    /**
     * Añade un nuevo nivel a la lista enlazada, con todos los elementos que este poseerá:
     * los objetos, las entidades y la ubicación inicial del 
     * personaje principal.
     * <p>
     * Recorre el mapeo, obteniendo el código RGB de cada 10 pixeles para, posteriormente, crear el elemento requerido de 
     * acuerdo a dicho código. Esta información es almacenada en el nuevo nivel.
     * </p>
     * @param level mapeo del nivel. Cada color representa un tipo de entidad u objeto.
     * @see java.awt.image.BufferedImage#getRGB(int, int) 
     */
    public void createLevel(BufferedImage level) {
        nivel=new Nivel();
        
        JuegoPlataforma.etapa=EtapaJuego.loading;
        
        //for(Npc x: npc) x.setVisible(false);
        
        int width = level.getWidth();
        int height = level.getHeight();
        int paso = 10;
        
        for (int y = 0; y < height; y+=paso) {
            for (int x = 0; x < width; x+=paso) {
                
                int pixel = level.getRGB(x, y);
                
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                
                if (red == 0 && green == 0 && blue == 0){//Muro
                    nivel.addObjeto(new MixWall(x/paso * 64, y/paso * 64, JuegoPlataforma.muro.getWidth(),
                            JuegoPlataforma.muro.getHeight(), true, Id.muro, JuegoPlataforma.muro));
                }
                else if (red == 127 && green == 127 && blue == 127){//Concreto
                    nivel.addObjeto(new MixWall(x/paso * 64, y/paso * 64, JuegoPlataforma.concreto.getWidth(),
                            JuegoPlataforma.concreto.getHeight(), true, Id.concreto, JuegoPlataforma.concreto));
                }
                else if (red == 100 && green == 100 && blue == 100){//ConcretoPasto
                    nivel.addObjeto(new MixWall(x/paso * 64, y/paso * 64, JuegoPlataforma.concretoPasto.getWidth(),
                            JuegoPlataforma.concretoPasto.getHeight(), true, Id.concretoPasto, JuegoPlataforma.concretoPasto));
                }
                else if (red == 136 && green == 0 && blue == 21){//Madera
                    nivel.addObjeto(new MixWall(x/paso * 64, y/paso * 64, JuegoPlataforma.madera.getWidth(),
                            JuegoPlataforma.madera.getHeight(), true, Id.madera, JuegoPlataforma.madera));
                }
                else if (red==192 && green==137 && blue==7){//Jugador
                    jugador.setX(x/paso*64);
                    jugador.setY(y/paso*64);
                    jugador.setVelY(0);
                    jugador.setVelX(0);
                    jugador.agachado=true;
                    //jugador.moving=false;
                    int f=0, s=26;
                    if(jugador.getX()<JuegoPlataforma.getFrameWidth()/2){f=1; s=61;}
                    jugador.facing=f;
                    jugador.spriteTemp=s;
                    switch(JuegoPlataforma.nivel){
                        case 5: jugador.hack=true; break;
                        case 6: jugador.hack=false;
                    }
                }
                else if(red==0 && green==162 && blue==232){//El npc Aventurero
                    Npc tnpc=new Npc(JuegoPlataforma.entityModels[0],64,64,false, Id.aventurero);
                    nivel.addNpc(tnpc);
                    
                    tnpc.setVisible(true);
                    tnpc.setX(x/paso*64);
                    tnpc.setY(y/paso*64);
                    
                    int f=0, s=26;
                    if(tnpc.x<jugador.x){f=1; s=61;}
                    tnpc.facing=f;
                    tnpc.spriteTemp=s;
                    
                    String dir="src\\res\\Niveles\\Nivel_"+JuegoPlataforma.nivel+"\\dialogos\\male_adventurer";
                    File fl=new File(dir);
                    
                    if(fl.exists()){
                        File files[]=fl.listFiles();
                        for (File file : files) {
                            try {
                                tnpc.addConversacion(file);
                            } catch (IOException ex) {
                                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                else if(red==255 && green==201 && blue==14){//El npc Aventurera
                    Npc tnpc=new Npc(JuegoPlataforma.entityModels[1],64,64,JuegoPlataforma.nivel==4,Id.aventurera);
                    nivel.addNpc(tnpc);
                    
                    tnpc.setVisible(true);
                    tnpc.setX(x/paso*64);
                    tnpc.setY(y/paso*64);
                    
                    int f=0, s=26;
                    if(tnpc.x<jugador.x){f=1; s=61;}
                    tnpc.facing=f;
                    tnpc.spriteTemp=s;
                    
                    String dir="src\\res\\Niveles\\Nivel_"+JuegoPlataforma.nivel+"\\dialogos\\female_adventurer";
                    File fl=new File(dir);
                    
                    if(fl.exists()){
                        File files[]=fl.listFiles();
                        for (File file : files) {
                            try {
                                tnpc.addConversacion(file);
                            } catch (IOException ex) {
                                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                else if(red==95 && green==67 && blue==3){//El npc man
                    Npc tnpc=new Npc(JuegoPlataforma.entityModels[2],64,64,false,Id.man);
                    nivel.addNpc(tnpc);
                    
                    tnpc.setVisible(true);
                    tnpc.setX(x/paso*64);
                    tnpc.setY(y/paso*64);
                    
                    int f=0, s=26;
                    if(tnpc.x<jugador.x){f=1; s=61;}
                    tnpc.facing=f;
                    tnpc.spriteTemp=s;
                    
                    String dir="src\\res\\Niveles\\Nivel_"+JuegoPlataforma.nivel+"\\dialogos\\man";
                    File fl=new File(dir);
                    if(fl.exists()){
                        File files[]=fl.listFiles();
                        for (File file : files) {
                            try {
                                tnpc.addConversacion(file);
                            } catch (IOException ex) {
                                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                else if(red==30 && green==153 && blue==67){//El npc Zombie
                    Npc tnpc=new Npc(JuegoPlataforma.entityModels[3],64,64,false,Id.zombie);
                    nivel.addNpc(tnpc);
                    
                    tnpc.setVisible(true);
                    tnpc.setX(x/paso*64);
                    tnpc.setY(y/paso*64);
                    
                    int f=0, s=26;
                    if(tnpc.x<jugador.x){f=1; s=61;}
                    tnpc.facing=f;
                    tnpc.spriteTemp=s;
                    
                    String dir="src\\res\\Niveles\\Nivel_"+JuegoPlataforma.nivel+"\\dialogos\\zombie";
                    File fl=new File(dir);
                    
                    if(fl.exists()){
                        File files[]=fl.listFiles();
                        for (File file : files) {
                            try {
                                tnpc.addConversacion(file);
                            } catch (IOException ex) {
                                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                else if(red==128 && green==0 && blue==255){//El npc Robot
                    Npc tnpc=new Npc(JuegoPlataforma.entityModels[4],64,64,false,Id.robot);
                    nivel.addNpc(tnpc);
                    
                    tnpc.setVisible(true);
                    tnpc.setX(x/paso*64);
                    tnpc.setY(y/paso*64);
                    
                    int f=0, s=26;
                    if(tnpc.x<jugador.x){f=1; s=61;}
                    tnpc.facing=f;
                    tnpc.spriteTemp=s;
                    
                    String dir="src\\res\\Niveles\\Nivel_"+JuegoPlataforma.nivel+"\\dialogos\\robot";
                    File fl=new File(dir);
                    
                    if(fl.exists()){
                        File files[]=fl.listFiles();
                        for (File file : files) {
                            try {
                                tnpc.addConversacion(file);
                                
                            } catch (IOException ex) {
                                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                else if(red==46 && green==46 && blue==46){//El npc Genérico
                    Npc tnpc=new Npc(JuegoPlataforma.entityModels[5],64,64,false,Id.NPCgenérico);
                    nivel.addNpc(tnpc);
                    
                    tnpc.setVisible(true);
                    tnpc.setX(x/paso*64);
                    tnpc.setY(y/paso*64);
                    
                    tnpc.setRandomFacing();
                    tnpc.spriteTemp= tnpc.facing==0? 33:58;
                }
                else if (red == 255 && green == 0 && blue == 0){//Puerta Cerrada
                    Puerta p=new Puerta(x/paso * 64, y/paso * 64, JuegoPlataforma.puertaRed.getWidth(),
                            JuegoPlataforma.puertaRed.getHeight(), true, Id.puerta, JuegoPlataforma.puertaRed,
                            JuegoPlataforma.puertaGreen, true);
                    nivel.addPuerta(p); nivel.addObjeto(p);
                    
                    Llave l=new Llave(0, 0, JuegoPlataforma.llave.getWidth(),
                        JuegoPlataforma.llave.getHeight(), false, Id.llave, JuegoPlataforma.llave, p);
                    
                    nivel.addObjeto(l);
                    if(JuegoPlataforma.nivel==6){
                        l.setRecogido(true);
                        jugador.tieneLlave=true;
                        p.setLocked(false);
                        p.setSolid(false);
                        l.setDisponible(false);
                    }
                }
                else if (red == 0 && green == 255 && blue == 43){//Diamante
                    nivel.addObjeto(new Diamante(x/paso * 64, y/paso * 64, JuegoPlataforma.diamante.getWidth(),
                            JuegoPlataforma.diamante.getHeight(), false, Id.diamante, JuegoPlataforma.diamante));
                }
                else if(red==200 && green==191 && blue==231){//Casa
                    nivel.addObjeto(new Construcción(x/paso * 64, y/paso * 64, JuegoPlataforma.casa.getWidth(),
                            JuegoPlataforma.casa.getHeight(), false, Id.casa, JuegoPlataforma.casa));
                }
                else if(red==150 && green==191 && blue==255){//Casa1
                    nivel.addObjeto(new Construcción(x/paso * 64, y/paso * 64, JuegoPlataforma.casa1.getWidth(),
                            JuegoPlataforma.casa1.getHeight(), false, Id.casa1, JuegoPlataforma.casa1));
                }
                else if(red==19 && green==79 && blue==94){//Casa2
                    nivel.addObjeto(new Construcción(x/paso * 64, y/paso * 64, JuegoPlataforma.casa2.getWidth(),
                            JuegoPlataforma.casa2.getHeight(), false, Id.casa2, JuegoPlataforma.casa2));
                }
                else if(red==112 && green==146 && blue==190){//Edificio
                    nivel.addObjeto(new Construcción(x/paso * 64, y/paso * 64, JuegoPlataforma.edificio.getWidth(),
                            JuegoPlataforma.edificio.getHeight(), false, Id.edificio, JuegoPlataforma.edificio));
                }
                else if(red==153 && green==217 && blue==234){//Iglesia
                    nivel.addObjeto(new Construcción(x/paso * 64, y/paso * 64, JuegoPlataforma.iglesia.getWidth(),
                            JuegoPlataforma.iglesia.getHeight(), false, Id.iglesia, JuegoPlataforma.iglesia));
                }
                else if (red == 239 && green == 228 && blue == 176){//Madera2doPiso
                    nivel.addObjeto(new Construcción(x/paso * 64, y/paso * 64, JuegoPlataforma.madera2doPiso.getWidth(),
                            JuegoPlataforma.madera2doPiso.getHeight(), false, Id.madera2doPiso, JuegoPlataforma.madera2doPiso));
                }
                else if (red == 255 && green == 174 && blue == 200){//fondoNubesDía
                    int w=JuegoPlataforma.nivelesMapeo[JuegoPlataforma.nivel-1].getWidth()/10*64,
                        h=JuegoPlataforma.nivelesMapeo[JuegoPlataforma.nivel-1].getHeight()/10*64;
                    nivel.addObjeto(new Construcción(x/paso * 64, y/paso * 64, w, h, false, Id.fondoNubes,
                            JuegoPlataforma.fondoNubes[0]));
                }
                else if (red == 255 && green == 174 && blue == 201){//fondoNubesTarde
                    int w=JuegoPlataforma.nivelesMapeo[JuegoPlataforma.nivel-1].getWidth()/10*64,
                        h=JuegoPlataforma.nivelesMapeo[JuegoPlataforma.nivel-1].getHeight()/10*64;
                    nivel.addObjeto(new Construcción(x/paso * 64, y/paso * 64, w, h, false, Id.fondoNubes,
                            JuegoPlataforma.fondoNubes[1]));
                }
                else if (red == 255 && green == 174 && blue == 202){//fondoNubesTarde
                    int w=JuegoPlataforma.nivelesMapeo[JuegoPlataforma.nivel-1].getWidth()/paso*64,
                        h=JuegoPlataforma.nivelesMapeo[JuegoPlataforma.nivel-1].getHeight()/paso*64;
                    nivel.addObjeto(new Construcción(x/paso * 64, y/paso * 64, w, h, false, Id.fondoNubes,
                            JuegoPlataforma.fondoNubes[2]));
                }
                else if (red == 150 && green == 127 && blue == 127) {//paredX
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.paredX.getWidth(),
                            JuegoPlataforma.paredX.getHeight(), true, Id.paredX, JuegoPlataforma.paredX));
                }
                else if (red == 54 && green == 54 && blue == 54) {//paredY
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.paredY.getWidth(),
                            JuegoPlataforma.paredY.getHeight(), true, Id.paredY, JuegoPlataforma.paredY));
                }
                else if (red == 163 && green == 73 && blue == 164) {//mesa
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.mesa.getWidth(),
                            JuegoPlataforma.mesa.getHeight(), true, Id.oficinas, JuegoPlataforma.mesa));
                }
                else if (red == 255 && green == 0 && blue == 128) {//flechaDerecha
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.flechaDerecha.getWidth(),
                            JuegoPlataforma.flechaDerecha.getHeight(), false, Id.flechaDerecha, JuegoPlataforma.flechaDerecha));

                }
                else if (red == 181 && green == 230 && blue == 29) {//pantallaPlasma
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.pantallaPlasma.getWidth(),
                            JuegoPlataforma.pantallaPlasma.getHeight(), false, Id.pantallaPlasma, JuegoPlataforma.pantallaPlasma));
                }
                else if (red == 255 && green == 255 && blue == 128) {//concretoOndulado
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.concretoOndulado.getWidth(),
                            JuegoPlataforma.concretoOndulado.getHeight(), true, Id.concretoOndulado, JuegoPlataforma.concretoOndulado));
                }
                else if (red == 151 && green == 0 && blue == 151) {//concretoPiedra
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.concretoPiedra.getWidth(),
                            JuegoPlataforma.concretoPiedra.getHeight(), true, Id.concretoPiedra, JuegoPlataforma.concretoPiedra));
                }
                else if (red == 255 && green == 128 && blue == 0) {//computadoraBotones
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.computadoraBotones.getWidth(),
                            JuegoPlataforma.computadoraBotones.getHeight(), false, Id.computadoraBotones, JuegoPlataforma.computadoraBotones));
                }
                else if (red == 28 && green == 123 && blue == 112) {//izqIBM
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.izqIBM.getWidth(),
                            JuegoPlataforma.izqIBM.getHeight(), false, Id.izqIBM, JuegoPlataforma.izqIBM));
                }
                else if (red == 15 && green == 64 && blue == 57) {//drIBM
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.drIBM.getWidth(),
                            JuegoPlataforma.drIBM.getHeight(), false, Id.drIBM, JuegoPlataforma.drIBM));
                }
                else if (red == 211 && green == 220 && blue == 163) {//flechaArriba
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.flechaArriba.getWidth(),
                            JuegoPlataforma.flechaArriba.getHeight(), false, Id.flechaArriba, JuegoPlataforma.flechaArriba));
                }
                else if (red == 202 && green == 201 && blue == 181) {//flechaIzquierda
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.flechaArriba.getWidth(),
                            JuegoPlataforma.flechaIzquierda.getHeight(), false, Id.flechaIzquierda, JuegoPlataforma.flechaIzquierda));
                }
                else if (red == 20 && green == 131 && blue == 45) {//yerba
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.yerba.getWidth(),
                            JuegoPlataforma.yerba.getHeight(), false, Id.yerba, JuegoPlataforma.yerba));
                }
                else if (red == 113 && green == 53 && blue == 38) {//tierra
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.tierra.getWidth(),
                            JuegoPlataforma.tierra.getHeight(), true, Id.tierra, JuegoPlataforma.tierra));
                }
                else if (red == 150 && green == 117 && blue == 1) {//valla
                    nivel.addObjeto(new MixWall(x / paso * 64, y / paso * 64, JuegoPlataforma.valla.getWidth(),
                            JuegoPlataforma.valla.getHeight(), true, Id.valla, JuegoPlataforma.valla));
                }
            }
        }
        JuegoPlataforma.pausado=false;
        JuegoPlataforma.restartMusic();
    }
    /**
     * Cambia el nivel actual del jugador a su respectivo siguiente. Si no exista uun siguiente nivel, este será creado, 
     * invocando al método {@link #createLevel(java.awt.image.BufferedImage) }. En caso de que sí exista, al jugador se le 
     * asignará la ubicación en la cual dejó el nivel por última vez.
     * 
     */
    public synchronized void nextLevel(){
        if(++JuegoPlataforma.nivel<=JuegoPlataforma.nivelesMapeo.length){
            //JuegoPlataforma.nivel++;
            JuegoPlataforma.musicJuego.stop();
            JuegoPlataforma.etapa=EtapaJuego.loading;
            createLevel(JuegoPlataforma.nivelesMapeo[JuegoPlataforma.nivel-1]);
        }else{
            JuegoPlataforma.musicJuego.stop();
            JuegoPlataforma.etapa=EtapaJuego.loading;
            JuegoPlataforma.nivel=0;
        }
    }
    /**
     * Regresa al último nivel de la partida en curso
     */
    public synchronized void backToLevel(){
        JuegoPlataforma.etapa=EtapaJuego.juego;
        JuegoPlataforma.cam.tick(jugador);
        JuegoPlataforma.pausado=true;
        
        int posX=nivel.jugadorPosX,
        posY=nivel.jugadorPosY, f=0, s=26;
        if(posX<JuegoPlataforma.getFrameWidth()/2){f=1; s=61;}
        jugador.setX(posX);
        jugador.setY(posY);
        jugador.facing=f;
        jugador.spriteTemp=s;

        //jugador.agachado=true;
        //moving=false;
        JuegoPlataforma.restartMusic();
    }
    /**
     * Limpia la lista de niveles e inicia el juego desde el primer nivel invocando al método 
     * {@link #createLevel(java.awt.image.BufferedImage) }.
     * @see java.util.LinkedList#removeAll(java.util.Collection) 
     */
    public synchronized void nuevaPartida(){
        JuegoPlataforma.etapa=EtapaJuego.loading;
        JuegoPlataforma.menúAreaX=-1;
        JuegoPlataforma.menúAreaY=-1;
        JuegoPlataforma.nivel=1;
        jugador.numDiamantes=0;
        createLevel(JuegoPlataforma.nivelesMapeo[0]);
    }
    /**
     * Pide confirmación por parte del usuario para iniciar una nueva partida y perder todo el progreso actual.
     * @return true si usuario jugador acepta iniciar una nueva partida.
     */
    public boolean seConfirmaOficialmenteUnaNuevaPartida(){
        String m1, m2, op[]=new String[2];
        switch(JuegoPlataforma.langIndex){
            case 0:
                m1="¿Desea iniciar una partida desde cero?\n\nPerderá todo el progreso alcanzado.";
                m2="Confirmar"; op[0]="Sí"; op[1]="No";
                break;
            case 1:
                m1="Do you want to start a game from scratch?\n\nYou will lose all progress made.";
                m2="Confirm"; op[0]="Yes"; op[1]="No";
                break;
            default:
                m1="Voulez-vous démarrer un jeu à partir de zéro?\n\nVous perdrez tous les progrès réalisés.";
                m2="Confirmer"; op[0]="Oui"; op[1]="Non";
        }

        return JOptionPane.showOptionDialog
            (null,m1,m2,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,op,null)==0;
    }
    /**
     * Establece un cuadro de diálogo en caso de que no haya una partida para continuar.
     * @return false si no hay niveles.
     */
    public boolean hayPartida(){
        if(nivel==null){
            String m1, m2, op[]=new String[1];
            switch(JuegoPlataforma.langIndex){
                case 0:
                    javax.swing.JOptionPane.showMessageDialog(null, "Actualmente, no existe una partida.", "Error", 0);
                    break;
                case 1:
                    m1="Currently, there is no game."; m2="Wrong"; op[0]="Okay";
                    javax.swing.JOptionPane.showOptionDialog
                        (null, m1, m2, JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE,null,op,null);
                    break;
                default:
                    m1="Actuellement, il n'y a pas de jeu."; m2="Erreur"; op[0]="Accepter";
                    javax.swing.JOptionPane.showOptionDialog
                        (null, m1, m2, JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE,null,op,null);
            }
            return false;
        }
        return true;
    }
}