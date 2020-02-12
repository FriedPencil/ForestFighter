package com.example.forestfighter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by 曜爸 on 2020/2/10.
 */
public class Bullet implements Alive {
    private static final String TAG = "Bullet";
    int x;	//x,y为地图中的真实像素坐标
    int y;
    Direction direction;
    private boolean good;
    private boolean live = true;

    private GameView gameview;
    private Bitmap fBullet = null;
    private Bitmap mBullet = null;

    public int width;
    public int height;

    public Bullet(GameView gameview, int x, int y, Direction direction, boolean good)
    {
        this.gameview = gameview;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.good = good;
        live = true;	//子弹有生命

        loadBitmap();
    }

    private void loadBitmap()
    {
        fBullet = BitmapFactory.decodeResource(gameview.getResources(), R.drawable.fbullet);
        mBullet  = BitmapFactory.decodeResource(gameview.getResources(), R.drawable.mbullet);

        width = fBullet.getWidth();
        height = fBullet.getHeight();
    }

    @Override
    public void draw(Canvas canvas)
    {
        if(good)	//我方子弹
        {
            canvas.drawBitmap(fBullet, x, y, gameview.paint);
        }else	//对方子弹
        {
            canvas.drawBitmap(mBullet, x, y, gameview.paint);
        }
    }

    //我方子弹打敌方,若成功，则发出爆炸效果
    public boolean killMonster(Monster monster)
    {

        int x1 =  Constant.STARTGAME_XPOS + monster.getX()*monster.width;
        int y1 = Constant.STARTGAME_YPOS + monster.getY()*monster.height;


        //x,y两个坐标小于两个物体之间的连线，则为相撞
        if( Math.abs(x-x1)< Math.abs((width+monster.width)/2)
                &&  Math.abs(y-y1)< Math.abs((height+monster.height)/2) )
        {
            if(good) //我方子弹打怪兽
            {

                //相撞了
                gameview.fBullets.remove(this);	//移除我方子弹
                gameview.monsters.remove(monster);	//移除敌方

                monster.setBlive(false);//关闭线程
                live = false;	//关闭线程

                gameview.nKillMonster ++; 	//杀敌

                //画爆炸效果
                //产生爆炸效果
                Explosion explosion = new Explosion(gameview, monster.getX(), monster.getY());

                new Thread(new ExplosionRun(gameview, explosion)).start();

                gameview.explosions.add(explosion);

                gameview.info[monster.getX()][monster.getY()] = Kind.EXPLOSION;	//此点修正为爆炸

            }else //对方子弹打对方
            {
                gameview.mBullets.remove(this);	//移除对方子弹
                live = false;	//关闭线程
            }
            return true;
        }
        return false;
    }
    public boolean killFighter(Fighter fighter)
    {
        int x1 =  Constant.STARTGAME_XPOS + fighter.getX()*fighter.width;
        int y1 = Constant.STARTGAME_YPOS + fighter.getY()*fighter.height;


        //x,y两个坐标小于两个物体之间的连线，则为相撞
        if( Math.abs(x-x1)< Math.abs((width+fighter.width)/2)
                &&  Math.abs(y-y1)< Math.abs((height+fighter.height)/2) )
        {
            if(good == false) //对方子弹打我方
            {
                //相撞了
                live = false;	//关闭线程
                gameview.mBullets.remove(this);
                gameview.fighter.setNlive(gameview.fighter.getNlive()-1);	//生命数减１


                //画爆炸效果
                //产生爆炸效果
                Explosion explosion = new Explosion(gameview, fighter.getX(), fighter.getY());
                new Thread(new ExplosionRun(gameview, explosion)).start();
                gameview.explosions.add(explosion);

                gameview.createOneFighter();	//创建一个新的勇士
                gameview.regetInfo();	//重新得到信息列表

                gameview.info[fighter.getX()][fighter.getY()] = Kind.EXPLOSION;	//此点修正为爆炸

                if(gameview.fighter.getNlive() < 1)	//游戏结束
                {
                    gameview.bGameOver = true;
                    gameview.mainActivity.mHandler.sendEmptyMessage(Constant.MSG_LOSTHISGAME);
                }
            }else //我方打我方,不存在的，这里要删除
            {
              //  gameview.fBullets.remove(this);	//移除对方子弹
              //  live = false;	//关闭线程
            }
            return true;
        }
        return false;
    }

    public void killBullet(Bullet bullet)
    {
        if(good && !bullet.isGood()
                || !good && bullet.isGood())	//双方不一样才相互的打
        {
            int x1 = bullet.getX();
            int y1 = bullet.getY();

            if( Math.abs(x-x1)< width
                    &&  Math.abs(y-y1)< height)
            {
                if(good == true)
                {
                    gameview.fBullets.remove(this);	//移除子弹
                    gameview.mBullets.remove(bullet);
                }else
                {
                    gameview.fBullets.remove(bullet);	//移除子弹
                    gameview.mBullets.remove(this);
                }

                live = false;	//关闭线程
                bullet.setLive(false);
            }
        }
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public Direction getDir()
    {
        return direction;
    }

    public boolean isGood()
    {
        return good;
    }
    
    public boolean isLive()
    {
        return live;
    }

    public void setLive(boolean live)
    {
        this.live = live;
    }
}
