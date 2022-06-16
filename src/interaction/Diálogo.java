package interaction;
import entity.Entidad;
import java.awt.Graphics;
import java.util.ArrayList;
import juegoplataforma.JuegoPlataforma;

/**
 * 
 * Clase de los diálogos que conforman a una conversación en específico.
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class Diálogo {
    public String cuerpo, cuerpoTemp;
    public Conversación conversación;
    public Entidad entityTalking;
    public boolean escribiendo;
    public int delay, i, línea;
    public ArrayList<String> líneas;
    public final int lengthFirstLínea, numLíneas;
    public static final int ANCHO=40;
    /**
     * Construye un nuevo diálogo con su contenido, con la conversación a la que pertenece y con la entidad que lo expresa.
     * @param conversación conversación de la que el diálogo es parte
     * @param entityTalking entidad que expresa el diálogo
     * @param cuerpo cuerpo del diálogo
     */
    public Diálogo(Conversación conversación, Entidad entityTalking, String cuerpo){
        this.conversación=conversación;
        this.cuerpo=cuerpo;
        this.entityTalking=entityTalking;
        escribiendo=false;
        cuerpoTemp="";
        delay=i=0; línea=1;
        líneas=new ArrayList();
        líneas.add("");
        if(ANCHO>cuerpo.length()){
            lengthFirstLínea=cuerpo.substring(0,cuerpo.length()).length();
        }else{
            lengthFirstLínea=cuerpo.substring(0,ANCHO).length();
        }
        numLíneas=cuerpo.length()/ANCHO +1;
    }
    /**
     * Dibuja el contenido del diálogo.
     * @param g gráficos para dibujar el cuerpo del diálogo
     */
    public void render(Graphics g){
        int x=JuegoPlataforma.getFrameWidth()/2;
        g.drawImage(entityTalking.modelo.face, x-60, 5, null);
        g.drawImage(JuegoPlataforma.marco, x-ANCHO*8, 120, ANCHO*16, 45*numLíneas, null);
        
        for(int j=0; j<línea; j++){
            g.drawString(líneas.get(j), x-lengthFirstLínea*4-40, 150+j*35);
        }
    }
    /**
     * Escribe el diálogo, caracter por caracter, hasta que este finalice.
     */
    public void tick(){
        if(!isComplete()){
            if(delay==6){
                String temp=cuerpo.substring(i, i+1);
                cuerpoTemp+=temp;
                i++; delay=0;
                if(i%ANCHO==0){
                    if(!temp.equals(" ") && !isComplete())
                        temp+="-";
                    setCurrentLínea(getCurrentLínea()+temp);
                    líneas.add("");
                    línea++;
                }else{
                    setCurrentLínea(getCurrentLínea()+temp);
                }
            }else{
                delay++;
            }
            escribiendo=true;
        }else{
            i=delay=0;
            escribiendo=false;
        }
    }
    /**
     * Finaliza el diálogo en caso de que no lo estuviese.
     */
    public void finish(){
        int j=0; línea=1;
        while(j<cuerpo.length()){
            if(j+ANCHO>=cuerpo.length()){
                cuerpoTemp=cuerpo.substring(j,cuerpo.length());
                setCurrentLínea(cuerpoTemp);
            }else{
                cuerpoTemp=cuerpo.substring(j,j+ANCHO);
                if(!cuerpo.substring(j+ANCHO-1, j+ANCHO).equals(" "))
                    cuerpoTemp+="-";
                setCurrentLínea(cuerpoTemp);
                líneas.add("");
                línea++;
            }
            j+=ANCHO;
        }
        cuerpoTemp=cuerpo;
        escribiendo=false;
        delay=0; i=0;
    }
    /**
     * Determina si el diálogo ya ha sido completamente escrito.
     * @return true si el diálogo ya fue digitado en pantalla completamente
     */
    public boolean isComplete(){
        return cuerpoTemp.length()==cuerpo.length();
    }
    public String getCurrentLínea(){
        return líneas.get(línea-1);
    }
    public void setCurrentLínea(String temp){
        líneas.set(línea-1,temp);
    }
}