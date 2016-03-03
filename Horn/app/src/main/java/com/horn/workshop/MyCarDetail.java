package com.horn.workshop;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

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
public class MyCarDetail extends AppCompatActivity {
    public SMLocalStore smLocalStore;
    TextView car_id, car_names, car_make, car_model, car_varient, car_vehicle, car_registration, car_regyear;
    ImageView car_image;
    SQLiteHandler sqLiteHandler;
    ProgressDialog pDialog;
    String reg_nos, year_regs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_car_detail);
        displaydetails();
    }

    public void displaydetails() {
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
                        String car_vehicles = jsonObject.getString("car_vehicle");
                        String car_registrations = jsonObject.getString("car_registration");
                        String car_regyears = jsonObject.getString("car_regyear");
                        switch (car_varients) {
                            case "0":
                                car_varients = "Petrol";
                                break;
                            case "1":
                                car_varients = "Diesel";
                                break;
                            case "2":
                                car_varients = "Duo";
                                break;
                        }
                        car_names = (TextView) findViewById(R.id.mycar_name);
                        car_make = (TextView) findViewById(R.id.mycar_make);
                        car_model = (TextView) findViewById(R.id.mycar_model);
                        car_varient = (TextView) findViewById(R.id.mycar_varient);
                        car_vehicle = (TextView) findViewById(R.id.mycar_vehicle);
                        car_image = (ImageView) findViewById(R.id.mycar_photo);
                        car_registration = (TextView) findViewById(R.id.mycar_registration);
                        car_regyear = (TextView) findViewById(R.id.mycar_regyear);
                        car_make.setText(car_makes);
                        car_model.setText(car_models);
                        car_names.setText(car_namess);
                        car_vehicle.setText(car_vehicles);
                        car_varient.setText(car_varients);
                        car_registration.setText(car_registrations);
                        car_regyear.setText(car_regyears);

                        String url = "http://blueripples.org/horn/ajax-data/vehicle-images/" + car_images + "";
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
                        Toast.makeText(MyCarDetail.this, "No Network Connection", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                sqLiteHandler = new SQLiteHandler(MyCarDetail.this);
                HashMap<String, String> user = sqLiteHandler.getUserDetails();
                String apmnt_user_email = user.get("email");
                smLocalStore = new SMLocalStore(MyCarDetail.this);
                String car_id = smLocalStore.getProfileMyCar();
                Map<String, String> params = new HashMap<String, String>();
                params.put("add_car_id", car_id);
                params.put("get_mycars_email", apmnt_user_email);
                return params;
            }

        };

        AppController.getInstance().cancelPendingRequests("REQTAG");
        stringRequest.setTag("REQTAG");
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest, strreq);

    }

    public void registration_edit(View v) {
        car_registration = (TextView) findViewById(R.id.mycar_registration);
        car_regyear = (TextView) findViewById(R.id.mycar_regyear);
        LayoutInflater li = LayoutInflater.from(MyCarDetail.this);
        final View commentDialog = li.inflate(R.layout.profile_mycar_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MyCarDetail.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(commentDialog);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                TextView reg_no = (TextView) commentDialog.findViewById(R.id.registration);
                                TextView year_reg = (TextView) commentDialog.findViewById(R.id.year);
                                profile_edit_Car_ok(reg_no, year_reg);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        TextView reg_no = (TextView) commentDialog.findViewById(R.id.registration);
        TextView year_reg = (TextView) commentDialog.findViewById(R.id.year);
        reg_no.setText(car_registration.getText());
        year_reg.setText(car_regyear.getText());
        // show it
        alertDialog.show();
    }

    public void profile_edit_Car_ok(TextView reg_no, TextView year_reg) {
        reg_nos = reg_no.getText().toString();
        year_regs = year_reg.getText().toString();
        car_registration.setText(reg_no.getText());
        car_regyear.setText(year_reg.getText());
    }

    public void mycar_update(View v) {
        String strreq = "update_car";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADDCARDETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("hi", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        String status = jsonObject.getString("status");
                        if (status.equals("success")) {
                            Toast.makeText(MyCarDetail.this, "Updated Successfully !!!", Toast.LENGTH_LONG).show();

                            startActivity(new Intent(MyCarDetail.this, MyCars.class));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyCarDetail.this,"No Network Connection", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                String update_registartion = car_registration.getText().toString();
                String update_regyear = car_regyear.getText().toString();
                smLocalStore = new SMLocalStore(MyCarDetail.this);
                sqLiteHandler = new SQLiteHandler(MyCarDetail.this);
                HashMap<String, String> user = sqLiteHandler.getUserDetails();
                String update_user_email = user.get("email");
                smLocalStore = new SMLocalStore(MyCarDetail.this);
                String car_id = smLocalStore.getProfileMyCar();
                Map<String, String> params = new HashMap<String, String>();
                params.put("update_mycar", "update_mycar");
                params.put("update_car_id", car_id);
                params.put("update_car_registration", update_registartion);
                params.put("update_car_regyear", update_regyear);
                params.put("update_usermail", update_user_email);
                return params;
            }

        };

        AppController.getInstance().cancelPendingRequests("REQTAG");
        stringRequest.setTag("REQTAG");
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest, strreq);
    }

}
