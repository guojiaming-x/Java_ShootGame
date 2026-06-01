package cn.edu.game;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 小敌机类
 */
public class Airplane extends FlyingObject implements Score{

    private int speed;//速度
    private static BufferedImage[] images;

    //静态资源---static静态代码块
    static {
        images = new BufferedImage[5];
        images[0] = loadImage("airplane" + 0 + ".png");
        for (int i = 1; i < images.length; i++) {
            images[i] = loadImage("bom" + i + ".png");
        }
    }

    //构造方法
    public Airplane(){
        super(48, 50);//super()调用超类构造方法必须位于子类构造方法的第一行
        speed = 8;
    }

    /** 小敌机移动 */
    public void step(){
        y += speed;//小敌机向下移动
    }

    int index = 1;//爆炸图片的小标
    @Override
    public BufferedImage getImage() {
        if (isLife()){
            return images[0];//返回第一张图片
        } else if (isDead()) {
            BufferedImage img = images[index++];
            if (index == images.length){
                state = REMOVE;
            }
            return img;
        }
        return null;
    }

    @Override
    public int getScore() {
        return 1;//打掉小敌机，玩家得1分
    }
}