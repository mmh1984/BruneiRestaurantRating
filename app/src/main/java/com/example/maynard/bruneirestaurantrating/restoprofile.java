package com.example.maynard.bruneirestaurantrating;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.TotalCaptureResult;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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

public class restoprofile extends AppCompatActivity {

    ImageView profile;
    TextView tvname,tvaddress,tvdistrict,tvtype,tvratecount;
    String name,address,district,type,coordinates;
    Button showmap,btnrate,btndelete;
    RatingBar ratingBar;


    String rate[];
    String restoname[];
    String comments[];
    String email[];
    ListView listcomments;
    String userlevel;

    int total;
    int count;
    List<Reviews> rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restoprofile);

        Bundle bn=getIntent().getExtras();

        tvname= (TextView) findViewById(R.id.tvResto);
        tvtype=(TextView) findViewById(R.id.tvType);
        tvdistrict=(TextView) findViewById(R.id.tvDistrict);
        tvaddress=  (TextView) findViewById(R.id.tvAddress);
        profile= (ImageView) findViewById(R.id.ivPhoto);
        showmap= (Button) findViewById(R.id.btnMap);
        btnrate= (Button) findViewById(R.id.btnRateit);
        btndelete= (Button) findViewById(R.id.btnDelete);
        ratingBar= (RatingBar) findViewById(R.id.ratingBar);
        tvratecount= (TextView) findViewById(R.id.txtratecount);
        listcomments= (ListView) findViewById(R.id.lvComments);
        if(bn!=null){
            name=bn.getString("name");
            address=bn.getString("address");
            district=bn.getString("district");
            type=bn.getString("type");
            coordinates=bn.getString("coordinates");
            userlevel=bn.getString("usertype");

            if(userlevel.equals("admin")){
                btnrate.setVisibility(View.INVISIBLE);
            }
            else {
                btndelete.setVisibility(View.INVISIBLE);
            }



            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
            FetchRatingWorker fw=new FetchRatingWorker();
                    fw.execute();
            load_details();


            btnrate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog ratings=new Dialog(restoprofile.this);
                    ratings.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    ratings.setContentView(R.layout.ratingdialog);
                    ratings.setCancelable(false);
                    ratings.show();

                    final TextView comments= (TextView) ratings.findViewById(R.id.edCommentsDialog);
                    final RatingBar drating= (RatingBar) ratings.findViewById(R.id.ratingBarDialog);
                    Button send= (Button) ratings.findViewById(R.id.btndialogsubmit);

                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           RatingWorker rw=new RatingWorker();
                            rw.execute(name,userlevel,String.valueOf(drating.getRating()),comments.getText().toString());
                           // Toast.makeText(getApplicationContext(),String.valueOf(drating.getRating()),Toast.LENGTH_SHORT).show();
                            ratings.dismiss();
                       recreate();
                        }
                    });

                }
            });



        }



        showmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getApplicationContext(),map.class);
                in.putExtra("coordinates",coordinates);
                in.putExtra("name",name);
                in.putExtra("address",address);

                startActivity(in);
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteWorker dw=new DeleteWorker();

                dw.execute(name);
            }
        });

    }



    public void show_reviews() {
        rv=new ArrayList<Reviews>();
        for(int i=0;i<restoname.length;i++){
            if(restoname[i].equals(name)) {
                Reviews rs = new Reviews(email[i], rate[i], comments[i]);
                rv.add(rs);
            }
      }
       ReviewAdapter reviewAdapter=new ReviewAdapter(getApplicationContext(),rv);
        listcomments.setAdapter(reviewAdapter);
      reviewAdapter.notifyDataSetChanged();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void get_rating() {

        total=0;
        count=0;


        for(int i=0;i<rate.length;i++){
            if(restoname[i].equals(name)){
                total+=Integer.parseInt(rate[i]);
                count++;

            }

        }
        if(count>0) {
            int average = (total / count);
            ratingBar.setRating(average);
        }
        else {
            ratingBar.setRating(0);
        }
        tvratecount.setText("Based on "+ count + " reviews");
        //Toast.makeText(getApplicationContext(),String.valueOf(count) + ":" + String.valueOf(total),Toast.LENGTH_SHORT).show();
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

    public void load_details(){
        final String imgsrc=name.replace(" ","_");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Bitmap bmap=getBitmapfromURL("http://www.kemudaremittance.com/restobn/restaurant/" + imgsrc + ".JPG");
                    profile.setImageBitmap(bmap);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        tvname.setText(name);
        tvaddress.setText(address);
        tvtype.setText("Type: " + type);
        tvdistrict.setText("District: " + district);

    }

    class FetchRatingWorker extends AsyncTask<Void,Void,Void>{
        InputStream is=null;
        String result="";
        String line="";
        ProgressDialog p;
        Boolean isempty;
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

            p=new ProgressDialog(restoprofile.this);
            p.setMessage("Loading....");

            p.show();

            isempty=true;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String res=result.trim();

            if(!(res.equals("No Results Found!"))){
                try {


                        isempty=false;
                        JSONArray ja = new JSONArray(result);
                        JSONObject jo = null;
                        rate = new String[ja.length()];
                        restoname = new String[ja.length()];
                        comments = new String[ja.length()];
                        email = new String[ja.length()];
                        for (int i = 0; i < ja.length(); i++) {
                            jo = ja.getJSONObject(i);
                            rate[i] = jo.getString("ratings");
                            restoname[i] = jo.getString("restaurantname");
                            comments[i] = jo.getString("comments");
                            email[i] = jo.getString("memberemail");

                        }
                        p.dismiss();

                        get_rating();
                        show_reviews();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(restoprofile.this,res,Toast.LENGTH_LONG).show();
                p.dismiss();
            }

            /*

            */
        }
    }





    class RatingWorker extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String...details) {

        String registerurl= "http://www.kemudaremittance.com/restobn/addratings.php";

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
                String postdata= URLEncoder.encode("name","UTF-8")+"=" + URLEncoder.encode(details[0],"UTF-8");
                postdata +="&"+ URLEncoder.encode("email","UTF-8")+"=" + URLEncoder.encode(details[1],"UTF-8");
                postdata +="&"+ URLEncoder.encode("rate","UTF-8")+"=" + URLEncoder.encode(details[2],"UTF-8");
                postdata +="&"+ URLEncoder.encode("comments","UTF-8")+"=" + URLEncoder.encode(details[3],"UTF-8");

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
            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_LONG).show();

        }
    }

    class DeleteWorker extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String...name) {
            String registerurl="http://www.kemudaremittance.com/restobn/delete_restaurants.php";
            //String registerurl="http://10.0.2.2/restobn/delete_restaurants.php";
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

                String postdata= URLEncoder.encode("name","UTF-8")+"=" + URLEncoder.encode(name[0],"UTF-8");


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

            Toast.makeText(getApplicationContext(),r,Toast.LENGTH_LONG).show();
            Intent in=new Intent(restoprofile.this,admin.class);
            in.putExtra("usertype",userlevel);
            startActivity(in);
            finish();


        }
    }



}
