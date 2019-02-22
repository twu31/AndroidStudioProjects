package com.example.tonywu.knowyourgovernment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class Locate {


    private MainActivity mainActivity;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public Locate(MainActivity mainAct) {
        mainActivity = mainAct;

        if (locationPermission()) {
            setUpLocation();
        }
    }

    @SuppressLint("MissingPermission")
    public void setUpLocation() {
        if (locationManager != null) {
            return;
        }
        if (!locationPermission()) {
            return;
        }
        locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                mainActivity.doLocation(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //permission request
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    public void lookLocation() {
        if (!locationPermission()) {
            return;
        }
        if (locationManager == null) {
            setUpLocation();
        }
        if (locationManager != null) {
            //permission requests
            @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null){
                mainActivity.doLocation(location.getLatitude(),location.getLongitude());
                Log.d("Locate","lookloctation:networkprovider");
                return;
            }
        }
        if (locationManager !=null){
            @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (location != null){
                mainActivity.doLocation(location.getLatitude(),location.getLongitude());
                Log.d("Locate","lookloctation:passiveprovider");
                return;
            }
        }
        if (locationManager != null) {
            //permission request
           @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null){
                mainActivity.doLocation(location.getLatitude(),location.getLongitude());
                Log.d("Locate","lookloctation:GPSprovider");
                return;
            }
        }

        Toast.makeText(mainActivity, "no location possible", Toast.LENGTH_SHORT).show();
        return;
    }
    public void shutdown() {
        locationManager.removeUpdates(locationListener);
        locationManager = null;
    }

    private boolean locationPermission(){
        if(ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return false;
        }
        return true;
    }

}
