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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import app.AppConfig;
import app.AppController;

/**
 * Created by Sariga on 1/5/2016.
 */
public class ScheduledMaintenanceService extends AppCompatActivity {

    private TextView labour_cost, toatl_cost,washing_cost;
    private Button search_wrkshp_btn;
    private LinearLayout labr_cost_lyt, total_cost_tyt,washing_cost_lyt;
    private View hLine1, hLine2;
    float price_total = 0, total = 0;
    public String[] sm_service_listprice = new String[30];
    public String[] sm_service_list = new String[30];
    public String[] sm_service_listqty = new String[30];
    public String labour_Charge,washing;
    public String service_list;


    String provider;
    private static final String TAG = "SM_service";
    String strreqTAG = "ServiceReqTAG";
    private SMLocalStore smLocalStore;
    public ProgressDialog pDialog;
    public ArrayList<String> selectedStrings = new ArrayList<String>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_maintenance_services);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        hLine2 = findViewById(R.id.hori_2);
        total_cost_tyt = (LinearLayout) findViewById(R.id.total_layout);
        washing_cost_lyt = (LinearLayout) findViewById(R.id.washing_layout);
        /*
        * fetch the service list from db
        */
        pDialog.show();
        SM_Services();
       /*
        *
        */

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    public void SM_Services_display() {
        pDialog.dismiss();
        labr_cost_lyt.setVisibility(View.VISIBLE);
        total_cost_tyt.setVisibility(View.VISIBLE);
        washing_cost_lyt.setVisibility(View.VISIBLE);
        search_wrkshp_btn.setVisibility(View.VISIBLE);
        hLine1.setVisibility(View.VISIBLE);
        hLine2.setVisibility(View.VISIBLE);
        //sm_service_list = new String[]{"Engine Oil", "Oil filter", "Spark Plug(P)"};
        //sm_service_listqty = new String[]{"1", "1", "1"};
        // sm_service_listprice = new String[]{"100", "150", "95"};
        TableLayout ll = (TableLayout) findViewById(R.id.sm_service_list_table);
        TableRow rowh = new TableRow(this);
        TableRow.LayoutParams lph = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        rowh.setLayoutParams(lph);
        TextView priceh = new TextView(this);
        TextView qtyh = new TextView(this);
        TextView sericeh = new TextView(this);
        sericeh.setText("Services");
//        qtyh.setWidth(200);
//        priceh.setWidth(100);
//        sericeh.setWidth(400);
        sericeh.setGravity(Gravity.CENTER_VERTICAL);
        qtyh.setText("Quantity");
        priceh.setText("Price");
        rowh.addView(sericeh);
        rowh.addView(qtyh);
        rowh.addView(priceh);
        rowh.setMinimumHeight(100);
        ll.addView(rowh, 0);
        CheckBox[] check = new CheckBox[sm_service_list.length]; //maxCount is the number of rows in the database.
        price_total += Float.parseFloat(labour_Charge);
        price_total += Float.parseFloat(washing);
        for (int i = 0; i < sm_service_list.length; i++) {
            try{
                TableRow row = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setMinimumHeight(110);
                row.setLayoutParams(lp);
                Log.d(TAG, "qty:" + sm_service_listprice[i]);

                price_total += Float.valueOf(sm_service_listprice[i]);
                final float price_tot = Float.valueOf(sm_service_listprice[i]);



                check[i] = new CheckBox(getApplicationContext()); //con is Context class passed as argument.
                check[i].setText(Integer.toString(i));
                check[i].setId(100 + i);
                check[i].setText(sm_service_list[i]);
                check[i].setTextColor(Color.parseColor("#000000"));
                check[i].setChecked(true);
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
                price.setPadding(10, 10, 10, 10);
                qty.setPadding(10, 10, 10, 10);
                row.addView(qty);
                row.addView(price);
                row.setGravity(View.TEXT_ALIGNMENT_CENTER);
                ll.addView(row, i + 1);
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
                //  Intent launchActivity1 = new Intent(ScheduledMaintenanceService.this, ScheduledMaintenanceWorkshoplist.class);

                String labour = labour_cost.getText().toString();
                String total = toatl_cost.getText().toString();
                String selectedstring = selectedStrings.toString();
                smLocalStore = new SMLocalStore(ScheduledMaintenanceService.this);

                smLocalStore.setSMservice(labour, total, selectedstring);
                // Toast.makeText(ScheduledMaintenanceService.this,"here gps",Toast.LENGTH_LONG).show();



                provider = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


                final Dialog dialog = new Dialog(ScheduledMaintenanceService.this);
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("Choose location from ?");

                // set the custom dialog components - text, image and button
                Button btn = (Button) dialog.findViewById(R.id.ok);
                Button btn1 = (Button) dialog.findViewById(R.id.cancel);
                // text.setText("Android custom dialog example!");
                // ImageView image = (ImageView) dialog.findViewById(R.id.image);
                //image.setImageResource(R.drawable.ic_launcher);

                // if button is clicked, close the custom dialog
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RadioGroup radioGroup= (RadioGroup) dialog.findViewById(R.id.rg);

                        int selectedId = radioGroup.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);

                        if(radioButton.getText().equals("Current Location"))
                        {
                            Toast.makeText(ScheduledMaintenanceService.this,
                                    radioButton.getText(), Toast.LENGTH_SHORT).show();
                            if(!provider.equals("")){
                                //GPS Enabled
                                startActivity(new Intent(ScheduledMaintenanceService.this, ScheduledMaintenanceWorkshoplist.class));
                            }else{

                                AlertDialog.Builder builder = new AlertDialog.Builder(ScheduledMaintenanceService.this);
                                builder.setTitle("Horn");
                                builder.setMessage("Gps is not enabled.Click Ok to enable");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                                   Toast.makeText(MainActivity.this, "hgsadfasdf", Toast.LENGTH_LONG).show();
                                    }
                                })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.show();

                            }
                        }
                        else if(radioButton.getText().equals("Choose manually"))
                        {
                            Toast.makeText(ScheduledMaintenanceService.this,
                                    radioButton.getText(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(ScheduledMaintenanceService.this,PlacesAutoCompleteActivity.class));

                        }

                        dialog.dismiss();

                    }
                });
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

                dialog.show();


                /*if(!provider.equals("")){
                    //GPS Enabled
                    startActivity(new Intent(ScheduledMaintenanceService.this, ScheduledMaintenanceWorkshoplist.class));
                }else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(ScheduledMaintenanceService.this);
                    builder.setTitle("Horn");
                    builder.setMessage("Gps is not enabled.Click Ok to enable");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                                   Toast.makeText(MainActivity.this, "hgsadfasdf", Toast.LENGTH_LONG).show();
                        }
                    })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();

                }*/


            }
        });


    }

    public void SM_Services() {

        //final ProgressDialog loading;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SM_SERVICES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SM_Sevice Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        labour_Charge = jsonObject.getString("labour");
                        washing = jsonObject.getString("washing");
                        JSONArray service_list_qty = jsonObject.getJSONArray("service_list_qty");
                        JSONArray service_list_price = jsonObject.getJSONArray("service_list_price");
                        JSONArray service_list_js = jsonObject.getJSONArray("services");
                        //JSONArray service_labour = jsonObject.getJSONArray("labour");
                        //String service_list = jsonObject.getString("service_list");
                        String varient = smLocalStore.getSMhome_varient();
//                        String petrol = "1";
//                        String diesel = "2";
//                        String NA = "NA";
//                        if (varient.equals(petrol)) {
//                            labour_Charge = labour_Charge.split(",")[0];
//                            if (labour_Charge.equals(NA)) {
//                                labour_Charge = "0";
//                                no_service();
//                            }
//                        } else if (varient.equals(diesel)) {
//                            labour_Charge = labour_Charge.split(",")[1];
//                            if (labour_Charge.equals(NA)) {
//                                labour_Charge = "0";
//                                no_service();
//                            }
//                        }
                        // String[] testt = service_list.split(",");
                        sm_service_listprice = new String[service_list_qty.length()];
                        sm_service_list = new String[service_list_qty.length()];
                        sm_service_listqty = new String[service_list_qty.length()];
                        for (int i = 0; i < service_list_qty.length(); i++) {
                            sm_service_listqty[i] = service_list_qty.getString(i);
                            sm_service_listprice[i] = service_list_price.getString(i).toString();
                            sm_service_list[i] = service_list_js.getString(i);

                        }
                        SM_Services_display();
//                                             for(int i=0;i<service_list.length();i++)
//                                             {
//                                                 sm_service_listqty[i] = testt[i].split("|")[0];
//                                                 sm_service_listprice[i] = testt[i].split("|")[1];
//                                             }
                        //  Log.d(TAG,"qty:"+sm_service_listqty[2]);
                        //      Log.d(TAG,"qty:"+sm_service_list);
                        //   Log.d(TAG,"price:"+sm_service_listprice);
//                        sm_service_list = new String[]{"Engine Oil", "Oil filter", "Spark Plug(P)", "Air Filter(D)", "Brake Fluid", "Fuel Filter(P)",
//                                "Fuel Filter(D)", "Clutch Plate", "Pressure Plate", "Clutch Bearings", "Ft Suspension(L)", "Ft Suspension(R)", "Rear Suspension(L)",
//                                "Rear Suspension(R)", "Steering Ball Joint(L)", "Steering Ball Joint(R)", "Lower Arm(L)", "Lower Arm(R)", "Starter Motor", "Alternatoe Assy", "Radiator", "Condensor"};
                        // if(varient.equals(petrol))
                        // {
//                        String var_petrol = "P";
//                        String var_diesel = "D";
//                        for (int i = 0; i < sm_service_list.length; i++) {
//                            String sm_service_list_varient = sm_service_list[i].substring(sm_service_list[i].length() - 3);
//                            sm_service_list_varient = sm_service_list_varient.replaceAll("[-+.^:,()]", "");
//                            if (varient.equals(petrol)) {
//                                sm_service_list = new String[]{"Engine Oil", "Oil filter", "Spark Plug(P)", "Brake Fluid", "Fuel Filter(P)",
//                                        "Clutch Plate", "Pressure Plate", "Clutch Bearings", "Ft Suspension(L)", "Ft Suspension(R)", "Rear Suspension(L)",
//                                        "Rear Suspension(R)", "Steering Ball Joint(L)", "Steering Ball Joint(R)", "Lower Arm(L)", "Lower Arm(R)", "Starter Motor", "Alternatoe Assy", "Radiator", "Condensor"};
//                            } else if (varient.equals(diesel)) {
//                                sm_service_list = new String[]{"Engine Oil", "Oil filter", "Air Filter(D)", "Brake Fluid", "Fuel Filter(D)", "Clutch Plate", "Pressure Plate",
//                                        "Clutch Bearings", "Ft Suspension(L)", "Ft Suspension(R)", "Rear Suspension(L)", "Rear Suspension(R)", "Steering Ball Joint(L)",
//                                        "Steering Ball Joint(R)", "Lower Arm(L)", "Lower Arm(R)", "Starter Motor", "Alternatoe Assy", "Radiator", "Condensor"};
//                            }
//                        }
                        //      }



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
                        Toast.makeText(ScheduledMaintenanceService.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(ScheduledMaintenanceService.this);
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

        AppController.getInstance().addToRequestQueue(stringRequest, strreqTAG);
    }

    public void no_service() {
        pDialog.dismiss();
        String varient_error = smLocalStore.getSMhome_varient();
        String km_error = smLocalStore.getSMhome_kms();
        //  String varient_check = "1";
        //  varient_error = (varient_error.equals(varient_check)) ? "Petrol" : "Diesel";
        String vehicle_error = smLocalStore.getSMhome_vehicle();
        new AlertDialog.Builder(ScheduledMaintenanceService.this)
                .setMessage("No Service is available for " + vehicle_error + " for " + km_error)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ScheduledMaintenanceService.this, ScheduledMaintenanaceHome.class));
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ScheduledMaintenanceService Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.horn.workshop/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ScheduledMaintenanceService Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.horn.workshop/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
 /*   @Override
    public void onRestart()
    {
        super.onRestart();
        startActivity(getIntent());
    }*/


}