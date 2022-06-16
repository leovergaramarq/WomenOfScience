package interaction;
import entity.Npc;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * 
 * Clase de las conversaciones que distintos NPCs poseen.
 * <p>
 * Esta clase posee los diálogos que se presentarán entre el personaje principal y el NPC en cuestión.
 * </p>
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class Conversación {
    public ArrayList<Diálogo> dialogos;
    public boolean visible, superada, disponible;
    public Npc npc;
    public int dialogo;
    
    /**
     * Construye una nueva conversación con el NPC que es parte de esta y la visibilidad y disponibilidad de la misma.
     * @param npc NPC que posee la conversación
     * @param visible true si la conversación será
     * @param disponible true si la conversación parte como disponible
     */
    public Conversación(Npc npc, boolean visible, boolean disponible){
        dialogos = new ArrayList();
        this.npc = npc;
        this.visible=visible;
        this.disponible=disponible;
        superada=false;
        dialogo=-1;
    }
    /**
     * Asigna un NPC a la conversación para garantizar la bidireccionalidad entre esta clase y {@link entity.Npc}.
     * @param npc NPC que se asignará a la conversación
     */
    public void setNpc(Npc npc) {
        this.npc = npc;
    }
    /**
     * Añade un nuevo elemento a la lista de diálogos.
     * @param d nuevo diálogo de la conversación
     */
    public void addDiálogo(Diálogo d){
        dialogos.add(d);
    }
    /**
     * Elimina un diálogo de la conversación.
     * @param d diálogo a ser removido
     */
    public void removeDiálogo(int d){
        dialogos.remove(d);
    }
    /**
     * Retorna el elemento de la posición indicada de la lista de diálogos.
     * @param i índice del diálogo a ser devuelto
     * @return i-ésimo diálogo de la conversación
     */
    public Diálogo getDiálogo(int i){
        return dialogos.get(i);
    }
    /**
     * Dibuja en pantalla el diálogo habilitado en caso de que esta conversación posea alguno.
     * @param g gráficos para la renderización del diálogo
     */
    public void render(Graphics g){
        if(dialogos!=null && !dialogos.isEmpty())
            dialogos.get(dialogo).render(g);
    }
    /**
     * Genera la constante escritura del cuerpo del diálogo habilitado.
     */
    public void tick(){
        if(dialogos!=null && !dialogos.isEmpty())
            getEnabledDialog().tick();
    }
    /**
     * Utiliza el atributo {@link #dialogo} para retornar el diálogo en curso de la conversación.
     * @return diálogo en curso
     */
    public Diálogo getEnabledDialog(){
        if (dialogo==-1)
            return null;
        
        return dialogos.get(dialogo);
    }
    /**
     * Avanza al siguiente diálogo de la conversación en caso de que esta no haya finalizado.
     * @return false si la conversación no posee más diálogos.
     */
    public boolean nextDiálogo(){
        Diálogo d=getEnabledDialog();
        if(!d.escribiendo){
            //d.escribiendo=false;
            if(++dialogo>=dialogos.size()){
                dialogo=-1;
                return false;
            }
        }
        return true;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isSuperada() {
        return superada;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setSuperada(boolean superada) {
        this.superada = superada;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
}