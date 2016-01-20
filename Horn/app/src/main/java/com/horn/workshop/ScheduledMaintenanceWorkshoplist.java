package com.horn.workshop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import static com.horn.workshop.R.drawable.a;

/**
 * Created by Sariga on 1/8/2016.
 */
public class ScheduledMaintenanceWorkshoplist extends AppCompatActivity {


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public ProgressDialog pDialog;
    public static ArrayList<WorkshopDatas> workshop;
    private static final String TAG = "SM_workshopdata_search";
    public SMLocalStore smLocalStore;
    public static String[] nameArray;
    public static String[] phoneArray;
    public static String[] categoryArray;
    public static String[] addressArray;
    public static Integer[] picArray;
    public static String[] profilepicArray;
    public static Integer[] workshopidArray;
    public static String[] ratingArray, coordinateArray;


    //  static View.OnClickListener myOnClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_maintenance_workshoplist);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        search_workshop();
        // myOnClickListener = new MyOnClickListener(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Searching for workshops ...");
        pDialog.show();
    }

    //    public void new_activity_launch(String workshopid)
//    {
//        Intent intent1 = new Intent(this, ScheduledMaintenanceDetail.class);
//                startActivity(intent1);
//        //smLocalStore = new SMLocalStore(this);
//        //         smLocalStore.setSMworkshoplist(workshopid);
//       // startActivity(new Intent(ScheduledMaintenanceWorkshoplist.this, ScheduledMaintenanceService.class));
//    }
    public void search_workshop_display() {
        recyclerView = (RecyclerView) findViewById(R.id.workshop_rv);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        workshop = new ArrayList<WorkshopDatas>();
        for (int i = 0; i < nameArray.length; i++) {
            workshop.add(new WorkshopDatas(
                    nameArray[i],
                    addressArray[i],
                    phoneArray[i],
                    categoryArray[i],
                    profilepicArray[i],
                    workshopidArray[i],
                    ratingArray[i]
            ));
        }

        //    removedItems = new ArrayList<Integer>();

        adapter = new SMAdapter(workshop);
        recyclerView.setAdapter(adapter);
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
    public void search_workshop() {
    /*
    *Datas from DB starts
    */
        String strreq = "req";

        //final ProgressDialog loading;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SM_WORKSHOPDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        JSONArray phoneArrayj = jsonObject.getJSONArray("number");
                        JSONArray nameArrayj = jsonObject.getJSONArray("name");
                        JSONArray categoryArrayj = jsonObject.getJSONArray("category");
                        JSONArray addressArrayj = jsonObject.getJSONArray("address");
                        JSONArray workshopidArrayj = jsonObject.getJSONArray("workshopid");
                        JSONArray ratingArrayj = jsonObject.getJSONArray("rating");
                        JSONArray profilepicArrayj = jsonObject.getJSONArray("profilepic");
                        JSONArray coordinateArrayj = jsonObject.getJSONArray("coordinates");

                        nameArray = new String[nameArrayj.length()];
                        phoneArray = new String[nameArrayj.length()];
                        categoryArray = new String[nameArrayj.length()];
                        addressArray = new String[nameArrayj.length()];
                        picArray = new Integer[nameArrayj.length()];
                        workshopidArray = new Integer[nameArrayj.length()];
                        ratingArray = new String[ratingArrayj.length()];
                        profilepicArray = new String[profilepicArrayj.length()];
                        coordinateArray = new String[coordinateArrayj.length()];

                        for (int i = 0; i < nameArrayj.length(); i++) {
                            phoneArray[i] = phoneArrayj.getString(i);
                            nameArray[i] = nameArrayj.getString(i);
                            categoryArray[i] = categoryArrayj.getString(i);
                            addressArray[i] = addressArrayj.getString(i);
                            picArray[i] = R.drawable.workshop_sample;
                            workshopidArray[i] = workshopidArrayj.getInt(i);
                            ratingArray[i] = ratingArrayj.getString(i);
                            profilepicArray[i] = "http://blueripples.org/horn/ajax-data/profilepics/" + profilepicArrayj.getString(i);
                            coordinateArray[i] = coordinateArrayj.getString(i);

                        }
                        pDialog.dismiss();
                        search_workshop_display();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ScheduledMaintenanceWorkshoplist.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(ScheduledMaintenanceWorkshoplist.this);
                String location = "1";
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("location", location);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);
    }


    /*
    *Datas from db ends
     */

}

