package com.horn.workshop;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;

/**
 * Created by Admin on 3/7/2016.
 */
public class RunningMaintenanceHome extends AppCompatActivity {
    String [] nameArray,carImageArray,carIdArray,carMakeArray,carModelArray,carVarientArray,carVarientnameArray;
    ProgressDialog pDialog;
    String strreq = "KmReqTAG";
    SQLiteHandler sqLiteHandler;
    String carid_rm,make_rm,model_rm,varient_rm,vehicle_rm;
    static public String id_for_view;
    SMLocalStore smLocalStore;
    String status;
    static public String rm_response;
    String Strreq = "REQTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_home);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Button search_btn = (Button) findViewById(R.id.search_rm);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                smLocalStore = new SMLocalStore(RunningMaintenanceHome.this);

                EditText suggestion = (EditText)findViewById(R.id.rm_suggestion);

                smLocalStore.setRMhome(vehicle_rm, varient_rm, make_rm, model_rm);




                startActivity(new Intent(RunningMaintenanceHome.this, RMWorkshoplist.class));
            }
        });
        display_dropdown();

            }

            void showToast(String msg) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }

            private void setupViewPager(ViewPager viewPager, String carid_rms,String result) {
                id_for_view = carid_rms;
                rm_response = result;
                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                adapter.addFrag(new DummyFragment("wheel",carid_rms), "Wheel");
                adapter.addFrag(new DummyFragment("eletrical",carid_rms), "Electrical");
                adapter.addFrag(new DummyFragment("bs",carid_rms), "BS");
                adapter.addFrag(new DummyFragment("trans",carid_rms), "Transmission");
                adapter.addFrag(new DummyFragment("engine",carid_rms), "Engine");
                adapter.addFrag(new DummyFragment("body",carid_rms), "Body");
                adapter.addFrag(new DummyFragment("suggestion",carid_rms), "Suggestion");
                viewPager.setAdapter(adapter);
            }
            public void display_dropdown()
            {
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADDCARDETAIL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("sdsdsd", "car Response: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                int len = jsonObject.length();
                                String status_car = jsonObject.getString("status");
                                if (status_car.equals("Have_cars")) {

                                    JSONArray nameArrayj = jsonObject.getJSONArray("car_names");
                                    JSONArray carImageArrayj = jsonObject.getJSONArray("car_image");
                                    JSONArray carIdArrayj = jsonObject.getJSONArray("car_id");
                                    JSONArray carMakeArrayj = jsonObject.getJSONArray("car_make");
                                    JSONArray carModelArrayj = jsonObject.getJSONArray("car_model");
                                    JSONArray carVarientArrayj = jsonObject.getJSONArray("car_varient");
                                    JSONArray carVarientnameArrayj = jsonObject.getJSONArray("car_name_varient");
                                    nameArray = new String[nameArrayj.length()];
                                    carImageArray = new String[carImageArrayj.length()];
                                    carIdArray = new String[carIdArrayj.length()];
                                    carMakeArray = new String[carMakeArrayj.length()];
                                    carModelArray = new String[carMakeArrayj.length()];
                                    carVarientArray = new String[carVarientArrayj.length()];
                                    carVarientnameArray = new String[carVarientnameArrayj.length()];
                                    for (int i = 0; i < nameArrayj.length(); i++) {
                                        nameArray[i] = nameArrayj.getString(i);
                                        carImageArray[i] = carImageArrayj.getString(i);
                                        carIdArray[i] = carIdArrayj.getString(i);
                                        carMakeArray[i] = carMakeArrayj.getString(i);
                                        carModelArray[i] = carModelArrayj.getString(i);
                                        carVarientArray[i] = carVarientArrayj.getString(i);
                                        carVarientnameArray[i] = carVarientnameArrayj.getString(i);
                                    }
                                    pDialog.dismiss();
                                    display_vehicle();

                                }
                                else if(status_car.equals("No_cars"))
                                {
                                    pDialog.dismiss();

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
                                pDialog.dismiss();
                                Toast.makeText(RunningMaintenanceHome.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        String get_mycars = "get_mycars";
                        sqLiteHandler = new SQLiteHandler(RunningMaintenanceHome.this);
                        HashMap<String, String> user = sqLiteHandler.getUserDetails();
                        String  apmnt_user_email = user.get("email");
                        // Posting parameters to login url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("get_mycars", get_mycars);
                        params.put("get_mycars_email", apmnt_user_email);
                        return params;
                    }

                };




                AppController.getInstance().addToRequestQueue(stringRequest, Strreq);

            }
    public void display_vehicle()
    {
        final Spinner dropdown = (Spinner) findViewById(R.id.spinner_nav);
        String[] items = nameArray;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);


        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //vehicle_rm = dropdown.getSelectedItem().toString();
                vehicle_rm = carVarientnameArray[position];
                make_rm = carMakeArray[position];
                model_rm = carModelArray[position];
                varient_rm = carVarientArray[position];

                carid_rm = carIdArray[position];
                getTablename(carid_rm);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void getTablename(String carid_rm)
    {
        display_tablayout(carid_rm);
    }
    public void display_tablayout(final String carid_rm)
    {
        getDbdetails(carid_rm, "test", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                final ViewPager viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
                setupViewPager(viewPager, carid_rm,result);
                viewPager.setOffscreenPageLimit(7);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        viewPager.setCurrentItem(tab.getPosition());

                        switch (tab.getPosition()) {
                            case 0:
                                // showToast("One");
                                break;
                            case 1:
                                // showToast("Two");

                                break;
                            case 2:
                                // showToast("Three");
                                break;
                            case 3:
                                //showToast("four");
                                break;
                            case 4:
                                //showToast("five");
                                break;
                            case 5:
                                //showToast("six");
                                break;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        });

    }
    public void getDbdetails(final String carid_rms, String service, final VolleyCallback callback)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_RM_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        status = jsonObject.getString("status");
                        if (status.equals("true")) {

                            callback.onSuccess(response);
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
                        Toast.makeText(RunningMaintenanceHome.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(RunningMaintenanceHome.this);
                // Posting parameters to login url

                Map<String, String> params = new HashMap<String, String>();
                params.put("carid_rm", carid_rms);
                params.put("rm_home", "rm_home");

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest, Strreq);



    }
    public interface VolleyCallback{
        void onSuccess(String result);
    }
            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                getMenuInflater().inflate(R.menu.blank_menu, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case android.R.id.home:
                        finish();
                        return true;
                }
                return super.onOptionsItemSelected(item);
            }

            static class ViewPagerAdapter extends FragmentPagerAdapter {
                private final List<Fragment> mFragmentList = new ArrayList<>();
                private final List<String> mFragmentTitleList = new ArrayList<>();

                public ViewPagerAdapter(FragmentManager manager) {
                    super(manager);
                }

                @Override
                public Fragment getItem(int position) {
                    return mFragmentList.get(position);
                }

                @Override
                public int getCount() {
                    return mFragmentList.size();
                }

                public void addFrag(Fragment fragment, String title) {
                    mFragmentList.add(fragment);
                    mFragmentTitleList.add(title);
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return mFragmentTitleList.get(position);
                }
            }

            public static class DummyFragment extends Fragment {
                SimpleRecyclerAdapter adapter;
                String service;
                public String carid_rma;


                @SuppressLint("ValidFragment")
                public DummyFragment(String service,String carid_rma1) {
                    this.service = service;
                    this.carid_rma = carid_rma1;

                }

                public DummyFragment() {
                }

                @Override
                public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                    View view = inflater.inflate(R.layout.running_services_fragment, container, false);

                    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);
                    recyclerView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.off_white));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setHasFixedSize(true);

                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < VersionModel.data.length; i++) {
                        list.add(VersionModel.data[i]);
                    }

                    final Context context = getContext();
                    adapter = new SimpleRecyclerAdapter(service,id_for_view,rm_response,getContext());
                    recyclerView.setAdapter(adapter);
                    return view;
                }
            }
        }
