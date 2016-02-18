package adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.horn.workshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import data.CarData;
import helper.SQLiteHandler;

/**
 * Created by Sariga on 2/9/2016.
 */
public class MyCarAdapter extends RecyclerView.Adapter<MyCarAdapter.ViewHolder> {
    private List<CarData> mCarDatas;
    private List<CarData> mCarDatasremove;
     private Context context;
    public SQLiteHandler sqLiteHandler;
    ProgressDialog pDialog;
    // Pass in the contact array into the constructor

    public MyCarAdapter(ArrayList<CarData> carDatas) {
        mCarDatas = carDatas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.my_cars_list, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        CarData carData = mCarDatas.get(position);

        // Set item views based on the data model
        TextView textView = holder.nameTextView;
        final ImageView carImage = holder.carImageView;
        textView.setText(carData.getName());
        String url = "http://blueripples.org/horn/app_server/cars/" + carData.getImageName();
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    carImage.setImageBitmap(response.getBitmap());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCarDatas.size();
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public ImageView carImageView;
      //  public Context context;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            context = itemView.getContext();
            nameTextView = (TextView) itemView.findViewById(R.id.mycar_name);
            carImageView = (ImageView) itemView.findViewById(R.id.mycar_image);

        }
    }
    public void remove(final int position) {
        CarData carData = mCarDatas.get(position);
        final String car_id = carData.getCarId();
        String strreq = "req";
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loding...");
        pDialog.setCancelable(false);
        pDialog.show();
        //final ProgressDialog loading;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADDCARDETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        String status_remove = jsonObject.getString("status");
                        Log.d("remove status",status_remove);
                        pDialog.dismiss();
                        Toast.makeText(context, "Removed Successfully !!!", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                String remove_mycars = "remove_mycars";
                sqLiteHandler = new SQLiteHandler(context);
                HashMap<String, String> user = sqLiteHandler.getUserDetails();
                String  apmnt_user_email = user.get("email");
                String remove_mycars_id = car_id;
                Log.d("start",remove_mycars_id);
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("remove_mycars", remove_mycars);
                params.put("remove_mycars_email", apmnt_user_email);
                params.put("remove_mycars_id", remove_mycars_id);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);
        mCarDatas.remove(position);
        notifyItemRemoved(position);
    }


}
