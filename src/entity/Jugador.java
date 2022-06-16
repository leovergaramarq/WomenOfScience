package entity;

import entity.modelo.EntityModel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import juegoplataforma.Id;
import objeto.Objeto;
import juegoplataforma.JuegoPlataforma;
import juegoplataforma.Nivel;

/**
 * 
 * Clase del personaje principal, que hereda de la clase {@link Entidad}.
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class Jugador extends Entidad{
    
    public int frameDelay, numDiamantes, frame;
    public boolean animate, tieneLlave, hack;
    public Npc npcTalk;
    public BufferedImage face;
    
    /**
     * Crea al personaje principal, el cual es independiente de los niveles. Para esto, se vale del constructor 
     * {@link Entidad#Entidad(int, int, boolean, juegoplataforma.Id, juegoplataforma.Nivel) } de su clase padre.
     * <p>
     * Invoca a {@link #initSprites(java.lang.String) } para llenar el arreglo unidimensional de sprites..
     * </p>
     * @param path dirección de disco del spritesheet del personaje principal
     * @param pathFace dirección de disco de la cara del personaje principal
     * @param with ancho en pixeles del personaje principal
     * @param height alto en pixeles del personaje principal
     */
    public Jugador(String path, String pathFace, int with, int height){
        super(with, height, true, Id.jugador, null);
        frame=frameDelay=numDiamantes=0;
        hack=animate=tieneLlave=false;
        modelo = new EntityModel(path, pathFace);
    }
    /**
     * Crea un nuevo NPC con su modelo de NPC.
     * @param modelo {@link #modelo} del NPC
     * @param width ancho en pixeles del NPC
     * @param height alto en pixeles del NPC
     * @see entity.modelo.EntityModel
     */
    public Jugador(EntityModel modelo, int width, int height) {
        super(width, height, true, Id.jugador, null);
        frame=frameDelay=0;
        hack=animate=tieneLlave=false;
        this.modelo=modelo;
    }
    /*
    public void enRango(double r, Nivel nv){
        int c=0;
        for(Npc n: nv.npcs){
            if(n.isVisible() && handler.distanciaEntre(this,n)<=r){
                //System.out.println("N:  "+n);
                npcTalk=n;
                return;
            }
            c++;
        }
        npcTalk=null;
    }
    */
    /**
     * Determina, de los NPCs, cuál de ellos se encuentra dentro de un rango específico a la redonda del personaje 
     * principal. Si el método encuentra a más de un NPC, esta tomará al más cercano.
     * 
     * @param r la distancia a la cual se considera a los NPCs dentro del rango
     * @param nv el nivel del cual se tienen en cuenta los NPCs
     * @see juegoplataforma.Handler#distanciaEntre(entity.Entidad, entity.Entidad) 
     */
    public void enRango(double r, Nivel nv){
        npcTalk=null;
        double dist=r;
        for(Npc n: nv.npcs)
            if(n.isVisible() && n.id!=Id.NPCgenérico){
                n.setEnRango(false);
                double tempDist=handler.distanciaEntre(this,n);
                if(tempDist<=dist){
                    dist=tempDist;
                    npcTalk=n;
                }
            }
        if(npcTalk!=null)
            npcTalk.setEnRango(true);
    }
    /**
     * Dadas 3 posibles posiciones en el vector de sprites, el método asigna uno "aleatoriamente" al personaje principal.
     * @param a primer posible sprite
     * @param b segundo posible sprite
     * @param c tercer posible sprite
     * @see java.lang.Math#random() 
     */
    public void setRandomSprite(int a, int b, int c){
        int i;
        do{
            i=(int)(Math.random()*10);
        }while(!(i==a/10 || i==b/10 || i==c/10));
        
        if(i==a/10) spriteTemp=a;
        else if(i==b/10) spriteTemp=b;
        else spriteTemp=c;
    }
    /**
     * Lleva a cabo la renderización del personaje principal.
     * <p>
     * Dependiendo de su estado, se dibujará un sprite u otro.
     * Este método llama a {@link #enRango(double, juegoplataforma.Nivel) } para hallar al NPC más cercano.
     * </p>
     * @param g gráficos empleados para dibujar al personaje
     */
    @Override
    public void render(Graphics g) {
        enRango(200, handler.nivel);
        switch (estado) {
            case onFloor:
                switch(facing){
                    case 0:
                        if(agachado)
                            g.drawImage(modelo.sprites[55].getBufferedImage(), x, y, width, height, null);
                        else{
                            if(moving)
                                g.drawImage(modelo.sprites[89-frame*10].getBufferedImage(), x, y, width, height, null);
                            else
                                g.drawImage(modelo.sprites[spriteTemp].getBufferedImage(), x, y, width, height, null);
                        }
                        break;
                    case 1:
                        if(agachado)
                            g.drawImage(modelo.sprites[30].getBufferedImage(), x, y, width, height, null);
                        else
                            if(moving)
                                g.drawImage(modelo.sprites[frame*10+4].getBufferedImage(), x, y, width, height, null);
                            else
                                g.drawImage(modelo.sprites[spriteTemp].getBufferedImage(), x, y, width, height, null);
                }   break;
            case jumping:
                switch(facing){
                    case 0:
                        g.drawImage(modelo.sprites[75].getBufferedImage(), x, y, width, height, null);
                        break;
                    case 1:
                        g.drawImage(modelo.sprites[10].getBufferedImage(), x, y, width, height, null);
                }   break;
            case falling:
                int i;
                switch(facing){
                    case 0:
                        i=65;
                        if(aceleración>=10) i-=20;
                        if(aceleración>=20) i+=39;
                        g.drawImage(modelo.sprites[i].getBufferedImage(), x, y, width, height, null);
                        break;
                    case 1:
                        i=20;
                        if(aceleración>=10) i+=20;
                        if(aceleración>=20) i-=31;
                        g.drawImage(modelo.sprites[i].getBufferedImage(), x, y, width, height, null);
                }
        }
    }
    /**
     * Produce el movimiento del jugador.
     * <p>
     * Este método detecta a los elementos sólidos del mapa para generar colisiones, maneja la gravedad, 
     * la aceleración del jugador y sus saltos.
     * </p>
     */
    @Override
    public synchronized void tick() {
        x+=velX;
        y+=velY;
        
        moving = velY!=0 || velX!=0;
        if(velX!=0) agachado=false;
        
        if (x <= 0) x=0;
       //if (y <= 0)y=0;
       
       //**************************************tamaño x MAX*************************************************
        if (x+width >= 99999999) x= 1080-width;
        //**************************************tamaño y MAX*************************************************
        if (y+height >= 999999) y=771-height;
        animate = velX != 0;
        
        if(estado!=Estado.jumping) estado=Estado.falling;
        
        for(Objeto t: handler.nivel.objetos){
            if (t.isSolid()){
                if (getBoundsTop().intersects(t.getBounds())) {
                    setVelY(0);
                    if (estado==Estado.jumping) {
                        estado=Estado.falling;
                        aceleración = 0.0;
                    }
                }
                if(getBoundsBottom().intersects(t.getBounds())){
                    if(aceleración>=20) agachado=true;
                    setVelY(0);
                    if(estado!=Estado.jumping) estado=Estado.onFloor;
                    while(y+width>t.y) y--;
                }
                if (estado!=Estado.falling && estado!=Estado.jumping) {
                    aceleración = 0.0;
                    estado=Estado.onFloor;
                }
                if (getBoundsLeft().intersects(t.getBounds())) {
                    setVelX(0);
                    x = t.getX()+t.width;
                }
                if (getBoundsRight().intersects(t.getBounds())) {
                    setVelX(0);
                    x = t.getX()-t.width;
                }
            }else{
                if(getBoundsBottom().intersects(t.getBounds()) || getBoundsTop().intersects(t.getBounds())
                        || getBoundsLeft().intersects(t.getBounds()) || getBoundsRight().intersects(t.getBounds())){
                    
                    if(t.getId()==Id.diamante){//Diamante
                        if(x+width>t.x+36 && x<t.x+t.width-36 && y+height>t.y+36 && y<t.y+t.height-36){
                            objeto.Diamante d=((objeto.Diamante)t);
                            if(!d.isRecogido()){
                                d.setRecogido(true, this);
                                //numDiamantes++;
                                switch(JuegoPlataforma.nivel){
                                    case 3:
                                        if(numDiamantes==10)
                                            handler.nivel.actualizarNivel(null);
                                        break;
                                    case 5:
                                        if(!handler.nivel.hayDiamantes())
                                            handler.nivel.actualizarNivel(null);
                                        break;
                                }
                                
                            }
                        }
                    }else if(t.getId()==Id.llave){//Llave
                        objeto.Llave l=((objeto.Llave)t);
                        if(l.isDisponible()){
                            if(x+width>t.x+36 && x<t.x+t.width-36 && y+height>t.y+36 && y<t.y+t.height-36){
                                l.setRecogido(true);
                                tieneLlave=true;
                                l.puerta.setLocked(false);
                                l.puerta.setSolid(false);
                                l.setDisponible(false);
                            }
                        }
                    }
                    else if(t.getId()==Id.puerta){//Puerta
                        if(!((objeto.Puerta)t).locked){
                            if(JuegoPlataforma.nivel==1 || JuegoPlataforma.nivel==5){
                                handler.nextLevel();
                            }
                            if(tieneLlave)
                                tieneLlave=false;
                        }
                    }
                }
            }
        }
        for(Npc n: handler.nivel.npcs){
            if(n.isSolid()){
                if (getBoundsTop().intersects(n.getBounds())) {
                    setVelY(0);
                    if (estado==Estado.jumping) {
                        estado=Estado.falling;
                        aceleración = 0.0;
                    }
                }
                if(getBoundsBottom().intersects(n.getBounds())){
                    if(aceleración>=20) agachado=true;
                    setVelY(0);
                    if(estado!=Estado.jumping) estado=Estado.onFloor;
                    while(y+width>n.y) y--;
                }
                if (estado!=Estado.falling && estado!=Estado.jumping) {
                    aceleración = 0.0;
                    estado=Estado.onFloor;
                }
                if (getBoundsLeft().intersects(n.getBounds())) {
                    setVelX(0);
                    x = n.getX()+n.width;
                }
                if (getBoundsRight().intersects(n.getBounds())) {
                    setVelX(0);
                    x = n.getX()-n.width;
                }
            }
            
        }
        if(estado==Estado.jumping){
            aceleración-=0.2;
            setVelY((int)-aceleración);
            if (aceleración <= 0.0)
                estado=Estado.falling;
        }
        if(estado==Estado.falling){
            if(aceleración<22) aceleración+=0.2;
            
            setVelY((int) aceleración);
        }
        if(animate){
            frameDelay++;
            if (frameDelay >= 6 ) {
                frame++;
                if (frame>=8) frame = 0;
                frameDelay =0;
            }
        }
    }
}