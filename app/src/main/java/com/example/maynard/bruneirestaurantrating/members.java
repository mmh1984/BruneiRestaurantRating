package com.example.maynard.bruneirestaurantrating;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

public class members extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    public String myJson;
    Button btnadd;
    String[] restname;
    String[] resttype;
    String[] restaddress;
    String[] restdistrict;
    String[] restcoor;
    String[] restyear;
    InputStream is = null;
    String result = "";
    String line = "";
    ListView restolist;
    String userlevel;
    List<Resto> r;
    Boolean isempty;

    EditText etSearch;

    String username[],useremail[],userfb[];
    //String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);


        btnadd = (Button) findViewById(R.id.btnadd);
        isempty = true;
        restolist = (ListView) findViewById(R.id.lvresto);
        etSearch= (EditText) findViewById(R.id.eTSearch);
        //get the user type
        Bundle bn = getIntent().getExtras();

        if (bn != null) {
            userlevel = bn.getString("usertype");


        }
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        getData();

        if (!isempty) {

            load_restaurant("all");
        } else {
            Toast.makeText(getApplicationContext(), "No content", Toast.LENGTH_SHORT).show();
        }

        btnadd.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query=etSearch.getText().toString();
                load_restaurant("resto" + query);
            }
        });


    }

    public void load_restaurant(String district) {


        r = new ArrayList<Resto>();
        if (district.equals("all")) {
            for (int x = 0; x < restname.length; x++) {
                Resto rst = new Resto(restname[x], restaddress[x], restdistrict[x], resttype[x], restcoor[x], restyear[x]);
                r.add(rst);
            }
        }
        else if(district.contains("resto")) {
            String query=district.replace("resto","");

            for (int x = 0; x < restname.length; x++) {
                if (restname[x].toString().contains(query)) {
                    Resto rst = new Resto(restname[x], restaddress[x], restdistrict[x], resttype[x], restcoor[x], restyear[x]);
                    r.add(rst);
                }
            }

        }

        else {
            for (int x = 0; x < restname.length; x++) {
                if (restdistrict[x].toString().equals(district)) {
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


    public void getProfile() {

        InputStream ins=null;
        String line2;
        String result2 = "";
        try {

            String registerurl = "http://www.kemudaremittance.com/restobn/get_members.php";
            URL url = new URL(registerurl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            ins = new BufferedInputStream(urlConnection.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            StringBuilder sb = new StringBuilder();

            while ((line2 = br.readLine()) != null) {
                sb.append(line2 + "\n");
            }
            ins.close();
            result2 = sb.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }


        try {


            JSONArray ja = new JSONArray(result2);
            JSONObject jo = null;

          username=new String[ja.length()];
            useremail=new String[ja.length()];
            userfb=new String[ja.length()];
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                username[i]=jo.getString("memail");
                useremail[i]=jo.getString("mfullname");
                userfb[i] =jo.getString("facebookid");

            }



    } catch(
    JSONException e)

    {
        e.printStackTrace();
    }

}





    public void getData() {



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


        try {

            if (!result.equals("No Results Found!")) {
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



            }
            else {

                Toast.makeText(getApplicationContext(),"No content",Toast.LENGTH_SHORT).show();

            }
        } catch(JSONException e){
            e.printStackTrace();
        }



    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnadd){
        getProfile();
            Intent in=new Intent(getApplicationContext(),memberprofile.class);
            for(int i=0;i<username.length;i++){
                if(username[i].equals(userlevel)){
                    in.putExtra("email",username[i]);
                    in.putExtra("name",useremail[i]);
                    in.putExtra("fb",userfb[i]);
                    in.putExtra("usertype",userlevel);
                    startActivity(in);
                }
            }



        }


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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



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



