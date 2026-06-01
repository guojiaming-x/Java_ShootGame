package cn.edu.game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 天空背景类：
 */
public class Sky extends FlyingObject{

    private int speed;//速度
    private int y1;//第二张图片的位置
    private static BufferedImage image;

    //静态资源---static静态代码块
    static {
        image = loadImage("background.png");
    }
    //构造方法
    public Sky(){
        super(MainGame.WIDTH, MainGame.HEIGHT, 0 , 0);
        speed = 1;
        y1 = -MainGame.HEIGHT;
    }

    public void step(){
        y += speed;
        y1 += speed;
        if (y >= MainGame.HEIGHT){
            y = -MainGame.HEIGHT;
        }
        if (y1 >= MainGame.HEIGHT){
            y1 = -MainGame.HEIGHT;
        }
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void paintObject(Graphics g) {
        g.drawImage(getImage(), x, y, null);
        g.drawImage(getImage(), x, y1, null);//画第二张图
    }
}