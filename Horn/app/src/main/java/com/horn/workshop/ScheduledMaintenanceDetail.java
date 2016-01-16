package com.horn.workshop;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;

public class ScheduledMaintenanceDetail extends AppCompatActivity {
    public SMLocalStore smLocalStore;
    public String phone , name ,category , address , workshopid,rating, profilepic,coordinates ;
    Integer pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_maintenance_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // String workshopid = "608";
        smLocalStore = new SMLocalStore(ScheduledMaintenanceDetail.this);
        String workshopid = smLocalStore.getSMworkshopdetail_id();
getdetailFromDb(workshopid);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
    public void getdetailFromDb(final String workshopid)
    {
        String strreq = "workshop_detail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SM_WORKSHOPDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        Log.d("details:::",jsonObject.getString("number"));
                        phone = jsonObject.getString("number");
                        name = jsonObject.getString("name");
                        category = jsonObject.getString("category");
                        address = jsonObject.getString("address");
                        String workshopid = jsonObject.getString("workshopid");
                        rating = jsonObject.getString("rating");
                        profilepic = jsonObject.getString("profilepic");
                        coordinates = jsonObject.getString("coordinates");
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
                        Toast.makeText(ScheduledMaintenanceDetail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
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

    public void workshopdisplay_detail()
    {
        TextView workshopname = (TextView) findViewById(R.id.workshopdetail_name);
        TextView workshopphone = (TextView) findViewById(R.id.workshopdetail_phone);
        TextView workshopaddress = (TextView) findViewById(R.id.workshopdetail_address);
        TextView workshopcategory = (TextView) findViewById(R.id.workshopdetail_category);
        ImageView workshopimage = (ImageView) findViewById(R.id.workshopdetail_photo);
        TextView ratings = (TextView) findViewById(R.id.rating);

        workshopname.setText(name);
        workshopaddress.setText(address);
        workshopcategory.setText(category);
        workshopimage.setImageResource(pic);
        workshopphone.setText(phone);
        ratings.setText(rating);
    }

}
