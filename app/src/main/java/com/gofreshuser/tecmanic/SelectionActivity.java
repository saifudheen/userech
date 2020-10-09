package com.gofreshuser.tecmanic;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SelectionActivity extends AppCompatActivity {

    LocationManager locationManager;

    SharedPreferences.Editor loceditor;

    ProgressDialog progressDialog;

    SharedPreferences locationpre;

    Button currentlocationbtn;

    String latitude, longitude;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selection);

        currentlocationbtn = findViewById(R.id.currentlocationbtn);

        progressDialog=new ProgressDialog(this);

        progressDialog.setMessage("Loading");

        locationpre=getSharedPreferences("location",MODE_PRIVATE);

        loceditor=locationpre.edit();


        currentlocationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                statusCheck();


//        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 25, mLocListener);

            }
        });


    }





    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location loc) {

            latitude= String.valueOf(loc.getLatitude());


            longitude= String.valueOf(loc.getLongitude());
            Log.e("lat",latitude);
            Log.e("longi",longitude);

            progressDialog.dismiss();
            loceditor.putString("lat",latitude);

            loceditor.putString("lng",longitude);

            loceditor.commit();


            Intent startmain = new Intent(SelectionActivity.this, MainActivity.class);

            startActivity(startmain);


        }
        public void onProviderDisabled(String arg0) {

        }
        public void onProviderEnabled(String provider) {

        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    public void statusCheck() {
        final LocationManager mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            progressDialog.dismiss();

        }
        else {
            LocationListener mLocListener = new MyLocationListener();

            if (ActivityCompat.checkSelfPermission(SelectionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SelectionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            boolean gps_enabled = false;
            boolean network_enabled = false;


            gps_enabled = mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = mLocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Location net_loc = null, gps_loc = null, finalLoc = null;

            if (gps_enabled)
                mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,425,mLocListener);
            if (network_enabled)
                mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,425,mLocListener);

        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}

