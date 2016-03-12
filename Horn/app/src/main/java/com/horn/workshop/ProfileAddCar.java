package com.horn.workshop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.w3c.dom.Text;

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
    TextView car_id,car_names,car_make,car_model,car_varient,car_vehicle;
    ImageView car_image;
    Button add_car;
    SQLiteHandler sqLiteHandler;
    ProgressDialog pDialog;
    String reg_nos , year_regs,km_dones;
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
                        String car_vehicles = jsonObject.getString("car_vehicle");
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
                        car_vehicle = (TextView) findViewById(R.id.addcar_vehicle);
                        car_image = (ImageView) findViewById(R.id.addcar_photo);
                        car_make.setText(car_makes);
                        car_model.setText(car_models);
                        car_names.setText(car_namess);
                        car_varient.setText(car_varients);
                        car_vehicle.setText(car_vehicles);

                        String url = "http://blueripples.org/horn/ajax-data/vehicle-images/"+car_images+"";
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
        LayoutInflater li = LayoutInflater.from(ProfileAddCar.this);
        final View commentDialog = li.inflate(R.layout.profile_mycar_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ProfileAddCar.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(commentDialog);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                EditText reg_no = (EditText) commentDialog.findViewById(R.id.registration);
                                EditText km_done = (EditText) commentDialog.findViewById(R.id.km_done);
                EditText year_reg  = (EditText)commentDialog.findViewById(R.id.year);
                reg_nos = reg_no.getText().toString();
                year_regs = year_reg.getText().toString();
                km_dones = km_done.getText().toString();

                                    profile_add_Car_ok();


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

        // show it
        alertDialog.show();
         alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        EditText reg_no = (EditText) commentDialog.findViewById(R.id.registration);
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


//        EditText reg_no = (EditText) commentDialog.findViewById(R.id.registration);
//        if( reg_no.getText().toString().trim().equals("")){
//            Log.d("noreg","noregggg");
//            reg_no.setError( "Registration no is required!" );
//        }

//        final EditText userInput = (EditText) commentDialog
//                .findViewById(R.id.editTextDialogUserInput);

//        final Dialog commentDialog = new Dialog(this);
//        commentDialog.setContentView(R.layout.profile_mycar_popup);
//        Button okBtn = (Button) commentDialog.findViewById(R.id.ok);
//        okBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //do anything you want here before close the dialog
//                EditText reg_no = (EditText) commentDialog.findViewById(R.id.registration);
//                EditText year_reg  = (EditText)commentDialog.findViewById(R.id.year);
//                reg_nos = reg_no.getText().toString();
//                year_regs = year_reg.getText().toString();
//               profile_add_Car_ok();
//            }
//        });
//        Button cancelBtn = (Button) commentDialog.findViewById(R.id.cancel);
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                commentDialog.cancelLongPress();
//            }
//        });
//        commentDialog.show();
    }
    public void profile_add_Car_ok()
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

Log.d("registration..",reg_nos+".."+year_regs+".."+km_dones);
                Map<String, String> params = new HashMap<String, String>();
                params.put("add_tomycar", car_id);
                params.put("user_email", apmnt_user_email);
                params.put("user_reg_no", reg_nos);
                params.put("user_year_reg", year_regs);
                params.put("user_km_done", km_dones);


                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);
    }
}
