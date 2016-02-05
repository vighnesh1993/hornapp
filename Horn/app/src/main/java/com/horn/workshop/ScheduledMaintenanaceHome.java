package com.horn.workshop;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

/**
 * Created by Sariga on 1/5/2016.
 */
public class ScheduledMaintenanaceHome extends AppCompatActivity {

    private SMLocalStore smLocalStore;
    public String varient;
    public String make ;
    public String model;
    public String[] sm_service;
    private static final String TAG = "SM_homekm";
    String strreqTAG = "KmReqTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_maintenance_home);

      dropdown_display();


    }

    public void dropdown_display()
    {
        /** SM vehicle dropdown **/

        Spinner dropdown = (Spinner) findViewById(R.id.vehicle);
        String[] items = new String[]{"Etios", "Innova", "Fortuner"};
       varient = "1";
        make = "Maruthi";
        model = "A-star";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);


        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Log.d("helloo", String.valueOf(id));
                Spinner spinner = (Spinner) findViewById(R.id.vehicle);
              // final String vehicle_km = spinner.getSelectedItem().toString();
                LinearLayout ll = (LinearLayout) findViewById(R.id.sm_service);
                ll.removeAllViews();
               final String vehicle_km = "VXI";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SM_SERVICES, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "SM_home Response: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                int len = jsonObject.length();
                                JSONArray km_list = jsonObject.getJSONArray("kilometers");
                                sm_service =  new String[km_list.length()];
                                    for(int i=0;i<km_list.length();i++)
                                    {
                                        sm_service[i] = km_list.getString(i);
                                    }

                                km_display();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ScheduledMaintenanaceHome.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        smLocalStore = new SMLocalStore(ScheduledMaintenanaceHome.this);
                        String vehiclekm = vehicle_km;
                        String makekm = make;
                        String modelkm = model;
                        // Posting parameters to login url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("vehicle_km", vehiclekm);
                        params.put("make_km", makekm);
                        params.put("model_km", modelkm);
                        return params;
                    }

                };

                AppController.getInstance().addToRequestQueue(stringRequest, strreqTAG);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void km_display()
    {
       // final String[] sm_service = new String[]{"1000", "5000", "10000", "15000", "20000", "25000", "30000", "35000", "40000", "45000",
//                "50000", "55000", "60000", "65000", "70000", "75000", "80000", "85000", "90000", "95000", "100000", "105000", "110000",
//                "115000", "120000", "125000", "130000", "135000", "140000", "145000", "150000", "155000", "160000", "165000", "170000", "175000",
//                "180000"};

        final String km = " km";
        for (int i = 0; i < sm_service.length; i++) {
            final Button myButton = new Button(this);
            myButton.setText(sm_service[i] + km);
            LinearLayout ll = (LinearLayout) findViewById(R.id.sm_service);
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            myButton.setHeight(150);
            //  myButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_room_black_48dp, 0, 0, 0);
            ll.addView(myButton, lp);
            final String service = sm_service[i];
            myButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    smLocalStore = new SMLocalStore(ScheduledMaintenanaceHome.this);
                    Spinner spinner = (Spinner) findViewById(R.id.vehicle);
                    //String vehicle = spinner.getSelectedItem().toString();
                    String vehicle = "VXI";
                    smLocalStore.setSMhome(vehicle, service, varient,make,model);

                    Intent launchActivity1 = new Intent(ScheduledMaintenanaceHome.this, ScheduledMaintenanceDesc.class);
                    startActivity(launchActivity1);
                }
            });
        }

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
        } else if (id == R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
