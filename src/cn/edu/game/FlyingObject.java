package cn.edu.game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 父类：
 */
public abstract class FlyingObject {
    protected int x;//x坐标
    protected int y;//y坐标
    protected int width;//宽
    protected int height;//高

    public static final int LIFE = 0;//活着的
    public static final int DEAD = 1;//死了的
    public static final int REMOVE = 2;//删除的
    protected int state = LIFE;//当前状态 默认是活着的

    /**
     * 判断对象是不是活着的
     */
    public boolean isLife(){
        return state == LIFE;//如果当前状态是活着的，返回true，否则返回false
    }
    /**
     * 判断对象是不是死了的
     */
    public boolean isDead(){
        return state == DEAD;//如果当前状态是死了的，返回true，否则返回false
    }
    /**
     * 判断对象是不是删除的
     */
    public boolean isRemove(){
        return state == REMOVE;//如果当前状态是删除的，返回true，否则返回false
    }

    /** 敌人对象
     * 小敌机、大敌机、奖励机
     * @param width
     * @param height
     */
    public FlyingObject(int width, int height){
        this.width = width;
        this.height = height;
        Random rand = new Random();
        x = rand.nextInt(MainGame.WIDTH - this.width);
        y = -height;//负的对象的高
    }

    /**
     * 天空、英雄机、子弹
     * @param width
     * @param height
     * @param x
     * @param y
     */
    public FlyingObject(int width, int height, int x, int y){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    //飞行物移动  设计为抽象方法
    public abstract void step();

    //读取图片
    public static BufferedImage loadImage(String fileName){
        try {
            BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName));//读取同包之内的图片
            return img;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*获取对象的图片*/
    public abstract BufferedImage getImage();

    //画对象
    public void paintObject(Graphics g){
        g.drawImage(this.getImage(), this.x, this.y, null);
    }

    /*检测越界*/
    public boolean outOfBounds(){
        return this.y >= MainGame.HEIGHT;//敌人的y坐标>=窗口的高的时候，越界了
    }

    /**
     * 碰撞算法:
     * 敌人：this
     * 子弹或者英雄机：other
     */
    public boolean hit(FlyingObject other){
        int x = other.x;//子弹的x
        int y = other.y;//子弹的y
        int x1 = this.x - other.width;
        int y1 = this.y - other.height;
        int x2 = this.x + this.width;
        int y2 = this.y + this.height;
        return x >= x1 && x <= x2 && y >= y1 && y<= y2;
    }

    /**
     * 飞行物去死
     */
    public void goDead(){
        state = DEAD;
    }



}
