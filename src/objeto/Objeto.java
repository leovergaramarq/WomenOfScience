package objeto;

import entity.Entidad;
import gfx.Sprite;
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

/**
 * 
 * Clase para los distintos objetos dentro de los niveles del juego.
 * <p>
 * De esta clase heredan otras como {@link Puerta}, {@link Diamante} y {@link Muro}.
 * </p>
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public abstract class Objeto {
    public int x,y, velX, velY;
    public int width, height;
    
    public boolean solid;
    public Id id;
    public Sprite sprite;
    
    public Handler handler;
    /**
     * Construye un nuevo objeto con su ubicación en el mapa, su ancho, su alto, su estado de sólido, su identificador 
     * y el nivel al cual pertenece.
     * @param x coordenada x del objeto
     * @param y coordenada y del objeto
     * @param width ancho en pixeles del objeto
     * @param height alto en pixeles del objeto
     * @param solid true si no es posible moverse a través del objeto
     * @param id identificación del objeto
     * @param sprite sprite del objeto
     */
    public Objeto(int x, int y, int width, int height, boolean solid, Id id, Sprite sprite){
        this.sprite=sprite;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.solid = solid;
        this.id = id;
        this.handler = JuegoPlataforma.handler;
    }
    /**
     * Método abstracto, el cual es sobreescrito por las clases que heredan de {@link Objeto} para generar el 
     * renderizado del objeto en cuestión.
     * @param g gráficos para dibujar al objeto
     */
    public abstract void render(Graphics g);
    /**
     * Método abstracto, el cual es sobreescrito por las clases que heredan de {@link Objeto} para generar un posible
     * movimiento del objeto en cuestión.
     */
    public abstract void tick();
    /**
     * Retorna la coordenada en x del objeto.
     * @return coordenada x del objeto
     */
    public int getX() {
        return x;
    }
    /**
     * Asigna al objeto una nueva coordenada horizontal.
     * @param x nueva coordenada x del objeto
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * Retorna la coordenada en y del objeto.
     * @return coordenada y del objeto
     */
    public int getY() {
        return y;
    }
    /**
     * Asigna al objeto una nueva coordenada vertical.
     * @param y nueva coordenada y del objeto
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     * Retorna la identificación del objeto.
     * @return Id del objeto
     */
    public Id getID(){
        return id;
    }
    /**
     * Retorna el tamaño horizontal en pixeles del objeto.
     * @return ancho del objeto
     */
    public int getWidth() {
        return width;
    }
    /**
     * Asigna al objeto un nuevo ancho.
     * @param witdth nuevo ancho del objeto
     */
    public void setWidth(int witdth) {
        this.width = witdth;
    }
    /**
     * Retorna el tamaño vertical en pixeles del objeto.
     * @return alto del objeto
     */
    public int getHeight() {
        return height;
    }
    /**
     * Asigna al objeto un nuevo ancho.
     * @param height nuevo ancho del objeto
     */
    public void setHeight(int height) {
        this.height = height;
    }
    /**
     * Retorna si el objeto es sólido.
     * @return true si el objeto es sólido
     */
    public boolean isSolid() {
        return solid;
    }
    /**
     * Asigna un nuevo valor de sólido al objeto.
     * @param solid nuevo valor de sólido
     */
    public void setSolid(boolean solid) {
        this.solid = solid;
    }
    /**
     * Retorna la velocidad actual en x del objeto.
     * @return velocidad actual en x del objeto
     */
    public int getVelX() {
        return velX;
    }
    /**
     * Retorna la velocidad actual en y del objeto.
     * @return velocidad actual en y del objeto
     */
    public int getVelY() {
        return velY;
    }
    /**
     * Asigna una nueva velocidad en y al objeto.
     * @param velY nueva velocidad en y
     */
    public void setVelY(int velY) {
        this.velY = velY;
    }
    /**
     * Asigna una nueva velocidad en x al objeto.
     * @param velX nueva velocidad en x
     */
    public void setVelX(int velX) {
        this.velX = velX;
    }
    /**
     * Retorna el manejador del objeto.
     * @return manejador del objeto
     */
    public Handler getHandler() {
        return handler;
    }
    /**
     * Retorna la identificación del objeto.
     * @return Id del objeto
     */
    public Id getId() {
        return id;
    }
    /**
     * Asigna una identificación al objeto.
     * @param id nuevo Id del objeto
     */
    public void setId(Id id) {
        this.id = id;
    }
    /**
     * Retorna un nuevo {@link java.awt.Rectangle} con el área cubierto por el objeto.
     * @return {@link java.awt.Rectangle} con el área del objeto
     */
    public Rectangle getBounds(){
        return new Rectangle(x,y,width, height);
    }
    protected BufferedImage imageFrom(String ruta){
        try {
            return ImageIO.read(new File("src\\res"+ruta));
        } catch (IOException ex) {
            Logger.getLogger(Entidad.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
