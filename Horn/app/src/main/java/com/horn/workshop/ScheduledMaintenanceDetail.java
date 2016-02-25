package com.horn.workshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;

public class ScheduledMaintenanceDetail extends AppCompatActivity {
    public SMLocalStore smLocalStore;
    private ProgressDialog pDialog;
    public String phone, name, category, address, workshopid, rating, profilepic, coordinates;
    Integer pic;
    TextView ratings;

    LatLng latLng1,latLng2;
    private  double coordLatitude =0.0;
    private double coordLongitude = 0.0;
    ScheduledMaintenanceWorkshoplist sw;
    String dist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_maintenance_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        ed.putString("currentLatitude",""+currentLatitude);
//        ed.putString("currentLongitude",""+currentLongitude);
        smLocalStore = new SMLocalStore(ScheduledMaintenanceDetail.this);

        HashMap<String, String> latlog = smLocalStore.getSmwCurrentLatlng();
        String lat = latlog.get("lat");
        String log = latlog.get("log");

       double d1=Double.parseDouble(lat);
       double d2=Double.parseDouble(log);

        latLng1=new LatLng(d1,d2);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        // String workshopid = "608";

        String workshopid = smLocalStore.getSMworkshopdetail_id();
        Button booknow = (Button) findViewById(R.id.book_now);
        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smLocalStore.setSMworkshopname(name);
                startActivity(new Intent(ScheduledMaintenanceDetail.this,ScheduledMaintenanceAppointment.class));
            }
        });
        getdetailFromDb(workshopid);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void getdetailFromDb(final String workshopid) {
        String strreq = "workshop_detail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SM_WORKSHOPDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        Log.d("details:::", jsonObject.getString("number"));
                        phone = jsonObject.getString("number");
                        name = jsonObject.getString("name");
                        category = jsonObject.getString("category");
                        address = jsonObject.getString("address");
                        String workshopid = jsonObject.getString("workshopid");
                        rating = jsonObject.getString("rating");
                        profilepic = jsonObject.getString("profilepic");
                        coordinates = jsonObject.getString("coordinates");
                        String[] parts = coordinates.split(",");
                        String part1 = parts[0]; // 004
                        String part2 = parts[1];

                        smLocalStore.setSmdcoordinates(part1,part2);

                        coordLatitude=Double.parseDouble(part1);
                        coordLongitude=Double.parseDouble(part2);
                        latLng2 = new LatLng(coordLatitude, coordLongitude);
                        sw=new ScheduledMaintenanceWorkshoplist();
                       dist=sw.getDistance(latLng1,latLng2);


                        pic = R.drawable.workshop_sample;
                        workshopdisplay_detail();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ScheduledMaintenanceDetail.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(ScheduledMaintenanceDetail.this);

                Map<String, String> params = new HashMap<String, String>();
                params.put("workshopid_detail", workshopid);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);
    }


    public void workshopdisplay_detail() {
        pDialog.dismiss();
        TextView workshopname = (TextView) findViewById(R.id.workshopdetail_name);
        TextView workshopphone = (TextView) findViewById(R.id.workshopdetail_phone);
        TextView workshopaddress = (TextView) findViewById(R.id.workshopdetail_address);
        TextView workshopcategory = (TextView) findViewById(R.id.workshopdetail_category);
        TextView ws_distance = (TextView) findViewById(R.id.ws_distance);
        final ImageView workshopimage = (ImageView) findViewById(R.id.workshopdetail_photo);
        ratings = (TextView) findViewById(R.id.rating);
        String url = "http://blueripples.org/horn/ajax-data/profilepics/" + profilepic;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    workshopimage.setImageBitmap(response.getBitmap());
                }
            }
        });
if((category).equals("Authorised")) {category = "Exclusive"; }
        workshopname.setText(name);
        workshopaddress.setText(address);
        workshopcategory.setText(category);
        workshopphone.setText(phone);
        ratings.setText(rating);
        setRatingBackround(rating);
        ws_distance.setText(dist);
        ws_distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ob=new Intent(ScheduledMaintenanceDetail.this,MapsActivity.class);
                ob.putExtra("workshop",name);
                startActivity(ob);
            }
        });

    }
    private void setRatingBackround(String ratingValue) {
        switch (ratingValue) {
            case "0":
                ratings.setBackgroundResource(R.drawable.rating_bg_0);
                break;
            case "0.5":
                ratings.setBackgroundResource(R.drawable.rating_bg_0_5);
                break;
            case "1":
                ratings.setBackgroundResource(R.drawable.rating_bg_1);
                break;
            case "1.5":
                ratings.setBackgroundResource(R.drawable.rating_bg_1_5);
                break;
            case "2":
                ratings.setBackgroundResource(R.drawable.rating_bg_2);
                break;
            case "2.5":
                ratings.setBackgroundResource(R.drawable.rating_bg_2_5);
                break;
            case "3":
                ratings.setBackgroundResource(R.drawable.rating_bg_3);
                break;
            case "3.5":
                ratings.setBackgroundResource(R.drawable.rating_bg_3_5);
                break;
            case "4":
                ratings.setBackgroundResource(R.drawable.rating_bg_4);
                break;
            case "4.5":
                ratings.setBackgroundResource(R.drawable.rating_bg_4_5);
                break;
            case "5":
                ratings.setBackgroundResource(R.drawable.rating_bg_5);
                break;
        }
    }
}
