import com.sun.tools.javac.Main;

public class Enemy implements Runnable{
    Subtexture texture;

    Vec2D dir;
    public int size;
    public final Vec2D startPos;
    public Vec2D currPos;
    int speed;
    boolean shouldclose=false;
    Thread enemyThread;
    MainWindow mw;
    public static int offsetX =0;
    public static int offsetY =0;

    public Enemy(int pSize, Subtexture pTexture, Vec2D pDir, Vec2D pStartPos, int pSpeed, MainWindow mw){
        this.texture=pTexture;
        this.dir=pDir;
        this.size=pSize;
        //this.startPos=pStartPos;
        this.currPos=pStartPos;
        this.startPos=new Vec2D(pStartPos.x,pStartPos.y);
        this.speed=pSpeed;
        this.mw=mw;

    }
    public void kill(){shouldclose=true;}

    public void start(){if(enemyThread==null){
        enemyThread=new Thread(this);
    }
        enemyThread.start();}


    @Override
    public void run() {
        while(!shouldclose){

            if(mw.camerX>this.currPos.x+10){currPos.x+=speed;}
            else{currPos.x+=-speed;}
             if(mw.cameraY>this.currPos.y+10){currPos.y+=speed;}
            else{currPos.y+=-speed;}






            try {
                Thread.sleep(32);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}