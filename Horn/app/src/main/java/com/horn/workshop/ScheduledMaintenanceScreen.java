package com.horn.workshop;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import activity.AddCar;
import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;

/**
 * Created by Sariga on 2/16/2016.
 */
public class ScheduledMaintenanceScreen extends AppCompatActivity {
    /*
    for SM home
     */
    private SMLocalStore smLocalStore;
    public String varient;
    public String make ;
    public String model;
    public String[] sm_service;
    String [] nameArray,carImageArray,carIdArray,carMakeArray,carModelArray;
    private static final String TAG = "SM_homekm";
    String strreqTAG = "KmReqTAG";
    SQLiteHandler sqLiteHandler;
    ProgressDialog pDialog;
    String strreq = "req";

    /*
    for SM services
     */
    private TextView labour_cost, toatl_cost,washing_cost;
    private Button search_wrkshp_btn;
    private LinearLayout labr_cost_lyt, total_cost_tyt,washing_cost_lyt;
    private View hLine1, hLine2;
    float price_total = 0, total = 0;
    public String[] sm_service_listprice = new String[30];
    public String[] sm_service_list = new String[30];
    public String[] sm_service_listqty = new String[30];
    public String[] sm_service_listfield = new String[30];
    public String labour_Charge,washing;
    public String service_list;


    String provider;
    private static final String TAG1 = "SM_service";
    String strreqTAG1 = "ServiceReqTAG";
    public ArrayList<String> selectedStrings = new ArrayList<String>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_maintenance_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      /*
      SM home starts
       */
        UserLocalStore userLocalStore=new UserLocalStore(this);
        String mylatlog=userLocalStore.getMylocationLatlog();
      //  Toast.makeText(getApplicationContext(),"mylatlog: "+mylatlog,Toast.LENGTH_LONG).show();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loding...");
        pDialog.setCancelable(false);
       dropdown_data();

        /*
      SM home ends
       */
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blank_menu, menu);
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
    public void dropdown_data()
    {
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
                            nameArray = new String[nameArrayj.length()];
                            carImageArray = new String[carImageArrayj.length()];
                            carIdArray = new String[carIdArrayj.length()];
                            carMakeArray = new String[carMakeArrayj.length()];
                            carModelArray = new String[carModelArrayj.length()];
                            for (int i = 0; i < nameArrayj.length(); i++) {
                                nameArray[i] = nameArrayj.getString(i);
                                carImageArray[i] = carImageArrayj.getString(i);
                                carIdArray[i] = carIdArrayj.getString(i);
                                carMakeArray[i] = carMakeArrayj.getString(i);
                                carModelArray[i] = carModelArrayj.getString(i);
                            }
                            pDialog.dismiss();
                            dropdown_display();
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
                        Toast.makeText(ScheduledMaintenanceScreen.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                String get_mycars = "get_mycars";
                sqLiteHandler = new SQLiteHandler(ScheduledMaintenanceScreen.this);
                HashMap<String, String> user = sqLiteHandler.getUserDetails();
                String  apmnt_user_email = user.get("email");
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("get_mycars", get_mycars);
                params.put("get_mycars_email", apmnt_user_email);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);

    }

    public void dropdown_display()
    {
        /** SM vehicle dropdown **/

        Spinner dropdown = (Spinner) findViewById(R.id.vehicle);
        String[] items = nameArray;//new String[]{"Etios", "Innova", "Fortuner"};
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
                                sm_service = new String[km_list.length()];
                                for (int i = 0; i < km_list.length(); i++) {
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
                                Toast.makeText(ScheduledMaintenanceScreen.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        smLocalStore = new SMLocalStore(ScheduledMaintenanceScreen.this);
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
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            myButton.setHeight(100);
            myButton.setTextSize(15);
            //  myButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_room_black_48dp, 0, 0, 0);
            ll.addView(myButton, lp);
            final String service = sm_service[i];
            myButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    smLocalStore = new SMLocalStore(ScheduledMaintenanceScreen.this);
                    Spinner spinner = (Spinner) findViewById(R.id.vehicle);
                    //String vehicle = spinner.getSelectedItem().toString();
                    String vehicle = "VXI";
                    smLocalStore.setSMhome(vehicle, service, varient, make, model);
LinearLayout smservice = (LinearLayout) findViewById(R.id.SM_services);

                    smservice.setVisibility(View.VISIBLE);

                    sm_services();
                }
            });
        }

    }
    public void sm_services()
    {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");

        labour_cost = (TextView) findViewById(R.id.labour_cost_value);
        washing_cost = (TextView) findViewById(R.id.washing_cost_value);
        toatl_cost = (TextView) findViewById(R.id.total_cost_value);
        search_wrkshp_btn = (Button) findViewById(R.id.search_wrkshop_btn);
        labr_cost_lyt = (LinearLayout) findViewById(R.id.labour_layout);
        total_cost_tyt = (LinearLayout) findViewById(R.id.total_layout);
        hLine1 = findViewById(R.id.hori_1);
        //hLine2 = findViewById(R.id.hori_2);
        total_cost_tyt = (LinearLayout) findViewById(R.id.total_layout);
        washing_cost_lyt = (LinearLayout) findViewById(R.id.washing_layout);
        /*
        * fetch the service list from db
        */
        pDialog.show();
        SM_Services();
    }
    public void SM_Services_display() {
        pDialog.dismiss();
        labr_cost_lyt.setVisibility(View.VISIBLE);
        total_cost_tyt.setVisibility(View.VISIBLE);
        washing_cost_lyt.setVisibility(View.VISIBLE);
        search_wrkshp_btn.setVisibility(View.VISIBLE);
        hLine1.setVisibility(View.VISIBLE);
       // hLine2.setVisibility(View.VISIBLE);
        TableLayout ll = (TableLayout) findViewById(R.id.sm_service_list_table);
        TableLayout ll2 = (TableLayout) findViewById(R.id.sm_service_list_table1);
        ll.removeAllViews();
        ll2.removeAllViews();
        TableRow rowh = new TableRow(this);
        TableRow.LayoutParams lph = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        rowh.setLayoutParams(lph);
        TextView priceh = new TextView(this);
        TextView qtyh = new TextView(this);
        TextView sericeh = new TextView(this);
        sericeh.setText("Services");
        qtyh.setPadding(15, 0, 15, 0);
        priceh.setPadding(15, 0, 15, 0);
        sericeh.setPadding(15, 0, 15, 0);
        qtyh.setTextSize(15);
        priceh.setTextSize(15);
        sericeh.setTextSize(15);
        qtyh.setTextColor(Color.BLACK);
        priceh.setTextColor(Color.BLACK);
        sericeh.setTextColor(Color.BLACK);
        sericeh.setGravity(Gravity.CENTER_VERTICAL);
        qtyh.setText("Quantity");
        priceh.setText("Price");
        rowh.addView(sericeh);
        TextView I = (TextView) findViewById(R.id.inspect);


        rowh.addView(qtyh);
        rowh.addView(priceh);
        rowh.setMinimumHeight(100);
        ll.addView(rowh, 0);
        price_total = 0;
        total = 0;
        CheckBox[] check = new CheckBox[sm_service_list.length]; //maxCount is the number of rows in the database.
        price_total += Float.parseFloat(labour_Charge);
        price_total += Float.parseFloat(washing);

        int j=0;
        int inspect = 0;
        int replace = 0;
        for (int i = 0; i < sm_service_list.length; i++) {
            try{
                if((sm_service_listfield[i]).equals("I")) {
                    inspect++;
                    if(inspect==1)
                    {
                        I.setText("INSPECTION");
                        I.setTextSize(15);
                        I.setTextColor(Color.BLACK);
                    }
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setMinimumHeight(110);
                    row.setLayoutParams(lp);
                    Log.d(TAG1, "qty:" + sm_service_listprice[i]);

                    price_total += Float.valueOf(sm_service_listprice[i]);
                    final float price_tot = Float.valueOf(sm_service_listprice[i]);


                    check[i] = new CheckBox(getApplicationContext()); //con is Context class passed as argument.
                    check[i].setText(Integer.toString(i));
                    check[i].setId(100 + i);
                    check[i].setText(sm_service_list[i]);
                    check[i].setTextColor(Color.parseColor("#000000"));
                    check[i].setChecked(true);
                    check[i].setMaxWidth(430);
                    check[i].setMinWidth(430);
                    row.addView(check[i]);
                    check[i].setId(100 + i);
                    selectedStrings.add(check[i].getText().toString());
                    final String check_selected = check[i].getText().toString();
                    check[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (!buttonView.isChecked()) {
                                price_total -= price_tot;
                                selectedStrings.remove(check_selected);
                            } else {
                                selectedStrings.add(check_selected);
                                price_total += price_tot;

                            }
                            //price_total = price_total;
                            toatl_cost.setText("INR " + price_total);
                        }
                    });

                    TextView price = new TextView(this);
                    TextView qty = new TextView(this);
                    qty.setText(sm_service_listqty[i]);
                    price.setText(sm_service_listprice[i]);
                    price.setPadding(15, 10, 15, 10);
                    qty.setPadding(15, 10, 15, 10);
                    row.addView(qty);
                    row.addView(price);
                    row.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    ll.addView(row, j + 1);
                    j++;
                }
            }catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
int k=0;
        for (int i = 0; i < sm_service_list.length; i++) {
            try{


                if((sm_service_listfield[i]).equals("R")) {
                    replace++;
                    if(replace==1)
                    {
                        TextView R1 = (TextView)findViewById(R.id.replacement);
                        R1.setText("Replacement");
                        R1.setTextSize(15);
                        R1.setTextColor(Color.BLACK);
                        TableRow rowh1 = new TableRow(this);
                        TableRow.LayoutParams lph1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                        rowh.setLayoutParams(lph1);
                        TextView priceh1 = new TextView(this);
                        TextView qtyh1 = new TextView(this);
                        TextView sericeh1 = new TextView(this);
                        sericeh1.setText("Services");
                        qtyh1.setPadding(15, 10, 15, 10);
                        priceh1.setPadding(15, 10, 15, 10);
                        sericeh1.setPadding(15, 10, 15, 10);
                        qtyh1.setTextSize(15);
                        priceh1.setTextSize(15);
                        sericeh1.setTextSize(15);
                        qtyh1.setTextColor(Color.BLACK);
                        priceh1.setTextColor(Color.BLACK);
                        sericeh1.setTextColor(Color.BLACK);
                        sericeh1.setGravity(Gravity.CENTER_VERTICAL);
                        qtyh1.setText("Quantity");
                        priceh1.setText("Price");
                        rowh1.addView(sericeh1);

                        rowh1.addView(qtyh1);
                        rowh1.addView(priceh1);
                        rowh1.setMinimumHeight(100);
                       ll2.addView(rowh1, 0);
                     k++;

                    }
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setMinimumHeight(110);
                    row.setLayoutParams(lp);
                    Log.d(TAG1, "qty:" + sm_service_listprice[i]);

                    price_total += Float.valueOf(sm_service_listprice[i]);
                    final float price_tot = Float.valueOf(sm_service_listprice[i]);


                    check[i] = new CheckBox(getApplicationContext()); //con is Context class passed as argument.
                    check[i].setText(Integer.toString(i));
                    check[i].setId(100 + i);
                    check[i].setText(sm_service_list[i]);
                    check[i].setTextColor(Color.parseColor("#000000"));
                    check[i].setChecked(true);
                    check[i].setMaxWidth(430);
                    check[i].setMinWidth(430);
                    row.addView(check[i]);
                    check[i].setId(100 + i);
                    selectedStrings.add(check[i].getText().toString());
                    final String check_selected = check[i].getText().toString();
                    check[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (!buttonView.isChecked()) {
                                price_total -= price_tot;
                                selectedStrings.remove(check_selected);
                            } else {
                                selectedStrings.add(check_selected);
                                price_total += price_tot;

                            }
                            //price_total = price_total;
                            toatl_cost.setText("INR " + price_total);
                        }
                    });

                    TextView price = new TextView(this);
                    TextView qty = new TextView(this);
                    qty.setText(sm_service_listqty[i]);
                    price.setText(sm_service_listprice[i]);
                    price.setPadding(15, 10, 15, 10);
                    qty.setPadding(15, 10, 15, 10);
                    row.addView(qty);
                    row.addView(price);
                    row.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    ll2.addView(row, k);
                    k++;
                }
            }catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
        total = total + price_total;
        //total = total + Float.parseFloat(labour_Charge)+Float.parseFloat(washing);
        labour_cost.setText("INR " + labour_Charge);
        washing_cost.setText("INR " + washing);
        toatl_cost.setText("INR " + (int) total);

        search_wrkshp_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                 String labour = labour_cost.getText().toString();
                String total = toatl_cost.getText().toString();
                String selectedstring = selectedStrings.toString();
                smLocalStore = new SMLocalStore(ScheduledMaintenanceScreen.this);

                smLocalStore.setSMservice(labour, total, selectedstring);
                EditText descedit = (EditText)findViewById(R.id.sm_suggestion);
                String desc = descedit.getText().toString();
                smLocalStore = new SMLocalStore(ScheduledMaintenanceScreen.this);
                smLocalStore.setSMdesc(desc);

                provider = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

                startActivity(new Intent(ScheduledMaintenanceScreen.this, ScheduledMaintenanceWorkshoplist.class));

                        }
        });


    }

    public void SM_Services() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SM_SERVICES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG1, "SM_Sevice Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        labour_Charge = jsonObject.getString("labour");
                        washing = jsonObject.getString("washing");
                        JSONArray service_list_qty = jsonObject.getJSONArray("service_list_qty");
                        JSONArray service_list_price = jsonObject.getJSONArray("service_list_price");
                        JSONArray service_list_js = jsonObject.getJSONArray("services");
                        JSONArray service_listfield_js = jsonObject.getJSONArray("field");
                         String varient = smLocalStore.getSMhome_varient();
                        sm_service_listprice = new String[service_list_qty.length()];
                        sm_service_list = new String[service_list_qty.length()];
                        sm_service_listqty = new String[service_list_qty.length()];
                        sm_service_listfield = new String[service_listfield_js.length()];
                        for (int i = 0; i < service_list_qty.length(); i++) {
                            sm_service_listqty[i] = service_list_qty.getString(i);
                            sm_service_listprice[i] = service_list_price.getString(i).toString();
                            sm_service_list[i] = service_list_js.getString(i);
                            sm_service_listfield[i] = service_listfield_js.getString(i);

                        }
                        SM_Services_display();
                    }
                    else
                    {
                        no_service();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ScheduledMaintenanceScreen.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(ScheduledMaintenanceScreen.this);
                String km = smLocalStore.getSMhome_kms();
                String vehicle = smLocalStore.getSMhome_vehicle();
                String varient = smLocalStore.getSMhome_varient();
                String make = smLocalStore.getSMhome_make();
                String model = smLocalStore.getSMhome_model();
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vehicle", vehicle);
                params.put("kms", km);
                params.put("varient", varient);
                params.put("make", make);
                params.put("model", model);
                return params;
            }

        };
        AppController.getInstance();
        AppController.getInstance().addToRequestQueue(stringRequest, strreqTAG1);
    }

    public void no_service() {
        pDialog.dismiss();
        String varient_error = smLocalStore.getSMhome_varient();
        String km_error = smLocalStore.getSMhome_kms();
        //  String varient_check = "1";
        //  varient_error = (varient_error.equals(varient_check)) ? "Petrol" : "Diesel";
        String vehicle_error = smLocalStore.getSMhome_vehicle();
        new AlertDialog.Builder(ScheduledMaintenanceScreen.this)
                .setMessage("No Service is available for " + vehicle_error + " for " + km_error)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ScheduledMaintenanceScreen.this, ScheduledMaintenanaceHome.class));
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "ScheduledMaintenanceService Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.horn.workshop/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "ScheduledMaintenanceService Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.horn.workshop/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//
//
//    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }


}



