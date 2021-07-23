package com.example.maynard.bruneirestaurantrating;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class memberprofile extends AppCompatActivity implements View.OnClickListener {
String name,email,facebook;
    ImageView profile;
    TextView tvName,tvEmail,tvFb;
    List<Reviews> rv;


    String rate[];
    String restoname[];
    String comments[];
    String email_add[];
    ListView listcomments;
    Button btndelete;
    String userlevel;
    Button btnclearreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberprofile);

        profile= (ImageView) findViewById(R.id.ivprofile);
        tvEmail= (TextView) findViewById(R.id.tvEmail);
        tvName= (TextView) findViewById(R.id.tvName);
        tvFb= (TextView) findViewById(R.id.tvfb);
        listcomments= (ListView) findViewById(R.id.lvreviews);
        btndelete= (Button) findViewById(R.id.btndeleteaccount);
        btnclearreview= (Button) findViewById(R.id.btnClearReviews);
       Bundle values=getIntent().getExtras();

        if(values!=null){
            name=values.getString("name");
            email=values.getString("email");
            facebook=values.getString("fb");
            userlevel=values.getString("usertype");

      if(userlevel.equals("admin")){
          btnclearreview.setVisibility(View.INVISIBLE);
      }



        }


       LoadProfileWorker lw=new LoadProfileWorker();
        lw.execute();

        btndelete.setOnClickListener(this);
        btnclearreview.setOnClickListener(this);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void load_profile() {
        String imgsrc=email.replace("email: ","");
        String fbsrc=facebook.replace("facebookid: ","http://");

        tvName.setText(name);
        tvEmail.setText(email);
        tvFb.setText(Html.fromHtml("<a href='"+ fbsrc +"'>"+ facebook +"</a>"));
        tvFb.setMovementMethod(LinkMovementMethod.getInstance());
        Bitmap bmap=getBitmapfromURL("http://www.kemudaremittance.com/restobn/members/"+ imgsrc +".JPG");

        profile.setImageBitmap(bmap);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }
    class LoadProfileWorker extends AsyncTask<Void,Void,Void>{
ProgressDialog p;
        InputStream is=null;
        String result="";
        String line="";
        @Override
        protected Void doInBackground(Void... voids) {



            try {

                String registerurl = "http://www.kemudaremittance.com/restobn/get_ratings.php";
                URL url = new URL(registerurl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                is = new BufferedInputStream(urlConnection.getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();


                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }




            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p=new ProgressDialog(memberprofile.this);
            p.setMessage("Loading...");
            p.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            load_profile();
            try {
                if(!(result.trim().equals("No Results Found!"))) {
                    JSONArray ja = new JSONArray(result);
                    JSONObject jo = null;
                    rate = new String[ja.length()];
                    restoname = new String[ja.length()];
                    comments = new String[ja.length()];
                    email_add = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        rate[i] = jo.getString("ratings");
                        restoname[i] = jo.getString("restaurantname");
                        comments[i] = jo.getString("comments");
                        email_add[i] = jo.getString("memberemail");

                    }
                    p.dismiss();

                    show_reviews();
                }
                else {
                    p.dismiss();
                    Toast.makeText(memberprofile.this,"No Reviews Yet",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void getData() {


    }
    public void show_reviews() {
        rv=new ArrayList<Reviews>();
        String trimmedemail=email.replace("email: ","");
        for(int i=0;i<restoname.length;i++){
            if(email_add[i].equals(trimmedemail)) {
                Reviews rs = new Reviews(restoname[i], rate[i], comments[i]);
                rv.add(rs);
            }
        }
        ReviewAdapter reviewAdapter=new ReviewAdapter(getApplicationContext(),rv);
        listcomments.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();
        listcomments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


    }


    public Bitmap getBitmapfromURL(String src){

        try {
            URL url=new URL(src);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input=connection.getInputStream();

            Bitmap mybit= BitmapFactory.decodeStream(input);

            return mybit;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btndeleteaccount){
          confirm_delete();


        }
        if(v.getId()==R.id.btnClearReviews){
            DeleteWorker dw=new DeleteWorker();
           dw.execute("deletereviews",userlevel);
        }
    }





    public void confirm_delete() {
        AlertDialog.Builder draw=new AlertDialog.Builder(this,android.R.style.Theme_Material_Light_Dialog);
        draw.setMessage("Delete your account?");
        draw.setTitle("Members");
        draw.setCancelable(false);

        draw.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteWorker dw=new DeleteWorker();

                        dw.execute("deleteaccount",userlevel);
                        dialog.cancel();


                    }
                }

        );

        draw.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
dialog.cancel();
                    }
                }

        );



        AlertDialog showmessage=draw.create();
        showmessage.show();

    }

    class DeleteWorker extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String...email) {
            String registerurl="";
            if(email[0].equals("deleteaccount")) {

                registerurl= "http://www.kemudaremittance.com/restobn/delete_members.php";
            }
            else if(email[0].equals("deletereviews")) {
                registerurl = "http://www.kemudaremittance.com/restobn/delete_reviews.php";
            }
            String result="";
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


                String postdata= URLEncoder.encode("emailadd","UTF-8")+"=" + URLEncoder.encode(email[1],"UTF-8");


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
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }




            return null;
        }

        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            Toast.makeText(memberprofile.this, r, Toast.LENGTH_LONG).show();
            if(r.equals("Reviews Deleted")){
                rv.clear();
                recreate();
            }
            else {


                finish();
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        }
    }
}
