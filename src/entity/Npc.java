package entity;

import entity.modelo.EntityModel;
import interaction.Conversación;
import interaction.Diálogo;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import juegoplataforma.Id;

/**
 * 
 * Clase de los NPCs que se presentan en diferentes niveles del juego.
 * <p>
 * Esta clase posee las conversaciones entre el personaje principal y el NPC, así como un modelo de NPC, el cual 
 * le asigna un sprtesheet y un vector de sprites Hereda de {@link Entidad}.
 * </p>
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class Npc extends Entidad {
    private boolean talking, enRango, visible;
    public ArrayList<Conversación> conversaciones;
    public Conversación cours;
    /**
     * Crea un nuevo NPC con la dirección en disco de su modelo de NPC.
     * @param path dirección en disco del {@link #modelo}
     * @param pathFace dirección en disco de la cara del {@link #modelo}
     * @param width ancho en pixeles del NPC
     * @param height alto en pixeles del NPC
     * @param solid true si no es posible moverse a través de la entidad
     * @param id identificación del NPC
     * @see entity.modelo.EntityModel
     */
    public Npc(String path, String pathFace, int width, int height, boolean solid, Id id) {
        super(width, height, solid, id, null);
        conversaciones=new ArrayList();
        modelo = new EntityModel(path, pathFace);
        talking=enRango=visible=false;
    }
    /**
     * Crea un nuevo NPC con su modelo de NPC.
     * @param modelo {@link #modelo} del NPC
     * @param width ancho en pixeles del NPC
     * @param height alto en pixeles del NPC
     * @param solid true si no es posible moverse a través de la entidad
     * @param id identificación del NPC
     * @see entity.modelo.EntityModel
     */
    public Npc(EntityModel modelo, int width, int height, boolean solid, Id id) {
        super(width, height, solid, id, null);
        conversaciones=new ArrayList();
        this.modelo=modelo;
        talking=enRango=visible=false;
    }
    /**
     * Finaliza una conversación con el personaje principal en caso de que no haya más diálogos para mostrar.
     */
    public void nextConversación(){
        if(!cours.nextDiálogo()){
            talking=false;
            handler.nivel.etapa=juegoplataforma.EtapaNivel.tranquiloDeLaVida;
            
            //cours.disponible=false;
            handler.nivel.actualizarNivel(this);
            cours.superada=true;
            cours=null;
        }
    }
    
    /**
     * Asigna un valor de visibilidad al NPC.
     * @param visible true si el NPC pasará a ser visible
     */
    public void setVisible(boolean visible) {
        this.visible=visible;
    }
    /**
     * Retorna un valor booleano correspondiente a la visibilidad del NPC.
     * @return true si el NPC es visible
     */
    public boolean isVisible() {
        return visible;
    }
    /**
     * Añade un nuevo elemento a la lista de conversaciones.
     * @param c nueva conversación a añadir
     */
    public void addConversacion(Conversación c){
        conversaciones.add(c);
        c.setNpc(this);
    }
    /**
     * Crea y añade un nuevo elemento a la lista de conversaciones.
     * @param ruta dirección en disco del archivo de texto con la nueva conversación
     */
    public void addConversacion(String ruta){
        Conversación c=new Conversación(this,false,false);
        conversaciones.add(c);
    }
    /**
     * Retorna true si el NPC se encuentra en una conversación con el personaje principal.
     * @return true si el NPC se encuentra en una conversación
     */
    public boolean isTalking() {
        return talking;
    }
    /**
     * Coloca o no al NPC en una conversación con el personaje principal.
     * <p>
     * En caso de que entable la conversación, este método buscará la conversación habilitada, la cual se 
     * encuentra en la lista de conversaciones que el NPC tiene con el jugador. Si dicha conversación ya 
     * fue mostrada, esta iniciará desde el primer diálogo de la misma.
     * </p>
     * @param talking true si el NPC va a ser introducido en una conversación.
     * @see #getEnabledConversacion() 
     */
    public void setTalking(boolean talking) {
        this.talking = talking;
        if(talking){
            cours=getEnabledConversacion();
            if(cours!=null)
                cours.dialogo=0;
            else
                this.talking=false;
            
        }
    }
    /**
     * Lleva a cabo la renderización del NPC.
     * <p>
     * Si hay una conversación en curso, el método llamará a 
     * {@link interaction.Conversación#render(java.awt.Graphics) } para dibujarla en pantalla. Si el personaje principal 
     * no ha tenido con el NPC la última conversación habilitada, se mostrará un signo de exclamación para atraer al 
     * jugador.
     * </p>
     * @param g gráficos empleados para dibujar al NPC
     * @param g2 gráficos empleados para dibujar la conversación habilitada
     */
    public synchronized void render(Graphics g,Graphics g2){
        if(!visible) return;
        
        if(id!=Id.NPCgenérico){
            switch (facing) {
                case 0:
                    g.drawImage(modelo.sprites[85].getBufferedImage(), x, y, width, height, null);
                    break;
                case 1:
                    g.drawImage(modelo.sprites[0].getBufferedImage(), x, y, width, height, null);
                    break;
            }
            if(talking)
                if(cours!=null)
                    cours.render(g2);

            if(enRango && getEnabledConversacion()!=null && !getEnabledConversacion().superada && !talking)
                g.drawImage(exclam, x+width/2-exclam.getWidth(), y-exclam.getHeight()+10, exclam.getWidth(), exclam.getHeight()-10, null);
        }else
            g.drawImage(modelo.sprites[spriteTemp].getBufferedImage(), x, y, width, height, null);
    }
    /**
     * Produce el movimiento del NPC.
     * <p>
     * Este método dibuja en pantalla la conversación actual del NPC con el jugador, en caso de que la haya.
     * </p>
     */
    @Override
    public synchronized void tick() {
        if (talking && cours!=null)
            cours.tick();
    }
    /**
     * Retorna la conversación habilitada con el jugador. Si esta no existe, retorna null.
     * @return conversación habilitada con el jugador.
     */
    public Conversación getEnabledConversacion() {
        for (Conversación c : conversaciones)
            if (c.disponible)
                return c;
        
        return null;
    }
    /**
     * Retorna un valor booleano que indica si existe por lo menos una conversación disponible.
     * @return true si hay alguna conversación disponible
     */
    public boolean haveAvailableConversations() {
        for (Conversación c : conversaciones)
            if (c.disponible)
                return true;
        
        return false;
    }
    /**
     * Crea y añade un nuevo elemento a la lista de conversaciones.
     * <p>
     * El archivo de texto con la nueva conversación utiliza secuencias para identificar a la entidad a la cual 
     * le pertenece la línea de diálogo: "-p    " para el personaje principal y "-n    " para el NPC en cuestión.
     * </p>
     * @param file archivo de texto que contiene los diálogos de la nueva conversación
     * @throws FileNotFoundException si el archivo no se encuentra en el directorio
     * @throws IOException si ocurre un error durante la lectura del archivo
     */
    public void addConversacion(File file) throws FileNotFoundException, IOException{
        BufferedReader bf = new BufferedReader(new FileReader(file));
        Conversación c=new Conversación(this,false,true);
        conversaciones.add(c);
        String linea,modelo[]=new String[2];
        linea=bf.readLine();
        while(linea!=null && !"".equals(linea)){
            modelo=linea.split("\t");
            if (modelo[0].equals("-n")) {
                c.addDiálogo(new Diálogo(c,this,modelo[1]));
            }else if(modelo[0].equals("-p")){
                c.addDiálogo(new Diálogo(c,handler.jugador,modelo[1]));
            }
            linea=bf.readLine();
        }
    }
    /**
     * Sobreescritura del método {@link Entidad#render(java.awt.Graphics) }.
     * @param g objeto de tipo Graphics
     */
    @Override
    public void render(Graphics g) {}
    /**
     * Retorna el modelo del NPC
     * @return modelo del NPC
     */
    public EntityModel getModelo() {
        return modelo;
    }
    /**
     * Retorna un booleano que indica si el NPC se encuentra dentro del rango necesario para una conversación.
     * @return true si el NPC se halla dentro del rango del personaje principal.
     */
    public boolean isEnRango() {
        return enRango;
    }
    /**
     * Retorna la lista de conversaciones.
     * @return lista de conversaciones
     */
    public ArrayList<Conversación> getConversaciones() {
        return conversaciones;
    }
    /**
     * Retorna la conversación actual, en caso de que la halla.
     * @return conversación actual del NPC
     */
    public Conversación getCours() {
        return cours;
    }
    /**
     * Asigna un modelo al NPC
     * @param modelo nuevo modelo para el NPC
     */
    public void setModelo(EntityModel modelo) {
        this.modelo = modelo;
    }
    /**
     * Asigna un booleano al atributo {@link #enRango}.
     * @param enRango nuevo valot para el atributo {@link #enRango}.
     */
    public void setEnRango(boolean enRango) {
        this.enRango = enRango;
    }
    /**
     * Retorna la lista de conversaciones.
     * @param conversaciones lista de conversaciones
     */
    public void setConversaciones(ArrayList<Conversación> conversaciones) {
        this.conversaciones = conversaciones;
    }
    /**
     * Asigna una conversación en curso al NPC.
     * @param cours nueva conversación en curso
     */
    public void setCours(Conversación cours) {
        this.cours = cours;
    }
    public Conversación getLastConversación(){
        if(conversaciones==null || conversaciones.isEmpty()) return null;
        
        return conversaciones.get(conversaciones.size()-1);
    }
}