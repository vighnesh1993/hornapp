package com.horn.workshop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.ServicesManager;

/**
 * Created by Sariga on 2/5/2016.
 */
public class ProfileMyAppoinmentList extends AppCompatActivity {
    //private static RecyclerView.Adapter adapter;
    private SQLiteHandler sqLiteHandler;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private SMLocalStore smLocalStore;
    private static final String TAG = "SM_homekm";
    public String[]  partner_ids,apmntids,dates,statuss,appointment_ons;
    String strreqTAG = "apmntdetailTAG";
    ProfileAppoinmentAdapter adapter;
    public static ArrayList<ProfileappointmentData> apnmnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_my_appoinment_list);
        recyclerView = (RecyclerView) findViewById(R.id.profile_appointment);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        apnmnt = new ArrayList<ProfileappointmentData>();

       // recyclerView.setAdapter(adapter);
       // apnmnt = new ArrayList<ProfileappointmentData>();
       // adapter = new ProfileAppoinmentAdapter(apnmnt);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SM_SERVICES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SM_profile Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        JSONArray partner_id = jsonObject.getJSONArray("partner_id");
                        JSONArray apmntid = jsonObject.getJSONArray("apmntid");
                        JSONArray date = jsonObject.getJSONArray("date");
                        JSONArray status = jsonObject.getJSONArray("status");
                        JSONArray appointment_on = jsonObject.getJSONArray("appointment_on");

                        partner_ids = new String[partner_id.length()];
                        apmntids = new String[apmntid.length()];
                        dates = new String[date.length()];
                        statuss = new String[status.length()];
                        appointment_ons = new String[appointment_on.length()];

                        for (int i = 0; i < partner_id.length(); i++) {

                            partner_ids[i] = partner_id.getString(i);
                            apmntids[i] = apmntid.getString(i);
                            dates[i] = date.getString(i);
                            statuss[i] = status.getString(i);
                            appointment_ons[i] = appointment_on.getString(i);

                            apnmnt.add(new ProfileappointmentData(
                                    partner_ids[i],
                                    apmntids[i],
                                    dates[i],
                                    statuss[i],
                                    appointment_ons[i]
                            ));
                        }

                        adapter = new ProfileAppoinmentAdapter(apnmnt);
                        recyclerView.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileMyAppoinmentList.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(ProfileMyAppoinmentList.this);
             //  String apnmnt_id =  smLocalStore.getProfileAppointment();
                sqLiteHandler = new SQLiteHandler(ProfileMyAppoinmentList.this);
                HashMap<String, String> user = sqLiteHandler.getUserDetails();
                String  apmnt_user_email = user.get("email");
                String  apmnt_user_name = user.get("name");
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("apmnt_user_email", apmnt_user_email);
                params.put("apmnt_user_name", apmnt_user_name);
                              return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest, strreqTAG);




    }
}
