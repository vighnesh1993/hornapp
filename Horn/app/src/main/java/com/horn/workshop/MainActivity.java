package com.horn.workshop;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.pkmmte.view.CircularImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import activity.ChoiceLogin;
import activity.MyCars;
import activity.MyProfile;
import app.AppController;
import helper.ServicesManager;
import helper.SQLiteHandler;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private TextView loggedUser, nav_name, nav_email;
    private Button logout;
    private UserLocalStore userLocalStore;
    private SQLiteHandler sqLiteHandler;
    private ImageView mProfileImage;
    public GoogleApiClient mGoogleApiClient;
    private CircularImageView circularImageView;
    private RecyclerView mRecyclerView;
    private ServicesAdapter mAdapter;
    private SMLocalStore smLocalStore;
    private LocationRequest mLocationRequest;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    private LatLng latLng;
    private android.support.v7.app.ActionBar ab;
    String subLocality = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbar.setSubtitle("sub-title");
        smLocalStore = new SMLocalStore(MainActivity.this);
        userLocalStore = new UserLocalStore(this);
        userLocalStore.setMylocationLatlog(userLocalStore.getMyManuallocationLatlog());

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(Color.BLACK);
        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                fab.getDrawable().mutate().setTint(Color.WHITE);
                Snackbar.make(view, "" +
                        "Hey! I am Emergency Coming Soon.. :)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        ab = getSupportActionBar();
        String mLocation = userLocalStore.getManualLocation();
        ab.setSubtitle(mLocation);


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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_main2, null);
        nav_name = ((TextView) nav_header.findViewById(R.id.nav_name));
        nav_email = ((TextView) nav_header.findViewById(R.id.nav_email));
        mProfileImage = ((ImageView) nav_header.findViewById(R.id.profile_picture));
        circularImageView = ((CircularImageView) nav_header.findViewById(R.id.circularImage));
        navigationView.addHeaderView(nav_header);


        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ServicesAdapter(ServicesManager.getInstance().getServices(), R.layout.activity_main_content, this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (position == 0) {
                            Intent intent1 = new Intent(MainActivity.this, ScheduledMaintenanceScreen.class);
                            startActivity(intent1);
                            //Toast.makeText(getApplicationContext(),"Scheduled Maintenance : " +position,Toast.LENGTH_LONG).show();
                        } else if (position == 1) {
                            Toast.makeText(getApplicationContext(), "Running Maintenance : " + position, Toast.LENGTH_LONG).show();
                        } else if (position == 2) {
                            //Toast.makeText(getApplicationContext(), "Body and Painting : " + position, Toast.LENGTH_LONG).show();
                            smLocalStore.clearBpimages();
                            smLocalStore.clrbpDescription();
                            Intent in = new Intent(MainActivity.this, BodyPaintingHome.class);
                            in.putExtra("value", "1");
                            startActivity(in);
                        } else if (position == 3) {
                            Toast.makeText(getApplicationContext(), "Value Added Services : " + position, Toast.LENGTH_LONG).show();
                        } else if (position == 4) {
                            Toast.makeText(getApplicationContext(), "Others : " + position, Toast.LENGTH_LONG).show();
                        }

                    }
                })
        );
        sqLiteHandler = new SQLiteHandler(this);

        if (userLocalStore.getGoogleUserLoggedIn()) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }


    }

    private void handleNewLocation(Location location) {
        //  Log.d(TAG, location.toString());


        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
       // Toast.makeText(getApplicationContext(), "userLocalStore.getMyManuallocationLatlog() : " + userLocalStore.getMyManuallocationLatlog(), Toast.LENGTH_LONG).show();

        if(userLocalStore.getMyManuallocationLatlog().equals("")) {
            userLocalStore.setMylocationLatlog(currentLatitude + "," + currentLongitude);
        }
        else
        {
            userLocalStore.setMylocationLatlog(userLocalStore.getMyManuallocationLatlog());
        }
        smLocalStore.setSmwCurrentLatlng("" + currentLatitude, "" + currentLongitude);

        latLng = new LatLng(currentLatitude, currentLongitude);

        //String mLocation = userLocalStore.getManualLocation();
        // if (mLocation.equals("")) {
        getCurrentLocation(currentLatitude, currentLongitude);
        //}

    }

    public void onConnected(Bundle bundle) {
        mUpdateLocation();
    }


    private void mUpdateLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userLocalStore.getFBUserLoggedIn()) {
            setFBUserProfile();
        } else if (userLocalStore.getGoogleUserLoggedIn()) {
            setGoogleUserProfile();
        } else if (userLocalStore.getUserLoggedIn()) {
            setUserProfile();
        } else if (userLocalStore.getGuestUserLoggedIn()) {
            setGuestUserProfile();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_logout) {
            if (userLocalStore.getFBUserLoggedIn()) {
                userFBLogout();
                return true;
            } else if (userLocalStore.getGoogleUserLoggedIn()) {
                signOut();
                return true;
            } else if (userLocalStore.getUserLoggedIn()) {
                userLogout();
                return true;
            } else if (userLocalStore.getGuestUserLoggedIn()) {
                guestUserLogout();
            }
        } else if (id == R.id.action_choose_loc) {

            userLocalStore.setindicator(true);
            startActivity(new Intent(MainActivity.this, PlacesAutoCompleteActivity.class));
        } /*else if (id == R.id.action_compass) {
            userLocalStore.setManualLocation("");
            userLocalStore.setMyManuallocationLatlog("");
            String sLatLong = userLocalStore.getGPSLatlong();
            mUpdateLocation();
            if (getGpsStatus()) {
                userLocalStore.setMyManuallocationLatlog("");
                userLocalStore.setManualLocation("");
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);

            }
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, MyProfile.class));
        } else if (id == R.id.nav_cars) {
            startActivity(new Intent(MainActivity.this, MyCars.class));
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_privacy) {

        } else if (id == R.id.nav_terms) {

        } else if (id == R.id.nav_appoinment) {
            startActivity(new Intent(this, ProfileMyAppoinmentList.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        if (!getGpsStatus()) {


            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }

            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            try {
                if (!location.equals("")) {

                    userLocalStore = new UserLocalStore(this);
                    handleNewLocation(location);
                }
                else {

                }

            }
            catch(Exception e)
            {

            }
        }


    }


    private boolean getGpsStatus() {

        String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private void userLogout() {
        startActivity(new Intent(this, ChoiceLogin.class));
        finish();
        Toast.makeText(getApplicationContext(), R.string.horn_logout, Toast.LENGTH_LONG).show();
        userLocalStore.setUserLoggedIn(false);
        sqLiteHandler.deleteUsers();
        userLocalStore.setManualLocationChoosen(false);
    }

    private void guestUserLogout() {
        startActivity(new Intent(this, ChoiceLogin.class));
        finish();
        Toast.makeText(getApplicationContext(), R.string.horn_logout, Toast.LENGTH_LONG).show();
        userLocalStore.setGuestUserLoggedIn(false);
        sqLiteHandler.deleteUsers();
        userLocalStore.setManualLocationChoosen(false);
    }

    private void userFBLogout() {
        startActivity(new Intent(this, ChoiceLogin.class));
        finish();
        Toast.makeText(getApplicationContext(), R.string.fb_logedout, Toast.LENGTH_LONG).show();
        LoginManager.getInstance().logOut();
        userLocalStore.setFBUserLoggedIn(false);
        userLocalStore.setManualLocationChoosen(false);
    }

    private void signOut() {
        startActivity(new Intent(this, ChoiceLogin.class));
        finish();
        userLocalStore.setGoogleUserLoggedIn(false);
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        Toast.makeText(getApplicationContext(), R.string.goo_signedout, Toast.LENGTH_LONG).show();
        mGoogleApiClient.disconnect();
        mGoogleApiClient.connect();
        userLocalStore.setManualLocationChoosen(false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), R.string.goo_conne_failed, Toast.LENGTH_LONG).show();
    }

    private void setUserProfile() {
        circularImageView.setVisibility(View.GONE);
        HashMap<String, String> user = sqLiteHandler.getUserDetails();
        String name = user.get("name");
        String email = user.get("email");
        nav_name.setText(name);
        nav_email.setText(email);
        String lName = name.toLowerCase();
        char alphabet = lName.charAt(0);
        setProfilePictureWithAlphabet(alphabet);
    }

    private void setProfilePictureWithAlphabet(char alphabet) {
        char a = 'a', b = 'b', c = 'c', d = 'd', e = 'e', f = 'f', g = 'g', h = 'h', i = 'i', j = 'j', k = 'k', l = 'l', m = 'm',
                n = 'n', o = 'o', p = 'p', q = 'q', r = 'r', s = 's', t = 't', u = 'u', v = 'v', w = 'w', x = 'x', y = 'y', z = 'z';
        if (alphabet == a) {
            mProfileImage.setImageResource(R.drawable.a);
        } else if (alphabet == b) {
            mProfileImage.setImageResource(R.drawable.b);
        } else if (alphabet == c) {
            mProfileImage.setImageResource(R.drawable.c);
        } else if (alphabet == d) {
            mProfileImage.setImageResource(R.drawable.d);
        } else if (alphabet == e) {
            mProfileImage.setImageResource(R.drawable.e);
        } else if (alphabet == f) {
            mProfileImage.setImageResource(R.drawable.f);
        } else if (alphabet == g) {
            mProfileImage.setImageResource(R.drawable.g);
        } else if (alphabet == h) {
            mProfileImage.setImageResource(R.drawable.h);
        } else if (alphabet == i) {
            mProfileImage.setImageResource(R.drawable.i);
        } else if (alphabet == j) {
            mProfileImage.setImageResource(R.drawable.j);
        } else if (alphabet == k) {
            mProfileImage.setImageResource(R.drawable.k);
        } else if (alphabet == l) {
            mProfileImage.setImageResource(R.drawable.l);
        } else if (alphabet == m) {
            mProfileImage.setImageResource(R.drawable.m);
        } else if (alphabet == n) {
            mProfileImage.setImageResource(R.drawable.n);
        } else if (alphabet == o) {
            mProfileImage.setImageResource(R.drawable.o);
        } else if (alphabet == p) {
            mProfileImage.setImageResource(R.drawable.p);
        } else if (alphabet == q) {
            mProfileImage.setImageResource(R.drawable.q);
        } else if (alphabet == r) {
            mProfileImage.setImageResource(R.drawable.r);
        } else if (alphabet == s) {
            mProfileImage.setImageResource(R.drawable.s);
        } else if (alphabet == t) {
            mProfileImage.setImageResource(R.drawable.t);
        } else if (alphabet == u) {
            mProfileImage.setImageResource(R.drawable.u);
        } else if (alphabet == v) {
            mProfileImage.setImageResource(R.drawable.v);
        } else if (alphabet == w) {
            mProfileImage.setImageResource(R.drawable.w);
        } else if (alphabet == x) {
            mProfileImage.setImageResource(R.drawable.x);
        } else if (alphabet == y) {
            mProfileImage.setImageResource(R.drawable.y);
        } else {
            mProfileImage.setImageResource(R.drawable.z);
        }

    }

    private void setFBUserProfile() {
        HashMap<String, String> user = sqLiteHandler.getSocialUserDetails();
        String name = user.get("name");
        String email = user.get("email");
        nav_name.setText(name);
        nav_email.setText(email);

        mProfileImage.setVisibility(View.GONE);
        String url = userLocalStore.getFBuserProfileUrl();
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    circularImageView.addShadow();
                    circularImageView.setImageBitmap(response.getBitmap());
                }
            }
        });

    }

    private void setGoogleUserProfile() {
        HashMap<String, String> user = sqLiteHandler.getSocialUserDetails();
        String name = user.get("name");
        String email = user.get("email");
        nav_name.setText(name);
        nav_email.setText(email);

        mProfileImage.setVisibility(View.GONE);
        String url = userLocalStore.getGoogleUserProfileUrl();
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    circularImageView.addShadow();
                    circularImageView.setImageBitmap(response.getBitmap());
                }
            }
        });

    }

    private void setGuestUserProfile() {
        circularImageView.setVisibility(View.GONE);
        HashMap<String, String> guestUser = userLocalStore.getGuestUserDetails();
        String gstUserName = guestUser.get("name");
        String gstUserEmail = guestUser.get("email");
        nav_name.setText(gstUserName);
        nav_email.setText(gstUserEmail);

        String lName = gstUserName.toLowerCase();
        char alphabet = lName.charAt(0);
        setProfilePictureWithAlphabet(alphabet);
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

    private void getCurrentLocation(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            subLocality = addresses.get(0).getSubLocality();
            String mLocation = userLocalStore.getManualLocation();
            if(userLocalStore.getMyManuallocationLatlog().equals("")) {
                userLocalStore.setMylocationLatlog(latitude+","+longitude);
            }
            else
            {
                userLocalStore.setMylocationLatlog(userLocalStore.getMyManuallocationLatlog());
            }

           // Toast.makeText(getApplicationContext(),"manualLocation  "+userLocalStore.getMyManuallocationLatlog(),Toast.LENGTH_SHORT).show();
            if(mLocation.equals(""))
            {
                ab.setSubtitle(subLocality);
            }
            else
            {
                ab.setSubtitle(mLocation);
            }
            userLocalStore.setGPSLatlong(""+latitude+","+longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}