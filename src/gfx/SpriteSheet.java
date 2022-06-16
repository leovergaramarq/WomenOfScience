package gfx;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * 
 * Clase que representa los sprites de un spritesheet. Posee un {@link java.awt.image.BufferedImage} y un 
 * {@link SpriteSheet}.
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class SpriteSheet {
    private BufferedImage sheet;
    /**
     * Crea un nuevo spritesheet a partir de la dirección en disco de un archivo de imagen.
     * @param path dirección en disco de la imagen
     */
    public SpriteSheet(String path){
        try {
            sheet = ImageIO.read(new File("src\\res"+path));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(path);
        }
    }
    /**
     * Retorna un extracto de imagen del spritesheet de 64x64 pixeles, dadas las coordenadas.
     * <p>
     * Este método se vale de la función {@link java.awt.image.BufferedImage#getSubimage(int, int, int, int) }.
     * </p>
     * @param x coordenada x del punto inicial del nuevo sprite
     * @param y coordenada x del punto inicial del nuevo sprite
     * @param width ancho del nuevo sprite
     * @param height alto del nuevo sprite
     * @return sprite de tamaño 64x64 a partir de una pareja (x,y)
     */
    public BufferedImage getSprite(int x, int y, int width, int height){
        return sheet.getSubimage(64*(x-1), 64*(y-1), width, height);
    }
}
