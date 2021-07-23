package com.example.maynard.bruneirestaurantrating;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

public class adminrestaurant extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    public String myJson;
    Button btnadd;
    String[] restname;
    String[] resttype;
    String[] restaddress;
    String[] restdistrict;
    String[] restcoor;
    String[] restyear;

    ListView restolist;
    String userlevel;
    List<Resto> r;

    //String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminrestaurant);



        btnadd = (Button) findViewById(R.id.btnadd);

        restolist= (ListView) findViewById(R.id.lvresto);

        //get the user type
        Bundle bn=getIntent().getExtras();

        if(bn!=null){
            userlevel=bn.getString("usertype");

            if(!userlevel.equals("admin")){
                btnadd.setVisibility(View.INVISIBLE);
            }
        }
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        LoadWorker lw=new LoadWorker();
        lw.execute();



        btnadd.setOnClickListener(this);
    }

public void load_restaurant(String district){

            r=new ArrayList<Resto>();

            if(district.equals("all")) {
                for (int x = 0; x < restname.length; x++) {
                    Resto rst = new Resto(restname[x], restaddress[x], restdistrict[x], resttype[x], restcoor[x], restyear[x]);
                    r.add(rst);
                }
            }
            else {
                for (int x = 0; x < restname.length; x++) {
                    if(restdistrict[x].toString().equals(district)) {
                        Resto rst = new Resto(restname[x], restaddress[x], restdistrict[x], resttype[x], restcoor[x], restyear[x]);
                        r.add(rst);
                    }
                }
            }
            RestoAdapter ra = new RestoAdapter(getApplicationContext(), r);
            restolist.setAdapter(ra);
            ra.notifyDataSetChanged();
            restolist.setOnItemClickListener(this);



        }

    class LoadWorker extends AsyncTask<Void,Void,Void>{
        InputStream is=null;
        String result="";
        String line="";
        Boolean isempty;
        ProgressDialog p;
        @Override
        protected Void doInBackground(Void... voids) {

            try {

                String registerurl = "http://www.kemudaremittance.com/restobn/get_restaurants.php";
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
            isempty=true;
            p=new ProgressDialog(adminrestaurant.this);
            p.setMessage("Fetching data...");
            p.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {

                if (!result.trim().equals("No Results Found!")) {

                    JSONArray ja = new JSONArray(result);
                    JSONObject jo = null;
                    isempty=false;
                    restname = new String[ja.length()];
                    restaddress = new String[ja.length()];
                    restcoor = new String[ja.length()];
                    restdistrict = new String[ja.length()];
                    resttype = new String[ja.length()];
                    restyear = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        restname[i] = (jo.getString("restname")).replace("'","");
                        restaddress[i] = jo.getString("restaddress").replace("'","");
                        restcoor[i] = jo.getString("coordinates");
                        restdistrict[i] = jo.getString("district");
                        resttype[i] = jo.getString("resttype");
                        restyear[i] = jo.getString("yearfound");

                    }

                    p.dismiss();
                    if(!isempty) {

                        load_restaurant("all");
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No content",Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    p.dismiss();
                }

            } catch(JSONException e){
                e.printStackTrace();
            }

        }
    }



    @Override
    public void onClick(View v) {
if(v.getId()==R.id.btnadd){
    Intent in=new Intent(adminrestaurant.this,addrestaurant.class);
    in.putExtra("usertype",userlevel);
    startActivity(in);

    finish();
}


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {


               switch(item.getItemId()){
                   case R.id.d1:

                       load_restaurant("Brunei-Muara");
                       break;
                   case R.id.d2:
                       load_restaurant("Tutong");
                       break;
                   case R.id.d3:
                       load_restaurant("Temburong");
                       break;
                   case R.id.d4:
                       load_restaurant("Belait");
                       break;



               }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if(userlevel.equals("admin")) {

            Intent in=new Intent(adminrestaurant.this,restoprofile.class);
            in.putExtra("name",restname[i]);
            in.putExtra("district",restdistrict[i]);
            in.putExtra("type",resttype[i]);
            in.putExtra("address",restaddress[i]);
            in.putExtra("coordinates",restcoor[i]);
            in.putExtra("usertype",userlevel);

            startActivity(in);
        }
        else {
            Intent in=new Intent(getApplicationContext(),restoprofile.class);
            in.putExtra("name",restname[i]);
            in.putExtra("district",restdistrict[i]);
            in.putExtra("type",resttype[i]);
            in.putExtra("address",restaddress[i]);
            in.putExtra("coordinates",restcoor[i]);
            in.putExtra("usertype",userlevel);

            startActivity(in);
        }

    }


    public void confirm_delete(final int i) {
        AlertDialog.Builder draw=new AlertDialog.Builder(this,android.R.style.Theme_Material_Light_Dialog);
        draw.setMessage("Choose Operation");
        draw.setTitle("Restaurant");
        draw.setCancelable(false);

        draw.setPositiveButton(
                "Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteWorker dw=new DeleteWorker();

                        dw.execute(restname[i]);
                        r.remove(i);
                        dialog.cancel();


                    }
                }

        );

        draw.setNegativeButton(
                "View",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent in=new Intent(adminrestaurant.this,restoprofile.class);
                        in.putExtra("name",restname[i]);
                        in.putExtra("district",restdistrict[i]);
                        in.putExtra("type",resttype[i]);
                        in.putExtra("address",restaddress[i]);
                        in.putExtra("coordinates",restcoor[i]);
                        in.putExtra("usertype",userlevel);

                        startActivity(in);

                    }
                }

        );



        AlertDialog showmessage=draw.create();
        showmessage.show();

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
            //load_restaurant("all");
         recreate();


        }
    }
}



