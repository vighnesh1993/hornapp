package com.horn.workshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activity.MyCars;
import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;

/**
 * Created by Sariga on 2/9/2016.
 */
public class ProfileAddCar extends AppCompatActivity {
    public SMLocalStore smLocalStore;
    TextView car_id,car_names,car_make,car_model,car_varient;
    ImageView car_image;
    Button add_car;
    SQLiteHandler sqLiteHandler;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_add_car);
        displaydetails();
    }
    public void displaydetails()
    {
        String strreq = "addcar_detail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADDCARDETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("hi",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();


                       String car_ids = jsonObject.getString("car_id");
                       String car_namess = jsonObject.getString("car_names");
                       String car_makes = jsonObject.getString("car_make");
                       String car_models = jsonObject.getString("car_model");
                       String car_images = jsonObject.getString("car_image");
                       String car_varients = jsonObject.getString("car_varient");
                        switch(car_varients)
                        {
                            case "0" : car_varients = "Petrol"; break;
                            case "1" : car_varients = "Diesel"; break;
                            case "2" : car_varients = "Duo"; break;
                        }
                        car_names = (TextView) findViewById(R.id.addcar_name);
                        car_make = (TextView) findViewById(R.id.addcar_make);
                        car_model = (TextView) findViewById(R.id.addcar_model);
                        car_varient = (TextView) findViewById(R.id.addcar_varient);
                        car_image = (ImageView) findViewById(R.id.addcar_photo);
                        car_make.setText(car_makes);
                        car_model.setText(car_models);
                        car_names.setText(car_namess);
                        car_varient.setText(car_varients);

                        String url = "http://blueripples.org/horn/app_server/cars/"+car_images+"";
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
                                    car_image.setImageBitmap(response.getBitmap());
                                }
                            }
                        });


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileAddCar.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(ProfileAddCar.this);
String car_id = smLocalStore.getProfileAddCar();
                Map<String, String> params = new HashMap<String, String>();
                params.put("add_car_id", car_id);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);

    }
    public void profile_add_Car(View v)
    {
        pDialog = new ProgressDialog(ProfileAddCar.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading....");
        pDialog.show();
        String strreq = "addcar_detail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADDCARDETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("hi",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        pDialog.dismiss();
                        Toast.makeText(ProfileAddCar.this, "Successfully added to My Cars!!!", Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(ProfileAddCar.this, MyCars.class);
                        startActivity(intent1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileAddCar.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(ProfileAddCar.this);
                String car_id = smLocalStore.getProfileAddCar();
                sqLiteHandler = new SQLiteHandler(ProfileAddCar.this);
                HashMap<String, String> user = sqLiteHandler.getUserDetails();
                String  apmnt_user_email = user.get("email");

                Map<String, String> params = new HashMap<String, String>();
                params.put("add_tomycar", car_id);
                params.put("user_email", apmnt_user_email);


                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);
    }
}
