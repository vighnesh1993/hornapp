package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.horn.workshop.ProfileAddCar;
import com.horn.workshop.R;
import com.horn.workshop.RecyclerItemClickListener;
import com.horn.workshop.SMLocalStore;

import java.util.ArrayList;
import java.util.List;

import app.AppController;
import data.CarData;

/**
 * Created by user on 03-02-2016.
 */
public class AddCarAdapter extends RecyclerView.Adapter<AddCarAdapter.MyViewHolder> {


    private ArrayList<CarData> mCarDatas;
    private Context context;
    private SMLocalStore smLocalStore;
    // Pass in the contact array into the constructor


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public ImageView carImageView;
        public String mitem;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public MyViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            context = itemView.getContext();
            this.nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            this.carImageView = (ImageView) itemView.findViewById(R.id.car_image);
            itemView.setOnClickListener(this);
        }
        public void setItem(String item) {
            mitem = item.toString();
        }

        @Override
        public void onClick(View v) {
            smLocalStore = new SMLocalStore(context);
            smLocalStore.setProfileAddCar(mitem);
            Intent intent1 = new Intent(context, ProfileAddCar.class);
            context.startActivity(intent1);

        }


    }

    public AddCarAdapter(ArrayList<CarData> carDatas) {
        this.mCarDatas = carDatas;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Context context = parent.getContext();
//        LayoutInflater inflater = LayoutInflater.from(context);
//
//        // Inflate the custom layout
//        View contactView = inflater.inflate(R.layout.item_car_view, parent, false);
        View contactView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car_view, parent, false);

        // Return a new holder instance
        MyViewHolder myViewHolder = new MyViewHolder(contactView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // Get the data model based on position
        //CarData carData = mCarDatas.get(position);

        // Set item views based on the data model
        TextView textView = holder.nameTextView;
        final ImageView carImage = holder.carImageView;
        textView.setText(mCarDatas.get(position).getName());
        String url = "http://blueripples.org/horn/app_server/cars/" + mCarDatas.get(position).getImageName();
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
        holder.setItem(mCarDatas.get(position).getCarId());
    }

    @Override
    public int getItemCount() {
        return mCarDatas.size();
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access

    public void setFilter(List<CarData> carDatas) {
        mCarDatas = new ArrayList<>();
        mCarDatas.addAll(carDatas);
        notifyDataSetChanged();
    }
}
