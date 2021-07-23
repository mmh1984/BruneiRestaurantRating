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
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Maynard on 4/8/2017.
 */

public class ReviewAdapter extends BaseAdapter{

    Context context;
    List<Reviews> rowItems;

    ReviewAdapter(Context context, List<Reviews> rowItems) {
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
        TextView email;
        RatingBar rating;
        TextView comments;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder = null;
        final ViewHolder holderimage=new ViewHolder();
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.reviewlist, null);
            holder = new ViewHolder();

            holder.email = (TextView) convertView
                    .findViewById(R.id.tvemail);
            holderimage.profile_pic = (ImageView) convertView
                    .findViewById(R.id.imageView2);
            holder.comments = (TextView) convertView.findViewById(R.id.tvcomments);
            holder.rating = (RatingBar) convertView.findViewById(R.id.ratingBar2);
            convertView.setTag(holder);





            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Reviews row_pos = rowItems.get(position);

        holder.email.setText(row_pos.getEmail());
        holder.comments.setText(row_pos.getComments());
        int r=Integer.parseInt(row_pos.getRating());
        holder.rating.setRating(r);

        String rname=row_pos.getEmail();

        String url="";
        if(rname.contains("@")){
            url="http://www.kemudaremittance.com/restobn/members/" +  rname + ".JPG";
        }
        else{
            rname=rname.replace(" ","_");
            url="http://www.kemudaremittance.com/restobn/restaurant/" +  rname + ".JPG";

        }

        final String finalurl=url;

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Bitmap bmap=getBitmapfromURL(finalurl);
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
