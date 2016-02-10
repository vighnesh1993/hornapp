package com.horn.workshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Sariga on 1/5/2016.
 */
public class ScheduledMaintenanceDesc extends AppCompatActivity {
    private SMLocalStore smLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_maintenance_desc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button sm_sug_button = (Button) findViewById(R.id.sm_sug_button);
        sm_sug_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText descedit = (EditText)findViewById(R.id.sm_suggestion);
                String desc = descedit.getText().toString();
                smLocalStore = new SMLocalStore(ScheduledMaintenanceDesc.this);
                smLocalStore.setSMdesc(desc);
                startActivity(new Intent(ScheduledMaintenanceDesc.this, ScheduledMaintenanceService.class));


                // Intent launchActivity2 = new Intent(ScheduledMaintenanceDesc.this, ScheduledMaintenanceService.class);
                //startActivity(launchActivity2);
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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
        }else if(id ==R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}