package entity;

import entity.modelo.EntityModel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import juegoplataforma.Handler;
import juegoplataforma.Id;
import juegoplataforma.JuegoPlataforma;
import juegoplataforma.Nivel;

/**
 * 
 * Clase abstracta que representa a cualquier posible entidad viviente en el juego.
 * <p>
 * De esta clase heredan los NPCs y el personaje principal.
 * </p>
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public abstract class Entidad {
    public int x, y, velX, velY;
    public double aceleración;
    public int width, height;
    public int facing, spriteTemp;
    
    public boolean solid, agachado, moving;
    public Estado estado=Estado.falling;
    
    public Handler handler;
    public Nivel nivel;
    public Id id;
    public BufferedImage exclam;
    public EntityModel modelo;
    /**
     * Crea una nueva entidad, a la cual se le asigna un ancho, un alto, un estado sólido, un Id y un nivel.
     * @param width ancho en pixeles de la entidad
     * @param height alto en pixeles de la entidad
     * @param solid true si no es posible moverse a través de la entidad
     * @param id identificador del tipo de entidad en cuestión
     * @param nivel nivel al cual la entidad pertenece
     */
    public Entidad(int width, int height, boolean solid, Id id, Nivel nivel){
        this.width = width;
        this.height = height;
        this.solid = solid;
        this.id = id;
        this.nivel=nivel;
        handler=JuegoPlataforma.handler;
        aceleración=0.0;
        facing=spriteTemp=0;
        agachado=moving=false;
        exclam=imageFrom("/Spritesheets/exclam0.png");
    }
    /**
     * Crea una nueva entidad, para lo cual llama al constructor 
     * {@link #Entidad(int, int, boolean, juegoplataforma.Id, juegoplataforma.Nivel) }. Este método le asigna una 
     * posición de partida a la nueva entidad.
     * @param x coordenada x de la entidad
     * @param y coordenada y de la entidad
     * @param width ancho en pixeles de la entidad
     * @param height alto en pixeles de la entidad
     * @param solid true si no es posible moverse a través de la entidad
     * @param id identificador del tipo de entidad en cuestión
     * @param nivel nivel al cual la entidad pertenece
     */
    public Entidad(int x, int y,int width, int height, boolean solid, Id id, Nivel nivel){
        this(width, height, solid, id, nivel);
        this.x = x; this.y = y;
    }
    /**
     * Método abstracto, el cual es sobreescrito por las clases que heredan de {@link Entidad} para generar el 
     * renderizado de la entidad en cuestión.
     * @param g gráficos para dibujar a la entidad
     */
    public abstract void render(Graphics g);
    /**
     * Método abstracto, el cual es sobreescrito por las clases que heredan de {@link Entidad} para generar el 
     * movimiento de la entidad en cuestión.
     */
    public abstract void tick();
    /**
     * Produce la muerte de la entidad, eliminándola de la lista de entidades del nivel al que pertenece.
     * @see juegoplataforma.Nivel#removeEntidad(entity.Entidad) 
     */
    public void die(){
        nivel.removeEntidad(this);
    }
    /**
     * Retorna la coordenada en x de la entidad.
     * @return coordenada x de la entidad
     */
    public int getX() {
        return x;
    }
    /**
     * Asigna a la entidad una nueva coordenada horizontal.
     * @param x nueva coordenada x de la entidad
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * Retorna la coordenada en y de la entidad.
     * @return coordenada y de la entidad
     */
    public int getY() {
        return y;
    }
    /**
     * Asigna a la entidad una nueva coordenada vertical.
     * @param y nueva coordenada y de la entidad
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     * Retorna la identificación de la entidad.
     * @return Id de la entidad
     */
    public Id getID(){
        return id;
    }
    /**
     * Retorna el tamaño horizontal en pixeles de la entidad.
     * @return ancho de la entidad
     */
    public int getWidth() {
        return width;
    }
    /**
     * Asigna a la entidad un nuevo ancho.
     * @param witdth nuevo ancho de la entidad
     */
    public void setWidth(int witdth) {
        this.width = witdth;
    }
    /**
     * Retorna el tamaño vertical en pixeles de la entidad.
     * @return alto de la entidad
     */
    public int getHeight() {
        return height;
    }
    /**
     * Asigna a la entidad un nuevo ancho.
     * @param height nuevo ancho de la entidad
     */
    public void setHeight(int height) {
        this.height = height;
    }
    /**
     * Retorna si la entidad es sólida.
     * @return true si la entidad es sólida
     */
    public boolean isSolid() {
        return solid;
    }
    /**
     * Asigna un nuevo valor de sólido a la entidad.
     * @param solid nuevo valor de sólido
     */
    public void setSolid(boolean solid) {
        this.solid = solid;
    }
    /**
     * Retorna la velocidad actual en x de la entidad.
     * @return velocidad actual en x de la entidad
     */
    public int getVelX() {
        return velX;
    }
    /**
     * Retorna la velocidad actual en y de la entidad.
     * @return velocidad actual en y de la entidad
     */
    public int getVelY() {
        return velY;
    }
    /**
     * Asigna una nueva velocidad en y a la entidad.
     * @param velY nueva velocidad en y
     */
    public void setVelY(int velY) {
        this.velY = velY;
    }
    /**
     * Asigna una nueva velocidad en x a la entidad.
     * @param velX nueva velocidad en x
     */
    public void setVelX(int velX) {
        this.velX = velX;
    }
    /**
     * Retorna un nuevo {@link java.awt.Rectangle} con el área cubierto por la entidad.
     * @return {@link java.awt.Rectangle} con el área de la entidad
     */
    public Rectangle getBounds(){
        return new Rectangle(x,y,width, height);
    }
    /**
     * Retorna un nuevo {@link java.awt.Rectangle} con el área de la zona superior de la entidad.
|     * @return {@link java.awt.Rectangle} con el área de la zona superior de la entidad
     */
    public Rectangle getBoundsTop(){
        return new Rectangle(x+10,getY(),width-20,5);
    }
    /**
     * Retorna un nuevo {@link java.awt.Rectangle} con el área de la zona inferior de la entidad.
     * @return {@link java.awt.Rectangle} con el área de la zona inferior de la entidad
     */
    public Rectangle getBoundsBottom(){
        return new Rectangle(getX()+10, getY()+width-1, width-20, 2);
    }
    /**
     * Retorna un nuevo {@link java.awt.Rectangle} con el área de la zona derecha de la entidad.
     * @return {@link java.awt.Rectangle} con el área la zona derecha de la entidad
     */
    public Rectangle getBoundsRight(){
        return new Rectangle (getX()+width-5, getY()+10, 5, height-20);
    }
    /**
     * Retorna un nuevo {@link java.awt.Rectangle} con el área de la zona izquierda de la entidad.
     * @return {@link java.awt.Rectangle} con el área de la zona izquierda de la entidad
     */
    public Rectangle getBoundsLeft(){
        return new Rectangle (getX(), getY()+10, 5, height-20);
    }
    /**
     * Retorna un entero 0 (izquierda) o 1 (derecha) según la dirección a la que apunta la entidad.
     * @return 0 si la entidad mira hacia la izquierda ó 1 si la entidad mira hacia la derecha
     */
    public int getFacing() {
        return facing;
    }
    /**
     * Asigna una nueva dirección hacia la cual la entidad mira.
     * @param facing nueva dirección hacia la cual la entidad apunta
     */
    public void setFacing(int facing) {
        this.facing = facing;
    }
    /**
     * Retorna el manejador de la entidad.
     * @return manejador de la entidad
     */
    public Handler getHandler() {
        return handler;
    }
    /**
     * Retorna la identificación de la entidad.
     * @return Id de la entidad
     */
    public Id getId() {
        return id;
    }
    /**
     * Asigna una identificación a la entidad.
     * @param id nuevo Id de la entidad
     */
    public void setId(Id id) {
        this.id = id;
    }
    /**
     * Retorna la aceleración vertical actual de la entidad.
     * @return aceleración vertical de la entidad
     */
    public double getAceleración() {
        return aceleración;
    }
    /**
     * Asigna una nueva aceleración vertical a la entidad.
     * @param aceleración nueva aceleración vertical de la entidad
     */
    public void setAceleración(double aceleración) {
        this.aceleración = aceleración;
    }
    /**
     * Inicializa atributos de tipo {@link java.awt.image.BufferedImage}. Si ocurre una excepción de tipo 
     * {@link java.io.IOException}, esta es atrapada y se retorna null.
     * 
     * @param ruta dirección de disco de la imagen dentro de su paquete
     * @return un {@link java.awt.image.BufferedImage} de acuerdo a la dirección que reciba como parámetro, o null
     */
    protected BufferedImage imageFrom(String ruta){
        try {
            return ImageIO.read(new File("src\\res"+ruta));
        } catch (IOException ex) {
            Logger.getLogger(Entidad.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public void setRandomFacing(){
        facing=Math.random()*10<5 ? 0:1;
    }
}