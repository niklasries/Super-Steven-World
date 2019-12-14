import java.util.HashMap;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class Control {
    MainWindow mW;
    long window;
    HashMap hm = new HashMap();
    boolean runnin=true;
    public Thread controler;
    public static int speed=1;
    int previousAnimstate=0;
    boolean casted=false;
    public Control(long window1,MainWindow mainWindow){

        System.out.println("Init Controls");
        mW=mainWindow;
        glfwMakeContextCurrent(window1);
        this.window= window1;

         controler=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("tread started running");

                while(runnin){
                updateMW();
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mW.closeWindow();





            }
            //public void update1(){updateMW();}
        });
        controler.start();


        glfwSetMouseButtonCallback(window,(window,  button,  action,  mods)->
        {
            if (!hm.containsKey("cast")&&button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS){
                System.out.println("M1-Pressed");

                mW.castDown.anim=0;
                mW.castUp.anim=0;
                mW.castLeft.anim=0;
                mW.castRight.anim=0;

                hm.put("cast",1);
                casted=true;



               //updateMW();
            }

            if (!hm.containsKey("attack")&&button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS){
                System.out.println("M2-Pressed");

                mW.attackDown.anim=0;
                mW.attackUp.anim=0;
                mW.attackLeft.anim=0;
                mW.attackRight.anim=0;

                hm.put("attack",1);

                mW.spawnProjectiles(previousAnimstate);

                //updateMW();
            }

        });

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            //controler.stop();

            //updateMW();
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                mW.closeWindow();
                //System.out.println("ESC pressed");
                runnin=false;


            }
            if (key == GLFW_KEY_UP && action == GLFW_RELEASE) {
                hm.remove("movement_UP");
                //System.out.println("IP released");
            }
            if (!hm.containsKey("movement_UP")&&key == GLFW_KEY_UP && action == GLFW_PRESS) {
                hm.put("movement_UP",1);
                //System.out.println("UP pressed");
            }

            if (key == GLFW_KEY_LEFT_SHIFT && action == GLFW_RELEASE) {
                hm.remove("sprint");
                //System.out.println("IP released");
            }
            if (!hm.containsKey("sprint")&&key == GLFW_KEY_LEFT_SHIFT && action == GLFW_PRESS) {
                hm.put("sprint",125);
                //System.out.println("UP pressed");
            }

            if (key == GLFW_KEY_DOWN && action == GLFW_RELEASE) {
                hm.remove("movement_DOWN");
                //System.out.println("IP released");
            }
            if (key == GLFW_KEY_DOWN && action == GLFW_PRESS) {
                hm.put("movement_DOWN",1);
                //System.out.println("DOWN pressed");
            }
            if (key == GLFW_KEY_LEFT && action == GLFW_RELEASE) {
                hm.remove("movement_LEFT");
                //System.out.println("LEFT released");
            }
            if (key == GLFW_KEY_LEFT && action == GLFW_PRESS) {
                hm.put("movement_LEFT",1);
                //System.out.println("LEFT pressed");
            }

            if (key == GLFW_KEY_RIGHT && action == GLFW_PRESS) {
                hm.put("movement_RIGHT",1);
                //System.out.println("RIGKT pressed");
            }
            if (key == GLFW_KEY_RIGHT && action == GLFW_RELEASE) {
                hm.remove("movement_RIGHT");
                //System.out.println("RIGHT released");
            }



            //controler.start();

        });


    }


    public void removeAnime(String state){hm.remove(state);}

    public void updateMW() {

        mW.animationState=0;
        //if(mW.castDown.finished){try{removeAnime("cast");}catch (Exception e){};mW.castDown.finished=false;}

        if(hm.containsKey("cast")){

        switch (previousAnimstate){
            case 1:mW.animationState=5;break;
            case 2:mW.animationState=6;break;
            case 3:mW.animationState=7;break;
            case 4:mW.animationState=8;break;
            default:mW.animationState=6;break;

        }

        }
        else if(hm.containsKey("attack")){
            switch (previousAnimstate){
                case 1:mW.animationState=9;break;
                case 2:mW.animationState=10;break;
                case 3:mW.animationState=11;break;
                case 4:mW.animationState=12;break;
                default:mW.animationState=10;break;

            }

        }
        else {

            //System.out.println(hm.containsKey("updated movement"));
            if (hm.containsKey("movement_UP")) {
                mW.setCameraY(-5 * speed);
                mW.animationState = 1;
                previousAnimstate=1;
            } else if (hm.containsKey("movement_DOWN")) {
                mW.setCameraY(5 * speed);
                mW.animationState = 2;
                previousAnimstate=2;
            } else if (hm.containsKey("movement_LEFT")) {
                mW.setCamerX(-5 * speed);
                mW.animationState = 3;
                previousAnimstate=3;
            } else if (hm.containsKey("movement_RIGHT")) {
                mW.setCamerX(5 * speed);
                mW.animationState = 4;
                previousAnimstate=4;
            }

            if (hm.containsKey("sprint")) {
                SpriteAnimation.setSpeed((int) hm.get("sprint"));
                speed = 2;
            } else {
                SpriteAnimation.resetSpeed();
                speed = 1;
            }


        }

    }
}
