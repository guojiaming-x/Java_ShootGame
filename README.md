# Java ShootGame — 飞机大战

基于 Java Swing 的 2D 飞机射击游戏，采用面向对象设计。

## 游戏截图

![start](src/cn/edu/game/start.png)

## 类继承体系

```
FlyingObject（抽象父类 — 碰撞检测、越界判断、状态管理）
├── Sky             滚动背景（双图无缝循环）
├── Hero            英雄机（鼠标控制移动、子弹发射、生命/火力管理）
├── Bullet          子弹（直线飞行、越界删除）
├── Airplane        小敌机（直线下落，1分，实现 Score 接口）
├── BigAirplane     大敌机（直线下落，3分，实现 Score 接口）
└── AwardAirplane   奖励机（蛇形移动，随机掉落双倍火力或生命，实现 Award 接口）
```

## 接口设计

```java
interface Score { int getScore(); }           // 击毁后返回分数
interface Award { int DOUBLE_FIRE=0; int LIFE=1; int getAwardType(); }  // 奖励类型
```

## 游戏机制

### 主循环（10ms 定时器）

```
敌人入场 (400ms) → 子弹发射 (300ms)
→ 所有飞行物移动
→ 越界删除
→ 子弹×敌人碰撞检测
→ 英雄机×敌人碰撞检测
→ 检测游戏结束
→ 重绘
```

### 状态机

```
START ──点击──→ RUNNING ──生命≤0──→ GAME_OVER ──点击──→ START
                  ↑ ↓
               PAUSE ←── 鼠标移出窗口
```

### 碰撞检测

矩形相交算法：子弹的 (x,y) 落在敌人的碰撞矩形内即为击中。

### 子弹系统

- 默认单发（英雄机顶部中心）
- 吃到火力奖励后双发（间隔 1/4 机身宽，持续 20 发）
- 撞到敌人后火力清零

### 敌人随机生成

| 类型 | 概率 | 分数 | 特点 |
|------|------|------|------|
| 小敌机 Airplane | 50% | 1分 | 直线下落 |
| 大敌机 BigAirplane | 40% | 3分 | 直线下落 |
| 奖励机 AwardAirplane | 10% | 0分 | 蛇形移动，掉落奖励 |

### 爆炸动画

敌机被击毁后循环播放 4 帧爆炸序列图，播完后移除。

## 玩法

- **移动**：鼠标控制英雄机
- **射击**：自动连发
- **暂停**：鼠标移出窗口
- **重新开始**：游戏结束后点击画面

## 编译运行

```bash
# 编译
javac -encoding UTF-8 src/cn/edu/game/*.java

# 运行
java -cp src cn.edu.game.MainGame
```

或直接用 IntelliJ IDEA 打开项目根目录运行。
