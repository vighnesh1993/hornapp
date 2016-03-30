package com.horn.workshop;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
<<<<<<< HEAD
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
=======

import android.support.v7.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

>>>>>>> master
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
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
   public TextView car_id,car_names,car_make,car_model,car_varient,car_vehicle , car_registration,car_regyear,car_km_done,car_insurer,car_insure_date,car_pollution_date;
    ImageView car_image;
    SQLiteHandler sqLiteHandler;
    ProgressDialog pDialog;
    TextView insurer,insurerlbl,pollu_date,pollu_datelbl,insur_date,insur_datelbl,insurancedate;
    View insurerv,pollu_datev,insur_datev;
    String reg_nos , year_regs,km_dones,insurers,insure_dates,pollution_dates, car_insurers, car_insurance_dates,car_pollution_dates;
   int year,month,day;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_car_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        String car_vehicles = jsonObject.getString("car_vehicle");
                        String car_registrations = jsonObject.getString("car_registration");
                        String car_regyears = jsonObject.getString("car_regyear");
                        String car_km_dones = jsonObject.getString("car_km_done");
                        car_insurers = jsonObject.getString("car_insurer");
                        car_insurance_dates = jsonObject.getString("car_insurance_date");
                        car_pollution_dates = jsonObject.getString("car_pollution_date");
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
                        car_vehicle = (TextView) findViewById(R.id.mycar_vehicle);
                        car_image = (ImageView) findViewById(R.id.mycar_photo);
                        car_registration = (TextView) findViewById(R.id.mycar_registration);
                        car_regyear = (TextView) findViewById(R.id.mycar_regyear);
                        car_km_done = (TextView) findViewById(R.id.mycar_kmdone);
                        car_insurer = (TextView) findViewById(R.id.mycar_insurer);
                        car_insure_date = (TextView) findViewById(R.id.mycar_insurance_date);
                        car_pollution_date = (TextView) findViewById(R.id.mycar_pollution_date);
                        if(!car_insurers.equals("0"))
                        {
                            insurer = (TextView) findViewById(R.id.mycar_insurer);
                            insurerlbl = (TextView) findViewById(R.id.mycar_insurer_lbl);
                            insurerv = (View) findViewById(R.id.hori_insurer);
                            insurer.setVisibility(View.VISIBLE);
                           insurerv.setVisibility(View.VISIBLE);
                           insurerlbl.setVisibility(View.VISIBLE);
                            insurer.setText(car_insurers);
                        }
                        if(!car_pollution_dates.equals("0"))
                        {
                            pollu_date = (TextView) findViewById(R.id.mycar_insurance_date);
                            pollu_datelbl = (TextView) findViewById(R.id.mycar_insdate_lbl);
                            pollu_datev = (View) findViewById(R.id.hori_pollu_date);
                            pollu_date.setVisibility(View.VISIBLE);
                            pollu_datev.setVisibility(View.VISIBLE);
                            pollu_datelbl.setVisibility(View.VISIBLE);
                            pollu_date.setText(car_insurance_dates);
                        }
                        if(!car_insurance_dates.equals("0"))
                        {
                            insur_date = (TextView) findViewById(R.id.mycar_pollution_date);
                            insur_datelbl = (TextView) findViewById(R.id.mycar_polludate_lbl);
                            insur_datev = (View) findViewById(R.id.hori_ins_date);
                            insur_date.setVisibility(View.VISIBLE);
                            insur_datev.setVisibility(View.VISIBLE);
                            insur_datelbl.setVisibility(View.VISIBLE);
                            insur_date.setText(car_pollution_dates);
                        }


                        car_make.setText(car_makes);
                        car_model.setText(car_models);
                        car_names.setText(car_namess);
                        car_vehicle.setText(car_vehicles);
                        car_varient.setText(car_varients);
                        car_registration.setText(car_registrations);
                        car_regyear.setText(car_regyears);
                        car_km_done.setText(car_km_dones);

                        String url = "http://blueripples.org/horn/ajax-data/vehicle-images/"+car_images+"";
                        Picasso.with(getApplicationContext())
                                .load(url)
                                .placeholder(R.drawable.car_placeholder)
                                .into(car_image);
//                        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//                        imageLoader.get(url, new ImageLoader.ImageListener() {
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                // Log.e(TAG, "Image Load Error: " + error.getMessage());
//                            }
//
//                            @Override
//                            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
//                                if (response.getBitmap() != null) {
//                                    // load image into imageview
//                                    car_image.setImageBitmap(response.getBitmap());
//                                }
//                            }
//                        });


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
                sqLiteHandler = new SQLiteHandler(MyCarDetail.this);
                HashMap<String, String> user = sqLiteHandler.getUserDetails();
                String  apmnt_user_email = user.get("email");
                smLocalStore = new SMLocalStore(MyCarDetail.this);
                String car_id = smLocalStore.getProfileMyCar();
                Map<String, String> params = new HashMap<String, String>();
                params.put("add_car_id", car_id);
                params.put("get_mycars_email", apmnt_user_email);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);

    }
    public void registration_edit(View v)
    {
        car_registration = (TextView) findViewById(R.id.mycar_registration);
        car_regyear = (TextView) findViewById(R.id.mycar_regyear);
        car_km_done = (TextView) findViewById(R.id.mycar_kmdone);

        LayoutInflater li = LayoutInflater.from(MyCarDetail.this);
        final View commentDialog = li.inflate(R.layout.profile_mycar_popup, null);
        LinearLayout update2 = (LinearLayout) commentDialog.findViewById(R.id.update_car_popup);
        update2.setVisibility(View.VISIBLE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MyCarDetail.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(commentDialog);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                TextView reg_no = (TextView) commentDialog.findViewById(R.id.registration);
                                TextView year_reg  = (TextView)commentDialog.findViewById(R.id.year);
                                TextView km_done = (TextView) commentDialog.findViewById(R.id.km_done);
                                TextView insurer  = (TextView)commentDialog.findViewById(R.id.insurer);
                                insurancedate  = (TextView)commentDialog.findViewById(R.id.insurance_due_date);
                                TextView pollutiondate  = (TextView)commentDialog.findViewById(R.id.pollution_due_date);
                                profile_edit_Car_ok(reg_no,year_reg,km_done,insurer,insurancedate,pollutiondate);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        TextView reg_no = (TextView) commentDialog.findViewById(R.id.registration);
        TextView year_reg  = (TextView)commentDialog.findViewById(R.id.year);
        TextView km_done = (TextView)commentDialog.findViewById(R.id.km_done);
        reg_no.setText(car_registration.getText());
        year_reg.setText(car_regyear.getText());
        km_done.setText(car_km_done.getText());
        if(!car_insurers.equals("0"))
        {
            TextView insurer1 = (TextView)commentDialog.findViewById(R.id.insurer);
           insurer1.setText(insurer.getText());
        }
        if(!car_pollution_dates.equals("0"))
        {   insur_date = (TextView) findViewById(R.id.mycar_pollution_date);
            insur_datelbl = (TextView) findViewById(R.id.mycar_polludate_lbl);
            insur_datev = (View) findViewById(R.id.hori_ins_date);
            TextView ins_date1 = (TextView)commentDialog.findViewById(R.id.insurance_due_date);
           ins_date1.setText(insur_date.getText());
        }
        if(!car_insurance_dates.equals("0"))
        {
            pollu_date = (TextView) findViewById(R.id.mycar_insurance_date);
            pollu_datelbl = (TextView) findViewById(R.id.mycar_insdate_lbl);
            pollu_datev = (View) findViewById(R.id.hori_pollu_date);
            TextView pollu_date1 = (TextView)commentDialog.findViewById(R.id.pollution_due_date);
            pollu_date1.setText(pollu_date.getText());
        }

        // show it
        alertDialog.show();

        reg_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    alertDialog.getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    alertDialog.getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
    });
    }
    public void profile_edit_Car_ok(TextView reg_no,TextView year_reg,TextView km_done,TextView insurer,TextView insurancedate,TextView pollutiondate)
    {
        reg_nos = reg_no.getText().toString();
        year_regs = year_reg.getText().toString();
        km_dones = km_done.getText().toString();
        insurers = insurer.getText().toString();
        insure_dates = insurancedate.getText().toString();
        pollution_dates = pollutiondate.getText().toString();

        car_registration.setText(reg_no.getText());
        car_regyear.setText(year_reg.getText());
        car_km_done.setText(km_dones);
        car_insurer.setText(insurers);
        car_insure_date.setText(insure_dates);
        car_pollution_date.setText(pollution_dates);

        TextView insurer1 = (TextView) findViewById(R.id.mycar_insurer);
        TextView insurerlbl = (TextView) findViewById(R.id.mycar_insurer_lbl);
        View insurerv = (View) findViewById(R.id.hori_insurer);
        insurer1.setVisibility(View.VISIBLE);
        insurerv.setVisibility(View.VISIBLE);
        insurerlbl.setVisibility(View.VISIBLE);
        TextView pollu_date = (TextView) findViewById(R.id.mycar_insurance_date);
        TextView pollu_datelbl = (TextView) findViewById(R.id.mycar_insdate_lbl);
        View pollu_datev = (View) findViewById(R.id.hori_pollu_date);
        pollu_date.setVisibility(View.VISIBLE);
        pollu_datev.setVisibility(View.VISIBLE);
        pollu_datelbl.setVisibility(View.VISIBLE);
        TextView insur_date = (TextView) findViewById(R.id.mycar_pollution_date);
        TextView insur_datelbl = (TextView) findViewById(R.id.mycar_polludate_lbl);
        View insur_datev = (View) findViewById(R.id.hori_ins_date);
        insur_date.setVisibility(View.VISIBLE);
        insur_datev.setVisibility(View.VISIBLE);
        insur_datelbl.setVisibility(View.VISIBLE);

    }
    public void mycar_update(View v)
    {
        String strreq = "update_car";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADDCARDETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("hi",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
String status = jsonObject.getString("status");
                        if(status.equals("success"))
                        {
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
                        Toast.makeText(MyCarDetail.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                String update_registartion = car_registration.getText().toString();
                String update_regyear = car_regyear.getText().toString();
                String update_kmdone = car_km_done.getText().toString();
                String update_insurer = car_insurer.getText().toString();
                String update_ins_date = car_insure_date.getText().toString();
                String update_polu_date = car_pollution_date.getText().toString();
                smLocalStore = new SMLocalStore(MyCarDetail.this);
                sqLiteHandler = new SQLiteHandler(MyCarDetail.this);
                HashMap<String, String> user = sqLiteHandler.getUserDetails();
                String  update_user_email = user.get("email");
                smLocalStore = new SMLocalStore(MyCarDetail.this);
                String car_id = smLocalStore.getProfileMyCar();
                Map<String, String> params = new HashMap<String, String>();
                params.put("update_mycar", "update_mycar");
                params.put("update_car_id", car_id);
                params.put("update_car_registration", update_registartion);
                params.put("update_car_regyear", update_regyear);
                params.put("update_car_kmdone", update_kmdone);
                params.put("update_car_insurer", update_insurer);
                params.put("update_car_insdate", update_ins_date);
                params.put("update_car_polludate", update_polu_date);
                params.put("update_usermail", update_user_email);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);
    }



}
