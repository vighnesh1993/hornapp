package com.horn.workshop;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;


/**
 * Created by Sariga on 1/18/2016.
 */
public class ScheduledMaintenanceAppointment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
public String  km_apnmt,vehicle_apnmt,workshopid_apnmt,description_apnmt,customername_apnmt,services_apmnt,date_apmnt,email_apnmt,phone_apnmt, make_apnmt, model_apnmt, regno_apnmt,time_apmnt;
    private SMLocalStore smLocalStore;
public static EditText timeview,dateviews,timeviews;
    private SQLiteHandler sqLiteHandler;

    private ProgressDialog pDialog;
   public static final Calendar c = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_maintenance_appoinment);
        TextView workshopname= (TextView) findViewById(R.id.apmnt_name);
        EditText vehicle= (EditText) findViewById(R.id.apmnt_vehicle);
        EditText km= (EditText) findViewById(R.id.apmnt_km);
        smLocalStore = new SMLocalStore(ScheduledMaintenanceAppointment.this);
        km.setText(smLocalStore.getSMhome_kms()+" KM");
        vehicle.setText(smLocalStore.getSMhome_vehicle());
        workshopname.setText(smLocalStore.getSMworkshop_name());
       timeview=(EditText)findViewById(R.id.apmnt_time);

           }


public void setDateTimeField(View v) {


    DialogFragment newFragment = new DatePickerFragment();
    newFragment.show(getSupportFragmentManager(), "datePicker");
    

 }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        //do some stuff for example write on log and update TextField on activity
        month = month+1;
        ((EditText) findViewById(R.id.dateview)).setText(day + "/" + month + "/" + year);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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
        }else if(id ==R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v) {
        setDateTimeField(v);
    }

public void timeviewclick(View v)
{

    //final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
   int am_pm = c.get(Calendar.AM_PM);
    CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(ScheduledMaintenanceAppointment.this, timeSetListener, hour, minute, false);

    timePickerDialog.setTitle("Set the time");
        timePickerDialog.show();
}

    public static class CustomTimePickerDialog extends TimePickerDialog {

        public static final int TIME_PICKER_INTERVAL=15;


        public CustomTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
            super(context, callBack, hourOfDay, minute, is24HourView);
        }


        @Override
        public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
            super.onTimeChanged(timePicker, hourOfDay, minute);



               //minute = getRoundedMinute(minute);
                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                c.set(Calendar.MINUTE,minute);


//                timePicker.s
//                timePicker.setMinute(minute);
 //            timePicker.setCurrentMinute(minute);
                //timeview.setText(String.format("%02d", hourOfDay) + ":" +String.format("%02d", minute));
                StringBuilder sb = new StringBuilder();
                if(hourOfDay>12){
                    sb.append(hourOfDay-12).append( ":" ).append(String.format("%02d", minute)).append(" PM");

                }else if(hourOfDay==0){
                    sb.append("12").append( ":" ).append(String.format("%02d", minute)).append(" AM");
                }
                else if(hourOfDay==12){
                    sb.append("12").append( ":" ).append(String.format("%02d", minute)).append(" PM");
                }
                else{
                    sb.append(hourOfDay).append( ":" ).append(String.format("%02d", minute)).append(" AM");
                }
                timeview.setText(sb);



        }

        public static int getRoundedMinute(int minute){
            if(minute % TIME_PICKER_INTERVAL != 0){
                int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
                minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
                if (minute == 60)  minute=0;
            }

            return minute;
        }
    }

    private CustomTimePickerDialog.OnTimeSetListener timeSetListener = new CustomTimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

           //minute = CustomTimePickerDialog.getRoundedMinute(minute);

            c.set(Calendar.HOUR_OF_DAY,hourOfDay);
            c.set(Calendar.MINUTE,minute);
            StringBuilder sb = new StringBuilder();
            if(hourOfDay>12){
                sb.append(hourOfDay-12).append( ":" ).append(String.format("%02d", minute)).append(" PM");
            }
            else if(hourOfDay==0){
                sb.append("12").append( ":" ).append(String.format("%02d", minute)).append(" AM");
            }
            else if(hourOfDay==12){
                sb.append("12").append( ":" ).append(String.format("%02d", minute)).append(" PM");
            }
            else{
                sb.append(hourOfDay).append( ":" ).append(String.format("%02d", minute)).append(" AM");
            }
            timeview.setText(sb);
        }
    };


    public void sm_appointment(View v)
    {
        pDialog = new ProgressDialog(ScheduledMaintenanceAppointment.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please wait while we are saving your details");
        pDialog.show();

        String strreq = "workshop_appnmt";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SM_SERVICES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject != null) {

                        pDialog.dismiss();
                        Toast.makeText(ScheduledMaintenanceAppointment.this, "Successfully booked an appointment", Toast.LENGTH_LONG).show();
                        new AlertDialog.Builder(ScheduledMaintenanceAppointment.this)
                                .setMessage("Thank You!!! Your appointment id is: "+jsonObject.getString("appointment_booked"))
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent launchActivity1 = new Intent(ScheduledMaintenanceAppointment.this, ScheduledMaintenanaceHome.class);
                                        startActivity(launchActivity1);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setCancelable(false)
                                .show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ScheduledMaintenanceAppointment.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(ScheduledMaintenanceAppointment.this);
                km_apnmt = smLocalStore.getSMhome_kms()+"K";
                vehicle_apnmt = smLocalStore.getSMhome_vehicle();
                workshopid_apnmt = smLocalStore.getSMworkshopdetail_id();
                description_apnmt = smLocalStore.getSMdesc();
               //make_apnmt =
                // model_apnmt =
                // regno_apnmt =
                services_apmnt = smLocalStore.getSMservices();
                timeviews = (EditText) findViewById(R.id.apmnt_time);
                dateviews = (EditText) findViewById(R.id.dateview);
                date_apmnt = dateviews.getText().toString();

                SimpleDateFormat parseFormat1 = new SimpleDateFormat("dd/mm/yyyy");
                SimpleDateFormat displayFormat1 = new SimpleDateFormat("yyyy-mm-dd");
                Date date1 = new Date();
                try {
                    date1 = parseFormat1.parse(date_apmnt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String date_apmnt1 = displayFormat1.format(date1).toString();
                String time_apmnt1 = timeviews.getText().toString();
                SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                Date date = new Date();
                try {
                    date = parseFormat.parse(time_apmnt1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                time_apmnt = displayFormat.format(date);


                sqLiteHandler = new SQLiteHandler(ScheduledMaintenanceAppointment.this);
                HashMap<String, String> user = sqLiteHandler.getUserDetails();
                customername_apnmt = user.get("name");
                email_apnmt = user.get("email");
                phone_apnmt = user.get("phone");
                Calendar cc = Calendar.getInstance();
                int booked_on_day = cc.get(Calendar.DAY_OF_MONTH);
                int booked_on_month = cc.get(Calendar.MONTH);
                int booked_on_year = cc.get(Calendar.YEAR);
                int booked_on = booked_on_year+'-'+booked_on_month+'-'+booked_on_day;
                Log.d("datas",km_apnmt);

                Map<String, String> params = new HashMap<String, String>();
                params.put("workshopid_apnmt", workshopid_apnmt);
                params.put("customername_apnmt", customername_apnmt);
                params.put("phone_apnmt", phone_apnmt);
                params.put("email_apnmt", email_apnmt);
                params.put("services_apmnt", services_apmnt);
                params.put("date_apmnt", date_apmnt1);
                params.put("description_apnmt",description_apnmt );
                params.put("km_apnmt", km_apnmt);
                params.put("vehicle_apnmt", vehicle_apnmt);
                params.put("time_apmnt",time_apmnt);
                params.put("appointment_booking", "1");
                params.put("booked_on", String.valueOf(booked_on));
                   return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);

    }
}
