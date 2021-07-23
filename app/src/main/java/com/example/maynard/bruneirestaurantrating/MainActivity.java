package com.example.maynard.bruneirestaurantrating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    ImageView logo,loading;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        logo= (ImageView) findViewById(R.id.ivLogo);
        title= (TextView) findViewById(R.id.lblTitle);
        loading= (ImageView) findViewById(R.id.ivLoading);



        loading.setVisibility(View.INVISIBLE);


show_loading();


    }

    public void show_loading(){
        Handler h=new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.VISIBLE);

                //Toast.makeText(getApplicationContext(),"Yehey",Toast.LENGTH_SHORT).show();
                hide_loading();
            }
        },3000);

    }
    public void hide_loading(){
        Handler h2=new Handler();
        h2.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.clearAnimation();
                loading.setVisibility(View.INVISIBLE);
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        },5000);
    }


    }
