package com.horn.workshop;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
   // ImageView ii;
    private GoogleApiClient client;
    RelativeLayout defaultimgl;
    UserLocalStore userLocalStore;
    android.support.v7.app.ActionBar ab;
    Toolbar toolbar;
    LinearLayout liner_for_grid,gimage;
    EditText send_qt;
    private List<String> imgPic;

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

//            gimage.setVisibility(View.GONE);
//            liner_for_grid.setVisibility(View.VISIBLE);
        }
        else if (a.equals("0"))
        {
            String q=userLocalStore.getBpquotes();
            send_qt.setText(q);

//            gimage.setVisibility(View.GONE);
//            liner_for_grid.setVisibility(View.VISIBLE);
        }
        else if (a.equals("5"))
        {

            String str = userLocalStore.getGridImages();
            Toast.makeText(getApplicationContext(), "deleted :" + str, Toast.LENGTH_SHORT).show();
            Log.i("GRIDVIEW IMAGE :", str);
            liner_for_grid.setVisibility(View.GONE);
            gimage.setVisibility(View.VISIBLE);

//            grid.setVisibility(View.VISIBLE);
        }
        else if (a.equals("2"))
        {
            String q=userLocalStore.getBpquotes();
            send_qt.setText(q);
            onGalleryImage();
//            grid.setVisibility(View.VISIBLE);
           liner_for_grid.setVisibility(View.GONE);
            gimage.setVisibility(View.VISIBLE);

        }
        sendquotes= (Button) findViewById(R.id.send_quotes);
        sendquotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                userLocalStore.setBpquotes("");
                Intent in = new Intent(BodyPaintingHome.this, BodyPaintingHome.class);
                in.putExtra("value", "1");
                startActivity(in);
            }
        });

        liner_for_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                try {

                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(captureIntent, CAMERA_CAPTURE);
                } catch (ActivityNotFoundException anfe) {
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast toast = Toast.makeText(BodyPaintingHome.this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
//                grid.setVisibility(View.VISIBLE);
//                liner_for_grid.setVisibility(View.GONE);
//                gimage.setVisibility(View.VISIBLE);
            }
        });
       /*grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // do something here
                *//*AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setView(grid);
                builder.setTitle("Goto");
                builder.show();*//**//**//**//*


                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                ImageView imageView = new ImageView(grid.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap bitmap = view.getDrawingCache();
                imageView.setImageBitmap(bitmap);
                builder.setView(imageView);
                builder.create().show();*//*
                Toast.makeText(getBaseContext(),"kkkkkkk:"+view.getTag(position),Toast.LENGTH_SHORT).show();
            }
        });*/

        Button b1 = (Button) findViewById(R.id.gallery);
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


//               Intent intent = new Intent();
//                intent.setType("image*//**//*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
//


                /*Intent i = new Intent(GAction.ACTION_MULTIPLE_PICK);
                startActivityForResult(i,200);*/

            }
        });



        /*b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GridViewDemo/");
                if (dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(dir, children[i]).delete();
                    }
                    SMLocalStore smLocalStore = new SMLocalStore(MainActivity.this);
                    smLocalStore.clearBpimages();
                    Intent ob = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(ob);

                }
            }
        });*/
        tFileList = null;
        listOfImagesPath = null;

        if (listOfImagesPath != null) {

            grid.setLayoutParams(new GridView.LayoutParams(GridLayout.LayoutParams.FILL_PARENT, GridLayout.LayoutParams.FILL_PARENT));

        }
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void captureimng() {

        try {

           /* File f1 = new File(y[v]);
            BitmapFactory.Options options1 = new BitmapFactory.Options();
            options1.inSampleSize = 8;
            Bitmap bitmap1 = BitmapFactory.decodeFile(f1.getAbsolutePath(), options1);*/

            BitmapFactory.Options options1 = new BitmapFactory.Options();
            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.camera_icon);
            // defaultimgl.setLayoutParams(new GridView.LayoutParams(200, 200));
            //options1.inSampleSize = 8;
            // String imgcurTime = dateFormat.format(new Date());
            File imageDirectory = new File(GridViewDemo_ImagePath);
            imageDirectory.mkdirs();

            String _path = GridViewDemo_ImagePath + "zzzzzzzzzzz.jpg";
            try {
                FileOutputStream out = new FileOutputStream(_path);
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.close();
            } catch (FileNotFoundException e) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return true;
    }

    public void onClick(View arg0) {
// TODO Auto-generated method stub

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {

                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
                String imgcurTime = dateFormat.format(new Date());
                File imageDirectory = new File(GridViewDemo_ImagePath);
                imageDirectory.mkdirs();
                String _path = GridViewDemo_ImagePath + imgcurTime + ".jpg";
                try
                {
                    FileOutputStream out = new FileOutputStream(_path);
                    thePic.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();
                }
                catch (FileNotFoundException e)
                {
                    e.getMessage();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
//                grid.setVisibility(View.VISIBLE);
               liner_for_grid.setVisibility(View.GONE);
                gimage.setVisibility(View.VISIBLE);

                listOfImagesPath = null;
                listOfImagesPath = RetriveCapturedImagePath();
                if (listOfImagesPath != null) {
                    grid.setAdapter(new ImageListAdapter(this, listOfImagesPath));
                    listOfImagesPath = null;
                }
                listOfImagesPath = null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SMLocalStore smLocalStore = new SMLocalStore(BodyPaintingHome.this);
        //smLocalStore.clearBpimages();
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

                for (int v = 0; v < k; v++)
                {

                    f1 = new File(y[v]);
                    BitmapFactory.Options options1 = new BitmapFactory.Options();
                    //options1.inSampleSize = 8;
                    Bitmap bitmap1 = BitmapFactory.decodeFile(f1.getAbsolutePath(), options1);
                    String imgcurTime = dateFormat.format(new Date());
                    File imageDirectory = new File(GridViewDemo_ImagePath);
                    imageDirectory.mkdirs();
                    String st = generateRandomString();
                    String _path = GridViewDemo_ImagePath + st + ".jpg";
                    try {
                        FileOutputStream out = new FileOutputStream(_path);
                        bitmap1.compress(Bitmap.CompressFormat.JPEG,10, out);
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.getMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    grid.setVisibility(View.VISIBLE);
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


           // Toast.makeText(BodyPaintingHome.this, "imgPic.size(); : "+imgPic.size(), Toast.LENGTH_SHORT).show();

           /* lGrid.setVisibility(View.VISIBLE);
            defaultimgl.setVisibility(View.GONE);*/
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



            /*holder.checkbox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub



                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsselection[id]) {
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                        cl--;
                    } else {


                        if(cl>=6)
                        {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(BodyPaintingGallery.this);
                            builder.setTitle("Horn");
                            builder.setMessage("You can select maximum upto 5 images");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //dialog.cancel();

                                    dialog.dismiss();
                                }
                            });

                            builder.show();
                            cb.setChecked(false);
                        }

                        else
                        {
                            cb.setChecked(true);
                            thumbnailsselection[id] = true;
                            cl++;
                        }

                    }


                }
            });*/


            //Toast.makeText(getApplicationContext(),"imgPic.size():" +imgPic.size(),Toast.LENGTH_SHORT).show();


             /*   holder.img.setId(position);
               holder.imageview.setId(position);*/




            try {
                String dfer = imgPic.get(position);
                String[] wwer = dfer.split("/");
                int gger = wwer.length;
                String dffed = wwer[gger - 1];
                if (dffed.equals("zzzzzzzzzzz.jpg")) {
                    holder.img.setVisibility(View.GONE);
                }
                if (dffed.equals("zzzzzzzzzzz.jpg")&&position==0) {

                  /*  gimage.setVisibility(View.GONE);
                    liner_for_grid.setVisibility(View.VISIBLE);*/
                    String quotes=send_qt.getText().toString();
                    userLocalStore.setBpquotes(quotes);
                    Intent in = new Intent(BodyPaintingHome.this, BodyPaintingHome.class);
                    in.putExtra("value", "0");
                    startActivity(in);

                }
               /* if (position>5) {
                    imgPic.remove(position);
                    //grid.setAdapter(new ImageListAdapter(context, imgPic));
                  Toast.makeText(getApplicationContext(),"Exceeded",Toast.LENGTH_SHORT).show();

                }*/
             /*   else
                {*/
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
               // }



            } catch (Exception e) {

            }
           /* if(position>=2)
            {
                //imgPic.remove(position);
                grid.setAdapter(new ImageListAdapter(context, imgPic));
            }*/
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
                 /*   Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image*//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//*");
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"pathhhhhh :"+imgPic.get(position),Toast.LENGTH_SHORT).show();*/

                    /*Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("content:/" + imgPic.get(position)));
                    intent.setClassName("com.android.gallery3d", null);
                    startActivity(intent);
                    startActivity(new Intent(Intent.ACTION_VIEW));*/

                    String dfer = imgPic.get(position);
                    String[] wwer = dfer.split("/");
                    int gger = wwer.length;
                    String dffed = wwer[gger - 1];
                    //Toast.makeText(getApplicationContext(), "String  :" +dffed, Toast.LENGTH_SHORT).show();

                    if (dffed.equals("zzzzzzzzzzz.jpg")) {



                            int l=imgPic.size();

                            if(l<6)
                            {
                                try {
                                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                startActivityForResult(captureIntent, CAMERA_CAPTURE);
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
                        //Toast.makeText(BodyPaintingHome.this, "gallery view\n"+ Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                                        Uri.parse("content:/"+imgPic.get(position)));
//                                intent.setClassName("com.android.gallery3d",null);
//                                startActivity(intent);
//                                startActivity(new Intent(Intent.ACTION_VIEW));

                    }

                }
            });


            return convertView;
        }

    }



    /*public class ImageListAdapter extends BaseAdapter {
        private Context context;
        private List<String> imgPic;

        private LayoutInflater mInflater;

        public ImageListAdapter(Context c, List<String> thePic) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            context = c;
            imgPic = thePic;
        }

        public int getCount() {
            if (imgPic != null)
                return imgPic.size();
            else
                return 0;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(final int position, View convertView, ViewGroup parent) {

            ImageView imageView;

            BitmapFactory.Options bfOptions = new BitmapFactory.Options();
            bfOptions.inDither = false;                     //Disable Dithering mode
            bfOptions.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
            bfOptions.inInputShareable = true;             //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
            bfOptions.inTempStorage = new byte[32 * 1024];
            if (convertView == null) {
                imageView = new ImageView(context);
                //Toast.makeText(getApplicationContext(), "convrtview null", Toast.LENGTH_SHORT).show();
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));


            } else {
               // Toast.makeText(getApplicationContext(), "convrtview not null", Toast.LENGTH_SHORT).show();

                imageView = (ImageView) convertView;
                imageView.setLayoutParams(new GridView.LayoutParams(200,200));

            }
            FileInputStream fs = null;
            Bitmap bm;
            try {
                fs = new FileInputStream(new File(imgPic.get(position).toString()));

                if (fs != null) {

                    bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
                    imageView.setImageBitmap(bm);
                    imageView.setId(position);
                    imageView.setLayoutParams(new GridView.LayoutParams(200, 200));


                    imageView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(getApplicationContext(), "imgPic.get(position) :" + imgPic.get(position), Toast.LENGTH_SHORT).show();
                            Log.i("pathhh :", imgPic.get(position));
                            String dfer = imgPic.get(position);
                            String[] wwer = dfer.split("/");
                            int gger = wwer.length;
                            String dffed = wwer[gger-1];
                            Toast.makeText(getApplicationContext(), "String  :" +dffed, Toast.LENGTH_SHORT).show();

                            if (dffed.equals("zzzzzzzzzzz.jpg")) {
                                try {

                                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                    startActivityForResult(captureIntent, CAMERA_CAPTURE);
                                } catch (ActivityNotFoundException anfe) {
                                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                                    Toast toast = Toast.makeText(BodyPaintingHome.this, errorMessage, Toast.LENGTH_SHORT);
                                    toast.show();
                                }


                            } else {
                                viewImage(imgPic.get(position));
                                Toast.makeText(BodyPaintingHome.this, "gallery view\n"+ Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                                        Uri.parse("content:/"+imgPic.get(position)));
//                                intent.setClassName("com.android.gallery3d",null);
//                                startActivity(intent);
//                                startActivity(new Intent(Intent.ACTION_VIEW));

                            }

                        }

                    });

                } else {

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fs != null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return imageView;
        }
    }*/

    public void viewImage(String x) {
        /*File file = new File(Environment.getExternalStorageDirectory()
                + "/Download/images-1.jpg");*/

        File file = new File(x);
        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "image/*");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(BodyPaintingHome.this, "No                                                                       Application available to view Image",
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
