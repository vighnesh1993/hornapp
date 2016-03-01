package com.example.br1007.imagetesting;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> imageUrls;
    private DisplayImageOptions options;
    private ImageAdapter imageAdapter;
    SparseBooleanArray mSparseBooleanArray;
    private static final String DIRECTORY = "AndroOne";
    Uri imgUri,imageUri;
    final private int CAPTURE_IMAGE = 1;
    String imgPath;
    GridView gridView;
    Cursor imagecursor;
    private ArrayList<String> nImageUrl = new ArrayList<String>();
    private ArrayList<Integer> checkImage = new ArrayList<Integer>();
    private static final String IMAGE_PATH = "path";
    String currentPhotoPath = "";
    LinearLayout changeLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSparseBooleanArray = new SparseBooleanArray();

        final Button selectBtn = (Button) findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(selectOnClickListener);

        final Button captureBtn = (Button) findViewById(R.id.captureBtn);
        captureBtn.setOnClickListener(captureOnClickListener);

        changeLayout = (LinearLayout)findViewById(R.id.changeLayout);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        final String[] columns = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        @SuppressWarnings("deprecation")
        Cursor imagecursor1 = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        this.imageUrls = new ArrayList<String>();
        imageUrls.size();

        for (int i = 0; i < imagecursor1.getCount(); i++) {
            imagecursor1.moveToPosition(i);
            int dataColumnIndex = imagecursor1
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor1.getString(dataColumnIndex));
        }

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.stub_image)
                .showImageForEmptyUri(R.drawable.image_for_empty_url)
                .cacheInMemory().cacheOnDisc().build();

        imageAdapter = new ImageAdapter(this, imageUrls);

        gridView = (GridView) findViewById(R.id.PhoneImageGrid);
        gridView.setAdapter(imageAdapter);
    }

    @Override
    protected void onStop() {
        imageLoader.stop();
        super.onStop();
    }

    public void btnChoosePhotosClick(View v) {
        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
        Toast.makeText(MultiPhotoSelectActivity.this,"Total photos selected: " + selectedItems.size(),Toast.LENGTH_SHORT).show();
        Log.d(MultiPhotoSelectActivity.class.getSimpleName(),"Selected Items: " + selectedItems.toString());
    }

    public class ImageAdapter extends BaseAdapter {

        ArrayList<String> mList;
        LayoutInflater mInflater;
        Context mContext;

        public ImageAdapter(Context context, ArrayList<String> imageList) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mList = new ArrayList<String>();
            this.mList = imageList;

        }

        public ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();

            for (int i = 0; i < mList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList.get(i));
                }
            }
            return mTempArry;
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.griditem,null);
            }

            final CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            imageLoader.displayImage("file://" + imageUrls.get(position),imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(Bitmap loadedImage) {
                    Animation anim = AnimationUtils.loadAnimation(MultiPhotoSelectActivity.this,R.anim.fade_in);
                    imageView.setAnimation(anim);
                    anim.start();
                }
            });

            mCheckBox.setTag(position);
            if(nImageUrl.contains(imageUrls.get(position))){
                mCheckBox.setChecked(true);
                checkImage.add(position);
            }
            if(!checkImage.contains(position)){
                mCheckBox.setChecked(false);
            }
            mCheckBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = imageUrls.get(position);
                    if(mCheckBox.isChecked()){
                        nImageUrl.add(url);
                    }else{
                        nImageUrl.remove(url);
                    }
                }
            });
            return convertView;
        }

    }

    View.OnClickListener selectOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (nImageUrl.size() < 11) {
                if (nImageUrl.size() == 0) {
                    Toast.makeText(getApplicationContext(),"Please select at least one image",Toast.LENGTH_LONG).show();
                } else {
                    Intent imageIntent = new Intent(ManiActivity.this,UploadActivity.class);
                    imageIntent.putStringArrayListExtra("NIMAGE", nImageUrl);
                    startActivity(imageIntent);
                }
            } else {
                Toast.makeText(getApplicationContext(),"Upload ten Images only", Toast.LENGTH_LONG).show();
            }
        }
    };

    View.OnClickListener captureOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";
            File myDirectory = new File(Environment.getExternalStorageDirectory() + "/" + DIRECTORY + "/");
            if (!myDirectory.exists())
                myDirectory.mkdirs();
            File file = new File(myDirectory, fileName);
            imageUri = Uri.fromFile(file);
            currentPhotoPath = file.getAbsolutePath();
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAPTURE_IMAGE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE) {
            if(resultCode == Activity.RESULT_OK) {
                MediaScannerConnection.scanFile(getApplicationContext(), new String[] { imageUri.getPath() }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Scanned = ", "Scanned " + path);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        //UI Works
                                        loadData();
                                    }
                                });
                            }
                        });
            }
            else if(resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Picture could not be taken.", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void loadData() {
        final String[] columns = { MediaStore.Images.Media.DATA,MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        @SuppressWarnings("deprecation")
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,null, orderBy + " DESC");

        Log.i("imageUrls = ", "Scanned " + imageUrls);
        this.imageUrls.clear();

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
        }

        nImageUrl.add(imageUrls.get(0));
        imageAdapter = new ImageAdapter(this, imageUrls);
        gridView.setAdapter(imageAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(IMAGE_PATH, currentPhotoPath);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        currentPhotoPath = savedInstanceState.getString(IMAGE_PATH);
    }
}