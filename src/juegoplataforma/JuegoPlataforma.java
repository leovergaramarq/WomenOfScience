package juegoplataforma;

import entity.Entidad;
import gfx.Sprite;
import gfx.SpriteSheet;
import input.KeyInput;
import input.MouseInput;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import entity.modelo.EntityModel;
import java.io.File;
//import jdk.nashorn.internal.codegen.CompilerConstants;

/**
 * 
 * Clase principal {@link JuegoPlataforma}, encargada de dirigir a las demás clases.
 * <p>
 * JuegoPlataforma posee el método main() y el hilo de ejeución principal. Esta clase maneja la renderización 
 * del juego, al igual que la velocidad de ejecución. Contiene información fundamental para la ejecución del juego.
 * </p>
 * @see java.awt.Canvas
 * @see javax.swing.JFrame
 * @version 1.0
 * @author Leonardo Aguilera, Leonardo Lizcano, Leonardo Vergara, Henry Caicedo, Fernando Acuña
 * 
 */
public class JuegoPlataforma extends Canvas implements Runnable {
    private Thread thread;
    public static final int WIDTH = 270;
    public static final int HEIGHT = WIDTH / 14 * 10;
    public static final int SCALE = 4;
    private static final int FPS=60;
    public static final String TITLE = "Women Of Science";
    
    public static int langIndex, creditosIndex;
    private static boolean running;
    public static boolean pausado;
    public static boolean audioOn;
    public static boolean musicOn;
    
    public static EtapaJuego etapa;
    public static EntityModel playerModel;
    public static EntityModel[] entityModels;
    public static Handler handler;
    
    public static String[] fondosNombres={"estacion_espacial1.jpg","estacion_espacial2.jpg","espacio.jpg","tierra.jpg",
        "occidental.jpg","muro.jpg","pruebaGimp.jpg"};
    public static String[] soundTracks={"bensound-inspire.wav","bensound-thejazzpiano.wav","Scheming Weasel Faster.wav"};
    public static BufferedImage[] nivelesMapeo;
    public static BufferedImage Wstem;
    public static BufferedImage fondoJuego;
    public static BufferedImage pausa;
    public static BufferedImage menúFondo;
    public static BufferedImage menúPrinc[];
    public static BufferedImage menúJuego[];
    public static BufferedImage menúAjustes[];
    public static BufferedImage menúCréditos[];
    public static BufferedImage menúAudioOn;
    public static BufferedImage menúAudioOff;
    public static BufferedImage menúMusicOn;
    public static BufferedImage menúMusicOff;
    public static BufferedImage menúArea;
    public static BufferedImage loadBar;
    public static BufferedImage progress;
    public static BufferedImage marco;
    public static int menúAreaX;
    public static int menúAreaY;

    public static Cámara cam;
    
    public static SpriteSheet mix;
    public static Sprite muro;
    public static Sprite concreto;
    public static Sprite concretoPasto;
    public static Sprite madera;
    public static Sprite puertaGreen;
    public static Sprite puertaRed;
    public static Sprite llave;
    public static Sprite diamante;
    public static BufferedImage casa;
    public static BufferedImage edificio;
    public static BufferedImage iglesia;
    
    public static Sprite maderaRobusta;
    public static BufferedImage madera2doPiso;
    public static BufferedImage casa1;
    public static BufferedImage casa2;
    public static BufferedImage[] fondoNubes;
    public static SpriteSheet tecnologia;
    public static Sprite concretoPiedra;
    public static Sprite paredX;
    public static Sprite yerba;
    public static Sprite tierra;
    public static Sprite valla;
    //tercer nivel
    public static Sprite paredY;
    public static Sprite flechaDerecha;
    public static Sprite flechaArriba;
    public static Sprite flechaIzquierda;
    public static Sprite pantallaPlasma;
    public static Sprite concretoOndulado;
    public static Sprite computadoraBotones;
    public static Sprite izqIBM;
    public static Sprite drIBM;
    public static Sprite mesa;

    public static Sonido musicMenú, musicJuego;
    
    private static String fps, ups;
    public static int nivel;
    public static float percent;
    /**
     * Crea un objeto de tipo {@link JuegoPlataforma}.
     * <p>
     * Define las dimensiones de la ventana del juego.
     * </p>
     * @see java.awt.Dimension
     */
    public JuegoPlataforma() {
        running=false;
        Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }
    /**
     * Inicializa el hilo principal en caso de que el juego no se encuentre en ejecución.
     * Llama al método {@link java.lang.Thread#start() }.
     */
    private synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this, "Thread");
        thread.start();
    }
    /**
     * Detiene el hilo principal en caso de que el juego se encuentre en ejecución.
     * Llama al método método {@link java.lang.Thread#join() }.
     */
    private synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            thread.join();

        } catch (InterruptedException ex) {
            Logger.getLogger(JuegoPlataforma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sobreescitura del método {@link java.lang.Runnable#run() }. Invoca al método {@link #init() } para inicializar e instanciar atributos
     * de la clase JuegoPlataforma.
     * <p>
     * Emplea la constante {@link #FPS } (el número de frames por segundo de la ejecución del juego) 
     * para determinar la frecuencia con la que se llama al método {@link #tick()}. Este método invoca constantemente a 
     * {@link #render() } para producir la renderización del juego.
     * </p>
     * 
     * @see #render()
     * @see #tick()
     */
    @Override
    public void run() {
        init();
        requestFocus();
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double delta = 0.0;
        double ns = 1000000000.0/FPS;
        int frames = 0;
        int ticks = 0;
        while (running) {
            if(etapa==EtapaJuego.loading || etapa==EtapaJuego.juego){
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                while (delta >= 1) {
                    //if(etapa==EtapaJuego.juego || etapa==EtapaJuego.loading)
                    tick();
                    ticks++;
                    delta--;
                }
                frames++;
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    fps = String.valueOf(frames);
                    ups = String.valueOf(ticks);
                    //System.out.println(frames + " fps, " + ticks + " ups");
                    frames = 0;
                    ticks = 0;
                }
            }
            render();
        }
        stop();
    }
    /**
     * Produce el movimiento de los elementos del juego (objetos, entidades, diálogos).
     * <p>
     * Llama al método {@link Handler#tick() } para generar el movimiento de todos los objetos y entidades del juego, 
     * y a {@link Cámara#tick(entity.Entidad) } para generar el movimiento de la cámara hacia la posición del jugador.
     * </p>
     * 
     */
    public synchronized void tick() {
        if(etapa==EtapaJuego.loading){
            if(nivel==0)
                percent+=0.4;
            else
                percent+=0.8;
            
            if(percent>=100){
                percent=0;
                if(nivel!=0)
                    etapa=EtapaJuego.juego;
                else{
                    handler.nivel=null;
                    etapa=EtapaJuego.créditos;
                }
                    
            }
        }else if(etapa==EtapaJuego.juego){
            if(pausado) return;
            handler.tick();
            cam.tick(handler.jugador);
        }
    }
    /**
     * Lleva a cabo la renderización de los elementos del juego (objetos, entidades, diálogos).
     * <p>
     * Hace uso de la clase {@link java.awt.Graphics} para dibujar dichos elementos en la ventana. 
     * Llama al método {@link Handler#render(java.awt.Graphics, java.awt.Graphics) } para generar el movimiento 
     * de todos los objetos y entidades del juego, y a {@link java.awt.Graphics#translate(int, int)  } para posicionar 
     * al canvas en las coordenadas del jugador.
     * </p>
     * 
     * @see java.awt.image.BufferStrategy
     */
    public synchronized void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics(), g2 = bs.getDrawGraphics();
        g2.setColor(Color.WHITE);
        g2.setFont( new Font( "Tahoma", Font.BOLD, 20 ) );
        //g: Para la ventana
        //g2: Para el canvas
        if(etapa!=EtapaJuego.juego && etapa!=EtapaJuego.loading){
            g.drawImage(menúFondo,0, 0, getWidth(), getHeight(),null);
            switch (etapa){
                case menúPrinc:
                    g2.drawImage(menúPrinc[langIndex],0, 0, getWidth(), getHeight(),null);
                    g2.drawString("DISEÑO CON FINES ACADÉMICOS", getWidth()-580, getHeight()-110);
                    g2.drawString("(NO COMERCIAL) PARA PROYECTO", getWidth()-600, getHeight()-80);
                    g2.drawImage(Wstem,getWidth()-230, getHeight()-150, 180, 100,null);
                    break;
                case menúJuego:
                    g2.drawImage(menúJuego[langIndex],0, 0, getWidth(), getHeight(),null);
                    break;
                case ajustes:
                    g2.drawImage(menúAjustes[langIndex],0, 0, getWidth(), getHeight(),null);
                    if(musicOn)
                        g2.drawImage(menúMusicOn, 0, 0, getWidth(), getHeight(),null);
                    else
                        g2.drawImage(menúMusicOff, 0, 0, getWidth(), getHeight(),null);

                    if(audioOn)
                        g2.drawImage(menúAudioOn, 0, 0, getWidth(), getHeight(),null);
                    else
                        g2.drawImage(menúAudioOff, 0, 0, getWidth(), getHeight(),null);
                    break;
                case créditos:
                    g2.drawImage(menúCréditos[creditosIndex],0, 0, getWidth(), getHeight(),null);
            }
            if(menúAreaX!=-1 && menúAreaY!=-1)
                g2.drawImage(menúArea,menúAreaX, menúAreaY, 452, 48 ,null);
            
            musicJuego.stop();
            
            if(musicOn)
                musicMenú.play();
            else
                musicMenú.stop();
            
        }else if(etapa==EtapaJuego.loading){
            g2.drawImage(menúFondo,0, 0, getWidth(), getHeight(),null);
            g2.drawImage(loadBar,200,326,740,160,null);
            g2.drawImage(progress,230,351,(int)(percent*34/5),110,null);
            String s;
            if(nivel!=0){
                switch(langIndex){
                case 0:
                    s="Cargando nivel "+nivel+"..."; break;
                case 1:
                    s="Loading level "+nivel+"..."; break;
                default:
                    s="Le niveau "+nivel+" se charge...";
                }
                g2.drawString(s, 454, 295);
            }else{
                String s2;
                switch(langIndex){
                case 0:
                    s="Gracias por jugar Women Of Science.";
                    s2="A continuación, los créditos."; break;
                case 1:
                    s="Thanks for playing Women Of Science.";
                    s2="Loading credits."; break;
                default:
                    s="Merci d'avoir joué à Women Of Science.";
                    s2="Chargement des crédits.";
                }
                g2.drawString(s, 395, 295);
                g2.drawString(s2, 424, 520);
            }
            musicMenú.stop();
            musicJuego.stop();
        }else if(etapa==EtapaJuego.juego){
            g2.drawImage(fondoJuego,0, 0, getWidth(), getHeight(),null);
            g.translate(cam.getX(), cam.getY());
            handler.render(g,g2);
            drawInfo(g2);
            
            musicMenú.stop();
            if(!pausado){
                if(musicOn)
                    musicJuego.play();
                else
                    musicJuego.stop();
            }else{
                g2.drawImage(pausa,getFrameWidth()/2-200, getFrameHeight()/2-200, 400,400,null);
                musicJuego.stop();
            }
        }
        bs.show(); bs.dispose();
    }
    /**
     * Reinicia la música del juego.
     */
    public static void restartMusic(){
        if(musicOn)
            musicJuego.restart();
    }
    /**
     * Reinicia la música de los menús.
     */
    public static void restartMenuMusic() {
        if(musicOn)
            musicMenú.restart();
    }
    /**
     * Dibuja información en la ventana.
     * @param g objeto de tipo Graphics que se usará para imprimir dicha información
     */
    public void drawInfo(Graphics g){
        if(handler.nivel.etapa==EtapaNivel.cinematica) return;
        String info;
        switch(langIndex){
            case 0:
                info="NIVEL: "+nivel+".";
                break;
            case 1:
                info="LEVEL: "+nivel+".";
                break;
            default:
                info="NIVEAU: "+nivel+".";
        }
        g.drawString("FPS: "+fps+", UPS: "+ups, 10, 20);
        g.drawString(info, 10, 50);
        g.drawImage(diamante.image,10, 62, 40, 40,null);
        g.drawString(String.valueOf(handler.jugador.numDiamantes)+".", 60, 90);
        if(handler.jugador.tieneLlave)
            g.drawImage(llave.image,10, 100, 40, 40,null);
    }
    /**
     * Retorna el tamaño horizontal total de la ventana, medido en pixeles.
     * @return ancho total de la ventana
     */
    public static int getFrameWidth() {
        return WIDTH * SCALE;
    }
    /**
     * Retorna el tamaño vertical total de la ventana, medido en pixeles.
     * @return alto total de la ventana
     */
    public static int getFrameHeight() {
        return HEIGHT * SCALE;
    }
    /**
     * Inicializa los principales atributos de la clase.
     * <p>
     * Añade los KeyListener, MouseListener y MouseMotionLister. Inicializa los spritesheets, sprites e imágenes en general
     * con su respectiva ruta dentro de los paquetes de recursos.
     * </p>
     * <p>
     * Llama al método {@link #imageFrom(java.lang.String) } para inicializar a los {@link java.awt.image.BufferStrategy} 
     * y atrapar a cualquier posible excepción.
     * </p>
     * @see #initMenús(java.lang.String, int) 
     * @see gfx.Sprite
     * @see gfx.SpriteSheet
     * @see java.awt.image.BufferedImage
     */
    private void init() {
        etapa=EtapaJuego.menúPrinc;
        pausado=false;
        percent=creditosIndex=langIndex=nivel=0;
        menúAreaX=-1; menúAreaY=-1;
        
        addKeyListener(new KeyInput());
        addMouseListener(new MouseInput());
        addMouseMotionListener(new MouseInput());
        
        playerModel=new EntityModel("\\Spritesheets\\woman.png","\\Spritesheets\\woman_face.png");
        entityModels=new EntityModel[6];
        entityModels[0]=new EntityModel("\\Spritesheets\\male_adventurer.png","\\Spritesheets\\male_adventurer_face.png");
        entityModels[1]=new EntityModel("\\Spritesheets\\female_adventurer.png","\\Spritesheets\\female_adventurer_face.png");
        entityModels[2]=new EntityModel("\\Spritesheets\\man.png","\\Spritesheets\\man_face.png");
        entityModels[3]=new EntityModel("\\Spritesheets\\zombie.png","\\Spritesheets\\zombie_face.png");
        entityModels[4]=new EntityModel("\\Spritesheets\\robot.png","\\Spritesheets\\robot_face.png");
        entityModels[5]=new EntityModel("\\Spritesheets\\man.png","\\Spritesheets\\man_face.png");//Genérico
        
        handler = new Handler();
        handler.setEntidades();
        
        mix = new SpriteSheet("\\Spritesheets\\mix_1.png");
        cam = new Cámara();
        muro = new Sprite(mix, 5, 5, 64, 64);
        puertaGreen = new Sprite(mix, 9, 6, 64, 64*2);
        puertaRed = new Sprite(mix, 10, 6, 64, 64*2);
        llave = new Sprite(mix, 9, 5, 64, 64);
        diamante = new Sprite(mix, 8, 3, 64, 64);
        concreto = new Sprite(mix, 4, 2, 64, 64);
        concretoPasto = new Sprite(mix, 1, 2, 64, 64);
        concretoPiedra = new Sprite(mix, 4, 5, 64, 64);
        madera = new Sprite(mix, 4, 4, 64, 64);
        
        iglesia = imageFrom("\\Spritesheets\\iglesia.png");
        edificio = imageFrom("\\Spritesheets\\edificio.png");
        casa = imageFrom("\\Spritesheets\\casa.png");
        
        fondoNubes=new BufferedImage[3];
        for(int i=0; i<3; i++)
            fondoNubes[i]=imageFrom("\\Spritesheets\\fondoNubes"+(i+1)+".png");
        
        casa1=imageFrom("\\Spritesheets\\casa1.png");
        casa2=imageFrom("\\Spritesheets\\casa2.png");
        maderaRobusta= new Sprite(mix,5,6,64,64);
        madera2doPiso= imageFrom("\\Spritesheets\\FondoMaderaCasa.png");
        
        yerba = new Sprite(mix, 3, 6, 64, 64);
        tierra = new Sprite(mix, 1, 1, 64, 64);
        valla = new Sprite(mix, 1, 5, 64, 64);
        //tercer nivel
        tecnologia = new SpriteSheet("\\Spritesheets\\tecnologia.png");
        mesa = new Sprite(mix, 2, 5, 64, 64);
        paredX = new Sprite(tecnologia, 1, 1, 64, 64);
        paredY = new Sprite(tecnologia, 7, 8, 64, 64);
        flechaDerecha = new Sprite(tecnologia, 8, 1, 64, 64);
        flechaIzquierda = new Sprite(tecnologia, 8, 2, 64, 64);
        flechaArriba = new Sprite(tecnologia, 8, 3, 64, 64);
        pantallaPlasma = new Sprite(tecnologia, 7, 7, 64, 64);
        concretoOndulado = new Sprite(tecnologia, 2, 4, 64, 64);
        computadoraBotones = new Sprite(tecnologia, 6, 4, 64, 64);
        izqIBM = new Sprite(tecnologia, 4, 1, 64 * 2, 64);
        drIBM = new Sprite(tecnologia, 4, 2, 64 * 2, 64);
        nivelesMapeo = new BufferedImage[6];
        for(int i=0; i<nivelesMapeo.length; i++)
            nivelesMapeo[i]=imageFrom("\\Niveles\\Nivel_"+(i+1)+"\\mapeo.png");
        
        Wstem=imageFrom("\\Menús/UI\\Wstem1.png");
        //menúFondo=imageFrom("/Menús/UI/fondo.png"); //:v
        menúFondo=imageFrom("\\Menús\\UI\\fondo_espacio.jpg");
        menúArea=imageFrom("\\Menús\\area.png");
        menúAudioOn=imageFrom("\\Menús\\UI\\menu_ajustes\\audio_on.png");
        menúAudioOff=imageFrom("\\Menús\\UI\\menu_ajustes\\audio_off.png");
        menúMusicOn=imageFrom("\\Menús\\UI\\menu_ajustes\\musica_on.png");
        menúMusicOff=imageFrom("\\Menús\\UI\\menu_ajustes\\musica_off.png");
        menúPrinc=new BufferedImage[3];
        menúJuego=new BufferedImage[3];
        menúAjustes=new BufferedImage[3];
        menúCréditos=new BufferedImage[2];
        initMenús("espanol", 0);
        initMenús("ingles", 1);
        initMenús("frances", 2);
        menúCréditos[0]=imageFrom("\\Menús\\UI\\cred1.png");
        menúCréditos[1]=imageFrom("\\Menús\\UI\\cred2.png");
        
        pausa=imageFrom("\\Menús\\pausaEs.png");
        musicMenú=new Sonido("\\Sonidos\\Blue Bird.wav");
        musicJuego=new Sonido("\\Sonidos\\Scheming Weasel Faster.wav");
        musicOn=audioOn=true;
        
        loadBar=imageFrom("\\Niveles\\loadBar.png");
        progress=imageFrom("\\Niveles\\progress.png");
        marco=imageFrom("\\Spritesheets\\marco1.png");
    }
    /**
     * Inicializa las impáenes los menús de acuerdo al idioma que se pase como parámetro. 
     * <p>
     * Llama al método {@link #imageFrom(java.lang.String) } para inicializar a los {@link java.awt.image.BufferStrategy} 
     * y atrapar a cualquier posible excepción.
     * </p>
     * @param idioma nombre de la carpeta contenedora de los archivos de imagen para los menús.
     * @param i índice del idioma: 0==español, 1==inglés, 2==francés.
     * 
     */
    private void initMenús(String idioma, int i){
        menúPrinc[i]=imageFrom("\\Menús\\UI\\"+idioma+"\\menu_principal.png");
        menúJuego[i]=imageFrom("\\Menús\\UI\\"+idioma+"\\menu_jugar.png");
        menúAjustes[i]=imageFrom("\\Menús\\UI\\"+idioma+"\\menu_ajustes.png");
    }
    
    /**
     * Inicializa atributos de tipo {@link java.awt.image.BufferedImage}. Si ocurre una excepción de tipo 
     * {@link java.io.IOException}, esta es atrapada y se retorna null.
     * 
     * @param ruta dirección de disco de la imagen dentro de su paquete
     * @return un {@link java.awt.image.BufferedImage} de acuerdo a la dirección que reciba como parámetro, o null
     */
    protected BufferedImage imageFrom(String ruta){
        try {
            return ImageIO.read(new File("src\\res"+ruta));
        } catch (IOException ex) {
            Logger.getLogger(Entidad.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    /**
     * Asigna al atributo {@link #fondoJuego} una imagen de acuerdo al índice que recibe como parámetro.
     * <p>
     * El método busca en el paquete de recursos la imagen cuyo nombre es igual al valor de {@link #fondosNombres} en la 
     * posición i.
     * </p>
     * @param i índice del vector {@link #fondosNombres}
     * @see Nivel#initConfig() 
     */
    public static void setFondo(int i) {
        File im=new File("src\\res\\Fondos",fondosNombres[i-1]);
        if (im.exists()) {
            try {
                fondoJuego=ImageIO.read(im.toURI().toURL());
                System.out.println("nice");
            } catch (IOException ex) {
                Logger.getLogger(JuegoPlataforma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * Asigna al atributo {@link #fondoJuego} una imagen de acuerdo al nombre del archivo de imagen que recibe como 
     * parámetro.
     * @param arch nombre de la imagen que se colocará como fondo
     * @see Nivel#initConfig() 
     */
    static void setFondo(String arch){
        File im=new File("src\\res\\Fondos",arch);
        
        if (im.exists()) {
            try {
                fondoJuego=ImageIO.read(im.toURI().toURL());
                System.out.println("nice");
            } catch (IOException ex) {
                Logger.getLogger(JuegoPlataforma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            javax.swing.JOptionPane.showMessageDialog(null, "Fondo eliminado");
            setFondo(3);
        }
    }
    /**
     * Asigna al atributo {@link #musicJuego} una nueva instancia de tipo {@link Sonido} con el sonido.
     * <p>
     * El método busca en el paquete de recursos el archivo de sonido cuyo nombre es igual al valor de 
     * {@link #soundTracks} en la posición i.
     * </p>
     * @param i índice del vector {@link #soundTracks}
     * @see Nivel#initConfig() 
     */
    public static void selectMusic(int i){
        musicJuego.stop();
        System.out.println("i: "+i);
        musicJuego=new Sonido("\\Sonidos\\"+soundTracks[i]);
        //musicJuego.play();
    }
    /**
     * Asigna al atributo {@link #musicJuego} una nueva instancia de tipo {@link Sonido}.
     * <p>
     * El método busca en el paquete de recursos el archivo de sonido cuyo nombre es igual a la cadena recibida por 
     * parámetro.
     * </p>
     * @param di nombre del archivo que se colocará como música de fondo
     * @see Nivel#initConfig() 
     */
    static void selectMusic(String di){
        musicJuego.stop();
        //System.out.println("i: "+i);
        if((new File("src\\res\\Sonidos",di)).exists()){
            System.out.println("sonidooooo "+di);
            musicJuego=new Sonido("\\Sonidos\\"+di);
            //selectMusic(4);
        }
        else selectMusic(4);
        //musicJuego=new Sonido("/Sonidos/"+soundTracks.get(i));
        //musicJuego.play();
    }
    /**
     * Crea un objeto {@link javax.swing.JFrame}, le añade un canvas de tipo {@link JuegoPlataforma} y llama al método 
     * {@link #start() } para iniciar el hilo.
     * 
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        JuegoPlataforma game = new JuegoPlataforma();
        JFrame frame = new JFrame(TITLE);
        //frame.setLayout();
        //-------------------------------------------------------------------------------------
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        /*javax.swing.GroupLayout layout = new javax.swing.GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );*/
        //--------------------------------------------------------------------------------------
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.start();
    }
}