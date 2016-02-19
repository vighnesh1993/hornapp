package com.horn.workshop;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChooseLocation extends AppCompatActivity {

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

        Button b1= (Button) findViewById(R.id.localtiy);
        Button b2= (Button) findViewById(R.id.enable_gps);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseLocation.this, PlacesAutoCompleteActivity.class));
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //turnGPSOn();
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

    @Override
    protected void onResume() {
        super.onResume();

        if(!getGpsStatus()) {
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
        /*Intent dialogIntent = new Intent(this, MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);*/
        Toast.makeText(getApplicationContext(),"back",Toast.LENGTH_SHORT).show();
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
        Settings.Secure.putString(this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED,newSet);
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
}
