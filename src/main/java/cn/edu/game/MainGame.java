package cn.edu.game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 程序的主类：
 *
 *
 */
public class MainGame extends JPanel {

    public static final int WIDTH = 480;//窗体的宽
    public static final int HEIGHT = 800;//窗体的高

    //设计4种状态
    public static final int START = 0;//启动状态
    public static final int RUNNING = 1;//运行状态
    public static final int PAUSE = 2;//暂停状态
    public static final int GAME_OVER = 3;//结束状态
    private int state = START;//默认是运行状态

    private static BufferedImage start;//启动图片
    private static BufferedImage pause;//暂停图片
    private static BufferedImage gameover;//游戏结束图片

    static {//初始化静态资源的时候
        start = FlyingObject.loadImage("start.png");
        pause = FlyingObject.loadImage("pause.png");
        gameover = FlyingObject.loadImage("gameover.png");
    }


    Sky sky = new Sky();//背景
    Hero hero = new Hero();//英雄机对象
    FlyingObject[] flys = {};//敌人对象(小敌机、大敌机、奖励机对象)
    Bullet[] bullets = {};//子弹对象

    /**
     * 子弹入场
     */
    int shootIndex = 0;
    private void shootAction() {
        shootIndex++;
        if (shootIndex % 30 == 0){
            //获取英雄机发射的子弹对象
            Bullet[] bs = hero.shoot();
            //数组的扩容
            bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
            //数组的复制
            System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);
        }
    }

    /**
     * 实现敌人入场
     */
    int index = 0;//进场计数的
    public void enterAction(){
        index++;//每10毫秒加1
        if (index % 40 == 0){//每400(10*40)毫秒走一次
            FlyingObject obj = nextOne();//生成一个敌人对象
            //数组的扩容
            flys = Arrays.copyOf(flys, flys.length + 1);
            //将新生成的敌人对象放到扩容之后的数组中的最后一个位置
            flys[flys.length - 1] = obj;
        }
    }
    /**
     * 生成一个敌人对象
     * @return
     */
    public FlyingObject nextOne(){
        FlyingObject fly = null;
        Random random = new Random();
        int num = random.nextInt(100);//随机生成100以内的数
        if (num >=0 && num < 50){//生成小敌机    50
            fly = new Airplane();
        } else if (num >= 50 && num < 90) {//生成大敌机    40
            fly = new BigAirplane();
        } else {//生成奖励机    10
            fly = new AwardAirplane();
        }
        return fly;
    }


    /*重写paint方法：g画笔*/
    @Override
    public void paint(Graphics g) {
        //画对象
        sky.paintObject(g);//画背景
        hero.paintObject(g);//画英雄机对象
        for (int i = 0; i < flys.length; i++) {
            flys[i].paintObject(g);//画敌人
        }
        for (int i = 0; i < bullets.length; i++) {
            bullets[i].paintObject(g);
        }
        //画分数和生命值
        g.drawString("分数：" + score, 20, 25);
        g.drawString("生命值：" + hero.getLife(), 20, 45);

        switch (state){
            case START://启动状态时候，画启动的图片
                g.drawImage(start, 0, 0, null);
                break;
            case PAUSE://暂停状态
                g.drawImage(pause, 0, 0, null);
                break;
            case GAME_OVER://结束状态
                g.drawImage(gameover, 0, 0, null);
                break;
        }


    }

    /**
     * 飞行物移动
     */
    private void stepAction() {
        //天空背景的移动
        sky.step();
        //遍历敌人数组
        for (int i = 0; i < flys.length; i++) {
            flys[i].step();
        }
        //遍历子弹数组
        for (int i = 0; i < bullets.length; i++) {
            bullets[i].step();
        }
    }
    /**
     * 启动程序的执行
     */
    private void action() {
        //侦听器对象
        MouseAdapter ma = new MouseAdapter() {
            //鼠标的移动事件
            @Override
            public void mouseMoved(MouseEvent e) {
                if (state == RUNNING){
                    int x = e.getX();//获取鼠标的x坐标
                    int y = e.getY();//获取鼠标的y坐标
                    hero.moveTo(x, y);//英雄机随着鼠标的移动
                    //                System.out.println(x + "," + y);
                }
            }
            //鼠标的点击事件
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (state){
                    case START://启动状态的时候
                        state = RUNNING;
                        break;
                    case GAME_OVER://游戏结束
                        score = 0;
                        sky = new Sky();
                        hero = new Hero();
                        flys = new FlyingObject[0];
                        bullets = new Bullet[0];
                        state = START;//修改游戏的状态
                        break;
                }
            }
            /*鼠标的移出事件*/
            @Override
            public void mouseExited(MouseEvent e) {
                if (state == RUNNING){
                    state = PAUSE;//修改为暂停状态
                }
            }
            /*鼠标移入事件*/
            @Override
            public void mouseEntered(MouseEvent e) {
                if (state == PAUSE){
                    state = RUNNING;//修改为运行状态
                }
            }
        };
        this.addMouseListener(ma);//处理鼠标的操作事件
        this.addMouseMotionListener(ma);//处理鼠标的滑动事件

        //使用一个定时器对象
        Timer timer = new Timer();
        int intervel = 10;//定时间隔 以毫秒为单位的
        //匿名内部类
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state == RUNNING){
                    enterAction();//敌人入场 小敌机 大敌机 奖励机
                    shootAction();//子弹入场
                    stepAction();//飞行物移动
                    outOfBoundsAction();//删除越界的飞行物
                    bulletBangAction();//子弹与敌人碰撞
                    heroBangAction();//英雄机与敌人碰撞
                    checkGameOverAction();//检测游戏结束
                }
                repaint();//重绘  调用paint方法
            }
        },intervel, intervel);
    }

    /**
     * 删除越界的飞行物（敌人+子弹）
     */
    private void outOfBoundsAction() {//每10毫秒走一次
        int index = 0;//1）不越界的敌人数组的下标  2）不越界的敌人个数
        FlyingObject[] flysLives = new FlyingObject[flys.length];//不越界的敌人数组
        for (int i = 0; i < flys.length; i++) {//遍历敌人数组
            FlyingObject f = flys[i];//获取每一个敌人对象
            if (!f.outOfBounds()){//不越界
                flysLives[index] = f;//将不越界的敌人添加到不越界的敌人数组中
                index++;//1）不越界的敌人数组的下标增加1  2）不越界的敌人个数增加1
            }
        }
        flys = Arrays.copyOf(flysLives, index);//将不越界的敌人数组复制到了flys数组中，flys的长度就是index（不越界的敌人个数）

        //删除越界的子弹
        index = 0;//清零
        Bullet[] bulletsLives = new Bullet[bullets.length];
        for (int i = 0; i < bullets.length; i++) {//遍历所有的子弹对象
            Bullet b = bullets[i];
            if (!b.outOfBounds()){
                bulletsLives[index] = b;
                index++;
            }
        }
        bullets = Arrays.copyOf(bulletsLives, index);
    }

    /**
     * 子弹与敌人的碰撞
     */
    int score = 0;//玩家得分
    public void bulletBangAction(){//10毫秒走一次
        for (int i = 0; i < bullets.length; i++) {//遍历所有的子弹
            Bullet b = bullets[i];//获取每一个子弹对象
            for (int j = 0; j < flys.length; j++) {//遍历所有的敌人
                FlyingObject f = flys[j];//获取每一个敌人
                if (f.isLife() && b.isLife() && f.hit(b)){//撞上了
                    f.goDead();//敌人去死
                    b.goDead();//子弹去死

                    if (f instanceof Score){//如果被撞对象是敌人能得分
                        Score s = (Score) f;//将被撞对象转为得分接口
                        score += s.getScore();//玩家得分
                    }
                    if (f instanceof Award){//如果被撞对象是奖励
                        Award a = (Award) f;
                        int type = a.getAwardType();//获取奖励的类型
                        switch (type){
                            case Award.DOUBLE_FIRE://奖励类型是火力
                                hero.addDoubleFire();//英雄机增加火力
                                break;
                            case Award.LIFE:
                                hero.addLife();//英雄机增加生命值
                                break;
                        }
                    }
                }
            }

        }


    }

    /**
     * 英雄机与敌人的碰撞
     *
     */
    public void heroBangAction(){//10毫秒走一次
        for (int i = 0; i < flys.length; i++) {
            FlyingObject f = flys[i];
            if (hero.isLife() && f.isLife() && f.hit(hero)){
                f.goDead();
                hero.subtractLife();
                hero.clearDoubleFire();
            }
        }
    }

    /** 检测游戏结束*/
    public void checkGameOverAction(){
        if (hero.getLife() <= 0){//游戏结束
//            System.out.println("游戏结束.....");
            state = GAME_OVER;//将当前的状态修改为游戏的结束状态
        }
    }

    //快捷方式：输入main 回车 程序的入口
    public static void main(String[] args) {
        MainGame mainGame = new MainGame();

        JFrame jFrame = new JFrame();
        jFrame.add(mainGame);

        jFrame.setSize(WIDTH, HEIGHT);//设置窗体的大小
        jFrame.setLocationRelativeTo(null);//设置窗体居中显示
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.setVisible(true);//显示出来
        mainGame.action();
    }


}