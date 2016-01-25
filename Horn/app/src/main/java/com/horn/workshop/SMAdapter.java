package com.horn.workshop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import app.AppController;

//import static android.support.v4.app.ActivityCompat.startActivity;


/**
 * Created by Sariga on 1/8/2016.
 */
public class SMAdapter extends RecyclerView.Adapter<SMAdapter.MyViewHolder> {
    private ArrayList<WorkshopDatas> workshopDataSet;
    private Context context;
    private SMLocalStore smLocalStore;
    private TextView rating1;
//public ScheduledMaintenanceWorkshoplist workshoplist = new ScheduledMaintenanceWorkshoplist();

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public String mItem;
        TextView Name;
        TextView Address;
        TextView Phone;
        TextView Category;
        ImageView Picture;
        TextView Rating;
        TextView Distance;
        //  Button Booknow;
        // private SMAdapter activity;

        // TextView id_;
        public MyViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.Name = (TextView) itemView.findViewById(R.id.workshop_name);
            this.Address = (TextView) itemView.findViewById(R.id.workshop_address);
            this.Phone = (TextView) itemView.findViewById(R.id.workshop_phone);
            this.Category = (TextView) itemView.findViewById(R.id.workshopdetail_category);
            this.Picture = (ImageView) itemView.findViewById(R.id.workshopdetail_photo);
            this.Rating = (TextView) itemView.findViewById(R.id.rating);
            this.Distance = (TextView) itemView.findViewById(R.id.ws_distance);
            // this.Booknow = (Button) itemView.findViewById(R.id.booknow);
            itemView.setOnClickListener(this);
            //  Booknow.setOnClickListener(this);
        }

        public void setItem(Integer item) {
            mItem = item.toString();
        }

        @Override
        public void onClick(View v) {
            smLocalStore = new SMLocalStore(context);
            smLocalStore.setSMworkshoplist(mItem);
            Intent intent1 = new Intent(context, ScheduledMaintenanceDetail.class);
            context.startActivity(intent1);
            //SMAdapteronclick smAdapteronclick = new SMAdapteronclick();
            //smAdapteronclick.onclick_nav();
            //  workshoplist.new_activity_launch(mItem);

            //this.activity = SMAdapter.this;


        }
    }
//public class SMAdapteronclick extends Activity
//{
//    public void onclick_nav() {
//        Intent intent1 = new Intent(SMAdapteronclick.this, ScheduledMaintenanceDetail.class);
//        startActivity(intent1);
//    }
//}

    public SMAdapter(ArrayList<WorkshopDatas> workshop) {
        this.workshopDataSet = workshop;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workshoplist_cardview, parent, false);

        //  view.setOnClickListener(MainAcotivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView name1 = holder.Name;
        TextView address1 = holder.Address;
        TextView phone1 = holder.Phone;
        TextView category1 = holder.Category;
        final ImageView picture1 = holder.Picture;

        final TextView rating1 = holder.Rating;
        TextView dis1 = holder.Distance;
        // TextView id = holder.id_;


        name1.setText(workshopDataSet.get(listPosition).getName());
        address1.setText(workshopDataSet.get(listPosition).getAddress());
        phone1.setText(workshopDataSet.get(listPosition).getPhone());
        category1.setText(workshopDataSet.get(listPosition).getCategory());
       // dis1.setText(workshopDataSet.get(listPosition).getDistance());
        String url = workshopDataSet.get(listPosition).getProfilepic();
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
                    picture1.setImageBitmap(response.getBitmap());
                }
            }
        });
        holder.setItem(workshopDataSet.get(listPosition).getworkshopid());

        String ratingValue = workshopDataSet.get(listPosition).getrating();
        rating1.setText(ratingValue);
        //setRatingBackround(ratingValue);

        switch (ratingValue) {
            case "0":
                rating1.setBackgroundResource(R.drawable.rating_bg_0);
                break;
            case "0.5":
                rating1.setBackgroundResource(R.drawable.rating_bg_0_5);
                break;
            case "1":
                rating1.setBackgroundResource(R.drawable.rating_bg_1);
                break;
            case "1.5":
                rating1.setBackgroundResource(R.drawable.rating_bg_1_5);
                break;
            case "2":
                rating1.setBackgroundResource(R.drawable.rating_bg_2);
                break;
            case "2.5":
                rating1.setBackgroundResource(R.drawable.rating_bg_2_5);
                break;
            case "3":
                rating1.setBackgroundResource(R.drawable.rating_bg_3);
                break;
            case "3.5":
                rating1.setBackgroundResource(R.drawable.rating_bg_3_5);
                break;
            case "4":
                rating1.setBackgroundResource(R.drawable.rating_bg_4);
                break;
            case "4.5":
                rating1.setBackgroundResource(R.drawable.rating_bg_4_5);
                break;
            case "5":
                rating1.setBackgroundResource(R.drawable.rating_bg_5);
                break;
        }
    }

    private void setRatingBackround(String ratingValue) {

    }

    @Override
    public int getItemCount() {
        return workshopDataSet.size();
    }


}
