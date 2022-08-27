package com.example.finalchocohub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Thank_payment extends AppCompatActivity {

    Button backtohome;
    RelativeLayout  relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_payment);
        //getSupportActionBar().hide();
        backtohome=(Button)findViewById(R.id.backtohome);
        relativeLayout=(RelativeLayout)findViewById(R.id.relative);
        Animation animation= AnimationUtils.loadAnimation(Thank_payment.this,R.anim.bottom_to_up);
        relativeLayout.startAnimation(animation);
        backtohome.startAnimation(animation);

        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_offer=new Intent(Thank_payment.this,menu_list.class);
                startActivity(i_offer);
            }
        });
    }
}