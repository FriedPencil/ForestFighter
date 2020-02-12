package com.example.forestfighter;

/**
 * Created by 曜爸 on 2020/2/10.
 */
public class Constant {
    public final static int BRICK_X = 12; // 横坐标的格子数
    public final static int BRICK_Y = 12; // 纵坐标的格子数

    public final static int TIME_SLEEP = 100;	//每隔一段时间执行一次

    public static final int STARTGAME_XPOS = 0;	//地图的起始坐标
    public static final int STARTGAME_YPOS = 0;

    public static final int BULLET_SPEED = 10;	//子弹的速度px
    public static final int BULLET_TIME = 1;	//子弹时间间隔执行一次n*100ms

    public static final int EXPLOSION_TIME = 4;	//爆炸效果的时间间隔

    public static final int MONSTER_SPEED = 10;	//怪物的速度n*100ms

    public static final int MONSTER_COUNT = 4;	//初始时几只怪物

    public static final int FIGHTER_LIVE = 5;	//初始时，勇士的生命数

    public static final int BULLET_WIDTH = 10;	//子弹的宽度与高度px

    public static final int GAME_LEVEL = 10;	//游戏共有几级

    public static final int MSG_WELCOMGAMEEVIEW = 2;
    public static final int MSG_UPDATE = 6;	//更新消息
    public static final int MSG_WINTHISGAME = 7;	//羸了这一局
    public static final int MSG_LOSTHISGAME = 8;	//输了这一局
}
