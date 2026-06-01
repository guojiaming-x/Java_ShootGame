package cn.edu.game;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 奖励机类：
 * 1)生命值的奖励  2)火力值的奖励
 */
public class AwardAirplane extends FlyingObject implements Award{

    private int xSpeed;//x坐标的速度
    private int ySpeed;//y坐标的速度
    private int awardType;//奖励的类型 0 1
    private static BufferedImage[] images;

    static {//静态代码块
        images = new BufferedImage[5];
        images[0] = loadImage("bee0.png");
        for (int i = 1; i < images.length; i++) {
            images[i] = loadImage("bom" + i + ".png");
        }
    }

    //构造方法:初始化
    public AwardAirplane(){
        super(60, 51);
        xSpeed = 1;
        ySpeed = 2;
        Random random = new Random();
        awardType = random.nextInt(2);//0到1的随机数
    }
    //奖励机移动
    public void step(){
        y += ySpeed;//向下走
        x += xSpeed;//横向走
        //检测是否碰壁
        if (x <= 0 || x >= MainGame.WIDTH - width){
            //碰壁,使用xSpeed更换正负，达到切换方向的目的
            xSpeed *= -1;
        }
    }

    int index = 0;
    @Override
    public BufferedImage getImage() {
        if (isLife()){
            return images[0];
        } else if (isDead()) {
            index++;
            if (index == images.length - 1){
                state = REMOVE;
            }
            return images[index];
        }
        return null;
    }

    @Override
    public int getAwardType() {
        return awardType;//返回奖励的类型(0或者1)
    }
}
