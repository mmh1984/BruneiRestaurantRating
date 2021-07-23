package com.example.maynard.bruneirestaurantrating;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class map extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    String coordinates="";
    String rname,raddress;
    Double coor1,coor2=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
            Toast.makeText(this, "Perfect!!", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_map);

            Bundle bn=getIntent().getExtras();
            if(bn!=null)
            {
                rname=bn.getString("name");
                raddress=bn.getString("address");
                coordinates=bn.getString("coordinates");
                String [] arr=coordinates.split(",");
                coor1=Double.parseDouble(arr[0]);
                coor2=Double.parseDouble(arr[1]);

            }
            initMap();

        }
    }
    private void initMap() {
        MapFragment mfragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mfragment.getMapAsync(this);
    }
    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cannot connect to play services", Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    Marker marker;

    private void setMarker( double lat, double lng) {
        if(marker!=null){
            marker.remove();
        }

        MarkerOptions options = new MarkerOptions();
        options.title(rname);
        options.position(new LatLng(lat, lng));
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        options.snippet(raddress);
        marker = map.addMarker(options);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        gotolocationZoom(coor1,coor2,20);
    }

    private void gotolocationZoom(double lat, double lang, int zoom) {
        LatLng ll = new LatLng(lat, lang);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        map.moveCamera(update);
        setMarker(lat,lang);
        //add a marker

    }
}
