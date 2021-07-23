package com.example.maynard.bruneirestaurantrating;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Maynard on 4/6/2017.
 */

public class MemberCustomAdapter extends BaseAdapter {

    Context context;
    List<memberdetails> rowItems;

    MemberCustomAdapter(Context context, List<memberdetails> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }
    private class ViewHolder {
        ImageView profile_pic;
        TextView member_name;
        TextView status;
        TextView contactType;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final ViewHolder holderimage=new ViewHolder();
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listmember, null);
            holder = new ViewHolder();

            holder.member_name = (TextView) convertView
                    .findViewById(R.id.member_name);
            holderimage.profile_pic = (ImageView) convertView
                    .findViewById(R.id.profile_pic);
            holder.status = (TextView) convertView.findViewById(R.id.email);
            holder.contactType = (TextView) convertView
                    .findViewById(R.id.fbid);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        memberdetails row_pos = rowItems.get(position);

        holder.member_name.setText(row_pos.getMembername());
        holder.status.setText(row_pos.getMemberemail());
        holder.contactType.setText(row_pos.getMemberfacebook());
        final String membername=row_pos.getMemberphoto();
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    Bitmap bmap=getBitmapfromURL("http://www.kemudaremittance.com/restobn/members/" + membername + ".JPG");
                    holderimage.profile_pic.setImageBitmap(bmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();





        return convertView;
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
    }

    /* private view holder class */







