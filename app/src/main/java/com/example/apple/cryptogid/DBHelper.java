package com.example.apple.cryptogid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    private static volatile DBHelper instance;
    public static final String DATABASE_NAME = "database";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "coins";
    public static final String KEY_PRICE = "price";
    public static final String KEY_STATUS = "status";
    public static final String KEY_NAME = "name";
    public static final String KEY_IMG = "image";
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "( " + KEY_PRICE + "integer, " +
                               KEY_NAME + " text," + KEY_IMG + " integer, " + KEY_STATUS + "integer )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static DBHelper getInstance(Context context){
        if (instance==null){
            synchronized (DBHelper.class){
                if (instance==null)
                    instance = new DBHelper(context);
            }
        }
        return instance;
    }
}
