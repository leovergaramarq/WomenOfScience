package entity.modelo;
import gfx.Sprite;
import gfx.SpriteSheet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import juegoplataforma.JuegoPlataforma;

/**
 * 
 * Clase del modelo de los NPCs. Contiene el spritesheet y los sprites de un personaje en específico.
 * <p>
 * La importancia de esta clase radica en que, dado que los NPCs pertenecen a los niveles, 
 * crear sprites para cada uno de ellos sería poco eficiente en términos de memoria. Esta clase permite asignar un mismo 
 * sprite a varios objetos distintos de tipo {@link entity.Npc} si estos representan a un mismo personaje.
 * </p>
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class EntityModel {
    public Sprite sprites[];
    public SpriteSheet sheet;
    public BufferedImage face;
    /**
     * Crea un nuevo modelo de NPC con la ruta de disco del spritesheet que lo representa. Para ello, invoca al método 
     * {@link #initSprites(java.lang.String) }.
     * @param path dirección de disco del spritesheet
     * @param pathFace dirección de disco de la cara del modelo de NPC
     */
    public EntityModel(String path, String pathFace){
        initSprites(path, pathFace);
    }
    /**
     * Instancia los atributos de la clase y llena el vector de sprites con los sprites del spritesheet correspondiente. 
     * @param path dirección de disco del spritesheet
     */
    private void initSprites(String path, String pathFace){
        sheet=new SpriteSheet(path);
        sprites = new Sprite[9*10];
        int cont=0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                sprites[cont] = new Sprite(sheet, i + 1, j + 1, 64, 64);
                cont++;
            }
        }
        try{
            face=ImageIO.read(new File("src\\res"+pathFace));
        }catch (IOException ex) {
            Logger.getLogger(JuegoPlataforma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
