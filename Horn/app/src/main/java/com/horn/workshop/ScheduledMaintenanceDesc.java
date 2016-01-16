package com.horn.workshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
}
