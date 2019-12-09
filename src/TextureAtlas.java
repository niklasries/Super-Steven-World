import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

public class TextureAtlas extends Texture {
    private int srcX,srcY;
    private int srcWidth,srcHeight;
    private int countX,countY,count;


    public TextureAtlas(InputStream is, int width, int height) throws IOException {
        super(is);

        srcWidth=width;
        srcHeight=height;
        countX=super.width/srcWidth;
        countY=super.height/srcHeight;
        count=(countX*countY)-1;

    }

    public Subtexture getSubtexture(int pos){
        int y =srcHeight*(int)(pos*srcWidth/super.width);
        int scale=super.width/srcWidth;
        return new Subtexture(pos*srcWidth-(y*super.width),y,srcWidth,srcHeight);
    }

    public SpriteArray getSpriteArray(int start,int stop){
        int pos=start;
        int range=stop-start+1;

        Subtexture[] subtextures=new Subtexture[range];

        for (int i = 0; i <= range-1; i++) {


            subtextures[i]=getSubtexture(pos+i);

        }

        return new SpriteArray(subtextures);
    }

    public int getCount(){return count;}

}
