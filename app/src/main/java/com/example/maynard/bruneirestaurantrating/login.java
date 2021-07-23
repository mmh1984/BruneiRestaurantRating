package com.example.maynard.bruneirestaurantrating;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.BaseKeyListener;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;


public class login extends AppCompatActivity implements View.OnClickListener {

    EditText txtemail,txtpassword;
    Button btnlogin,btnregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtemail= (EditText) findViewById(R.id.etEmail);
        txtpassword= (EditText) findViewById(R.id.etPassword);

        btnlogin= (Button) findViewById(R.id.btnLogin);
        btnregister= (Button) findViewById(R.id.btnRegister);

        btnlogin.setOnClickListener(this);
        btnregister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnLogin:
            check_login();
                break;
            case R.id.btnRegister:
                Intent i=new Intent(getApplicationContext(),register.class);
                startActivity(i);

        }
    }

    public void check_login() {
        try {
            if(txtemail.getText().toString().isEmpty() || txtpassword.getText().toString().isEmpty()){
                txtemail.setError("Enter your email");
                txtpassword.setError("Enter your password");

            }
            else {
                if (isValidEmail(txtemail.getText().toString())==false){
                    txtemail.setError("invalid email");

                }
               else {
                    String email,password;
                    email=txtemail.getText().toString();
                    password=txtpassword.getText().toString();
                    BackgroundWorker2 work=new BackgroundWorker2();
                    work.execute(email,password);
                }
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    public class BackgroundWorker2 extends AsyncTask<String,Void,Void> {

        ProgressDialog p;
        AlertDialog a;

        String result="";
        String emailadd;

        @Override
        protected Void doInBackground(String... params) {
            String registerurl="http://www.kemudaremittance.com/restobn/login.php";
            //String registerurl="http://10.0.2.2/restobn/login.php";
            try {
                URL url=new URL(registerurl);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);


                //output stream
                OutputStream out=urlConnection.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));

                //create data url
                String postdata= URLEncoder.encode("email","UTF-8")+"=" + URLEncoder.encode(params[0],"UTF-8");
                postdata +="&"+ URLEncoder.encode("password","UTF-8")+"=" + URLEncoder.encode(params[1],"UTF-8");



                br.write(postdata);
                br.flush();
                br.close();
                out.close();

                InputStream in=urlConnection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(in,"ISO-8859-1"));

                String line="";

                while ((line=reader.readLine())!=null){
                    result+=line;
                }
                reader.close();
                in.close();
                urlConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;


        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            p=new ProgressDialog(login.this);
            p.setMessage("Performing your request");
            p.show();

        }

        @Override
        protected void onPostExecute(Void res) {
            super.onPostExecute(res);
            if(result.equals("Welcome admin")){
                Intent i=new Intent(login.this,admin.class);
                i.putExtra("usertype","admin");
                Toast.makeText(login.this,"Welcome admin",Toast.LENGTH_LONG).show();
                startActivity(i);
                finish();

            }
            else if(result.equals("Welcome member")){
                emailadd=txtemail.getText().toString();
                Intent i=new Intent(login.this,members.class);
                i.putExtra("usertype",emailadd);
                Toast.makeText(login.this,"Welcome " + emailadd,Toast.LENGTH_LONG).show();
                startActivity(i);
                finish();
            }





            p.hide();
            p.dismiss();
        }
    }

}
