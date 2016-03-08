package com.horn.workshop;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChooseLocation extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private UserLocalStore userLocalStore;
    private LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    private LatLng latLng;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    String subLocality="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//
//
//            }
//        });


        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();


        userLocalStore = new UserLocalStore(this);
        Button b1= (Button) findViewById(R.id.localtiy);
        Button b2= (Button) findViewById(R.id.enable_gps);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLocalStore.setindicator(true);
                startActivity(new Intent(ChooseLocation.this, PlacesAutoCompleteActivity.class));
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //turnGPSOn();
                //userLocalStore.setindicator(true);
                userLocalStore = new UserLocalStore(ChooseLocation.this);

                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);



                //startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                //startActivity(new Intent(ChooseLocation.this, MainActivity.class));
            /*try {
                Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
                intent.putExtra("enabled", true);
                sendBroadcast(intent);
            }
            catch (Exception e)
            {
                Log.e("error",""+e);
            }*/
            }
        });

    }


    private void handleNewLocation(Location location) {
        //  Log.d(TAG, location.toString());

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        //Toast.makeText(getApplicationContext(),"latlang :"+currentLatitude+" "+currentLongitude,Toast.LENGTH_LONG).show();
        //smLocalStore.setSmwCurrentLatlng("" + currentLatitude, "" + currentLongitude);

        latLng = new LatLng(currentLatitude, currentLongitude);

            getCurrentLocation(currentLatitude, currentLongitude);

    }
    private void getCurrentLocation(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            // String city = addresses.get(0).getLocality();
            Toast.makeText(getApplicationContext(),"Choose location current :"+latitude+","+longitude,Toast.LENGTH_SHORT).show();

            subLocality = addresses.get(0).getSubLocality();
            //Toast.makeText(getApplicationContext(),""+subLocality,Toast.LENGTH_SHORT).show();
           /* userLocalStore.setManualLocation(subLocality);
            ab.setSubtitle(subLocality);
            userLocalStore.setGPSLatlong(""+latitude+","+longitude);*/


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        //if(!getGpsStatus() && userLocalStore.getManualLocationChoosen()) {
        if(!getGpsStatus()) {
            mGoogleApiClient.connect();
            userLocalStore = new UserLocalStore(this);
            userLocalStore.setManualLocation("");
            //Toast.makeText(getApplicationContext(),""+subLocality,Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ChooseLocation.this, MainActivity.class));
            finish();
        }
    }
    private boolean getGpsStatus(){

        String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.equals(""))
        {
            return true;
        }
       else
        {
            return false;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void turnGPSOn()
    {
        /*Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        this.sendBroadcast(intent);

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.sendBroadcast(poke);


        }*/

      /*  Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        this.sendBroadcast(intent);

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.sendBroadcast(poke);
        }*/

        String beforeEnable=Settings.Secure.getString(this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        String newSet=String.format("%s,%s",beforeEnable, LocationManager.GPS_PROVIDER);
        Settings.Secure.putString(this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED, newSet);
        try
        {

        }
        catch(Exception e)
        {


            Log.e("error :",""+e);
        }

    }
    // automatic turn off the gps
    public void turnGPSOff()
    {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.sendBroadcast(poke);
        }
    }

    public void onConnected(Bundle bundle) {
        mUpdateLocation();
    }

    private void mUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
