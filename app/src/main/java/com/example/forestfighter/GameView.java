package com.example.forestfighter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by 曜爸 on 2020/2/10.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    SurfaceHolder mSurfaceHolder = null;
    MainActivity mainActivity = null;
    Paint paint = null;
    int screenW = 0;
    int screenH = 0;
    boolean bGameOver = false;    //游戏是否结束

    Bitmap wall = null;    //墙
    Bitmap empty = null; //空图
    Bitmap startmap = null;    //起点与终点图
    Bitmap endmap = null;

    Bitmap[] infoBitmap = null;    //信息图
    Bitmap[] fighterBitmap = null;    //勇士图
    Bitmap[] monsterBitmap = null;    //怪物图

    ArrayList<Monster> monsters = null;    //金鱼图
    ArrayList<Bullet> fBullets = null;    //我方子弹
    ArrayList<Bullet> mBullets = null;    //对方子弹
    ArrayList<Explosion> explosions = null;    //爆炸效果

    int level = 1;    //游戏级别
    int nKillMonster = 0;	//杀怪兽数
    int[][] mymap = null;    //地图信息
    Kind[][] info = null; //地图点类型信息

    Fighter fighter = null;

    public final Point start = new Point(0, 1);    //开始坐标(0,1)
    public final Point end = new Point(Constant.BRICK_X - 1, Constant.BRICK_Y - 1 - 1);    //终点坐标(11,10)

    public GameView(Context context, AttributeSet att) {
        super(context, att);

        mSurfaceHolder = this.getHolder();    //得到holder
        mSurfaceHolder.addCallback(this);    //添加回调函数
        setKeepScreenOn(true);    //设置背景常亮

        mainActivity = (MainActivity) context;

        setFocusable(true);
        paint = new Paint();
        paint.setAntiAlias(true);    //设置画笔无锯齿(如果不设置,可以看到效果好差)
        paint.setColor(Color.BLACK);

        loadBitmap();
        initGame();
    }

    //加载图片
    private void loadBitmap() {
        wall = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        empty = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        startmap = BitmapFactory.decodeResource(getResources(), R.drawable.start);
        endmap = BitmapFactory.decodeResource(getResources(), R.drawable.end);

        //EMPTY, WALL, WUGUI, JINYU, /* START, END, ZIDAN */
        infoBitmap = new Bitmap[2];
        infoBitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        infoBitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);

        //UP,RIGHT,DOWN,LEFT
        fighterBitmap = new Bitmap[4];
        fighterBitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fup);
        fighterBitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fright);
        fighterBitmap[2] = BitmapFactory.decodeResource(getResources(), R.drawable.fdown);
        fighterBitmap[3] = BitmapFactory.decodeResource(getResources(), R.drawable.fleft);

        //UP,RIGHT,DOWN,LEFT
        monsterBitmap = new Bitmap[4];
        monsterBitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.mup);
        monsterBitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.mright);
        monsterBitmap[2] = BitmapFactory.decodeResource(getResources(), R.drawable.mdown);
        monsterBitmap[3] = BitmapFactory.decodeResource(getResources(), R.drawable.mleft);
    }

    //初始化游戏
    private void initGame() {

        level = 1;//游戏级别
        mymap = MyMap.getMap(level);    //得到地图数据

        mymap[start.x][start.y] = 0;    //保证start 与 end两个点为空格
        mymap[end.x][end.y] = 0;

        info = new Kind[Constant.BRICK_X][Constant.BRICK_Y];    //初始化信息表

        fighter = new Fighter(this, start.x, start.y);    //初始位置在0,1上

        fBullets = new ArrayList<Bullet>();
        mBullets = new ArrayList<Bullet>();

        explosions = new ArrayList<Explosion>();
        monsters = new ArrayList<Monster>();

        //对信息赋初值
        for (int x = 0; x < Constant.BRICK_X; x++) {
            for (int y = 0; y < Constant.BRICK_Y; y++) {
                if (mymap[x][y] == 0)    //空格
                {
                    info[x][y] = Kind.EMPTY;
                } else    //墙面
                {
                    info[x][y] = Kind.WALL;
                }
            }
        }
        info[start.x][start.y] = Kind.FIGHTER;    //起始时，第一个点为勇士

        //初始时生成四条金鱼
        for (int n = 0; n < Constant.MONSTER_COUNT; n++) {
            createOneMonster();
        }
    }

    //添加怪物
    private void createOneMonster() {
        int x = 1;
        int y = 1;
        int ran = 0;

        //第一种方法生成怪兽
        while (true) {
            Random rdm = new Random(System.currentTimeMillis());
            ran = Math.abs(rdm.nextInt()) % ((Constant.BRICK_X - 1) * (Constant.BRICK_Y - 1));

            //先生成一个随机数
            x = ran / Constant.BRICK_Y + 1;
            y = ran / Constant.BRICK_X + 1;

            if (x >= 1 && x < Constant.BRICK_X - 1        //保证是合法的坐标
                    && y >= 1 && y < Constant.BRICK_Y - 1) {
                if (info[x][y] == Kind.EMPTY)    //是空格才进行生成怪兽
                {
                    Monster oneMonster = new Monster(this, x, y, (int) Math.random() * 4);
                    new Thread(new MonsterRun(this, oneMonster)).start();
                    monsters.add(oneMonster);
                    info[x][y] = Kind.MONSTER;
                    break;
                }
            }
            continue;    //继续产生怪兽
        }
    }

    public void createOneFighter() {
        if (fighter == null) {
            fighter = new Fighter(this, start.x, start.y);
        }
        fighter.setX(start.x);
        fighter.setY(start.y);

        info[start.x][start.y] = Kind.FIGHTER;
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        screenW = getWidth();
        screenH = getHeight();
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        new Thread(this).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        bGameOver = true;
    }

    @Override
    public void run() {
        while (!bGameOver) {
            synchronized (this)    //锁定后判断
            {
                kill();//判断是不是爆炸了
            }
            MyDraw();
            mainActivity.mHandler.sendEmptyMessage(Constant.MSG_UPDATE);    //发送更新的消息
            try {
                Thread.sleep(Constant.TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void kill()//此处有问题，不过大部分情况下都只有一颗子弹，问题不会出现
    {
        for (int i = 0; i < fBullets.size(); i++) {
            Bullet fBullet = fBullets.get(i);
            for (int j = 0; j < monsters.size(); j++) {
                Monster monster = monsters.get(j);
                if (fBullet.killMonster(monster) == true)    //我方子弹打怪兽
                {
                    break;
                }
            }
        }

        for (int i = 0; i < fBullets.size(); i++) {
            Bullet fBullet = fBullets.get(i);
            fBullet.killFighter(fighter); //我方子弹打勇士
        }

        for (int i = 0; i < fBullets.size(); i++)    //我方子弹打对方子弹
        {
            for (int j = 0; j < mBullets.size(); j++) {
                fBullets.get(i).killBullet(mBullets.get(j));
            }
        }

        for (int i = 0; i < mBullets.size(); i++)    //对方子弹打我方子弹
        {
            for (int j = 0; j < fBullets.size(); j++) {
                mBullets.get(i).killBullet(fBullets.get(j));
            }
        }

        for (int i = 0; i < mBullets.size(); i++) {
            mBullets.get(i).killFighter(fighter); //敌方子弹打勇士
        }

        for (int i = 0; i < mBullets.size(); i++) {
            for (int j = 0; j < monsters.size(); j++) {
                if (mBullets.get(i).killMonster(monsters.get(j)) == true) //敌方子弹打怪兽
                {
                    break;
                }
            }
        }
    }

    public void MyDraw() {
        Canvas canvas = mSurfaceHolder.lockCanvas();    //得到一个canvas实例
        if (canvas == null) {
            return;
        }
        canvas.drawRect(0, 0, screenW, screenH, paint);    //清屏操作
        canvas.save();

        //第一种方法
        drawGameview(canvas);
        drawBullet(canvas);//画双方子弹
        drawExplosion(canvas);    //画爆炸效果

    /*    if(migong.bNeedDrawTishi) {
            drawtishi(canvas);
        }
*/
        canvas.restore();
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void drawGameview(Canvas canvas) {
        int ntype = 0;
        //EMPTY, WALL, WUGUI, JINYU, /* START, END, ZIDAN */
        for (int x = 0; x < Constant.BRICK_X; x++) {
            for (int y = 0; y < Constant.BRICK_Y; y++) {
                ntype = info[x][y].ordinal();

                //先空白后画墙，可以优化
                canvas.drawBitmap(infoBitmap[0],    //空格
                        Constant.STARTGAME_XPOS + x * empty.getWidth(),
                        Constant.STARTGAME_YPOS + y * empty.getHeight(),
                        paint);

                if (ntype == 1) //墙
                {
                    canvas.drawBitmap(infoBitmap[1],    //空格
                            Constant.STARTGAME_XPOS + x * empty.getWidth(),
                            Constant.STARTGAME_YPOS + y * empty.getHeight(),
                            paint);
                }
            }
        }
        //单独画起点（没画）与终点

        canvas.drawBitmap(endmap,
                Constant.STARTGAME_XPOS + end.x * endmap.getWidth(),
                Constant.STARTGAME_YPOS + end.y * endmap.getHeight(),
                paint);

        for (int x = 0; x < Constant.BRICK_X; x++) {
            for (int y = 0; y < Constant.BRICK_Y; y++) {
                ntype = info[x][y].ordinal();

                if (ntype == 2)    //勇士
                {
                    Direction dir = fighter.getDir();

                    canvas.drawBitmap(fighterBitmap[dir.ordinal()],
                            Constant.STARTGAME_XPOS + x * empty.getWidth(),
                            Constant.STARTGAME_YPOS + y * empty.getHeight(),
                            paint);

                } else if (ntype == 3)    //怪兽
                {
                    Direction dir = Direction.RIGHT;
                    boolean bexist = false;

                    for (Monster monster : monsters) {
                        if (monster.getX() == x || monster.getY() == y) {
                            dir = monster.getDir();
                            bexist = true;
                            break;
                        }
                    }
                    if (bexist)    //怪兽存在
                    {
                        canvas.drawBitmap(monsterBitmap[dir.ordinal()],
                                Constant.STARTGAME_XPOS + x * empty.getWidth(),
                                Constant.STARTGAME_YPOS + y * empty.getHeight(),
                                paint);
                    }
                }
            }
        }
    }

    private void drawExplosion(Canvas canvas) {
        for (Explosion explosion : explosions) {
            explosion.draw(canvas);
        }
    }

    private void drawBullet(Canvas canvas) {
        for (Bullet bullet : fBullets) {
            bullet.draw(canvas);
        }
        for (Bullet bullet : mBullets) {
            bullet.draw(canvas);
        }
    }

    /*    private void drawtishi(Canvas canvas)
        {
            paint.setColor(Color.RED);	//画红线
            //npoint = 2, 3, 4
            Log.i("wcj", "gameview drawtishi");
            for(int n = 1; n < migong.result.size(); n++)
            {
                Rule next = migong.result.get(n);
                Rule pre = migong.result.get(n-1);



                canvas.drawLine(ContstUtil.STARTGAME_XPOS+pre.getX()*empty.getWidth()+empty.getWidth()/2,
                        ContstUtil.STARTGAME_YPOS+pre.getY()*empty.getHeight()+empty.getHeight()/2,
                        ContstUtil.STARTGAME_XPOS+next.getX()*empty.getWidth()+empty.getWidth()/2,
                        ContstUtil.STARTGAME_YPOS+next.getY()*empty.getHeight()+empty.getHeight()/2, paint);
            }
            paint.setColor(Color.BLACK);	//重新画黑线
        }
        */
    //重新得到当前的信息
    public void regetInfo() {
        for (int x = 0; x < Constant.BRICK_X; x++) {
            for (int y = 0; y < Constant.BRICK_Y; y++) {
                info[x][y] = Kind.EMPTY;
                if (mymap[x][y] == 1) {
                    info[x][y] = Kind.WALL;
                }
            }
        }

        info[fighter.getX()][fighter.getY()] = Kind.FIGHTER;

        for (Monster monster : monsters) {
            info[monster.getX()][monster.getY()] = Kind.MONSTER;
        }
    }

    public void resumeGame() {
        if(level>=10) level = 10;

        //清空链表信息
        monsters.clear();
        fBullets.clear();
        mBullets.clear();
        explosions.clear();

        mymap = MyMap.getMap(level);	//重新得到地图

        createOneFighter();	//得到新的勇士

        Random rdm = new Random(System.currentTimeMillis());
        int ran = Math.abs(rdm.nextInt())%3;
        //得到３+level个怪物

        for(int i = 0; i < level+ran; i++)
        {
            createOneMonster();
        }
        regetInfo(); //更新信息
    }

    public void clearGameviewData() {
        //消除自己
        if(fighter != null) {
            fighter= null;
        }
        //消除我方子弹
        for(Bullet fBullet : fBullets) {
            fBullet.setLive(false);	//停止线程
        }
        fBullets.clear();
        //消除敌方怪兽
        for(Monster monster : monsters) {
            monster.setBlive(false);	//停止线程
        }
        monsters.clear();
        //消除敌方子弹
        for(Bullet mBullet : mBullets) {
            mBullet.setLive(false);	//停止线程
        }
        mBullets.clear();

        bGameOver = true;	//游戏结束
    }
    public void lostGameDo()
    {
        bGameOver = true;	//游戏结束

        mainActivity.alertDialog.setTitle("游戏结束");
        mainActivity.alertDialog.setMessage("游戏将要结束，点击“确认”返回到主界面，点击“重玩”则重新开始游戏");

        mainActivity.alertDialog.setNegativeButton("重玩",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                clearGameviewData();	//清除用户数据

                level = 1;	//强制设置为第一关
                bGameOver = false;	//游戏没有结束
                resumeGame();
                new Thread(GameView.this).start();
            }});

        mainActivity.alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                mainActivity.mHandler.sendEmptyMessage(Constant.MSG_WELCOMGAMEEVIEW);
            }

        });
        mainActivity.alertDialog.show();
    }

}
