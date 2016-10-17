package net.npaka.helloworld;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    public final static String DB_NAME ="test.db";//DB名
    public final static String DB_TABLE="test";   //テーブル名
    public final static int    DB_VERSION=1;      //バージョン

    //データベースヘルパーのコンストラクタ(2)
    public DBHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }
    
    //データベースの生成(3)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+
            DB_TABLE+"(id text primary key,info text)");
        ContentValues values=new ContentValues();
        values.put("id","0");
        values.put("info","テストデータ1");
        db.insert(DB_TABLE,"",values);
        values.put("id","1");
        values.put("info","テストデータ2");
        db.insert(DB_TABLE,"",values);
        values.put("id","2");
        values.put("info","テストデータ3");
        db.insert(DB_TABLE,"",values);
    }

    //データベースのアップグレード(4)
    @Override
    public void onUpgrade(SQLiteDatabase db,
        int oldVersion,int newVersion) {
        db.execSQL("drop talbe if exists "+DB_TABLE);
        onCreate(db);
    }
}

