package objeto;

import gfx.Sprite;
import java.awt.Graphics;
import juegoplataforma.Id;

/**
 * 
 * Clase para objetos que funcionan como obstáculos y de suelo para el personaje principal dentro un determinado nivel.
 * <p>
 * Hereda de la clase {@link Objeto}.
 * </p>
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class MixWall extends Objeto{
    /**
     * Crea un nuevo objeto Muro con el constructor de su clase padre.
     * {@link Objeto#Objeto(int, int, int, int, boolean, juegoplataforma.Id, juegoplataforma.Nivel) }.
     * @param x coordenada x del objeto
     * @param y coordenada y del objeto
     * @param width ancho en pixeles del objeto
     * @param height alto en pixeles del objeto
     * @param solid true si no es posible moverse a través del objeto
     * @param id identificación del objeto
     * @param sprite sprite del objeto
     */
    public MixWall(int x, int y, int width, int height, boolean solid, Id id, Sprite sprite) {
        super(x, y, width, height, solid, id, sprite);
    }
    /**
     * Lleva a cabo la renderización del muro.
     * @param g gráficos empleados para dibujar al objeto
     */
    @Override
    public synchronized void render(Graphics g){
        g.drawImage(sprite.getBufferedImage(), x, y, null);
    }
    /**
     * Lleva a cabo un posible movimiento del objeto.
     */
    @Override
    public synchronized void tick(){
    }
}