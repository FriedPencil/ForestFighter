package com.example.forestfighter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 曜爸 on 2020/2/11.
 */
public class DBHelper extends SQLiteOpenHelper {
    //表名
    private static String TableName = "level";
    //数据库名
    private static String DBName = "test.db";
    //数据库版本号
    private static int DBVersion = 1;

    public static DBHelper dbHelper = null;

    private SQLiteDatabase database;

    private String createDBSql =
            "create table level(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "areaId TEXT NOT NULL default a UNIQUE," +
                    "level TEXT NOT NULL default '1');";

    public DBHelper(Context context){
        super(context,DBName,null,DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createDBSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static DBHelper getInstance(Context context){
        if(dbHelper == null){
            synchronized(DBHelper.class){
                if(dbHelper == null) dbHelper = new DBHelper(context);
            }
        }
        return dbHelper;
    }

    public void insertData(String values){
        ContentValues contentValues = new ContentValues();
        contentValues.put("areaId","a");
        contentValues.put("level",values);
        database = dbHelper.getWritableDatabase();
        database.insert(TableName,null,contentValues);
    }

    public void deleteDataById(String areaId){
        String[] args = {areaId};
        database = dbHelper.getWritableDatabase();
        database.delete(TableName,"areaId=?",args);
    }

    public String queryLevel(){
        String str="1";
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(TableName,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            str = cursor.getString(cursor.getColumnIndex("level"));
        }
        return str;
    }
}
