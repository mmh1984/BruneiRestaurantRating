package com.example.maynard.bruneirestaurantrating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class memberslist extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public String myJSON;
    String[] name;
    String []email;
    String []fb;

    ArrayAdapter<String> adapter;


    ListView list;
    List<memberdetails> mdetails;

    String userlevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberslist);


        list = (ListView) findViewById(R.id.mylist);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        if(getIntent().getExtras()!=null){
            userlevel=getIntent().getExtras().getString("usertype");
        }



        LoadMemberWorker lw=new LoadMemberWorker();
        lw.execute();





    }


    class LoadMemberWorker extends AsyncTask<Void,Void,Void>{
ProgressDialog p;
        String result = null;
        String line = "";
        Boolean isempty;
        InputStream is = null;
        @Override
        protected Void doInBackground(Void... voids) {

            try {

                String registerurl = "http://www.kemudaremittance.com/restobn/get_members.php";
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
            p=new ProgressDialog(memberslist.this);
            p.setMessage("Loading...");
            p.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (!result.equals("No Results Found!")) {
                    JSONArray ja = new JSONArray(result);
                    JSONObject jo = null;
                    name = new String[ja.length()];
                    email = new String[ja.length()];
                    fb = new String[ja.length()];
                    isempty=false;
                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        email[i] = jo.getString("memail");
                        name[i] = jo.getString("mfullname");
                        fb[i] = jo.getString("facebookid");

                    }

                    if(!isempty) {
                        mdetails = new ArrayList<memberdetails>();


                        for (int i = 0; i < name.length; i++) {
                            memberdetails m = new memberdetails(name[i], "email: " + email[i], "facebookid: " + fb[i], email[i]);
                            mdetails.add(m);
                        }

                        MemberCustomAdapter adapter = new MemberCustomAdapter(getApplicationContext(), mdetails);
                        list.setAdapter(adapter);
                        list.setOnItemClickListener(memberslist.this);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No members",Toast.LENGTH_SHORT).show();
                    }
                    p.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void getData() {






    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String mname,memail,mfacebook;
        mname=mdetails.get(i).getMembername();
        memail=mdetails.get(i).getMemberemail();
        mfacebook=mdetails.get(i).getMemberfacebook();

        Intent in=new Intent(getApplicationContext(),memberprofile.class);
        in.putExtra("name",mname);
        in.putExtra("email",memail);
        in.putExtra("fb",mfacebook);
        in.putExtra("usertype",userlevel);
        startActivity(in);
    }
}

