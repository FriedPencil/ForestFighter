package com.example.forestfighter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by 曜爸 on 2020/2/10.
 */
public class Monster implements Alive{
    private GameView gameView;
    private int x;
    private int y;
    private boolean blive;	//是否有生命

    private Bitmap[] monsters;
    Direction direction;	//方向

    public int width;
    public int height;

    int nlive;	//怪物的生命数

    public Monster(GameView gameView, int x, int y)
    {
        // TODO Auto-generated constructor stub
        this.gameView = gameView;
        this.x = x;
        this.y = y;

        blive = true;
        direction = Direction.RIGHT;

        loadBitmap();
    }

    //带方向的构造方法
    public Monster(GameView gameView2, int x2, int y2, int i)
    {
        this(gameView2, x2, y2);

        switch(i)
        {
            case 0:
                direction = Direction.UP;
                break;
            case 1:
                direction = Direction.RIGHT;
                break;
            case 2:
                direction = Direction.DOWN;
                break;
            case 3:
                direction = Direction.LEFT;
                break;
            default:
                direction = Direction.RIGHT;
        }
    }
    private void loadBitmap()
    {
        // TODO Auto-generated method stub
        //UP,RIGHT,DOWN,LEFT
        monsters = new Bitmap[4];
        monsters[0] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.mup);
        monsters[1] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.mright);
        monsters[2] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.mdown);
        monsters[3] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.mleft);

        width = monsters[0].getWidth();
        height = monsters[0].getHeight();
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

    public boolean isBlive()
    {
        return blive;
    }

    public void setBlive(boolean blive)
    {
        this.blive = blive;
    }

    public Direction getDir()
    {
        return direction;
    }

    public void setDir(Direction dir)
    {
        this.direction = dir;
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(monsters[direction.ordinal()],
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
                            direction, false);

                    new Thread(new BulletRun(gameView, bullet)).start();
                    gameView.mBullets.add(bullet);
                }
                break;
            case 1:	//Dir.RIGHT
                if((x+1)< Constant.BRICK_X)	//空格才发子弹
                {
                    Bullet bullet = new Bullet(gameView,
                            Constant.STARTGAME_XPOS + (x)*gameView.wall.getWidth()+gameView.wall.getWidth(),
                            Constant.STARTGAME_YPOS + (y)*gameView.wall.getHeight()+gameView.wall.getHeight()/2-Constant.BULLET_WIDTH/2,
                            direction, false);

                    new Thread(new BulletRun(gameView, bullet)).start();
                    gameView.mBullets.add(bullet);
                }
                break;
            case 2:	//Dir.DOWN
                if((y+1)<Constant.BRICK_Y)	//空格才发子弹
                {
                    Bullet bullet = new Bullet(gameView,
                            Constant.STARTGAME_XPOS + (x)*gameView.wall.getWidth()+gameView.wall.getWidth()/2-Constant.BULLET_WIDTH/2,
                            Constant.STARTGAME_YPOS + (y)*gameView.wall.getHeight()+gameView.wall.getHeight(),
                            direction, false);

                    new Thread(new BulletRun(gameView, bullet)).start();
                    gameView.mBullets.add(bullet);
                }
                break;
            case 3:	//Dir.LEFT
                if((x-1)>=0)	//空格才发子弹
                {
                    Bullet bullet = new Bullet(gameView,
                            Constant.STARTGAME_XPOS + (x)*gameView.wall.getWidth()-Constant.BULLET_WIDTH,
                            Constant.STARTGAME_YPOS + (y)*gameView.wall.getHeight()+gameView.wall.getHeight()/2-Constant.BULLET_WIDTH/2,
                            direction, false);

                    new Thread(new BulletRun(gameView, bullet)).start();
                    gameView.mBullets.add(bullet);
                }
                break;
            default:
        }
    }
}
