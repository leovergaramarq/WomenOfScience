package objeto;

import gfx.Sprite;
import java.awt.Graphics;
import juegoplataforma.Id;

/**
 * 
 * Clase para objetos que sirven como puertas para que el personaje principal logre avanzar al siguiente nivel.
 * <p>
 * Hereda de la clase {@link Objeto}.
 * </p>
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class Puerta extends Objeto{
    public boolean locked;
    public Sprite spriteUnlocked;
    public Llave llave;
    /**
     * Crea un nuevo objeto Puerta con el constructor de su clase padre.
     * {@link Objeto#Objeto(int, int, int, int, boolean, juegoplataforma.Id, juegoplataforma.Nivel) }.
     * @param x coordenada x del objeto
     * @param y coordenada y del objeto
     * @param with ancho en pixeles del objeto
     * @param height alto en pixeles del objeto
     * @param solid true si no es posible moverse a través del objeto
     * @param id identificación del objeto
     * @param sprite sprite de la puerta bloqueada
     * @param spriteUnlocked sprite de la puerta desbloqueada
     * @param locked true si la puerta está bloqueada
     */
    public Puerta(int x, int y, int with, int height, boolean solid, Id id, Sprite sprite, Sprite spriteUnlocked,
            boolean locked) {
        super(x, y, with, height, solid, id, sprite);
        this.spriteUnlocked=spriteUnlocked;
        this.locked=locked;
    }
    /**
     * Lleva a cabo la renderización de la puerta.
     * @param g gráficos empleados para dibujar al objeto
     */
    
    @Override
    public synchronized void render(Graphics g){
        if(locked)
            g.drawImage(sprite.getBufferedImage(), x, y, null);
        else
            g.drawImage(spriteUnlocked.getBufferedImage(), x, y, null);;
    }
    
    /**
     * Lleva a cabo un posible movimiento del objeto.
     */
    @Override
    public synchronized void tick(){
    }
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    public boolean isLocked() {
        return locked;
    }

    public void setLlave(Llave llave) {
        this.llave = llave;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }
    
}