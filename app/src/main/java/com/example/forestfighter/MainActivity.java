package com.example.forestfighter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private ImageButton startgame;
    private ImageButton rulesgame;
    private ImageButton aboutgame;
    private ImageButton setting;
    private ImageButton quitgame;

    private ImageButton backbutton;
    private TextView aboutview;

    ImageButton upbutton = null;
    ImageButton rightbutton = null;
    ImageButton downbutton = null;
    ImageButton leftbutton = null;
    TextView info = null;	//显示信息的界面
    ImageButton attackbutton = null;
    ImageButton tipbutton = null;
    GameView gameview = null;

    AlertDialog.Builder alertDialog = null;
    DBHelper dbHelper = DBHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(MainActivity.this,Music.class));//启动背景音乐服务

        requestWindowFeature(Window.FEATURE_NO_TITLE); //隐去标题栏(程序的名字)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐去电池等图标和一切修饰部分(状态栏部分)

        welcome();
    }

    @Override
    protected void onStop() {
        stopService(new Intent(MainActivity.this,Music.class));
        super.onStop();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case Constant.MSG_WELCOMGAMEEVIEW:
                    welcome();
                    break;
                case Constant.MSG_UPDATE:	//更新信息
                    if(gameview!=null)
                    {
                        info.setText("Level:"+gameview.level+" "
                                +"Position("+gameview.fighter.getX()+","+gameview.fighter.getY()+")\n"
                                +"Lives:"+gameview.fighter.getNlive()+" "
                                +"Bullets:"+gameview.fBullets.size()+"\n"
                                +"Scores:"+gameview.nKillMonster+"\n"
                                +"Monsters:"+gameview.monsters.size()+" "
                                +"MBullets:"+gameview.mBullets.size()+"\n");
                    }
                    break;
                case Constant.MSG_WINTHISGAME:	//赢了这一局
                    Toast.makeText(MainActivity.this, "Congratulations,you won the level"+gameview.level+"！", Toast.LENGTH_LONG).show();
                    gameview.level ++; //游戏级别加１
                    gameview.fighter.setNlive(gameview.fighter.getNlive()+1);	//勇士的生命数加１
                    gameview.resumeGame();	//重新开始新的一局游戏
                    break;
                case Constant.MSG_LOSTHISGAME:	//输了这一局
                    //Toast.makeText(MiGong.this, "大虾再接再励！", Toast.LENGTH_LONG).show();
                    gameview.lostGameDo();
                    break;
                default:
                    break;
            }
        }
    };
    private void welcome(){
        setContentView(R.layout.activity_main);

        startgame = (ImageButton)findViewById(R.id.startgame);
        rulesgame = (ImageButton)findViewById(R.id.rulesgame);
        aboutgame = (ImageButton)findViewById(R.id.aboutgame);
        quitgame = (ImageButton)findViewById(R.id.quitgame);
        setting = (ImageButton)findViewById(R.id.setting);

        startgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        rulesgame.setOnClickListener(v->{
            rulesGame();
        });
        aboutgame.setOnClickListener(v->{aboutGame();});
        quitgame.setOnClickListener(v->{quitGame();});
        setting.setOnClickListener(v->{settings();});
    }
    private void startGame(){
        setContentView(R.layout.gameview);
        initGame();
        if(dbHelper.queryLevel().length() != 0){
            gameview.level = Integer.parseInt(dbHelper.queryLevel());
        }else{
            dbHelper.insertData("1");
        }
    }
    private void initGame()
    {

        gameview = (GameView)findViewById(R.id.mygameview);

        upbutton = (ImageButton)findViewById(R.id.up);
        rightbutton = (ImageButton)findViewById(R.id.right);
        downbutton = (ImageButton)findViewById(R.id.down);
        leftbutton = (ImageButton)findViewById(R.id.left);
        attackbutton = (ImageButton)findViewById(R.id.attack);
        tipbutton = (ImageButton)findViewById(R.id.tip);
        backbutton = (ImageButton)findViewById(R.id.back);

        info = (TextView)findViewById(R.id.info);

        backbutton.setOnClickListener(backlistener);
//***        tipbutton.setOnClickListener(zuiduanlistener);
        upbutton.setOnClickListener(uplistener);
        rightbutton.setOnClickListener(rightlistener);
        downbutton.setOnClickListener(downlistener);
        leftbutton.setOnClickListener(leftlistener);
        attackbutton.setOnClickListener(attacklistener);

        //游戏结束对话框
        alertDialog = new AlertDialog.Builder(MainActivity.this);
    }
    private void rulesGame()
    {
        setContentView(R.layout.rulesgame);
        backbutton = (ImageButton)findViewById(R.id.back);
        backbutton.setOnClickListener(backlistener);
    }
    private void aboutGame()
    {
        setContentView(R.layout.aboutgame);
        backbutton = (ImageButton)findViewById(R.id.back);
        aboutview = (TextView)findViewById(R.id.aboutview);
        String str = dbHelper.queryLevel();
        int i = Integer.parseInt(str);
        i = i - 1;
        str = String.valueOf(i);
        aboutview.setText(str);
        backbutton.setOnClickListener(backlistener);
    }
    private void settings(){
        setLevel();
    }
    public void setLevel(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Which level");
        final EditText edit = new EditText(MainActivity.this);
        dialog.setView(edit);
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dbHelper.deleteDataById("a");
                String str = edit.getText().toString();
                dbHelper.insertData(str);
            }
        });
        dialog.setNegativeButton("CANCLE",  new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        dialog.show();
    }
    private void quitGame(){
        finish();
    }

    View.OnClickListener backlistener = new View.OnClickListener() {
        public void onClick(View v) {
            if(gameview != null)	//游戏界面的返回处理
            {
                dbHelper.deleteDataById("a");
                dbHelper.insertData(gameview.level+"");

                gameview.clearGameviewData();	//清空游戏界面中的数据
                welcome();
                gameview = null;
            }else{
                welcome();
            }
        }
    };

    View.OnClickListener uplistener = new View.OnClickListener() {
        public void onClick(View arg0) {
            int x = gameview.fighter.getX();
            int y = gameview.fighter.getY();
            y--;
            if(gameview.fighter.getDir() != Direction.UP)	//方向不相同，则返回
            {
                gameview.fighter.setDir(Direction.UP);
                return;
            }

            if(y>=0 && gameview.info[x][y] == Kind.EMPTY)	//是空格，则可以到达
            {
                gameview.fighter.setX(x);
                gameview.fighter.setY(y);
                //对信息表中进行修正
                gameview.info[x][y] = Kind.FIGHTER;	//当前的为勇士
                if(y+1<Constant.BRICK_Y) gameview.info[x][y+1] = Kind.EMPTY;	//之前的为空格
            }
        }

    };

    View.OnClickListener rightlistener = new View.OnClickListener() {
        public void onClick(View arg0) {
            int x = gameview.fighter.getX();
            int y = gameview.fighter.getY();
            x++;
            if(gameview.fighter.getDir() != Direction.RIGHT)	//方向不相同，则返回
            {
                gameview.fighter.setDir(Direction.RIGHT);
                return;
            }
            if(x<Constant.BRICK_X && gameview.info[x][y] == Kind.EMPTY) {
                gameview.fighter.setX(x);
                gameview.fighter.setY(y);

                //对信息表中进行修正
                gameview.info[x][y] = Kind.FIGHTER;	//当前的为勇士
                if(x-1>=0) gameview.info[x-1][y] = Kind.EMPTY;	//之前的为空格

                if(x == gameview.end.x && y == gameview.end.y //是最后一个点
                        && gameview.monsters.size() <= 0 ) //没有了怪兽
                {
                    mHandler.sendEmptyMessage(Constant.MSG_WINTHISGAME);	//发送赢了这局的消息
                }
            }
        }
    };

    View.OnClickListener downlistener = new View.OnClickListener() {
        public void onClick(View arg0) {
            int x = gameview.fighter.getX();
            int y = gameview.fighter.getY();
            y++;
            if(gameview.fighter.getDir() != Direction.DOWN)	//方向不相同，则返回
            {
                gameview.fighter.setDir(Direction.DOWN);
                return;
            }

            if(y<Constant.BRICK_Y && gameview.info[x][y] == Kind.EMPTY) {
                gameview.fighter.setX(x);
                gameview.fighter.setY(y);
                //对信息表中进行修正
                gameview.info[x][y] = Kind.FIGHTER;	//当前的为勇士
                if(y-1>=0) gameview.info[x][y-1] = Kind.EMPTY;	//之前的为空格
            }
        }
    };

    View.OnClickListener leftlistener = new View.OnClickListener() {
        public void onClick(View arg0) {
            int x = gameview.fighter.getX();
            int y = gameview.fighter.getY();
            x--;
            if(gameview.fighter.getDir() != Direction.LEFT)	//方向不相同，则返回
            {
                gameview.fighter.setDir(Direction.LEFT);
                return;
            }

            if(x>=0 && gameview.info[x][y] == Kind.EMPTY) {
                gameview.fighter.setX(x);
                gameview.fighter.setY(y);
                //对信息表中进行修正
                gameview.info[x][y] = Kind.FIGHTER;	//当前的为勇士
                if(x+1<Constant.BRICK_X) gameview.info[x+1][y] = Kind.EMPTY;	//之前的为空格
            }
        }
    };

    View.OnClickListener attacklistener = new View.OnClickListener() {
        public void onClick(View arg0) {
            int x = gameview.fighter.getX();
            int y = gameview.fighter.getY();
            gameview.fighter.fire();	//勇士发射子弹
        }
    };
}
