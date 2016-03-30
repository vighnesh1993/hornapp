package com.horn.workshop;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;

public class BodyPaintingHome extends AppCompatActivity implements View.OnClickListener {


    final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    final int RANDOM_STRING_LENGTH = 10;
    Button sendquotes;
    final int CAMERA_CAPTURE = 1;
    final int PICTURE_RESULT = 1;
    //private Uri picUri;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private GridView grid;
    private List<String> listOfImagesPath;
    List<String> tFileList = null;
    public String GridViewDemo_ImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GridViewDemo/";
    String p[];
    String s;
    int i = 0;
    String gimg="";
    private ProgressDialog pDialog;
    private GoogleApiClient client;
    RelativeLayout defaultimgl;
    UserLocalStore userLocalStore;
    android.support.v7.app.ActionBar ab;
    Toolbar toolbar;
    LinearLayout liner_for_grid,gimage;
    EditText send_qt;
    private List<String> imgPic;
    String e;
    File f1;
    String en="";
    private Uri file_uri;
    private String encodeImage="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_painting_home);

        send_qt= (EditText) findViewById(R.id.send_qt);
        gimage = (LinearLayout) findViewById(R.id.gimage);
        userLocalStore=new UserLocalStore(this);

        liner_for_grid=(LinearLayout) findViewById(R.id.liner_for_grid);

        grid = (GridView) findViewById(R.id.gridviewimg);
        defaultimgl = (RelativeLayout) findViewById(R.id.defaultimgl);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        toolbar = (Toolbar) findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);


        ab = getSupportActionBar();
        ab.setTitle("Body and Painting");
        ab.setDisplayHomeAsUpEnabled(true);


        Intent ob = getIntent();
        String a = ob.getStringExtra("value");

        if (a.equals("1"))
        {
            captureimng();

        }
        else if (a.equals("0"))
        {
            captureimng();
            String q=userLocalStore.getBpquotes();
            send_qt.setText(q);

        }
        else if (a.equals("5"))
        {

            String str = userLocalStore.getGridImages();
            Toast.makeText(getApplicationContext(), "deleted :" + str, Toast.LENGTH_SHORT).show();
            Log.i("GRIDVIEW IMAGE :", str);
            liner_for_grid.setVisibility(View.GONE);
            gimage.setVisibility(View.VISIBLE);

        }
        else if (a.equals("2"))
        {
            String q=userLocalStore.getBpquotes();
            send_qt.setText(q);
            onGalleryImage();
            liner_for_grid.setVisibility(View.GONE);
            gimage.setVisibility(View.VISIBLE);

        }
        sendquotes= (Button) findViewById(R.id.send_quotes);
        sendquotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                userLocalStore.setBpquotes("");
                for(int i=0;i<imgPic.size();i++) {


                    try {
                        String dfer = imgPic.get(i);
                        String[] wwer = dfer.split("/");
                        int gger = wwer.length;
                        String dffed = wwer[gger - 1];
                        if (!dffed.equals("zzzzzzzzzzz.jpg")) {
                            f1 = new File(imgPic.get(i));
                            BitmapFactory.Options options1 = new BitmapFactory.Options();
                            //options1.inSampleSize =5;
                            Bitmap bitmap1 = BitmapFactory.decodeFile(f1.getAbsolutePath(), options1);
                            en = en + getStringImage(bitmap1);


                        }


                    } catch (Exception e) {

                    }
                }
                uploadImage();


            }
               /* Intent in = new Intent(BodyPaintingHome.this, MainActivity.class);
                in.putExtra("value", "1");
                startActivity(in);*/

        });

        liner_for_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try {

                    /*Intent cameraIntent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);*/

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "Horn");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    file_uri= getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                    startActivityForResult(intent, PICTURE_RESULT);





                } catch (ActivityNotFoundException anfe) {
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast toast = Toast.makeText(BodyPaintingHome.this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        RelativeLayout b1 = (RelativeLayout) findViewById(R.id.gallery);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quotes=send_qt.getText().toString();
                userLocalStore.setBpquotes(quotes);
                int l=imgPic.size();

                if(l<6)
                {
                    Intent ob = new Intent(BodyPaintingHome.this, BodyPaintingGallery.class);
                    ob.putExtra("length",""+l);
                    startActivity(ob);
                }
                else
                {
                    Toast.makeText(BodyPaintingHome.this, "You can upload maximum 5 images", Toast.LENGTH_SHORT).show();
                }


            }
        });



        tFileList = null;
        listOfImagesPath = null;

        if (listOfImagesPath != null) {

            grid.setLayoutParams(new GridView.LayoutParams(GridLayout.LayoutParams.FILL_PARENT, GridLayout.LayoutParams.FILL_PARENT));

        }
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private void captureimng() {

        try {


            BitmapFactory.Options options1 = new BitmapFactory.Options();
            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.camera_icon);
            File imageDirectory = new File(GridViewDemo_ImagePath);
            imageDirectory.mkdirs();

            String _path = GridViewDemo_ImagePath + "zzzzzzzzzzz.jpg";
            try {
                FileOutputStream out = new FileOutputStream(_path);
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.close();
            }
            catch (FileNotFoundException e)
            {
                e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }

            listOfImagesPath = null;
            listOfImagesPath = RetriveCapturedImagePath();
            grid.setAdapter(new ImageListAdapter(this, listOfImagesPath));
            listOfImagesPath = null;
            listOfImagesPath = null;
        } catch (Exception e) {

        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK ) {
            if (requestCode == PICTURE_RESULT) {



                try {
                    Bitmap thePic = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), file_uri);

                    String imgcurTime = dateFormat.format(new Date());
                    File imageDirectory = new File(GridViewDemo_ImagePath);
                    imageDirectory.mkdirs();
                    String _path = GridViewDemo_ImagePath+ imgcurTime+".jpeg";
                    try
                    {

                        FileOutputStream out = new FileOutputStream(_path);

                        int nh = (int) ( thePic.getHeight() * (512.0 / thePic.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(thePic, 512, nh, true);
                        scaled.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.close();

                    }
                    catch (Exception e)
                    {
                        e.getMessage();
                    }

                    liner_for_grid.setVisibility(View.GONE);
                    gimage.setVisibility(View.VISIBLE);

                    listOfImagesPath = null;
                    listOfImagesPath = RetriveCapturedImagePath();
                    if (listOfImagesPath != null)
                    {
                        grid.setAdapter(new ImageListAdapter(this, listOfImagesPath));
                        listOfImagesPath = null;
                    }

                    listOfImagesPath = null;



                } catch (Exception e) {
                    e.printStackTrace();
                }





                //Bitmap thePic = (Bitmap) data.getExtras().get("data");
             /*   String imgcurTime = dateFormat.format(new Date());
                File imageDirectory = new File(GridViewDemo_ImagePath);
                imageDirectory.mkdirs();
                 String _path = GridViewDemo_ImagePath+ imgcurTime+".jpeg";
                try
                {

                    FileOutputStream out = new FileOutputStream(_path);

                    int nh = (int) ( thePic.getHeight() * (512.0 / thePic.getWidth()) );
                    Bitmap scaled = Bitmap.createScaledBitmap(thePic, 512, nh, true);
                    scaled.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();

                }
                catch (Exception e)
                {
                    e.getMessage();
                }

                liner_for_grid.setVisibility(View.GONE);
                gimage.setVisibility(View.VISIBLE);

                listOfImagesPath = null;
                listOfImagesPath = RetriveCapturedImagePath();
                if (listOfImagesPath != null)
                {
                    grid.setAdapter(new ImageListAdapter(this, listOfImagesPath));
                    listOfImagesPath = null;
                }

                listOfImagesPath = null;*/
            }
        }
    }

    protected void onGalleryImage() {


        SMLocalStore smLocalStore = new SMLocalStore(this);
        s = smLocalStore.getBpimages();
        try {
            if (s != "") {

                String[] y = s.split(",");
                int k = 0;
                k = y.length;
                File f1;
                p = new String[k];

                for (int v = 0; v<k; v++)
                {

                    f1 = new File(y[v]);
                    BitmapFactory.Options options1 = new BitmapFactory.Options();
                    //options1.inSampleSize = 8;
                    Bitmap bitmap1 = BitmapFactory.decodeFile(f1.getAbsolutePath(), options1);
                    File imageDirectory = new File(GridViewDemo_ImagePath);
                    imageDirectory.mkdirs();
                    String st = generateRandomString();
                    String _path = GridViewDemo_ImagePath + st + ".jpeg";

                    try
                    {
                        FileOutputStream out = new FileOutputStream(_path);
                        bitmap1.compress(Bitmap.CompressFormat.JPEG,100, out);
                        out.close();
                    }
                    catch (Exception e)
                    {
                        e.getMessage();
                    }

                    liner_for_grid.setVisibility(View.GONE);
                    gimage.setVisibility(View.VISIBLE);

                    listOfImagesPath = null;
                    listOfImagesPath = RetriveCapturedImagePath();
                    if (listOfImagesPath != null)
                    {
                        grid.setAdapter(new ImageListAdapter(this, listOfImagesPath));
                        listOfImagesPath = null;
                    }
                    listOfImagesPath = null;

                }

            }

        } catch (Exception e) {

        }

    }


    public class ImageListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        ImageListAdapter ad;
        private Context context;


        public ImageListAdapter(Context c, List<String> thePic) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imgPic = thePic;

        }

        public int getCount() {
            if (imgPic != null)
                return imgPic.size();

            else
                return 0;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            BitmapFactory.Options bfOptions = new BitmapFactory.Options();
            bfOptions.inDither = false;                     //Disable Dithering mode
            bfOptions.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
            bfOptions.inInputShareable = true;            //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
            bfOptions.inTempStorage = new byte[32 * 1024];
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.bpgallery, null);

                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.img = (ImageView) convertView.findViewById(R.id.itemCheckBox);

                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            try {
                String dfer = imgPic.get(position);
                String[] wwer = dfer.split("/");
                int gger = wwer.length;
                String dffed = wwer[gger - 1];
                if (dffed.equals("zzzzzzzzzzz.jpg")) {
                    holder.img.setVisibility(View.GONE);
                }
                if (dffed.equals("zzzzzzzzzzz.jpg")&&position==0) {

                    String quotes=send_qt.getText().toString();
                    userLocalStore.setBpquotes(quotes);
                    Intent in = new Intent(BodyPaintingHome.this, BodyPaintingHome.class);
                    in.putExtra("value", "0");
                    startActivity(in);

                }
                holder.img.setId(position);
                holder.imageview.setId(position);


                FileInputStream fs = null;
                Bitmap bm;
                try {
                    fs = new FileInputStream(new File(imgPic.get(position).toString()));

                    if (fs != null) {


                        bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
                        holder.imageview.setImageBitmap(bm);
                        holder.id = position;

                    }
                } catch (Exception e) {

                }

                userLocalStore = new UserLocalStore(BodyPaintingHome.this);
                userLocalStore.setGridImages(gimg);


            } catch (Exception e) {

            }
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File dir = new File(imgPic.get(position));
                    if (dir.exists()) {
                        dir.delete();
                        holder.imageview.setVisibility(View.GONE);
                        holder.img.setVisibility(View.GONE);
                        imgPic.remove(position);
                        grid.setAdapter(new ImageListAdapter(context, imgPic));
                    } else {
                        Toast.makeText(getApplicationContext(), "no file", Toast.LENGTH_SHORT).show();

                    }
                }
            });


            holder.imageview.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();

                    String dfer = imgPic.get(position);
                    String[] wwer = dfer.split("/");
                    int gger = wwer.length;
                    String dffed = wwer[gger - 1];

                    if (dffed.equals("zzzzzzzzzzz.jpg")) {



                        int l=imgPic.size();

                        if(l<6)
                        {
                            try
                            {


                                /*Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(captureIntent, CAMERA_CAPTURE);*/


                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.TITLE, "Horn");
                                values.put(MediaStore.Images.Media.DESCRIPTION, "Horn");
                                file_uri= getContentResolver().insert(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                                startActivityForResult(intent, PICTURE_RESULT);


                            } catch (ActivityNotFoundException anfe) {
                                String errorMessage = "Whoops - your device doesn't support capturing images!";
                                Toast toast = Toast.makeText(BodyPaintingHome.this, errorMessage, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        else
                        {
                            Toast.makeText(BodyPaintingHome.this, "You can upload maximum 5 images", Toast.LENGTH_SHORT).show();
                        }




                    } else {
                        viewImage(imgPic.get(position));


                    }

                }
            });


            return convertView;
        }

    }



    private void uploadImage(){


        final String descgalupload=send_qt.getText().toString();
       // Toast.makeText(getApplicationContext(),"en :"+en,Toast.LENGTH_SHORT).show();

        String tag_string_req="bitmap";
        pDialog.setMessage("Uploading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.UPLOAD_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    Log.d("IMAGEPATH : ",status );
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                    Intent in=new Intent(BodyPaintingHome.this,MainActivity.class);
                    startActivity(in);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "No Network Connection", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("encoded_string",en);
                params.put("descupload",descgalupload);
                params.put("length", ""+(imgPic.size()-1));

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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

    public String getStringImage(Bitmap bmp)
    {

        int nh = (int) ( bmp.getHeight() * (512.0 / bmp.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 512, nh, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] imageBytes = baos.toByteArray();
        e = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        e=e+"<upload_string>";
        return e;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return true;
    }

    public void onClick(View arg0) {
// TODO Auto-generated method stub

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SMLocalStore smLocalStore = new SMLocalStore(BodyPaintingHome.this);
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GridViewDemo/");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
            smLocalStore.clearBpimages();
            Intent ob = new Intent(BodyPaintingHome.this, MainActivity.class);
            startActivity(ob);

        }
        finish();

    }


    public String generateRandomString() {

        StringBuffer randStr = new StringBuffer();
        for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }


    private int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }



    private List<String> RetriveCapturedImagePath() {

        tFileList = null;
        tFileList = new ArrayList<String>();
        File f = new File(GridViewDemo_ImagePath);
        if (f.exists()) {
            File[] files = f.listFiles();
            Arrays.sort(files);

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory())
                    continue;
                tFileList.add(file.getPath());
            }
        }
        return tFileList;
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        /*Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.horn.workshop.imageuploadfinal/http/host/path")

        );
        AppIndex.AppIndexApi.start(client, viewAction);*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BodyPaintingHome Page", // TODO: Define a title for the content shown.
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
                "BodyPaintingHome Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.horn.workshop/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        /*Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.br1007.imageuploadfinal/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);*/
        client.disconnect();
    }





    public void viewImage(String x) {

        File file = new File(x);
        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "image/*");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(BodyPaintingHome.this, "No Application available to view Image",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ViewHolder {
        ImageView imageview;
        ImageView img;
        int id;
    }
}
