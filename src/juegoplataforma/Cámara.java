package juegoplataforma;
import entity.Entidad;

/**
 * 
 * Clase que representa la cámara que sigue el movimiento del personaje principal dentro del juego.
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class Cámara {
    public int x,y;
    /**
     * Actualiza la posición de la cámara según la ubicación del personaje principal en el mapa.
     * @param player 
     */
    public void tick(Entidad player){
        setX(-player.getX()+JuegoPlataforma.WIDTH*2);
        setY(-player.getY()+JuegoPlataforma.HEIGHT*2);
    }
    /**
     * Retorna la coordenada en x de la cámara.
     * @return coordenada x de la cámara
     */
    public int getX() {
        return x;
    }
    /**
     * Asigna a la cámara una nueva coordenada horizontal.
     * @param x nueva coordenada x de la cámara
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * Retorna la coordenada en y de la cámara.
     * @return coordenada y de la cámara
     */
    public int getY() {
        return y;
    }
    /**
     * Asigna a la cámara una nueva coordenada vertical.
     * @param y nueva coordenada y de la cámara
     */
    public void setY(int y) {
        this.y = y;
    }
}
