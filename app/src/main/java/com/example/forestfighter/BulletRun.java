package com.example.forestfighter;

import android.util.Log;

/**
 * Created by 曜爸 on 2020/2/10.
 */
public class BulletRun implements Runnable {
    private static final String TAG = "BulletRun";
    private GameView gameview;
    private Bullet bullet;

    public BulletRun(GameView gameview, Bullet bullet){
        this.gameview = gameview;
        this.bullet = bullet;
    }
    @Override
    public void run()
    {

        while(bullet.isLive())
        {
            int x = bullet.getX();
            int y = bullet.getY();

            if(bullet.getDir() == Direction.UP)
            {
                y = y - Constant.BULLET_SPEED;
            }else if(bullet.getDir() == Direction.RIGHT)
            {
                x = x + Constant.BULLET_SPEED;
            }else if(bullet.getDir() == Direction.DOWN)
            {
                y = y + Constant.BULLET_SPEED;
            }else	//dir.left
            {
                x = x - Constant.BULLET_SPEED;
            }
            //子弹越界，删除子弹
            if(x < Constant.STARTGAME_XPOS + 0*gameview.wall.getWidth()
                    || x > Constant.STARTGAME_XPOS + (Constant.BRICK_X-1)*gameview.wall.getWidth()
                    || y < Constant.STARTGAME_YPOS + 0*gameview.wall.getHeight()
                    || y > Constant.STARTGAME_YPOS + (Constant.BRICK_Y-1)*gameview.wall.getHeight())
            {

                bullet.setLive(false);	//子弹消亡

                if(bullet.isGood())//我方子弹
                {
                    gameview.fBullets.remove(bullet);
                }else	//敌方子弹
                {
                    gameview.mBullets.remove(bullet);
                }
            }

            //根据x,y得到坐标中的x0, y0
            int x0 = (x- Constant.STARTGAME_XPOS)/gameview.wall.getWidth();
            int y0 = (y- Constant.STARTGAME_YPOS)/gameview.wall.getHeight();

            //子弹撞墙，删除子弹
            if(x0 >= 0 && x0 <Constant.BRICK_X-1
                    && y0 >= 0 && y0 <Constant.BRICK_Y-1
                    && gameview.info[x0][y0] == Kind.WALL )
            {
                bullet.setLive(false);	//子弹消亡

                if(bullet.isGood())//我方子弹
                {
                    gameview.fBullets.remove(bullet);
                }else	//敌方子弹
                {
                    gameview.mBullets.remove(bullet);
                }

            }

            //以上情况都不是,重新设置坐标
            bullet.setX(x);
            bullet.setY(y);

            try
            {
                Thread.sleep(Constant.TIME_SLEEP * Constant.BULLET_TIME );
            } catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
