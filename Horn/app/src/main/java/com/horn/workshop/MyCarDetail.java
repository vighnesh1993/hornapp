package com.horn.workshop;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;

/**
 * Created by Sariga on 2/9/2016.
 */
public class MyCarDetail extends AppCompatActivity {
    public SMLocalStore smLocalStore;
    TextView car_id,car_names,car_make,car_model,car_varient;
    ImageView car_image;
    SQLiteHandler sqLiteHandler;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_car_detail);
        displaydetails();
    }
    public void displaydetails()
    {
        String strreq = "addcar_detail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADDCARDETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("hi1", response);
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
                        car_names = (TextView) findViewById(R.id.mycar_name);
                        car_make = (TextView) findViewById(R.id.mycar_make);
                        car_model = (TextView) findViewById(R.id.mycar_model);
                        car_varient = (TextView) findViewById(R.id.mycar_varient);
                        car_image = (ImageView) findViewById(R.id.mycar_photo);
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
                        Toast.makeText(MyCarDetail.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(MyCarDetail.this);
                String car_id = smLocalStore.getProfileMyCar();
                Map<String, String> params = new HashMap<String, String>();
                params.put("add_car_id", car_id);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);

    }
}
