package com.horn.workshop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;

public class BodyPaintingHome extends AppCompatActivity {

    Button upload_quotes;


   // ImageView iv;

    Button upload;
    public static final String TAG = "MY MESSAGE";

    private ImageView imageView;

    private Bitmap bitmap;
    private SQLiteHandler db;
    private ProgressDialog pDialog;

    private Uri filePath;
    private Uri file_uri;
    private String image_name;
    private File file;
    private final int requestCode = 20;
    private String encodeImage="";
    String dataToSend;
    String descupload,e;
    EditText ed;
    String  p[];
    SMLocalStore smLocalStore;
    //String x;
    int k=0;
    GridView imagegrid;
    //Dialog fbDialogue;
    String s,encodedImage;
    String[] x;
    int val=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_painting_home);
        imagegrid= (GridView) findViewById(R.id.maingrid);
        imageView= (ImageView) findViewById(R.id.imgview);
        smLocalStore=new SMLocalStore(this);
        Intent ob=getIntent();
        String val=ob.getStringExtra("value");
        ed= (EditText) findViewById(R.id.bp_desc);
        String t=smLocalStore.getbpDescription();
        if(t!="")
        {
            ed.setText(t);
        }

        if(val.equals("2"))
        {
            imageView.setVisibility(View.GONE);
        }
        if(val.equals("1"))
        {
            imagegrid.setVisibility(View.GONE);
            k=0;
        }
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        s=smLocalStore.getBpimages();

        try
        {
                if (s != "") {
                    x = s.split(",");
                    int l = 0;
                    l = x.length;

                    ImageAdapte imageAdapte = new ImageAdapte();
                    imagegrid.setAdapter(imageAdapte);

                } else {

                }

        }
        catch (Exception e)
        {

        }

        upload_quotes= (Button) findViewById(R.id.send_quotes);
        upload_quotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (s != "") {
                    String[] y = s.split(",");
                    k=0;
                    k = y.length;
                    //k=k-1;
                    File f1;
                    p=new String[k];
                    encodeImage="";
                    Toast.makeText(getBaseContext(),"length :"+k,Toast.LENGTH_SHORT).show();
                    for(int x=0;x<k;x++) {

                        f1 = new File(y[x]);
                        BitmapFactory.Options options1 = new BitmapFactory.Options();
                        options1.inSampleSize = 8;
                        Bitmap bitmap1 = BitmapFactory.decodeFile(f1.getAbsolutePath(), options1);
                        encodeImage =encodeImage+getStringImage(bitmap1);

                    }
                    sendquotes();
                    smLocalStore.clearBpimages();
                    s="";
                    //k=0;
                }
               else {
                    k=0;
                    Toast.makeText(getBaseContext(),"l:"+k,Toast.LENGTH_SHORT).show();
                    sendquotes();
                   // encodeImage = "";
                }

            }
        });
        upload= (Button) findViewById(R.id.up_button);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text=ed.getText().toString();
                smLocalStore.bpDescription(text);

                final Dialog fbDialogue = new Dialog(BodyPaintingHome.this, android.R.style.Theme_Black_NoTitleBar);
                fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                fbDialogue.setContentView(R.layout.choice_upload);
                ImageView c= (ImageView) fbDialogue.findViewById(R.id.camera);
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        imageView.setVisibility(View.VISIBLE);
                        imagegrid.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        getFileUri();
                        i.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(i, 20);
                        fbDialogue.hide();
                    }
                });


                ImageView gal= (ImageView) fbDialogue.findViewById(R.id.gal);
                gal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pDialog.setMessage("Loading ...");
                        showDialog();
                        imageView.setVisibility(View.GONE);
                        imagegrid.setVisibility(View.VISIBLE);
                        Intent ob=new Intent(BodyPaintingHome.this,BodyPaintingGallery.class);
                        startActivity(ob);
                        imageView.setVisibility(View.GONE);
                    }
                });

                fbDialogue.setCancelable(true);
                fbDialogue.show();
                hideDialog();
            }


        });

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
    private void sendquotes() {


        //Toast.makeText(BodyPaintingHome.this,"imageViewOne.getDrawable():"+imageView.getDrawable(),Toast.LENGTH_SHORT).show();
        descupload=ed.getText().toString();
        Toast.makeText(BodyPaintingHome.this,"desccccc:"+descupload,Toast.LENGTH_SHORT).show();

        if ((imageView.getDrawable()==null)&&s.equals(""))
        {
            encodeImage="null";
        }

        if(!descupload.equals(""))
        {
            String tag_string_req = "bitmap";
            pDialog.setMessage("Uploading ...");
            showDialog();

            //Toast.makeText(getApplicationContext(), "inside", Toast.LENGTH_LONG).show();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.UPLOAD_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Upload Response: " + response);
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        String status = jObj.getString("status");
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();

                        smLocalStore.clrbpDescription();
                        Intent in=new Intent(BodyPaintingHome.this,BodyPaintingHome.class);
                        in.putExtra("value","1");
                        startActivity(in);


                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("encoded_string", encodeImage);
                    params.put("length", ""+k);
                    params.put("descupload", descupload);
                    // Log.d("ENCODED STRING", encodeImage);
                    return params;
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
        else
        {
            Toast.makeText(BodyPaintingHome.this,"Empty Quotes",Toast.LENGTH_SHORT).show();
        }


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
                imageView = new ImageView(BodyPaintingHome.this);
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
    private void getFileUri() {
        image_name = "'sample_image.jpeg";
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + image_name);
        file_uri = Uri.fromFile(file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {

           // Toast.makeText(getApplicationContext(),"onactivity",Toast.LENGTH_SHORT);
            new UploadImage().execute();

        }
    }

    public class UploadImage extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {


            bitmap = BitmapFactory.decodeFile(file_uri.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);


            scaled.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] array = stream.toByteArray();
            encodeImage = Base64.encodeToString(array, 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Bitmap bitma = BitmapFactory.decodeFile(file_uri.getPath());
            imageView.setImageBitmap(bitmap);
        }
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(BodyPaintingHome.this,MainActivity.class);
        startActivity(in);
        finish();
    }
}
