public class SpriteAnimation implements Runnable {
    private Thread animThread;
    private SpriteArray sArray;
    public static int speed=250;
    private int animSpeed=1;
    public int frames;
    public int anim=0;
    boolean loop=false;
    boolean finished=false;
    public static boolean shouldclose=false;

    public static void setSpeed(int newSpeed){speed=newSpeed;}
    public static void resetSpeed(){speed=250;}

    public SpriteAnimation(SpriteArray pSArray,boolean l,int aspeed){
        this.sArray=pSArray;
        this.animSpeed=aspeed;
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

            while(!loop&&!shouldclose) {
                //System.out.println(anim +" frame number from cast down");
                if (anim >= frames){ finished = true;}
                if (anim < frames&& finished == false) anim++;

                try {
                    Thread.sleep(speed/animSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (loop&&!shouldclose) {
                if (anim >= frames && loop == true) {
                    anim = 0;
                }
                if (anim < frames && loop == true) anim++;
                try {
                    Thread.sleep(speed/animSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
