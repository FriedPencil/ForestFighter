package com.example.forestfighter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by 曜爸 on 2020/2/10.
 */
public class Explosion implements Alive {
    int x;	//x,y不是绝对的坐标，为数组中的坐标相一致
    int y;

    int nType;	//1---5

    private GameView gameview;
    private Bitmap[] explosions = null;

    public Explosion(GameView gameview, int x, int y)
    {
        this.gameview = gameview;
        this.x = x;
        this.y = y;

        nType = 1;	//初始值为第一张图片

        loadBitmap();
    }
    private void loadBitmap()
    {
        explosions = new Bitmap[5];

        explosions[0] = BitmapFactory.decodeResource(gameview.getResources(), R.drawable.e1);
        explosions[1] = BitmapFactory.decodeResource(gameview.getResources(), R.drawable.e2);
        explosions[2] = BitmapFactory.decodeResource(gameview.getResources(), R.drawable.e3);
        explosions[3] = BitmapFactory.decodeResource(gameview.getResources(), R.drawable.e4);
        explosions[4] = BitmapFactory.decodeResource(gameview.getResources(), R.drawable.e5);
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(explosions[nType-1],
                Constant.STARTGAME_XPOS + x*explosions[0].getWidth(),
                Constant.STARTGAME_YPOS + y*explosions[0].getHeight(),
                gameview.paint);
    }


    public int getNType()
    {
        return nType;
    }

    public void setNType(int type)
    {
        nType = type;
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
}
