package com.example.maynard.bruneirestaurantrating;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

/**
 * Created by Maynard on 4/5/2017.
 */

public class BackgroundWorker extends AsyncTask<String,Void,String> {

    ProgressDialog p;
    AlertDialog a;
    Context x;
    String type;
    String emailadd;
    BackgroundWorker(Context ct){
       this.x=ct;
    }
    @Override
    protected String doInBackground(String... params) {

        String operation=params[0];
        String returnmsg="";
        type=operation;

        if(operation.equals("register")){
            String[]member_details=new String[6];
            member_details[0]=params[1];
            member_details[1]=params[2];
            member_details[2]=params[3];
            member_details[3]=params[4];
            member_details[4]=params[5];
            member_details[5]=params[6];
            returnmsg=register_member(member_details);

        }

        if(operation.equals("login")){
            String[]member_details=new String[6];
            member_details[0]=params[1];
            member_details[1]=params[2];
            emailadd=params[1];
            returnmsg=member_login(member_details);

        }
        if(operation.equals("addrestaurant")){
            String [] restaurantdetails=new String[8];
            restaurantdetails[0]=params[1];
            restaurantdetails[1]=params[2];
            restaurantdetails[2]=params[3];
            restaurantdetails[3]=params[4];
            restaurantdetails[4]=params[5];
            restaurantdetails[5]=params[6];
            restaurantdetails[6]=params[7];
            restaurantdetails[7]=params[8];
            returnmsg=add_restaurant(restaurantdetails);
        }





        return returnmsg;
    }



    public String add_restaurant(String [] details){


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
            String result="";
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


    public String register_member(String [] details){



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
            String postdata= URLEncoder.encode("email","UTF-8")+"=" + URLEncoder.encode(details[0],"UTF-8");
            postdata +="&"+ URLEncoder.encode("password","UTF-8")+"=" + URLEncoder.encode(details[1],"UTF-8");
            postdata +="&"+ URLEncoder.encode("fullname","UTF-8")+"=" + URLEncoder.encode(details[2],"UTF-8");
            postdata +="&"+ URLEncoder.encode("gender","UTF-8")+"=" + URLEncoder.encode(details[3],"UTF-8");
            postdata +="&"+ URLEncoder.encode("facebookid","UTF-8")+"=" + URLEncoder.encode(details[4],"UTF-8");
            postdata +="&"+ URLEncoder.encode("profile","UTF-8")+"=" + URLEncoder.encode(details[5],"UTF-8");


            br.write(postdata);
            br.flush();
            br.close();
            out.close();

            InputStream in=urlConnection.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(in,"ISO-8859-1"));
            String result="";
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




    public String member_login(String [] details){
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
            String postdata= URLEncoder.encode("email","UTF-8")+"=" + URLEncoder.encode(details[0],"UTF-8");
            postdata +="&"+ URLEncoder.encode("password","UTF-8")+"=" + URLEncoder.encode(details[1],"UTF-8");



            br.write(postdata);
            br.flush();
            br.close();
            out.close();

            InputStream in=urlConnection.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(in,"ISO-8859-1"));
            String result="";
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
    protected void onPreExecute() {
        super.onPreExecute();
        a=new AlertDialog.Builder(x).create();

        a.setTitle("Performing operation");

        p=new ProgressDialog(x);
        p.setMessage("Performing your request");
        p.show();
        type="";
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);

        a.setMessage(res);

        switch(type){
            case "register":
                if(res.equals("Registration Successfull")){
                   x.startActivity(new Intent(x,login.class));
                }
                else if(res.equals("email already taken")){
                    Toast.makeText(x,res.toString(),Toast.LENGTH_SHORT).show();


                }
                break;
            case "login":
                if(res.equals("Welcome admin")){
                    Intent i=new Intent(x,admin.class);
                    i.putExtra("usertype","admin");
                    x.startActivity(i);

                }
                else if(res.equals("Welcome member")){
                    Intent i=new Intent(x,members.class);
                    i.putExtra("usertype",emailadd);

                    x.startActivity(i);
                }

                break;
            case "addrestaurant":
                if(res.equals("Success")){

                    x.startActivity(new Intent(x,adminrestaurant.class));

                }
                break;




        }
        a.show();
        p.hide();
        p.dismiss();
    }
}
