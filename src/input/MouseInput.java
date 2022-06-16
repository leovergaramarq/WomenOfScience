package input;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JOptionPane;
import juegoplataforma.EtapaJuego;
import juegoplataforma.JuegoPlataforma;

/**
 * 
 * Clase que sirve de sensor de eventos del mouse del jugador.
 * <p>
 * Implementa las interfaces {@link java.awt.event.MouseListener} y {@link java.awt.event.MouseMotionListener}.
 * </p>
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class MouseInput implements MouseListener, MouseMotionListener{
    /**
     * Sobreescritura del método {@link java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent) }.
     * <p>
     * Dependiendo del punto en el cual se detecte el evento, el programa responderá. Por ejemplo, si el jugador hace 
     * clic en la zona de la ventana que posee el botón de jugar, el juego cambiará a la etapa de juego.
     * </p>
     * @param me evento del mouse detectado
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        juegoplataforma.Handler handler=JuegoPlataforma.handler;
        
        if(JuegoPlataforma.etapa==EtapaJuego.menúPrinc){
            if(me.getX()>319 && me.getX()<771){
                if(me.getY()>338 && me.getY()<386){
                    JuegoPlataforma.etapa=EtapaJuego.menúJuego;
                    JuegoPlataforma.menúAreaX=-1;
                    JuegoPlataforma.menúAreaY=-1;
                    
                }else if(me.getY()>386 && me.getY()<434){
                    JuegoPlataforma.etapa=EtapaJuego.ajustes;
                    JuegoPlataforma.menúAreaX=-1;
                    JuegoPlataforma.menúAreaY=-1;
                    
                }else if(me.getY()>434 && me.getY()<482){
                    JuegoPlataforma.etapa=EtapaJuego.créditos;
                    JuegoPlataforma.menúAreaX=-1;
                    JuegoPlataforma.menúAreaY=-1;
                    
                }else if(me.getY()>482 && me.getY()<530){
                    System.exit(0);
                }
            }else if(me.getX()>398 && me.getX()<692){
                if(me.getY()>732 && me.getY()<770){
                    JOptionPane.showMessageDialog(null, "En desarrollo...");
                }
            }
        }else if(JuegoPlataforma.etapa==EtapaJuego.menúJuego){
            if(me.getX()>319 && me.getX()<771){
                if(me.getY()>300 && me.getY()<348){
                    JuegoPlataforma.etapa=EtapaJuego.menúPrinc;
                    JuegoPlataforma.menúAreaX=-1;
                    JuegoPlataforma.menúAreaY=-1;
                    
                }else if(me.getY()>348 && me.getY()<396){
                    if(handler.nivel!=null && !handler.seConfirmaOficialmenteUnaNuevaPartida()) return; //:v
                        handler.nuevaPartida();
                    
                }else if(me.getY()>396 && me.getY()<444){
                    if(handler.hayPartida()){
                        if(handler.hayPartida()){
                            handler.backToLevel();
                        
                        JuegoPlataforma.menúAreaX=-1;
                        JuegoPlataforma.menúAreaY=-1;
                    }
                }
                }else if(me.getY()>444 && me.getY()<492){
                    JOptionPane.showMessageDialog(null, "En desarrollo...");
                }else if(me.getY()>492 && me.getY()<540){
                    JOptionPane.showMessageDialog(null, "En desarrollo...");
                }
            }
        }else if(JuegoPlataforma.etapa==EtapaJuego.créditos){
            if(me.getX()>319 && me.getX()<771){
                if(me.getY()>246 && me.getY()<294){
                    JuegoPlataforma.etapa=EtapaJuego.menúPrinc;
                    JuegoPlataforma.menúAreaX=-1;
                    JuegoPlataforma.menúAreaY=-1;
                }
            }
        }else if(JuegoPlataforma.etapa==EtapaJuego.ajustes){
            if(me.getX()>319 && me.getX()<771){
                if(me.getY()>298 && me.getY()<346){
                    JuegoPlataforma.etapa=EtapaJuego.menúPrinc;
                    JuegoPlataforma.menúAreaX=-1;
                    JuegoPlataforma.menúAreaY=-1;
                        
                }else if(me.getY()>356 && me.getY()<404){
                    JuegoPlataforma.audioOn=!JuegoPlataforma.audioOn;
                }else if(me.getY()>404 && me.getY()<452){
                    JuegoPlataforma.musicOn=!JuegoPlataforma.musicOn;
                }else if(me.getY()>452 && me.getY()<500){
                    JuegoPlataforma.langIndex++;
                    if(JuegoPlataforma.langIndex >=JuegoPlataforma.menúPrinc.length)
                        JuegoPlataforma.langIndex=0;
                }
            }
        }
    }
    /**
     * Sobreescritura del método {@link java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent) }.
     * @param me evento del mouse detectado
     */
    @Override
    public void mousePressed(MouseEvent me) {}
    /**
     * Sobreescritura del método {@link java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent) }.
     * @param me evento del mouse detectado
     */
    public void mouseReleased(MouseEvent me) {
    }
    /**
     * Sobreescritura del método {@link java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent) }.
     * @param me evento del mouse detectado
     */
    @Override
    public void mouseEntered(MouseEvent me) {}
    /**
     * Sobreescritura del método {@link java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent) }.
     * @param me evento del mouse detectado
     */
    @Override
    public void mouseExited(MouseEvent me) {}
    /**
     * Sobreescritura del método {@link java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent) }.
     * @param me evento del mouse detectado
     */
    @Override
    public void mouseDragged(MouseEvent me) {}
    /**
     * Sobreescritura del método {@link java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent) }.
     * <p>
     * Dependiendo del punto en el cual se detecte el evento, el programa responderá. Por ejemplo, si el mouse es movido 
     * en la zona cubierta por el botón de un menú, este será resaltado.
     * </p>
     * @param me evento del mouse detectado
     */
    @Override
    public void mouseMoved(MouseEvent me) {
        if(JuegoPlataforma.etapa==EtapaJuego.menúPrinc){
            if(me.getX()>319 && me.getX()<771){
                JuegoPlataforma.menúAreaX=319;
                if(me.getY()>338 && me.getY()<386){
                    JuegoPlataforma.menúAreaY=338;
                }else if(me.getY()>386 && me.getY()<434){
                    JuegoPlataforma.menúAreaY=386;
                }else if(me.getY()>434 && me.getY()<482){
                    JuegoPlataforma.menúAreaY=434;
                }else if(me.getY()>482 && me.getY()<530){
                    JuegoPlataforma.menúAreaY=482;
                }else{
                    JuegoPlataforma.menúAreaY=-1;
                }
            }else{
                JuegoPlataforma.menúAreaX=-1;
            }
        }else if(JuegoPlataforma.etapa==EtapaJuego.menúJuego){
            if(me.getX()>319 && me.getX()<771){
                JuegoPlataforma.menúAreaX=319;
                if(me.getY()>300 && me.getY()<348){
                    JuegoPlataforma.menúAreaY=300;
                }else if(me.getY()>348 && me.getY()<396){
                    JuegoPlataforma.menúAreaY=348;
                }else if(me.getY()>396 && me.getY()<444){
                    if(JuegoPlataforma.handler.nivel!=null)
                        JuegoPlataforma.menúAreaY=396;
                    else
                        JuegoPlataforma.menúAreaY=-1;;
                }else if(me.getY()>444 && me.getY()<492){
                    JuegoPlataforma.menúAreaY=444;
                }else if(me.getY()>492 && me.getY()<540){
                    JuegoPlataforma.menúAreaY=492;
                }else{
                    JuegoPlataforma.menúAreaY=-1;
                }
            }else{
                JuegoPlataforma.menúAreaX=-1;
            }
        }else if(JuegoPlataforma.etapa==EtapaJuego.créditos){
            if(me.getX()>319 && me.getX()<771){
                JuegoPlataforma.menúAreaX=319;
                if(me.getY()>246 && me.getY()<294){
                    JuegoPlataforma.menúAreaY=246;
                }else{
                    JuegoPlataforma.menúAreaY=-1;
                }
            }else{
                JuegoPlataforma.menúAreaX=-1;
            }
        }else if(JuegoPlataforma.etapa==EtapaJuego.ajustes){
            if(me.getX()>319 && me.getX()<771){
                JuegoPlataforma.menúAreaX=319;
                if(me.getY()>298 && me.getY()<346){
                    JuegoPlataforma.menúAreaY=298;
                }else if(me.getY()>356 && me.getY()<404){
                    JuegoPlataforma.menúAreaY=356;
                }else if(me.getY()>404 && me.getY()<452){
                    JuegoPlataforma.menúAreaY=404;
                }else if(me.getY()>452 && me.getY()<500){
                    JuegoPlataforma.menúAreaY=452;
                }else{
                    JuegoPlataforma.menúAreaY=-1;
                }
            }else{
                JuegoPlataforma.menúAreaX=-1;
            }
        }
    }
}