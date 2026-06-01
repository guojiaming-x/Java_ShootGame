package cn.edu.game;

public interface Award {
    public int DOUBLE_FIRE = 0;//火力值
    public int LIFE = 1;//生命值
    /** 获取奖励类型(0或者1)*/
    public int getAwardType();
}
