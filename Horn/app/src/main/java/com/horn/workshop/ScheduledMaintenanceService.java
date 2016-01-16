package com.horn.workshop;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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

/**
 * Created by Sariga on 1/5/2016.
 */
public class ScheduledMaintenanceService extends AppCompatActivity {
    float price_total = 0, total = 0;
    public String[] sm_service_listprice = new String[30];
    public String[] sm_service_list = new String[30];
    public String[] sm_service_listqty = new String[30];
    public String labour_Charge;
    public String service_list;
    private static final String TAG = "SM_service";
    private SMLocalStore smLocalStore;
    public ProgressDialog pDialog;
    public ArrayList<String> selectedStrings = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_maintenance_services);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");
        /*
        * fetch the service list from db
        */
        pDialog.show();
        SM_Services();
       /*
        *
        */

    }

    public void SM_Services_display() {
        pDialog.dismiss();
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
        // sericeh.setText("Services");
        qtyh.setWidth(200);
        priceh.setWidth(100);
        sericeh.setWidth(400);
        qtyh.setText("Quantity");
        priceh.setText("Price");
        rowh.addView(sericeh);
        rowh.addView(qtyh);
        rowh.addView(priceh);
        ll.addView(rowh, 0);
        final TextView total1 = new TextView(this);
        CheckBox[] check = new CheckBox[sm_service_list.length]; //maxCount is the number of rows in the database.
        price_total += Float.parseFloat(labour_Charge);
        for (int i = 0; i < sm_service_list.length; i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            price_total += Float.parseFloat(sm_service_listprice[i]);
            final float price_tot = Float.parseFloat(sm_service_listprice[i]);

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
                    total1.setText("Total : " + price_total);
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

        }
        total = total + price_total;
        total = total + Float.parseFloat(labour_Charge);
        final TextView price_total1 = new TextView(this);
        price_total1.setText("Labour : " + labour_Charge);
        total1.setText("Total : " + total);
        LinearLayout ll1 = (LinearLayout) findViewById(R.id.sm_service_list);
        ll1.addView(price_total1);
        ll1.addView(total1);
        price_total1.setGravity(View.TEXT_ALIGNMENT_CENTER);
        price_total1.setBottom(300);
        price_total1.setHeight(50);
        total1.setHeight(50);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 70, 0, 0);
        price_total1.setLayoutParams(llp);
        Button sm_button = new Button(this);
        sm_button.setText("Search");
        sm_button.setWidth(800);
        sm_button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ll1.addView(sm_button);
        sm_button.setLayoutParams(llp);
        sm_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //  Intent launchActivity1 = new Intent(ScheduledMaintenanceService.this, ScheduledMaintenanceWorkshoplist.class);

                String labour = price_total1.getText().toString();
                String total = total1.getText().toString();
                String selectedstring = selectedStrings.toString();
                smLocalStore = new SMLocalStore(ScheduledMaintenanceService.this);

                smLocalStore.setSMservice(labour, total, selectedstring);
                startActivity(new Intent(ScheduledMaintenanceService.this, ScheduledMaintenanceWorkshoplist.class));
            }
        });


               /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    public void SM_Services() {
        String strreq = "cancel req";

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
                        JSONArray service_list_qty = jsonObject.getJSONArray("service_list_qty");
                        JSONArray service_list_price = jsonObject.getJSONArray("service_list_price");
                        //String service_list = jsonObject.getString("service_list");
                        String varient = smLocalStore.getSMhome_varient();
                        String petrol = "1";
                        String diesel = "2";
                        String NA = "NA";
                        if (varient.equals(petrol)) {
                            labour_Charge = labour_Charge.split(",")[0];
                            if (labour_Charge.equals(NA)) {
                                labour_Charge = "0";
                                no_service();
                            }
                        } else if (varient.equals(diesel)) {
                            labour_Charge = labour_Charge.split(",")[1];
                            if (labour_Charge.equals(NA)) {
                                labour_Charge = "0";
                                no_service();
                            }
                        }
                        // String[] testt = service_list.split(",");
                        for (int i = 0; i < service_list_qty.length(); i++) {
                            sm_service_listqty[i] = service_list_qty.getString(i);
                            sm_service_listprice[i] = service_list_price.getString(i);
                            //  Log.d(TAG,"qty:"+sm_service_listqty[i]);
                        }
//                                             for(int i=0;i<service_list.length();i++)
//                                             {
//                                                 sm_service_listqty[i] = testt[i].split("|")[0];
//                                                 sm_service_listprice[i] = testt[i].split("|")[1];
//                                             }
                        //  Log.d(TAG,"qty:"+sm_service_listqty[2]);
                        //      Log.d(TAG,"qty:"+sm_service_list);
                        //   Log.d(TAG,"price:"+sm_service_listprice);
                        sm_service_list = new String[]{"Engine Oil", "Oil filter", "Spark Plug(P)", "Air Filter(D)", "Brake Fluid", "Fuel Filter(P)",
                                "Fuel Filter(D)", "Clutch Plate", "Pressure Plate", "Clutch Bearings", "Ft Suspension(L)", "Ft Suspension(R)", "Rear Suspension(L)",
                                "Rear Suspension(R)", "Steering Ball Joint(L)", "Steering Ball Joint(R)", "Lower Arm(L)", "Lower Arm(R)", "Starter Motor", "Alternatoe Assy", "Radiator", "Condensor"};
                        // if(varient.equals(petrol))
                        // {
                        String var_petrol = "P";
                        String var_diesel = "D";
                        for (int i = 0; i < sm_service_list.length; i++) {
                            String sm_service_list_varient = sm_service_list[i].substring(sm_service_list[i].length() - 3);
                            sm_service_list_varient = sm_service_list_varient.replaceAll("[-+.^:,()]", "");
                            if (varient.equals(petrol)) {
                                sm_service_list = new String[]{"Engine Oil", "Oil filter", "Spark Plug(P)", "Brake Fluid", "Fuel Filter(P)",
                                        "Clutch Plate", "Pressure Plate", "Clutch Bearings", "Ft Suspension(L)", "Ft Suspension(R)", "Rear Suspension(L)",
                                        "Rear Suspension(R)", "Steering Ball Joint(L)", "Steering Ball Joint(R)", "Lower Arm(L)", "Lower Arm(R)", "Starter Motor", "Alternatoe Assy", "Radiator", "Condensor"};
                            } else if (varient.equals(diesel)) {
                                sm_service_list = new String[]{"Engine Oil", "Oil filter", "Air Filter(D)", "Brake Fluid", "Fuel Filter(D)", "Clutch Plate", "Pressure Plate",
                                        "Clutch Bearings", "Ft Suspension(L)", "Ft Suspension(R)", "Rear Suspension(L)", "Rear Suspension(R)", "Steering Ball Joint(L)",
                                        "Steering Ball Joint(R)", "Lower Arm(L)", "Lower Arm(R)", "Starter Motor", "Alternatoe Assy", "Radiator", "Condensor"};
                            }
                        }
                        //      }

                        SM_Services_display();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ScheduledMaintenanceService.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(ScheduledMaintenanceService.this);
                String km = smLocalStore.getSMhome_kms();
                String vehicle = smLocalStore.getSMhome_vehicle();
                String varient = smLocalStore.getSMhome_varient();
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vehicle", vehicle);
                params.put("kms", km);
                params.put("varient", varient);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);
    }

    public void no_service() {
        pDialog.dismiss();
        String varient_error = smLocalStore.getSMhome_varient();
        String varient_check = "1";
        varient_error = (varient_error.equals(varient_check)) ? "Petrol" : "Diesel";
        String vehicle_error = smLocalStore.getSMhome_vehicle();
        new AlertDialog.Builder(ScheduledMaintenanceService.this)
                .setMessage("No Service is available for " + vehicle_error + " of varient " + varient_error)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ScheduledMaintenanceService.this, ScheduledMaintenanaceHome.class));
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();
    }
}
