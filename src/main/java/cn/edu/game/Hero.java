package cn.edu.game;

import java.awt.image.BufferedImage;

/**
 * 英雄机类：
 */
public class Hero extends FlyingObject{

    private int life;//生命值
    private int doubleFire;//火力值
    private static BufferedImage[] images;

    //静态资源---static静态代码块
    static {
        images = new BufferedImage[2];
        for (int i = 0; i < images.length; i++) {
            images[i] = loadImage("hero" + i + ".png");
        }
    }

    //构造方法：
    public Hero(){
        super(97, 139, MainGame.WIDTH/2 - 97/2, 400);
        this.doubleFire = 0;
        this.life = 5;
    }
    //英雄机图片切换的
    public void step(){
        System.out.println("英雄机图片切换...");
    }

    int index = 0;
    @Override
    public BufferedImage getImage() {
        index++;
        return images[index % images.length];
        //0%2 0     1 % 2  1  2%2 0
    }

    //英雄机的移动 鼠标的移动
    //x:鼠标的x  y:鼠标的y
    public void moveTo(int x, int y){
        this.x = x - this.width/2;
        this.y = y - this.height/2;
    }

    /** 生成子弹对象
     * 子弹可能是单发。也可能是2发子弹
     * @return 子弹数组
     */
    public Bullet[] shoot(){
        Bullet[] bullets = null;
        //将英雄机宽均分成4等分，方便定位
        int q = this.width / 4;
        if (doubleFire > 0){//发射双发子弹
            bullets = new Bullet[2];
            bullets[0] = new Bullet(this.x + q * 1, this.y);
            bullets[1] = new Bullet(this.x + q * 3, this.y);
            doubleFire--;
        } else {//发射单发子弹
            bullets = new Bullet[1];
            bullets[0] = new Bullet(this.x + q * 2, this.y);
        }
        return bullets;
    }

    /** 英雄机增加生命 */
    public void addLife(){
        life++;
    }
    /** 增加火力*/
    public void addDoubleFire(){
        doubleFire += 20;//火力值增加20
    }

    /** 获取生命值 */
    public int getLife(){
        return life;//返回生命值
    }

    public void subtractLife(){
        life--;
    }

    public void clearDoubleFire(){
        doubleFire = 0;
    }

}
