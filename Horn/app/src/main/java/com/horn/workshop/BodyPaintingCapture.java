package com.horn.workshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class BodyPaintingCapture extends AppCompatActivity implements View.OnClickListener {

    Button capture, upload;
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
    private String encodeImage;
    String dataToSend;
    String descupload;
    EditText ed;
    SMLocalStore smLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_painting_capture);
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        capture = (Button) findViewById(R.id.capture_button);
        upload = (Button) findViewById(R.id.upload_button);
        imageView = (ImageView) findViewById(R.id.imageView);
        capture.setOnClickListener(this);
        upload.setOnClickListener(this);
        ed= (EditText) findViewById(R.id.descupload);
        smLocalStore=new SMLocalStore(this);
        //String desc=smLocalStore.getbpDescription();
        //ed.setText(desc);

    }
    @Override
    public void onClick(View v) {

        if (v == upload) {
            uploadImage();

        }
        if (v == capture) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getFileUri();
            i.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
            startActivityForResult(i, 20);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {


            new UploadImage().execute();

        }
    }

    private void getFileUri() {
        image_name = "'sample_image.jpeg";
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + image_name);
        file_uri = Uri.fromFile(file);
    }

    public class UploadImage extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... params) {

          //  Toast.makeText(getApplicationContext(),"file_uri.getPath(): "+file_uri.getPath(),Toast.LENGTH_SHORT);
            bitmap = BitmapFactory.decodeFile(file_uri.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();


            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);


            scaled.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] array = stream.toByteArray();
            encodeImage = Base64.encodeToString(array,0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

           Bitmap bitma = BitmapFactory.decodeFile(file_uri.getPath());
            imageView.setImageBitmap(bitma);

           // uploadImage();
        }
    }

    private void uploadImage() {

         descupload=ed.getText().toString();
        String tag_string_req = "bitmap";
        pDialog.setMessage("Uploading ...");
        showDialog();

        Toast.makeText(getApplicationContext(), "inside", Toast.LENGTH_LONG).show();

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
                        "No Network Connection", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("encoded_str_camera", encodeImage);
                params.put("descupload", descupload);
               // Log.d("ENCODED STRING", encodeImage);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

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
