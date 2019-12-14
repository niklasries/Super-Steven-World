import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import javax.sql.rowset.CachedRowSet;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.stb.STBImage.*;


import static org.lwjgl.system.APIUtil.*;


import static org.lwjgl.system.MemoryUtil.*;


import org.lwjgl.glfw.*;
import org.lwjgl.system.*;


import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryStack.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;




public class MainWindow {


    private double startTime;
    private long   count;

    public double theTime;

    //create window dimensions
    public  int sceneW = 1200;
    public  int sceneH = 1200;

    public double curserX=0;
    public double curserY=0;

    public float camerX=0;
    public float cameraY=0;

    public int viewareaX=0;
    public int viewareaY=0;

    int charSize=150;

    Thread cam;

    public Texture text;
    public Texture text2;
    public TextureAtlas text1;
    public TextureAtlas textureAtlas;

    public TextureAtlas textureAtlas1;
    public int testcount;





    public int animationState=0;

    public SpriteAnimation walkDown;
    public SpriteAnimation walkLeft;
    public SpriteAnimation walkRight;
    public SpriteAnimation walkUp;

    public SpriteAnimation castDown;
    public SpriteAnimation castLeft;
    public SpriteAnimation castRight;
    public SpriteAnimation castUp;

    public SpriteAnimation attackDown;
    public SpriteAnimation attackLeft;
    public SpriteAnimation attackRight;
    public SpriteAnimation attackUp;
    MainWindow mw;

    ArrayList<Projectile> projectiles=new ArrayList<Projectile>();
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    ArrayList<Enemy> unModenemies;



    //controll agen
    Control c1;

    //
    public int frametoanimation=10;
    public int animation=0;

    //window handle
    public long window;

    //main game loop executed in run()
    public void run() {

        mw=this;

        startTime = System.currentTimeMillis() / 1000.0;

        //initialize the main window
        init();

        // run the main loop

        loop();


        // free window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        //terminate GLFW and free error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }
        private void init(){




            //setup error callback
        // print all errors in glfw in system.err
        GLFWErrorCallback.createPrint(System.err).set();

        //trow Exception if unable to initialize
        if (!glfwInit()) {
            throw new IllegalStateException("unable to initialize GLFW window");
        }

            //configure glfw
            //enable window hints
            glfwDefaultWindowHints();
            // make window invisible
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            //make the window resizeable
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

            //create the main window with the set dimensions an given title
            // TODO: 11/22/2019 extract title
            window = glfwCreateWindow(sceneW, sceneH, "Super(Steven)World", NULL, NULL);
            //throw Exception if not able to create window
            if (window == NULL)
                throw new RuntimeException("failed to create GLFW window");

            //keycallback called whenever key is pressed
            //set up the abort Key on Esc to close the window
            /*glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);

                }
                if (key == GLFW_KEY_UP && action == GLFW_RELEASE) {
                    testcount++;
                    if(testcount>text1.getCount())testcount=text1.getCount();

                }
                if (key == GLFW_KEY_DOWN && action == GLFW_RELEASE) {
                    testcount--;
                    if(testcount<=0)testcount=0;

                }

                if (key == GLFW_KEY_A && (action == GLFW_PRESS || action==GLFW_REPEAT)) {

                    camerX+=25*2;

                }
                if (key == GLFW_KEY_S &&( action == GLFW_PRESS|| action==GLFW_REPEAT)) {

                    cameraY-=25*2;

                }
                if (key == GLFW_KEY_D && (action == GLFW_PRESS|| action==GLFW_REPEAT)) {

                    camerX-=25*2;

                }
                if (key == GLFW_KEY_W && (action == GLFW_PRESS|| action==GLFW_REPEAT)) {

                    cameraY+=25*2;

                }
            });*/
            glfwSetCursorPosCallback(window, (window,cXpos,xYpos)->{
                curserX=cXpos;
                curserY=xYpos;

            });;




            //get the thread stack and push a new frame
            try (MemoryStack stack = stackPush()) {
                IntBuffer pWidth = stack.mallocInt(1);
                IntBuffer pHeight = stack.mallocInt(1);
                //get window size
                glfwGetWindowSize(window, pWidth, pHeight);

                //get resolution of primary display
                GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

                //center Window in main display
                glfwSetWindowPos(
                        window,
                        (vidmode.width() - pWidth.get(0)) / 2,
                        (vidmode.height() - pHeight.get(0)) / 2
                );

                //make gl context current
                glfwMakeContextCurrent(window);
                //enable v-sync
                glfwSwapInterval(1);
                //show the window
                glfwShowWindow(window);

               setIcon("files/steven.png");

                //initTestTexture();
            } catch (Exception e) {
                e.printStackTrace();
            }

            c1=new Control(window,this);





        }



    public void setCamerX(float camerX) {
        this.camerX += camerX;
    }

    public void setCameraY(float cameraY) {
        this.cameraY += cameraY;
    }

    public void spawnProjectiles(int previousAnimstate){

        Vec2D pDir;
        switch (previousAnimstate){
            case 1:pDir=new Vec2D(0,-1);break;
            case 2:pDir=new Vec2D(0,1);break;
            case 3:pDir=new Vec2D(-1,0);break;
            case 4:pDir=new Vec2D(1,0);break;
            default:pDir=new Vec2D(0,1);
        }
        
        projectiles.add(new Projectile(30,textureAtlas.getSubtexture(0),pDir,new Vec2D(camerX+charSize/2,cameraY+charSize/2),15));
        
    }

    public void castProjectiles(int previousAnimstate){

        Vec2D pDir;
        switch (previousAnimstate){
            case 1:pDir=new Vec2D(0,-1);break;
            case 2:pDir=new Vec2D(0,1);break;
            case 3:pDir=new Vec2D(-1,0);break;
            case 4:pDir=new Vec2D(1,0);break;
            default:pDir=new Vec2D(0,1);
        }

        projectiles.add(new Projectile(60,textureAtlas.getSubtexture(1),pDir,new Vec2D(camerX+charSize/3,cameraY+charSize/2),1));


    }

    private void loop(){
                //make ogl bindings available for use
                GL.createCapabilities();
                //set the clear color
                glClearColor(.0f,.0f,.1f,.0f);
                int count1=0;
                try {
                    //text=new Texture(new URL("https://raw.githubusercontent.com/wiki/mattdesl/lwjgl-basics/images/IGn1g.png"));
                    text1=new TextureAtlas(getClass().getClassLoader().getResourceAsStream("files/stevenSpritesheet.png"),64,64);
                    textureAtlas=new TextureAtlas(getClass().getClassLoader().getResourceAsStream("files/projectiles.png"),64,64);
                    textureAtlas1=new TextureAtlas(getClass().getClassLoader().getResourceAsStream("files/testanimation.png"),66,66);

                    text=new Texture(getClass().getClassLoader().getResourceAsStream("files/steven1.png"));
                    text2=new Texture(getClass().getClassLoader().getResourceAsStream("files/chessSet1.png"));

                    //text1.bind();
                    //text.bind();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                testcount=text1.getCount();


        walkDown=new SpriteAnimation(text1.getSpriteArray(13*10,13*10+7),true,1);
        walkDown.start();
        walkLeft=new SpriteAnimation(text1.getSpriteArray(13*9,13*9+7),true,1);
        walkLeft.start();
        walkRight=new SpriteAnimation(text1.getSpriteArray(13*11,13*11+7),true,1);
        walkRight.start();
        walkUp=new SpriteAnimation(text1.getSpriteArray(13*8,13*8+7),true,1);
        walkUp.start();

        castDown=new SpriteAnimation(text1.getSpriteArray(13*2,13*2+6),false,2);
        castDown.start();
        castLeft=new SpriteAnimation(text1.getSpriteArray(13*1,13*1+6),false,2);
        castLeft.start();
        castRight=new SpriteAnimation(text1.getSpriteArray(13*3,13*3+6),false,2);
        castRight.start();
        castUp=new SpriteAnimation(text1.getSpriteArray(13*0,13*0+6),false,2);
        castUp.start();

        attackDown=new SpriteAnimation(text1.getSpriteArray(13*14,13*14+5),false,3);
        attackDown.start();
        attackLeft=new SpriteAnimation(text1.getSpriteArray(13*13,13*13+5),false,3);
        attackLeft.start();
        attackRight=new SpriteAnimation(text1.getSpriteArray(13*15,13*15+5),false,3);
        attackRight.start();
        attackUp=new SpriteAnimation(text1.getSpriteArray(13*12,13*12+5),false,3);
        attackUp.start();



                //hide cursor
                glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

                //System.out.println(getClass().getClassLoader().getResourceAsStream("files/IGn1g.png").toString());

        cameraY=sceneH/2;
        camerX=sceneW/2;

        cam=new Thread(new Runnable() {

            @Override
            public void run() {
               // glfwMakeContextCurrent(window);
                //glfwMakeContextCurrent(window);

                while(!glfwWindowShouldClose(window)){

                    float bbposx=sceneW/2;
                    float bbposy=sceneH/2;
                    float bbposxx=600;
                    float bbposyy=300;

                    /*if(camerX+150<=bbposx-300){
                        System.out.println("collided in -x direction");
                        translate(viewareaX=-5,viewareaY,0);setCamerX(5);
                    }else{translate(viewareaX,viewareaY,0);}*/
                    if(camerX+charSize/2>=bbposx+300){
                        //System.out.println("collided in +x direction");
                        translate(viewareaX+=5*Control.speed,viewareaY,0);setCamerX(-5*Control.speed);
                        Enemy.offsetX=5*Control.speed;
                    }else{translate(viewareaX,viewareaY,0);Enemy.offsetX=0;}

                    if(camerX+charSize/2<=bbposx-300){
                        //System.out.println("collided in -x direction");
                        translate(viewareaX-=5*Control.speed,viewareaY,0);setCamerX(5*Control.speed);
                        Enemy.offsetX=5*Control.speed;
                    }else{translate(viewareaX,viewareaY,0);Enemy.offsetX=0;}

                    if(cameraY+charSize/2<=bbposy-150){
                        //System.out.println("collided in y direction");
                    translate(viewareaX,viewareaY-=5*Control.speed,0);setCameraY(5*Control.speed);
                        Enemy.offsetY=5*Control.speed;
                    }else{translate(viewareaX,viewareaY,0);Enemy.offsetY=0;}
                    if(cameraY+charSize/2>=bbposy+150){
                        //System.out.println("collided in -y direction");
                    translate(viewareaX,viewareaY+=5*Control.speed,0);setCameraY(-5*Control.speed);
                        Enemy.offsetY=5*Control.speed;
                    }else{translate(viewareaX,viewareaY,0);Enemy.offsetY=0;}

                    try {
                        Thread.sleep(16);                   } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });



cam.start();






        Thread enemiees = new Thread(new Runnable() {
            @Override
            public void run() {
                createEnemies();
                for (Enemy e:enemies
                ) {e.start();

                }
                while(!glfwWindowShouldClose(window)) {

                    try {
                        for (Enemy e : collisionProjectiles()
                        ) {
                            try {

                                enemies.remove(e);
                            } catch (Exception b) {
                            }

                        }
                        if (enemies.isEmpty()) {
                            createEnemies();
                            for (Enemy e : enemies
                            ) {
                                e.start();

                            }
                        }

                    }catch(Exception kp){}
                }

            }

            private void createEnemies() {
                for (int i = 0; i < 20000; i++) {
                    enemies.add(new Enemy(100,text1.getSubtexture(0),new Vec2D(0,0),new Vec2D((float)(Math.random()*1200*10),(float)(Math.random()*1200*10)),10,mw));

                }
            }


            private ArrayList<Enemy> collisionProjectiles() {
                ArrayList<Enemy>remove=new ArrayList<Enemy>();
                for (Projectile p: projectiles
                ) {

                    for (Enemy e:enemies
                    ) {


                        if (p.currPos.x < e.currPos.x + e.size &&
                                p.currPos.x + p.size > e.currPos.x &&
                                p.currPos.y < e.currPos.y + e.size &&
                                p.currPos.y + p.size > e.currPos.y) {

                            // collision detected!

                            //System.out.println("collided");
                            e.kill();
                            remove.add(e);
                        }
                    }

                }
                return remove;
            }
        });
enemiees.start();
        //while the window existent ent esc not pressed render
                while(!glfwWindowShouldClose(window)){

                    //take current time for frametime
                    long frametime =System.currentTimeMillis();

                    //listen for GL keyboard inputs
                    glfwPollEvents();

                    //todo clear buffers if exist
                    //someBuffer.clear()

                    updateWindowSize();
                    //clear the color buffer from previous frame
                    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
                    //swap color buffer
                    //glfwSwapBuffers(window);


                    //for 2D projection set the top left corner as origin for coordinates
                    glViewport(0, 0, sceneW , sceneH);
                    //
                    glMatrixMode(GL_PROJECTION);


                    glLoadIdentity();
                    //float aspect=sceneW/sceneH;


                    glOrtho(0, sceneW , sceneH,0,-1,1);



                    //glMatrixMode(GL_MODELVIEW);

                   //glLoadIdentity();


                    //



                    //todo draw stuff here




                        glTranslatef(viewareaX, viewareaY, 0);
                    textureTest(text2,0-viewareaX,0-viewareaY,10*sceneW,10*sceneH);


                        drawTest();


                        //System.out.println(camerX+" | "+cameraY);
                        textureAtlasTest(text1, camerX, cameraY, charSize, charSize);
                        textureTest(text,(float)curserX,(float)curserY, 100, 100);
                        
                        drawSubtextures(projectiles,textureAtlas);

                        unModenemies=(ArrayList<Enemy>)enemies.clone();
                        drawSubtextures(unModenemies,textureAtlas1,1);


                        count++;
                        count1++;
                        if (count1 == frametoanimation) {
                            animation++;
                            count1 = 0;
                        }
                        theTime = System.currentTimeMillis() / 1000.0;
                        if (theTime >= startTime + 1.0) {
                            System.out.format("%d fps\n", count);

                            startTime = theTime;
                            count = 0;
                        }



                        //swap buffers to display the frame and discard the old one
                        glfwSwapBuffers(window);
                        //glfwPollEvents();



                }
                //call the garbage collector
                System.gc();

            }



    public void translate(float x, float y, float z){
             viewareaX=(int)x;
             viewareaY=(int)y;
             //float diff=camerX-viewareaX;
             //System.out.println(camerX+" | "+cameraY);


            }
            

    private  void textureTest(Texture tex,float x,float y,float width,float height){

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        updateWindowSize();
        glViewport(0, 0, sceneW, sceneH);
        glOrtho(0,sceneW,sceneH,0,-1,1);

        glEnable(GL_TEXTURE_2D);

        tex.bind();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        //definde the u-v-coordinates for the texture like x-y to the size specified
                float u =0f;
                float v =0f;
                float u2 =1f;
                float v2 =1f;
        //Subtexture sText= tex.getSubtexture(testcount);






        //tegacy drawing for testing only
        glColor4f(1f,1f,1f,1f);
        glBegin(GL_QUADS);
        glTexCoord2f(u, v);
        glVertex2f(x, y);
        glTexCoord2f(u, v2);
        glVertex2f(x, y + height);
        glTexCoord2f(u2, v2);
        glVertex2f(x + width, y + height);
        glTexCoord2f(u2, v);
        glVertex2f(x + width, y);
        glEnd();

        glDisable(GL_BLEND);


    }

    private  void drawSubtextures(ArrayList<Enemy> enemy,TextureAtlas tex, int kp){

        glTranslatef(0, 0, 0);
        glMatrixMode(GL_PROJECTION);


        glEnable(GL_TEXTURE_2D);

        tex.bind();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        try{
        for (Enemy e:enemy) {

            if(e.shouldclose){}



            else{
                Subtexture sText = e.texture;
                float srcX = sText.x;
                float srcY = sText.y;
                float srcWidth = sText.width;
                float srcHeight = sText.height;

                float u = srcX / tex.width;
                float v = srcY / tex.height;
                float u2 = (srcX + srcWidth) / tex.width;
                float v2 = (srcY + srcHeight) / tex.height;


                float x = e.currPos.x;
                float y = e.currPos.y;
                float height = e.size;
                float width = e.size;
                //tegacy drawing for testing only
                glColor4f(1f, 1f, 1f, 1f);
                glBegin(GL_QUADS);
                glTexCoord2f(u, v);
                glVertex2f(x, y);
                glTexCoord2f(u, v2);
                glVertex2f(x, y + height);
                glTexCoord2f(u2, v2);
                glVertex2f(x + width, y + height);
                glTexCoord2f(u2, v);
                glVertex2f(x + width, y);
                glEnd();


            }
        }
        }catch(Exception kp1){}



        //System.out.println(animation);



        glDisable(GL_BLEND);


    }

    private  void drawSubtextures(ArrayList<Projectile> proj,TextureAtlas tex){

        glTranslatef(0, 0, 0);
        glMatrixMode(GL_PROJECTION);

            /*glLoadIdentity();
            updateWindowSize();
                glViewport(0, 0, sceneW, sceneH);
                glOrtho(0,sceneW,sceneH,0,-1,1);*/

        glEnable(GL_TEXTURE_2D);

        tex.bind();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        //definde the u-v-coordinates for the texture like x-y to the size specified
               /* float u =0f;
                float v =0f;
                float u2 =1f;
                float v2 =1f;



*/


            ArrayList<Projectile> remove=new ArrayList<Projectile>();
        for (Projectile p:proj) {


            //System.out.println(p.currPos.x+" | "+p.currPos.y);
            //System.out.println(p.startPos.x+" | "+p.startPos.y);

            if(Math.abs(p.currPos.x-p.startPos.x)>600||Math.abs(p.currPos.y-p.startPos.y)>600){remove.add(p);
            }


            {
                Subtexture sText = p.texture;
                float srcX = sText.x;
                float srcY = sText.y;
                float srcWidth = sText.width;
                float srcHeight = sText.height;

                float u = srcX / tex.width;
                float v = srcY / tex.height;
                float u2 = (srcX + srcWidth) / tex.width;
                float v2 = (srcY + srcHeight) / tex.height;


                float x = p.currPos.x;
                float y = p.currPos.y;
                float height = p.size;
                float width = p.size;
                //tegacy drawing for testing only
                glColor4f(1f, 1f, 1f, 1f);
                glBegin(GL_QUADS);
                glTexCoord2f(u, v);
                glVertex2f(x, y);
                glTexCoord2f(u, v2);
                glVertex2f(x, y + height);
                glTexCoord2f(u2, v2);
                glVertex2f(x + width, y + height);
                glTexCoord2f(u2, v);
                glVertex2f(x + width, y);
                glEnd();

                p.currPos.x += p.dir.x * p.speed;
                p.currPos.y += p.dir.y * p.speed;
            }

        }

        //System.out.println(animation);



        glDisable(GL_BLEND);

        for (Projectile p:remove) {
            proj.remove(p);

        }

    }

            private  void textureAtlasTest(TextureAtlas tex,float x,float y,float width,float height){

            glMatrixMode(GL_PROJECTION);
            /*glLoadIdentity();
            updateWindowSize();
                glViewport(0, 0, sceneW, sceneH);
                glOrtho(0,sceneW,sceneH,0,-1,1);*/

                glEnable(GL_TEXTURE_2D);

                tex.bind();

                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


                //definde the u-v-coordinates for the texture like x-y to the size specified
               /* float u =0f;
                float v =0f;
                float u2 =1f;
                float v2 =1f;



*/

               Subtexture sText;

                resetCastAnim();
                resetAttackAnim();
                switch (animationState){
                    case 1: sText= walkUp.getFrame();break;
                    case 2: sText= walkDown.getFrame();break;
                    case 3: sText= walkLeft.getFrame();break;
                    case 4: sText= walkRight.getFrame();break;


                    case 5: sText=castUp.getFrame();if(castUp.anim==castUp.frames-2){if(c1.casted){castProjectiles(1);
                        castProjectiles(2);
                        castProjectiles(3);
                        castProjectiles(4);
                        c1.casted=false;}}break;
                    case 6: sText=castDown.getFrame();if(castDown.anim==castDown.frames-2){if(c1.casted){castProjectiles(1);
                        castProjectiles(2);
                        castProjectiles(3);
                        castProjectiles(4);
                        c1.casted=false;}}break;
                    case 7: sText=castLeft.getFrame();if(castLeft.anim==castLeft.frames-2){if(c1.casted){castProjectiles(1);
                        castProjectiles(2);
                        castProjectiles(3);
                        castProjectiles(4);
                        c1.casted=false;}}break;
                    case 8: sText=castRight.getFrame();if(castRight.anim==castRight.frames-2){if(c1.casted){castProjectiles(1);
                        castProjectiles(2);
                        castProjectiles(3);
                        castProjectiles(4);
                        c1.casted=false;}}break;


                    case 9:sText=attackUp.getFrame();break;
                    case 10:sText=attackDown.getFrame();break;
                    case 11:sText=attackLeft.getFrame();break;
                    case 12:sText=attackRight.getFrame();break;
                    default:switch (c1.previousAnimstate){
                    case 1:sText=walkUp.getFrame(0);break;
                    case 2:sText=walkDown.getFrame(0);break;
                    case 3:sText=walkLeft.getFrame(0);break;
                    case 4:sText=walkRight.getFrame(0);break;
                        default: sText=walkDown.getFrame(0);break;}
                }

               //System.out.println(animation);

                float srcX = sText.x;
                float srcY = sText.y;
                float srcWidth = sText.width;
                float srcHeight = sText.height;

                float u = srcX / tex.width;
                float v = srcY / tex.height;
                float u2 = (srcX + srcWidth) / tex.width;
                float v2 = (srcY + srcHeight) / tex.height;


                //tegacy drawing for testing only
                glColor4f(1f,1f,1f,1f);
                glBegin(GL_QUADS);
                glTexCoord2f(u, v);
                glVertex2f(x, y);
                glTexCoord2f(u, v2);
                glVertex2f(x, y + height);
                glTexCoord2f(u2, v2);
                glVertex2f(x + width, y + height);
                glTexCoord2f(u2, v);
                glVertex2f(x + width, y);
                glEnd();

                glDisable(GL_BLEND);


            }

    private void resetCastAnim() {
        boolean cast=false;
        if(castUp.finished){c1.removeAnime("cast");castUp.finished=false;cast=true;}

        if(castDown.finished){c1.removeAnime("cast");castDown.finished=false;cast=true;}

        if(castLeft.finished){c1.removeAnime("cast");castLeft.finished=false;cast=true;}

        if(castRight.finished){c1.removeAnime("cast");castRight.finished=false;cast=true;}

    }

    private void resetAttackAnim() {
        if(attackUp.finished){c1.removeAnime("attack");attackUp.finished=false;}

        if(attackDown.finished){c1.removeAnime("attack");attackDown.finished=false;}

        if(attackLeft.finished){c1.removeAnime("attack");attackLeft.finished=false;}

        if(attackRight.finished){c1.removeAnime("attack");attackRight.finished=false;}
    }


    private void drawTest(){



                //map the pixelsizes to the clipping area to render a triangle spanning the whole screen
                //float sW=map(w.get(0),0,w.get(0),-1f,1f);
                //float sH=map(h.get(0),0,h.get(0),-1f,1f);


                //System.out.println("rendersze: "+sceneW+" | "+sceneH);
                glLineWidth(5);
                glBegin(GL_LINES);

                glColor3f(1,0,0);
                glVertex2f(sceneW/2-300,sceneH/2-150);
                glVertex2f(sceneW/2+300,sceneH/2-150);

                glVertex2f(sceneW/2-300,sceneH/2-150);
                glVertex2f(sceneW/2-300,sceneH/2+150);

                glVertex2f(sceneW/2-300,sceneH/2+150);
                glVertex2f(sceneW/2+300,sceneH/2+150);

                glVertex2f(sceneW/2+300,sceneH/2-150);
                glVertex2f(sceneW/2+300,sceneH/2+150);

             glEnd();
             glFlush();

            }

    private void updateWindowSize() {
        //buffers to pass into the Framebuffer getter
        IntBuffer w;
        IntBuffer h;

        //push buffers on the stack with size 1
        try(MemoryStack stack = stackPush()) {
            w = stack.mallocInt(1);
            h = stack.mallocInt(1);

        }
        //retrieve the current frameBuffer size in pixels
        glfwGetFramebufferSize(window, w, h);
        sceneW=w.get(0);
        sceneH=h.get(0);
        //System.out.println("windowsize: "+sceneW+" | "+sceneH);
    }

    public static void main(String[] args){
            //create the window and call rund to start the game
            new MainWindow().run();

            }

    public float map(float value, float start1, float stop1, float start2, float stop2) {

        //maps a given value from its current range (start1/end1) to a different range(start2/end2)
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }


    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    /**
     * Reads the specified resource and returns the raw data as a ByteBuffer.
     *
     * @param resource   the resource to read
     * @param bufferSize the initial buffer size
     *
     * @return the resource data
     *
     * @throws IOException if an IO error occurs
     */
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if ( Files.isReadable(path) ) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int)fc.size() + 1);
                while ( fc.read(buffer) != -1 ) ;
            }
        } else {
            try (
                    InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)
            ) {
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while ( true ) {
                    int bytes = rbc.read(buffer);
                    if ( bytes == -1 )
                        break;
                    if ( buffer.remaining() == 0 )
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                }
            }
        }

        buffer.flip();
        return buffer;
    }

    public void setIcon(String path) throws Exception{
        IntBuffer w = memAllocInt(1);
        IntBuffer h = memAllocInt(1);
        IntBuffer comp = memAllocInt(1);

        // Icons
        {
            ByteBuffer icon16;
            ByteBuffer icon32;
            try {
                icon16 = ioResourceToByteBuffer(path, 2048);
                icon32 = ioResourceToByteBuffer(path, 4096);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try ( GLFWImage.Buffer icons = GLFWImage.malloc(2) ) {
                ByteBuffer pixels16 = STBImage.stbi_load_from_memory(icon16, w, h, comp, 4);
                icons
                        .position(0)
                        .width(w.get(0))
                        .height(h.get(0))
                        .pixels(pixels16);

                ByteBuffer pixels32 = STBImage.stbi_load_from_memory(icon32, w, h, comp, 4);
                icons
                        .position(1)
                        .width(w.get(0))
                        .height(h.get(0))
                        .pixels(pixels32);

                icons.position(1);
                glfwSetWindowIcon(window, icons);

                STBImage.stbi_image_free(pixels32);
                STBImage.stbi_image_free(pixels16);
            }
        }

        memFree(comp);
        memFree(h);
        memFree(w);

    }


    public void closeWindow() {
        SpriteAnimation.shouldclose=true;
        glfwSetWindowShouldClose(window, true);
        System.gc();
    }
}



