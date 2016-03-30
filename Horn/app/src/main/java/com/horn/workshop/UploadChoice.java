package com.horn.workshop;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class UploadChoice extends AppCompatActivity implements View.OnClickListener{

    Button camera;
    Button gallery;

    private String image_name;
    private File file;
    private Uri file_uri;

    SMLocalStore smLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_choice);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        camera= (Button) findViewById(R.id.btn_camera);
        gallery= (Button) findViewById(R.id.btn_gallery);
        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);
        smLocalStore=new SMLocalStore(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        getWindow().setLayout((int) (width * .8), (int) (height * .5));

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onClick(View v) {


        if(v==camera)
        {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getFileUri();
            i.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
            startActivityForResult(i, 20);
            finish();
        }
        else if(v==gallery)
        {

        }


    }
    private void getFileUri() {
        image_name = "'sample_image.jpeg";
        String x=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + image_name;
        file = new File(x);
        file_uri = Uri.fromFile(file);
        Toast.makeText(UploadChoice.this, "x original:" + x, Toast.LENGTH_SHORT).show();
        //smLocalStore.putfilepath(x);
    }
}
