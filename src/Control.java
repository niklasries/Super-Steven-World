import java.util.HashMap;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class Control {
    MainWindow mW;
    long window;
    HashMap hm = new HashMap();
    boolean runnin=true;
    public Thread controler;
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

    public void updateMW() {

        mW.animationState=0;

        //System.out.println(hm.containsKey("updated movement"));
        if(hm.containsKey("movement_UP")){
            mW.setCameraY(-5);
            mW.animationState=1;
        }
        else if(hm.containsKey("movement_DOWN")){
            mW.setCameraY(5);
            mW.animationState=2;
        }
        else if(hm.containsKey("movement_LEFT")){
            mW.setCamerX(-5);
            mW.animationState=3;
        }
        else if(hm.containsKey("movement_RIGHT")){
            mW.setCamerX(5);
            mW.animationState=4;
        }

    }
}
