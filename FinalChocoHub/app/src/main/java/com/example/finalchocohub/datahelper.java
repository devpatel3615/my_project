package com.example.finalchocohub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class datahelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="MYDB";
    private static final String TABLE_LOGIN="LOGIN";
    private static final String KEY_ID="id";
    public datahelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE "+TABLE_LOGIN+ "("+ KEY_ID+" INTEGER PRIMARY KEY"+")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion , int newVersion ) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_LOGIN);
        onCreate(db);
    }
    public Integer delete(String id)
    {
        SQLiteDatabase db=getWritableDatabase();
        return db.delete(TABLE_LOGIN,null,null);
    }
    public Long setlogindetail(Integer id)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put(KEY_ID,id);
        return db.insert(TABLE_LOGIN,null,c);
    }
    public Cursor getlogindetail()
    {
        SQLiteDatabase db=getWritableDatabase();
        Cursor  c=db.rawQuery("select * from LOGIN ",null);
        return c;
    }

}
