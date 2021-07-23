package com.example.maynard.bruneirestaurantrating;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.List;

public class addrestaurant extends AppCompatActivity implements View.OnClickListener {
TextView link;
    Spinner spType,spDistrict,spYear;
    final static int GALLERY_ACCESS=0;
    ImageView profile;
    Button btndone;
    String userlevel;
    EditText etName,etOwner,etAddress,etCoordinates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrestaurant);
        link= (TextView) findViewById(R.id.tvhow);
        link.setMovementMethod(LinkMovementMethod.getInstance());


        load_spinners();
        etName= (EditText) findViewById(R.id.etName);
        etOwner= (EditText) findViewById(R.id.etowner);
        etAddress= (EditText) findViewById(R.id.etAddress);
        etCoordinates= (EditText) findViewById(R.id.etlanglat);
        profile= (ImageView) findViewById(R.id.ivprofile);
        btndone= (Button) findViewById(R.id.btndone);
        userlevel=getIntent().getExtras().getString("usertype");

        profile.setOnClickListener(this);
        btndone.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void load_spinners() {
        spType= (Spinner) findViewById(R.id.spinType);
        spDistrict= (Spinner) findViewById(R.id.spindistrict);
        spYear= (Spinner) findViewById(R.id.spinyear);

        List<String> typevalues=new ArrayList<String>();
        typevalues.add("Malay");
        typevalues.add("Chinese");
        typevalues.add("Japanese");
        typevalues.add("Indian");
        typevalues.add("Thai");
        typevalues.add("Western");
        typevalues.add("European");
        typevalues.add("Fast Food");

        ArrayAdapter<String> aType=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,typevalues);
        aType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(aType);

        List<String> dValues=new ArrayList<String>();
        dValues.add("Brunei Muara");
        dValues.add("Tutong");
        dValues.add("Temburong");
        dValues.add("Belait");

        ArrayAdapter<String> aDistrict=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,dValues);
        aDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDistrict.setAdapter(aDistrict);


        List<Integer> yValues=new ArrayList<Integer>();
        int year_now= Calendar.getInstance().get(Calendar.YEAR);
        for(int y=1920;y<=year_now;y++){
            yValues.add(y);
        }
        ArrayAdapter<Integer> aYear=new ArrayAdapter<Integer>(this,R.layout.support_simple_spinner_dropdown_item,yValues);
        aYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(aYear);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ivprofile){
            Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i,GALLERY_ACCESS);
        }
        else if(v.getId()==R.id.btndone){
            if(check_inputs()){
                String strname,strowner,straddress,strtype,strdistrict,stryear,strcoordinates,strphoto;
                strname=etName.getText().toString();
                strowner=etOwner.getText().toString();
                straddress=etAddress.getText().toString();
                strtype=spType.getSelectedItem().toString();
                strdistrict=spDistrict.getSelectedItem().toString();
                stryear=spYear.getSelectedItem().toString();
                strcoordinates=etCoordinates.getText().toString();

                Bitmap decodeimg=((BitmapDrawable)profile.getDrawable()).getBitmap();
                ByteArrayOutputStream btimage=new ByteArrayOutputStream();
                decodeimg.compress(Bitmap.CompressFormat.JPEG,100,btimage);
                strphoto= Base64.encodeToString(btimage.toByteArray(),Base64.DEFAULT);

                BackgroundWorker2 bw=new BackgroundWorker2();
                bw.execute(strname,strowner,straddress,strtype,strdistrict,stryear,strcoordinates,strphoto);

            }
        }
    }

    public Boolean check_inputs(){
        Boolean result=true;
        if(etName.getText().toString().isEmpty()){
            result=false;
            etName.setError("please enter the name");

        }
        else if(etName.getText().toString().contains("'")){
            result=false;
            etName.setError("Single quote cannot be added in the name");
        }

        else if(etOwner.getText().toString().isEmpty()) {
            result = false;
            etOwner.setError("please enter the owner name");
        }

        else if(etCoordinates.getText().toString().isEmpty()) {
            result = false;
            etCoordinates.setError("coordinates must be filled in");
        }

        else if(etAddress.getText().toString().isEmpty()) {
            result = false;
            etAddress.setError("please enter the address");
        }
        return result;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_ACCESS && resultCode==RESULT_OK){
            Uri selected=data.getData();
            profile.setImageURI(selected);
        }
    }


    public class BackgroundWorker2 extends AsyncTask<String,Void,Void> {

        ProgressDialog p;
        AlertDialog a;

        String result="";


        @Override
        protected Void doInBackground(String... details) {
            String registerurl="http://www.kemudaremittance.com/restobn/addrestaurant.php";
            //String registerurl="http://10.0.2.2/restobn/addrestaurant.php";
            try {
                URL url=new URL(registerurl);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);


                //output stream
                OutputStream out=urlConnection.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String photoname=details[0].replace(" ","_");
                //create data url
                //encode name and description
                String encodename,encodeaddress;

                String postdata= URLEncoder.encode("name","UTF-8")+"=" + URLEncoder.encode(details[0],"UTF-8");
                postdata +="&"+ URLEncoder.encode("owner","UTF-8")+"=" + URLEncoder.encode(details[1],"UTF-8");
                postdata +="&"+ URLEncoder.encode("address","UTF-8")+"=" + URLEncoder.encode(details[2],"UTF-8");
                postdata +="&"+ URLEncoder.encode("type","UTF-8")+"=" + URLEncoder.encode(details[3],"UTF-8");
                postdata +="&"+ URLEncoder.encode("district","UTF-8")+"=" + URLEncoder.encode(details[4],"UTF-8");
                postdata +="&"+ URLEncoder.encode("year","UTF-8")+"=" + URLEncoder.encode(details[5],"UTF-8");
                postdata +="&"+ URLEncoder.encode("coordinates","UTF-8")+"=" + URLEncoder.encode(details[6],"UTF-8");
                postdata +="&"+ URLEncoder.encode("photo","UTF-8")+"=" + URLEncoder.encode(details[7],"UTF-8");
                postdata +="&"+ URLEncoder.encode("photoname","UTF-8")+"=" + URLEncoder.encode(photoname,"UTF-8");

                br.write(postdata);
                br.flush();
                br.close();
                out.close();

                InputStream in=urlConnection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(in,"ISO-8859-1"));

                String line;

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

            p=new ProgressDialog(addrestaurant.this);
            p.setMessage("Performing your request");
            p.show();

        }

        @Override
        protected void onPostExecute(Void res) {
            super.onPostExecute(res);
            if(result.equals("Success")){
                Toast.makeText(addrestaurant.this,result.toString(),Toast.LENGTH_SHORT).show();
                p.dismiss();
                finish();
                Intent in=new Intent(addrestaurant.this,admin.class);
                in.putExtra("usertype",userlevel);
                startActivity(in);

            }
            else {
                Toast.makeText(addrestaurant.this,result.toString(),Toast.LENGTH_SHORT).show();
                p.dismiss();
            }




            p.hide();
            p.dismiss();
        }
    }

}
