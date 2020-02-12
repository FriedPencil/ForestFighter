package com.example.forestfighter;

/**
 * Created by 曜爸 on 2020/2/10.
 */
public class ExplosionRun implements Runnable {
    GameView gameview;
    Explosion explosion;
    private boolean over;

    public ExplosionRun(GameView gameview, Explosion explosion)
    {
        this.gameview = gameview;
        this.explosion = explosion;
        over = false;
    }
    @Override
    public void run()
    {
        int ntype = 1;
        int x = explosion.getX();
        int y = explosion.getY();

        while(!over)
        {
            ntype = explosion.getNType();

            ntype++;

            if(ntype<=5)
            {
                explosion.setNType(ntype);
            }else
            {
                gameview.explosions.remove(explosion);	//移除这个点
                over = true;	//爆炸结束
                gameview.info[explosion.getX()][explosion.getY()] = Kind.EMPTY;	//重新置为空
            }

            try
            {
                Thread.sleep(Constant.TIME_SLEEP* Constant.EXPLOSION_TIME);
            } catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
