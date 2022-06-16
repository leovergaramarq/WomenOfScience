package gfx;
import java.awt.image.BufferedImage;

/**
 * 
 * Clase que representa los sprites de un spritesheet. Posee un {@link java.awt.image.BufferedImage} y un 
 * {@link SpriteSheet}.
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class Sprite {
    public BufferedImage image;
    public SpriteSheet sheet;
    private int width, height;
    /**
     * Crea un nuevo sprite a partir de las coordenadas rectangulares y del spritesheet que recibe como parámetro.
     * <p>
     * Para inicializar la imagen, este método llama a la función {@link SpriteSheet#getSprite(int, int) }.
     * </p>
     * @param sheet {@link SpriteSheet} del cual se extrae la imagen
     * @param x coordenada en x de la sub-imagen
     * @param y coordenada en y de la sub-imagen
     * @param width ancho del sprite
     * @param height alto del sprite
     */
    public Sprite(SpriteSheet sheet, int x, int y, int width, int height){
        this.sheet=sheet;
        this.width=width;
        this.height=height;
        image = sheet.getSprite(x, y, width, height);
    }
    /**
     * Retorna la imagen del sprite.
     * @return imagen del sprite
     */
    public BufferedImage getBufferedImage(){
        return image;
    }
    /**
     * Retorna el ancho del sprite
     * @return ancho del sprite
     */
    public int getWidth(){
        return width;
    }
    /**
     * Retorna el alto del sprite
     * @return alto del sprite
     */
    public int getHeight(){
        return height;
    }
}