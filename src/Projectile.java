public class Projectile {
    Subtexture texture;

    Vec2D dir;
    public int size;
    public final Vec2D startPos;
    public Vec2D currPos;
    int speed;

    public Projectile(int pSize,Subtexture pTexture,Vec2D pDir,Vec2D pStartPos,int pSpeed){
        this.texture=pTexture;
        this.dir=pDir;
        this.size=pSize;
        //this.startPos=pStartPos;
        this.currPos=pStartPos;
        this.startPos=new Vec2D(pStartPos.x,pStartPos.y);
        this.speed=pSpeed;

    }



}
