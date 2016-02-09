package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.horn.workshop.R;

import java.util.ArrayList;
import java.util.List;

import app.AppController;
import data.CarData;

/**
 * Created by Sariga on 2/9/2016.
 */
public class MyCarAdapter extends
        RecyclerView.Adapter<MyCarAdapter.ViewHolder> {
    private List<CarData> mCarDatas;
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
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public ImageView carImageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.mycar_name);
            carImageView = (ImageView) itemView.findViewById(R.id.mycar_image);
        }
    }
}
