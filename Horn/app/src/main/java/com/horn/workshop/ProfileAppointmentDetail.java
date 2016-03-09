package com.horn.workshop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;

/**
 * Created by Sariga on 2/6/2016.
 */
public class ProfileAppointmentDetail  extends AppCompatActivity {
    String strreqTAG = "profileappointmnetdetail";
    private SMLocalStore smLocalStore;
    private static final String TAG = "SM_appointmentdetail";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_appointment_detail);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SM_SERVICES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SM_Apnmntdetail Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        JSONArray service = jsonObject.getJSONArray("service");
                        String partner_id = jsonObject.getString("partner_id");
                        String cname = jsonObject.getString("cname");
                        String work_details = jsonObject.getString("work_details");
                        String date = jsonObject.getString("date");
                        String time = jsonObject.getString("time");
                        String kilometers = jsonObject.getString("kilometers");
                        String status = jsonObject.getString("status");
                        String appointment_on = jsonObject.getString("appointment_on");
                        String vehicle_name = jsonObject.getString("vehicle_name");
                        String id = jsonObject.getString("id");
                        String price_total = jsonObject.getString("price_total");
                        String services_list = "";
                        for(int i=0;i<service.length();i++)
                        {
                            services_list += service.getString(i)+"\n";
                        }
                        TextView apmntid = (TextView) findViewById(R.id.apmntid);
                        TextView apmntworkshop = (TextView) findViewById(R.id.apmntworkshop);
                        TextView apmntkm = (TextView) findViewById(R.id.apmntkm);
                        TextView apmntstatus = (TextView) findViewById(R.id.apmntstatus);
                        TextView apmntdatefor = (TextView) findViewById(R.id.apmntdatefor);
                        TextView apmntdateon = (TextView) findViewById(R.id.apmntdateon);
                        TextView apmntdetails = (TextView) findViewById(R.id.apmntdetails);
                        TextView apmntservices = (TextView) findViewById(R.id.apmntservices);
                        TextView apmnttime = (TextView) findViewById(R.id.apmnttime);
                        TextView apmntvehicle = (TextView) findViewById(R.id.apmntvehicle);
                        TextView price_totalt = (TextView) findViewById(R.id.apmnttotal);

                        SimpleDateFormat parseFormat1 = new SimpleDateFormat("yyyy-mm-dd");
                        SimpleDateFormat displayFormat1 = new SimpleDateFormat("dd/mm/yyy");
                        Date date1 = new Date();
                        Date appointment_on1 = new Date();
                        try {
                            date1 = parseFormat1.parse(date);
                            appointment_on1 = parseFormat1.parse(appointment_on);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String date2 = displayFormat1.format(date1).toString();
                        String appointment_on2 = displayFormat1.format(appointment_on1).toString();

                        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
                        SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
                        Date date3 = new Date();
                        try {
                            date3 = parseFormat.parse(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                       String time1 = displayFormat.format(date3).toString();

                        apmntid.setText(id);
                        apmntworkshop.setText(partner_id);
                        apmntkm.setText(kilometers);
                        apmntstatus.setText(status);
                        apmntdatefor.setText(date2);
                        apmntdateon.setText(appointment_on2);
                        apmntdetails.setText(work_details);
                        apmntservices.setText(services_list);
                        apmnttime.setText(time1);
                        apmntvehicle.setText(vehicle_name);
                        price_totalt.setText("₹ "+price_total);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileAppointmentDetail.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(ProfileAppointmentDetail.this);
                String apnmnt_id =  smLocalStore.getProfileAppointment();

                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("appointmentid", apnmnt_id);
                               return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest, strreqTAG);


    }
}
