package objeto;
import gfx.Sprite;
import java.awt.Graphics;
import juegoplataforma.Id;

/**
 * 
 * Clase para objetos que pueden ser recogidos por el personaje principal dentro de un determinado nivel.
 * <p>
 * Hereda de la clase {@link Objeto}.
 * </p>
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class Diamante extends Objeto {
    private boolean recogido;
    /**
     * Crea un nuevo objeto Diamante con el constructor de su clase padre.
     * {@link Objeto#Objeto(int, int, int, int, boolean, juegoplataforma.Id, juegoplataforma.Nivel) }.
     * @param x coordenada x del objeto
     * @param y coordenada y del objeto
     * @param width ancho en pixeles del objeto
     * @param height alto en pixeles del objeto
     * @param solid true si no es posible moverse a través del objeto
     * @param id identificación del objeto
     * @param sprite sprite del objeto
     */
    public Diamante(int x, int y, int width, int height, boolean solid, Id id, Sprite sprite) {
        super(x, y, width, height, solid, id, sprite);
        recogido=false;
    }
    /**
     * Lleva a cabo la renderización del diamante.
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
    public synchronized void tick(){}
    /**
     * Retorna un booleano que indica si el diamante ha sido recogido por el personaje principal.
     * @return true si el diamante ha sido recogido
     */
    public boolean isRecogido(){
        return recogido;
    }
    /**
     * Asigna al diamante un nuevo estado de recogido.
     * @param r nuevo valor de para {@link #recogido}
     */
    public void setRecogido(boolean r, entity.Jugador jug){
        recogido=r;
        if(r) jug.numDiamantes++;
    }
}
