package juegoplataforma;
import entity.Entidad;
import entity.*;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import objeto.Objeto;

/**
 * 
 * Clase que representa los niveles del juego. Manejada a los NPCs y a los objetos.
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class Nivel {
    public LinkedList<Entidad> entidades;
    public LinkedList<Objeto> objetos;
    public ArrayList<Npc> npcs;
    public EtapaNivel etapa;
    public int jugadorPosX, jugadorPosY;
    //Nuevos
    public Cinematica cinematica;
    public ArrayList<objeto.Puerta> puertas;
    public int numDiamantes;
    /**
     * Crea un nuevo nivel con sus listas de NPCs, objetos y entidades en general.
     */
    public Nivel(){
        npcs=new ArrayList();
        entidades=new LinkedList();
        objetos=new LinkedList();
        etapa=EtapaNivel.cinematica;
        cinematica=new Cinematica(new File("src\\res\\Niveles\\Nivel_"+JuegoPlataforma.nivel+"\\Cinematica"));
        puertas=new ArrayList();
        initConfig();
        //Nuevos
        numDiamantes=0;
    }
    /**
     * Lleva a cabo la renderización de las entidades en general, NPCs en específico y de los objetos de este nivel.
     * <p>
     * Invoca a los métodos {@link entity.Entidad#render(java.awt.Graphics) } para las entidades, a 
     * {@link entity.Npc#render(java.awt.Graphics, java.awt.Graphics) } para los NPCs, y a 
     * {@link objeto.Objeto#render(java.awt.Graphics) } para los objetos.
     * </p>
     * @param g gráficos empleados para dibujar los elementos del nivel
     * @param g2 gráficos empleados para dibujar las conversaciones de los NPCs, si las tienen
     * @see JuegoPlataforma#getCurrentLevel() 
     * 
     */
    public synchronized void render(Graphics g,Graphics g2) {
        if(etapa!=EtapaNivel.cinematica){
            //System.out.println("Entro");
            for (Objeto ti : objetos) {
                if(ti.getId()==Id.diamante){
                    objeto.Diamante d = (objeto.Diamante)ti;
                    if(!d.isRecogido()) ti.render(g);
                }else
                    ti.render(g);
            }
            for (Npc npc : npcs) 
                npc.render(g,g2);
        }else{
            if(!cinematica.render(g2))
                playLevel();
        }
    }
    /**
     * Produce el movimiento de las entidades en general, NPCs en específico y de los objetos de este nivel.
     * <p>
     * Llama a los métodos {@link entity.Entidad#tick() } para las entidades, a {@link entity.Npc#tick() } para los NPCs, 
     * y a {@link objeto.Objeto#tick() } para los objetos.
     * </p>
     * 
     */
    public synchronized void tick() {
        if(etapa!=EtapaNivel.cinematica){
            for (Objeto ti : objetos)
                ti.tick();

            for (Npc npc : npcs)
                npc.tick();
        }else{
            cinematica.tick();
        }
    }
    /**
     * Añade un nuevo elemento a la lista de NPCs.
     * @param npc NPC a añadir.
     */
    public void addNpc(Npc npc){
        npcs.add(npc);
    }
    public void removeNpc(Npc npc) {
        npcs.remove(npc);
    }
    public void addPuerta(objeto.Puerta p){
        puertas.add(p);
    }
    /**
     * Añade un nuevo elemento a la lista de entidades.
     * @param en entidad a añadir.
     */
    public void addEntidad(Entidad en) {
        entidades.add(en);
    }
    /**
     * Elimina a un elemento de la lista de entidades.
     * @param en entidad a ser removida.
     */
    public void removeEntidad(Entidad en) {
        entidades.remove(en);
    }
    /**
     * Añade un nuevo elemento a la lista de objetos.
     * @param ti objeto a añadir.
     */
    public void addObjeto(Objeto ti) {
        objetos.add(ti);
    }
    /**
     * Elimina a un elemento de la lista de objetos.
     * @param ti objeto a ser removido.
     */
    public void removeObjeto(Objeto ti) {
        objetos.remove(ti);
    }
    /**
     * Asigna a la clase {@link JuegoPlataforma} un fondo y un soundtrack específicos, dependiendo de 
     * las indicaciones dadas en el archivo "c.config" de la carpeta de cada nivel.
     * <p>
     * El archivo "c.config" posee dos indicaciones:
     * </p>
     * <p>
     * La primera es el índice o el nombre del archivo de imagen que representa el fondo.
     * </p>
     * <p>
     * La segunda es el índice o el nombre del archivo de sonido que representa la música del nivel.
     * </p>
     * @see JuegoPlataforma#selectMusic(int) 
     * @see JuegoPlataforma#setFondo(int) 
     */
    private void initConfig(){
        String dir="src\\res\\Niveles\\Nivel_"+JuegoPlataforma.nivel;
        File config=new File(dir,"c.config");
        if (config.exists() && config.isFile()) {
            try {
                BufferedReader bf=new BufferedReader(new FileReader(config));
                String line,line2[]=new String[2];
                while((line=bf.readLine())!=null){
                    line2=line.split("\t");
                    if (line2.length==2) {
                        switch(line2[0]){
                            case "Fondo":
                                try{
                                    JuegoPlataforma.setFondo(Integer.parseInt(line2[1]));
                                }catch(Exception t){
                                    if ((t instanceof IllegalArgumentException)) {
                                        JuegoPlataforma.setFondo(line2[1]);
                                    }else
                                    JuegoPlataforma.setFondo(3);
                                }
                                break;
                            case "Sonido":
                                try{
                                    JuegoPlataforma.selectMusic(Integer.parseInt(line2[1])-1);
                                }catch(Exception t){
                                    if ((t instanceof IllegalArgumentException)) {
                                        System.out.println("SOnido2---  "+line2[1]);
                                        JuegoPlataforma.selectMusic(line2[1]);
                                    }else
                                        JuegoPlataforma.selectMusic(4);
                                }
                        }
                    }
                }
                
            }catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Problemas con la configuración");
                Logger.getLogger(Nivel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void nextScene() {
        cinematica.continuar();
    }
    
    public void playLevel() {
        etapa=EtapaNivel.tranquiloDeLaVida;
        cinematica.recharge();
    }
    
    public synchronized void actualizarNivel(Npc npc){
        switch(JuegoPlataforma.nivel){
            case 1:
                if(npc==null) return;
                if(npc.id==Id.robot && !npc.cours.isSuperada()){
                    int i=JuegoPlataforma.handler.jugador.x<npc.x? 1:-1;
                    puertas.get(0).llave.setY(npc.y);
                    puertas.get(0).llave.setX(npc.x+64*i);
                    puertas.get(0).llave.setDisponible(true);
                    for(Npc n: npcs){
                        if(n.id==Id.man){
                            for(interaction.Conversación c: n.conversaciones){
                                if(!c.equals(n.getLastConversación()))
                                    c.setDisponible(false);
                            }
                            break;
                        }
                    }
                }
                break;
            case 2:
                if(npc==null)
                    return;
                
                if(npc.id==Id.aventurero || npc.id==Id.man){
                    Npc segundo=null;
                    
                    if(npc.id==Id.aventurero)
                        for(Npc n: npcs){
                            if(n.id==Id.man){
                                segundo=n;
                                break;
                            }
                        }
                    else if(npc.id==Id.man)
                        for(Npc n: npcs){
                            if(n.id==Id.aventurero){
                                segundo=n;
                                break;
                            }
                        }
                    if(segundo!=null && segundo.getLastConversación().isSuperada()){
                        for(Npc n: npcs)
                            if(n.id==Id.robot){
                                for(interaction.Conversación c: n.conversaciones){
                                    if(!c.equals(n.getLastConversación()))
                                        c.setDisponible(false);
                                }
                                break;
                            }
                    }
                }else if(npc.id==Id.robot && npc.cours.equals(npc.getLastConversación())){
                    JuegoPlataforma.handler.nextLevel();
                }
                break;
            case 3:
                if(npc==null){
                    for(Npc n: npcs){
                        if(n.id==Id.aventurero){
                            for(interaction.Conversación c: n.conversaciones){
                                if(!c.equals(n.getLastConversación()))
                                    c.setDisponible(false);
                            }
                            n.setX(3000);
                            return;
                        }
                    }
                }else if(npc.id==Id.aventurero){
                    if(npc.cours.equals(npc.getLastConversación())){
                        JuegoPlataforma.handler.jugador.numDiamantes-=10;
                        JuegoPlataforma.handler.nextLevel();
                    }
                }
                break;
            case 4:
                if(npc==null) return;
                if(npc.id==Id.aventurera){
                    if(npc.conversaciones.get(0).equals(npc.cours)){
                        if(npc.cours.isSuperada()) return;
                        
                        for(Npc n: npcs){
                            if(n.id==Id.robot){
                                for(interaction.Conversación c: n.conversaciones){
                                    if(!c.equals(n.getLastConversación()))
                                        c.setDisponible(false);
                                }
                                break;
                            }
                        }
                        npc.setSolid(false);
                    }else{
                        JuegoPlataforma.handler.nextLevel();
                    }
                }else if(npc.id==Id.man && !npc.cours.isSuperada()){
                    int i=JuegoPlataforma.handler.jugador.x<npc.x? 1:-1;
                    objeto.Puerta p =puertas.get(0);
                    p.llave.setY(npc.y);
                    p.llave.setX(npc.x+64*i);
                    p.llave.setDisponible(true);
                }
                else if(npc.id==Id.aventurero && !npc.cours.isSuperada()){
                    int i=JuegoPlataforma.handler.jugador.x<npc.x? 1:-1;
                    objeto.Puerta p =puertas.get(1);
                    p.llave.setY(npc.y);
                    p.llave.setX(npc.x+64*i);
                    p.llave.setDisponible(true);
                    
                    for(Npc n: npcs){
                        if(n.id==Id.aventurera){
                            n.setX(2300);
                            n.setY(448);
                            for(interaction.Conversación c: n.conversaciones){
                                if(!c.equals(n.getLastConversación()))
                                    c.setDisponible(false);
                            }
                        }
                    }
                }
                break;
            case 5:
                if(npc==null){
                    for(Npc n: npcs){
                        if(n.id==Id.man){
                            for(interaction.Conversación c: n.conversaciones){
                                if(!c.equals(n.getLastConversación()))
                                    c.setDisponible(false);
                            }
                            break;
                        }
                    }
                }else if(npc.id==Id.man){
                    if(npc.cours.equals(npc.getLastConversación()) && !npc.cours.isSuperada()){
                        for(Npc n: npcs){
                            if(n.id==Id.robot){
                                for(interaction.Conversación c: n.conversaciones){
                                    if(!c.equals(n.getLastConversación()))
                                        c.setDisponible(false);
                                }
                                break;
                            }
                        }
                        JuegoPlataforma.handler.jugador.numDiamantes-=numDiamantes();
                    }
                    for(Npc n: npcs)
                        if(n.id==Id.aventurera){
                            removeNpc(n);
                            break;
                        }
                }
                else if(npc.id==Id.robot){
                    if(npc.cours.equals(npc.getLastConversación()) && !npc.cours.isSuperada()){
                        int i=JuegoPlataforma.handler.jugador.x<npc.x? 1:-1;
                        objeto.Puerta p =puertas.get(0);
                        p.llave.setY(npc.y);
                        p.llave.setX(npc.x+64*i);
                        p.llave.setDisponible(true);
                    }
                    for(Npc n: npcs)
                        if(n.id==Id.aventurera){
                            removeNpc(n);
                            break;
                        }
                }
                break;
            case 6:
                if(npc==null) return;
                if(npc.id==Id.robot){
                    JuegoPlataforma.handler.nextLevel();
                }
        }
    }
    public boolean hayDiamantes(){
        for(Objeto o: objetos)
            if(o.id==Id.diamante)
                if(!((objeto.Diamante)o).isRecogido())
                    return true;
        
        return false;
    }
    public int numDiamantes(){
        int num=0;
        for(Objeto o: objetos)
            if(o.id==Id.diamante)
                num++;
                    
        return num;
    }
}