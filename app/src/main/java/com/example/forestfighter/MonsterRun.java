package com.example.forestfighter;

import android.util.Log;

import java.util.Random;

/**
 * Created by 曜爸 on 2020/2/10.
 */
public class MonsterRun implements Runnable{
    private GameView gameview;
    private Monster monster;

    public MonsterRun(GameView gameView, Monster monster)
    {
        this.gameview = gameView;
        this.monster = monster;
    }

    @Override
    public void run()
    {
        int movedir = 0;
        int needfire = 0;
        int count = 0;

        int pinli =4; //方向切换频率（４,６）

        while(monster.isBlive())	//金鱼还存活着
        {
//***            if(gameview.migong.bisInSearcd == false)	//搜索时，金鱼不能移动，不在搜索时，才移动
            {
                needfire = (new Random()).nextInt(10);
                if( needfire == 8)
                {
                    monster.fire();	//发射子弹
                }
                synchronized (monster)
                {
                    if(count%pinli == 0)	//每隔两秒才修改下方向
                    {
                        movedir = (new Random()).nextInt(4);

                        if(pinli == 4) pinli = 6;
                        else           pinli = 4;
                    }
                    move(movedir);
                }
            }
            try
            {
                Thread.sleep(Constant.TIME_SLEEP* Constant.MONSTER_SPEED);
            } catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            count++;
        }
    }
    private void move(int ran)
    {
        int x = monster.getX();
        int y = monster.getY();

        //UP,RIGHT,DOWN,LEFT

        switch(ran)
        {
            case 0:	//Dir.UP
                if(monster.getDir() != Direction.UP)	//方向不同的，先改变方向
                {
                    monster.setDir(Direction.UP);
                    return;
                }
                y--;
                if(y>=0 && gameview.info[x][y] == Kind.EMPTY)	//是空格，则可以到达
                {
                    monster.setX(x);
                    monster.setY(y);
                    //对信息表中进行修正
                    gameview.info[x][y] = Kind.MONSTER;	//当前的为金鱼
                    gameview.info[x][y+1] = Kind.EMPTY;	//之前的为空格
                }
                break;
            case 1:	//Dir.RIGHT
                if(monster.getDir() != Direction.RIGHT)	//方向不同的，先改变方向
                {
                    monster.setDir(Direction.RIGHT);
                    return;
                }
                x++;
                if(x == gameview.end.x && y == gameview.end.y)	//终点不让金鱼进
                {
                    monster.setDir(Direction.LEFT);
                    return;
                }
                if(x<Constant.BRICK_X && gameview.info[x][y] == Kind.EMPTY)	//是空格，则可以到达
                {
                    monster.setX(x);
                    monster.setY(y);
                    //对信息表中进行修正
                    gameview.info[x][y] = Kind.MONSTER;	//当前的为金鱼
                    gameview.info[x-1][y] = Kind.EMPTY;	//之前的为空格
                }
                break;
            case 2:	//Dir.DOWN
                if(monster.getDir() != Direction.DOWN)	//方向不同的，先改变方向
                {
                    monster.setDir(Direction.DOWN);
                    return;
                }
                y++;
                if(y<Constant.BRICK_Y && gameview.info[x][y] == Kind.EMPTY)	//是空格，则可以到达
                {
                    monster.setX(x);
                    monster.setY(y);
                    //对信息表中进行修正
                    gameview.info[x][y] = Kind.MONSTER;	//当前的为金鱼
                    gameview.info[x][y-1] = Kind.EMPTY;	//之前的为空格
                }
                break;
            case 3:	//Dir.LEFT
                if(monster.getDir() != Direction.LEFT)	//方向不同的，先改变方向
                {
                    monster.setDir(Direction.LEFT);
                    return;
                }
                x--;
                if(x == gameview.start.x && y == gameview.start.y)	//起点不让金鱼进
                {
                    monster.setDir(Direction.RIGHT);
                    return;
                }
                if(x>=0 && gameview.info[x][y] == Kind.EMPTY)	//是空格，则可以到达
                {
                    monster.setX(x);
                    monster.setY(y);
                    //对信息表中进行修正
                    gameview.info[x][y] = Kind.MONSTER;	//当前的为金鱼
                    gameview.info[x+1][y] = Kind.EMPTY;	//之前的为空格
                }
                break;
            default:
                break;
        }
    }
}
