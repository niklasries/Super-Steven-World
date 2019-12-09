public class SpriteAnimation implements Runnable {
    private Thread animThread;
    private SpriteArray sArray;
    private int frames;
    public int anim=0;
    boolean loop=false;
    public static boolean shouldclose=false;

    public SpriteAnimation(SpriteArray pSArray,boolean l){
        this.sArray=pSArray;
        frames=sArray.textures.length-1;
        loop=l;


    }
    public Subtexture getFrame(){return sArray.textures[anim];}
    public Subtexture getFrame(int idx){return sArray.textures[idx];}

    public void start(){if(animThread==null){
        animThread=new Thread(this);
    }
    animThread.start();}

    @Override
    public void run() {




        //do stuff
        while(!shouldclose) {


            if (anim >= frames && loop == true){ anim = 0;}
            if (anim < frames)anim++;
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
