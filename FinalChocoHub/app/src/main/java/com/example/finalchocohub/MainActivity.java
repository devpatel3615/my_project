package com.example.finalchocohub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    datahelper db=null;

    String id=null;
    SharedPreferences ses=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Valentina choco");
        db=new datahelper(MainActivity.this);
        ses=getSharedPreferences(login.sessionLogin,MODE_PRIVATE);

        Thread th=new Thread()
        {
            public void run() {
                try {
                    Thread.sleep(2000);

                } catch (Exception e) {

                } finally {
                    Cursor cursor=db.getlogindetail();
                    Integer cnt=cursor.getCount();
                    if(cnt>0)
                    {
                        cursor.moveToFirst();
                        do{
                            id=cursor.getString(0);
                        }while (cursor.moveToNext());
                        SharedPreferences.Editor obj=ses.edit();
                        obj.putString("cid",id);
                        obj.commit();
                        Intent i = new Intent(MainActivity.this, Fragment_menu.class);
                        startActivity(i);

                    }
                    else {
                        Intent i = new Intent(MainActivity.this, login.class);
                        startActivity(i);
                    }
                }
            }
        };
        th.start();
    }
}