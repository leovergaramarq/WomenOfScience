package juegoplataforma;
import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * 
 * Clase que permite reproducir archivos de audio.
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class Sonido {
    private Clip clip;
    private int lastFramePosition=0;
    /**
     * Crea un nuevo objeto de tipo Sonido con su clip, a partir de la dirección en disco de un archivo de audio.
     * @param path dirección en disco del archivo de audio
     */
    public Sonido(String path){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("src\\res"+path));
            
            AudioFormat baseFormat = ais.getFormat();
            
            AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(),
            16, baseFormat.getChannels(), baseFormat.getChannels()*2, baseFormat.getSampleRate(), false);
            
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat,ais);
            
            clip = AudioSystem.getClip();
            clip.open(dais);
        
        } catch (Exception e) {
            System.out.println("NO AUDIO FOUND XD");
        }
    }
    /**
     * Inicia el {@link #clip}, validando que este no se encuentre en curso.
     */
    public void play(){
        if (clip == null || clip.isRunning()) return;
        
        stop();
        clip.setFramePosition(lastFramePosition);

        clip.start();
        System.out.println("Play");
    }
    /**
     * Cierra el {@link #clip}, pausándolo primero en caso de que este se encuentre en curso.
     */
    public void close(){
        stop();
        clip.close();
    }
    /**
     * Detiene el {@link #clip} en caso de que este se encuentre en curso y guarda la posición en la cual este acabó.
     */
    public void stop(){
        if (clip.isRunning()){
            lastFramePosition=clip.getFramePosition();
            clip.stop();
            System.out.println("Stop");
        }
    }
    /**
     * Reinicia el clip desde el inicio, deteniéndolo en primer lugar en caso de que este se encuentre en curso.
     */
    public void restart(){
        System.out.println("REWIND");
        stop();
        lastFramePosition=0;
        play();
    }
    /**
     * Retorna el clip de esta clase.
     * @return clip de la clase.
     */
    public Clip getClip(){
        return clip;
    }
}