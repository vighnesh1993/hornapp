package com.horn.workshop;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * Created by Sariga on 1/5/2016.
 */
public class ScheduledMaintenanaceHome extends AppCompatActivity {

    private SMLocalStore smLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_maintenance_home);

        /** SM vehicle dropdown **/

        Spinner dropdown = (Spinner) findViewById(R.id.vehicle);
        String[] items = new String[]{"Etios", "Innova", "Fortuner"};
        final String varient = "1";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String[] sm_service = new String[]{"1000", "5000", "10000", "15000", "20000", "25000"};
        for (int i = 0; i < sm_service.length; i++) {
            final Button myButton = new Button(this);
            myButton.setText(sm_service[i]);
            LinearLayout ll = (LinearLayout) findViewById(R.id.sm_service);
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            ll.addView(myButton, lp);
            final String service = sm_service[i];
            myButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    smLocalStore = new SMLocalStore(ScheduledMaintenanaceHome.this);
                    Spinner spinner = (Spinner) findViewById(R.id.vehicle);
                    String vehicle = spinner.getSelectedItem().toString();
                    smLocalStore.setSMhome(vehicle, service, varient);

                    Intent launchActivity1 = new Intent(ScheduledMaintenanaceHome.this, ScheduledMaintenanceDesc.class);
                    startActivity(launchActivity1);
                }
            });
        }

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

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
        }

        return super.onOptionsItemSelected(item);
    }
}
