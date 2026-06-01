package cn.edu.game;

import java.awt.image.BufferedImage;

/**
 * 子弹类：
 */
public class Bullet extends FlyingObject{

    private int speed;//速度
    private static BufferedImage image;

    //静态资源---static静态代码块
    static {
        image = loadImage("bullet.png");
    }

    //构造方法：有参构造
    public Bullet(int x, int y){
        super(8, 20, x, y);
        this.speed = 3;
    }
    /** 子弹移动 */
    public void step(){
        y -= speed;
    }

    @Override
    public BufferedImage getImage() {
        if (isLife()){
            return image;
        } else if (isDead()) {
            state = REMOVE;
        }
        return null;
    }

    /*子弹：重写检测越界的方法*/
    @Override
    public boolean outOfBounds() {
        return this.y <= -this.height;//子弹的y<=负的子弹的高，越界了
    }
}