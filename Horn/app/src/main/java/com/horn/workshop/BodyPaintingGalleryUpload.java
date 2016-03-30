package com.horn.workshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;

public class BodyPaintingGalleryUpload extends AppCompatActivity {

    String s;
    String[] x;
    ArrayList<String> ob;
    GridView imagegrid;
    public Bitmap bitmap;
    Button b;
    String encodedImage;
    String e;
    String en="";
    String  p[];
    String descgalupload;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    public static final String TAG = "MY MESSAGE";
    public SMLocalStore smLocalStore;
    EditText ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_painting_gallery_upload);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        ed= (EditText) findViewById(R.id.descgalupload);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        b = (Button) findViewById(R.id.upload_button);
        smLocalStore=new SMLocalStore(this);
        String desc=smLocalStore.getbpDescription();

        ed.setText(desc);
        s=smLocalStore.getBpimages();

        if (s == "") {

        } else {

            x = s.split(",");
            int l=0;
             l = x.length;
            GridView imagegrid = (GridView) findViewById(R.id.maingrid);
            ImageAdapte imageAdapte = new ImageAdapte();
            imagegrid.setAdapter(imageAdapte);
        }

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String[] y = s.split(",");
                int k=0;
                k = y.length;
                //k=k-1;
                File f1;
                p=new String[k];
                for(int x=0;x<k;x++) {

                    f1 = new File(y[x]);
                    BitmapFactory.Options options1 = new BitmapFactory.Options();
                    options1.inSampleSize = 8;
                    Bitmap bitmap1 = BitmapFactory.decodeFile(f1.getAbsolutePath(), options1);
                    en =en+getStringImage(bitmap1);

                }
                uploadImage(en);
            }
        });
    }
    public class ImageAdapte extends BaseAdapter {

        private LayoutInflater mInflater;

        public ImageAdapte() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return x.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(BodyPaintingGalleryUpload.this);
                imageView.setLayoutParams(new GridView.LayoutParams(230, 230));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            File f=new File(x[position]);
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
             bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageView.setImageBitmap(bitmap);
            return imageView;


        }
    }
    public String getStringImage(Bitmap bmp){



        int nh = (int) ( bmp.getHeight() * (512.0 / bmp.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 512, nh, true);


       // scaled.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] imageBytes = baos.toByteArray();
         e = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        e=e+"<upload_string>";
        return e;
    }
    private void uploadImage(String st){


        descgalupload=ed.getText().toString();
            String tag_string_req = "bitmap";
            pDialog.setMessage("Uploading ...");
            showDialog();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.UPLOAD_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Upload Response: " + response);
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        String status = jObj.getString("status");
                        Log.d("IMAGEPATH : ",status );
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                        Intent in=new Intent(BodyPaintingGalleryUpload.this,BodyPaintingHome.class);
                        startActivity(in);

                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "No Network Connection", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String,String>();
                    params.put("encoded_string",en);
                    params.put("descgalupload",descgalupload);
                    return params;
                }
            };

        strReq.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main2_drawer, menu);
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
   /* class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        int id;
    }
*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent ob=new Intent(BodyPaintingGalleryUpload.this,BodyPaintingGallery.class);
        startActivity(ob);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

