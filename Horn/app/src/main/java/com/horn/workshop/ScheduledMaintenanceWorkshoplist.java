package com.horn.workshop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;

import static com.horn.workshop.R.drawable.a;

/**
 * Created by Sariga on 1/8/2016.
 */
public class ScheduledMaintenanceWorkshoplist extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


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
    public static String[] distance;
    public static String[] profilepicArray;
    public static Integer[] workshopidArray;
    public static String[] ratingArray, coordinateArray;

    private  double currentLatitude =0.0;
    private double currentLongitude = 0.0;
    private  double coordLatitude =0.0;
    private double coordLongitude = 0.0;
    private  LatLng latLng;
    private LatLng latLng1;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Menu optionsMenu;

    //  static View.OnClickListener myOnClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_maintenance_workshoplist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Searching for workshops ...");
        search_workshop();
        // myOnClickListener = new MyOnClickListener(this);

    }

        public void new_activity_launch(String workshopid)
    {
        Intent intent1 = new Intent(this, ScheduledMaintenanceDetail.class);
                startActivity(intent1);
        //smLocalStore = new SMLocalStore(this);
        //         smLocalStore.setSMworkshoplist(workshopid);
       // startActivity(new Intent(ScheduledMaintenanceWorkshoplist.this, ScheduledMaintenanceService.class));
    }
    public void search_workshop_display() {
        recyclerView = (RecyclerView) findViewById(R.id.workshop_rv);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        workshop = new ArrayList<WorkshopDatas>();
        for (int i = 0; i < nameArray.length; i++) {
            Toast.makeText(getApplicationContext(),"coordinateArray[i] :"+coordinateArray[i],Toast.LENGTH_SHORT).show();


            String[] parts = coordinateArray[i].split(",");
            String part1 = parts[0]; // 004
            String part2 = parts[1];

            Toast.makeText(getApplicationContext(),"coordinateArray[i] :"+coordinateArray[i]+"phone :"+phoneArray[i],Toast.LENGTH_LONG).show();

             Toast.makeText(getApplicationContext(),"part a:"+part1+"partb :"+part2,Toast.LENGTH_LONG).show();

            coordLatitude=Double.parseDouble(part1);
            coordLongitude=Double.parseDouble(part2);
            latLng1 = new LatLng(coordLatitude, coordLongitude);
            distance[i]=getDistance(latLng, latLng1);
             Toast.makeText(getApplicationContext(),"distance:"+distance[i],Toast.LENGTH_LONG).show();

            Toast.makeText(getApplicationContext(),"coordLatitude :"+coordLatitude+" coordLongitude :"+coordLongitude,Toast.LENGTH_LONG).show();

            workshop.add(new WorkshopDatas(
                    nameArray[i],
                    addressArray[i],
                    phoneArray[i],
                    categoryArray[i],
                    profilepicArray[i],
                    workshopidArray[i],
                    ratingArray[i],distance[i]
            ));
        }

        //    removedItems = new ArrayList<Integer>();

        adapter = new SMAdapter(workshop);
        recyclerView.setAdapter(adapter);
    }

    public String getDistance(LatLng origin, LatLng dest) {
        Location l1=new Location("One");
        l1.setLatitude(origin.latitude);
        l1.setLongitude(origin.longitude);

        Location l2=new Location("Two");
        l2.setLatitude(dest.latitude);
        l2.setLongitude(dest.longitude);

        float distance=l1.distanceTo(l2);
        String dist=distance+" M";

        if(distance>1000.0f)
        {
            distance=distance/1000.0f;


            DecimalFormat df = new DecimalFormat("#.##");
            double di = Double.valueOf(df.format(distance));

            dist=di+" KM";
        }
        return dist;

    }
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.horn.workshop/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    private void handleNewLocation(Location location) {
        //  Log.d(TAG, location.toString());

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        smLocalStore=new SMLocalStore(this);

        smLocalStore.setSmwCurrentLatlng(""+currentLatitude,""+currentLongitude);

        latLng = new LatLng(currentLatitude, currentLongitude);


        //latLng = new LatLng(8.499586,76.959026);

        Log.d("latttttttttttt",""+currentLatitude);
        Log.d("lnggggggggggg ",""+currentLongitude);

       // Toast.makeText(getApplicationContext(),"lat:"+currentLatitude,Toast.LENGTH_LONG).show();
       // Toast.makeText(getApplicationContext(),"log:"+currentLongitude,Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            search_workshop();
            return true;
        }else if(id ==R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void search_workshop() {
        pDialog.show();
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
                        distance=new String[coordinateArrayj.length()];

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
                        search_workshop_display();
                        pDialog.dismiss();
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
                        pDialog.dismiss();
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

    @Override
    public void onConnected(Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }
        else {
            handleNewLocation(location);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }
    /*
    *Datas from db ends
     */
}

