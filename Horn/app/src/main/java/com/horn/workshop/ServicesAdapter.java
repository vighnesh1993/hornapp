package com.horn.workshop;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import helper.Services;

/**
 * Created by user on 13-01-2016.
 */
public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {
    private List<Services> servicesp;
    private int rowLayout;
    private Context mContext;

    public ServicesAdapter(List<Services> services, int rowLayout, Context context) {
        this.servicesp = services;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Services services = servicesp.get(i);
        viewHolder.servicesName.setText(services.name);
        String serviceName = servicesp.get(i).getservicesCount();
            viewHolder.servicesCount.setText(serviceName + " " + "Services");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.servicesImage.setImageDrawable(mContext.getDrawable(services.getImageResourceId(mContext)));
        } else {
            // viewHolder.countryImage.setImageDrawable(mContext.getDrawable(services.getImageResourceId(mContext)));
            viewHolder.servicesImage.setImageResource(servicesp.get(i).getImageResourceId(mContext));
        }
    }

    @Override
    public int getItemCount() {
        return servicesp == null ? 0 : servicesp.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView servicesName;
        public TextView servicesCount;
        public ImageView servicesImage;

        public ViewHolder(View itemView) {
            super(itemView);
            servicesName = (TextView) itemView.findViewById(R.id.servicesName);
            servicesCount = (TextView) itemView.findViewById(R.id.servicesCount);
            servicesImage = (ImageView) itemView.findViewById(R.id.countryImage);
        }

    }

}
