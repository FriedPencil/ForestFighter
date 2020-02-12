package com.example.forestfighter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by 曜爸 on 2020/2/10.
 */
public class Fighter implements Alive {
    private static final String TAG = "Fighter";
    private GameView gameView;
    private int x;
    private int y;

    private Bitmap[] fighter;
    Direction direction = null;	//方向

    public int width;	//图片的宽高度
    public int height;

    int nlive;	//生命数,初始有５条命

    public Fighter(GameView gameView, int i, int j)
    {
        this.gameView = gameView;
        this.x = i;
        this.y = j;

        direction = Direction.RIGHT;

        nlive = Constant.FIGHTER_LIVE;

        loadBitmap();
    }

    private void loadBitmap()
    {
        // TODO Auto-generated method stub
        //UP,RIGHT,DOWN,LEFT
        fighter = new Bitmap[4];
        fighter[0] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.fup);
        fighter[1] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.fright);
        fighter[2] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.fdown);
        fighter[3] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.fleft);

        width = fighter[0].getWidth();
        height = fighter[0].getHeight();
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

    public void setDir(Direction dir)
    {
        this.direction = dir;
    }

    public int getNlive()
    {
        return nlive;
    }

    public void setNlive(int nlive)
    {
        this.nlive = nlive;
    }

    @Override
    public void draw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        canvas.drawBitmap(fighter[direction.ordinal()],
                Constant.STARTGAME_XPOS + x*width,
                Constant.STARTGAME_YPOS + y*height,
                gameView.paint);
    }

    public void fire()
    {
        //UP,RIGHT,DOWN,LEFT
        switch(direction.ordinal())
        {
            case 0:	//Dir.UP
                if(y-1 >= 0)	//空格才发子弹
                {
                    Bullet bullet = new Bullet(gameView,
                            Constant.STARTGAME_XPOS + (x)*gameView.wall.getWidth()+gameView.wall.getWidth()/2-Constant.BULLET_WIDTH/2,
                            Constant.STARTGAME_YPOS + (y)*gameView.wall.getHeight()-Constant.BULLET_WIDTH,
                            direction, true);

                    new Thread(new BulletRun(gameView, bullet)).start();
                    gameView.fBullets.add(bullet);
                }
                break;
            case 1:	//Dir.RIGHT
                if((x+1)< Constant.BRICK_X)	//空格才发子弹
                {
                    Bullet bullet = new Bullet(gameView,
                            Constant.STARTGAME_XPOS + (x)*gameView.wall.getWidth()+gameView.wall.getWidth(),
                            Constant.STARTGAME_YPOS + (y)*gameView.wall.getHeight()+gameView.wall.getHeight()/2-Constant.BULLET_WIDTH/2,
                            direction, true);

                    new Thread(new BulletRun(gameView, bullet)).start();
                    gameView.fBullets.add(bullet);
                }
                break;
            case 2:	//Dir.DOWN
                if((y+1)<Constant.BRICK_Y)	//空格才发子弹
                {
                    Bullet bullet = new Bullet(gameView,
                            Constant.STARTGAME_XPOS + (x)*gameView.wall.getWidth()+gameView.wall.getWidth()/2-Constant.BULLET_WIDTH/2,
                            Constant.STARTGAME_YPOS + (y)*gameView.wall.getHeight()+gameView.wall.getHeight(),
                            direction, true);

                    new Thread(new BulletRun(gameView, bullet)).start();
                    gameView.fBullets.add(bullet);
                }
                break;
            case 3:	//Dir.LEFT
                if((x-1)>=0)	//空格才发子弹
                {
                    Bullet bullet = new Bullet(gameView,
                            Constant.STARTGAME_XPOS + (x)*gameView.wall.getWidth()-Constant.BULLET_WIDTH,	//子弹的宽度为５像素
                            Constant.STARTGAME_YPOS + (y)*gameView.wall.getHeight()+gameView.wall.getHeight()/2-Constant.BULLET_WIDTH,
                            direction, true);

                    new Thread(new BulletRun(gameView, bullet)).start();
                    gameView.fBullets.add(bullet);
                }
                break;
            default:
        }
    }
}
