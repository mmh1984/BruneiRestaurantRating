package com.example.maynard.bruneirestaurantrating;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class admin extends AppCompatActivity implements View.OnClickListener{
Button btnresto,btnmembers;
    String userlevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnresto= (Button) findViewById(R.id.btnrestaurants);
        btnmembers= (Button) findViewById(R.id.btnmembers);

        btnresto.setOnClickListener(this);
        btnmembers.setOnClickListener(this);

        userlevel=getIntent().getExtras().getString("usertype");
    }

    @Override
    public void onBackPressed() {


        final Dialog d=new Dialog(this);
        d.setCancelable(false);
        d.setContentView(R.layout.activity_logout);
        d.show();

        Button yes= (Button) d.findViewById(R.id.btnyes);
        Button no= (Button) d.findViewById(R.id.btnno);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

    }


    @Override
    public void onClick(View view) {
        Intent in;
        if(view.getId()==R.id.btnmembers){
            in=new Intent(getApplicationContext(),memberslist.class);
            in.putExtra("usertype",userlevel);
           startActivity(in);
        }

        if(view.getId()==R.id.btnrestaurants ){

            in=new Intent(getApplicationContext(),adminrestaurant.class);
            in.putExtra("usertype",userlevel);
            startActivity(in);




        }
    }
}
