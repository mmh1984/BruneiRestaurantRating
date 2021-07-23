package com.example.maynard.bruneirestaurantrating;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class register extends AppCompatActivity implements View.OnClickListener {
Spinner gender;
    ImageView profile;
    final static int GALLERY_ACCESS=1;
    EditText txtfname,txtpassword,txtfacebookid,txtemail;
    Button btndone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        gender= (Spinner) findViewById(R.id.spinGender);
        load_gender();

        txtfname= (EditText) findViewById(R.id.etFullname);
        txtemail= (EditText) findViewById(R.id.etEmail);
        txtpassword= (EditText) findViewById(R.id.etPassword);
        txtfacebookid= (EditText) findViewById(R.id.etFacebook);
        btndone= (Button) findViewById(R.id.btndone);

        profile= (ImageView) findViewById(R.id.ivprofile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,GALLERY_ACCESS);
            }
        });

        btndone.setOnClickListener(this);
    }

    public Boolean input_ok() {
        Boolean result=true;

        if(txtemail.getText().toString().isEmpty()|| isValidEmail(txtemail.getText().toString())==false){
            txtemail.setError("email must be valid");
            result=false;
        }
        else if(txtpassword.getText().toString().isEmpty()){
            txtpassword.setError("enter your password");
            result=false;
        }

        else if(txtfname.getText().toString().isEmpty()){
            txtfname.setError("enter your name");
            result=false;
        }
        else if(txtfacebookid.getText().toString().isEmpty()){
            txtfacebookid.setError("enter fb id");
            result=false;
        }


        return  result;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_ACCESS && resultCode==RESULT_OK){
            Uri image=data.getData();
            profile.setImageURI(image);
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void load_gender() {
        List<String> gvalues=new ArrayList<String>();

        gvalues.add("Male");
        gvalues.add("Female");

        ArrayAdapter<String> dataadapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,gvalues);
        dataadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(dataadapter);

    }

    @Override
    public void onClick(View view) {
        if(input_ok()){

            String fullname,email,genderselected,facebook,photo,password;

            fullname=txtfname.getText().toString();
            email=txtemail.getText().toString();
            genderselected=gender.getSelectedItem().toString();
            password=txtpassword.getText().toString();
            facebook=txtfacebookid.getText().toString();

            Bitmap decodeimg=((BitmapDrawable)profile.getDrawable()).getBitmap();
            ByteArrayOutputStream btimage=new ByteArrayOutputStream();
            decodeimg.compress(Bitmap.CompressFormat.JPEG,100,btimage);
            photo= Base64.encodeToString(btimage.toByteArray(),Base64.DEFAULT);


            BackgroundWorker2 work=new BackgroundWorker2();
            work.execute(email,password,fullname,genderselected,facebook,photo);


        }
    }


    public class BackgroundWorker2 extends AsyncTask<String,Void,Void> {

        ProgressDialog p;
        AlertDialog a;

        String result="";


        @Override
        protected Void doInBackground(String... params) {
            String registerurl="http://www.kemudaremittance.com/restobn/register.php";
            //String registerurl="http://10.0.2.2/restobn/register.php";
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
                postdata +="&"+ URLEncoder.encode("fullname","UTF-8")+"=" + URLEncoder.encode(params[2],"UTF-8");
                postdata +="&"+ URLEncoder.encode("gender","UTF-8")+"=" + URLEncoder.encode(params[3],"UTF-8");
                postdata +="&"+ URLEncoder.encode("facebookid","UTF-8")+"=" + URLEncoder.encode(params[4],"UTF-8");
                postdata +="&"+ URLEncoder.encode("profile","UTF-8")+"=" + URLEncoder.encode(params[5],"UTF-8");


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

            p=new ProgressDialog(register.this);
            p.setMessage("Performing your request");
            p.show();

        }

        @Override
        protected void onPostExecute(Void res) {
            super.onPostExecute(res);
            if(result.equals("Registration Successfull")){
                Toast.makeText(register.this,result.toString(),Toast.LENGTH_SHORT).show();
                p.dismiss();
                startActivity(new Intent(register.this,login.class));
            }
            else if(result.equals("email already taken")){
                Toast.makeText(register.this,result.toString(),Toast.LENGTH_SHORT).show();
                p.dismiss();
                txtemail.setError("This email is already taken");

            }





            p.hide();
            p.dismiss();
        }
    }
}
