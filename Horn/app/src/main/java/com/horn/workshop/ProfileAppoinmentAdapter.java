package com.horn.workshop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sariga on 2/5/2016.
 */
public class ProfileAppoinmentAdapter extends RecyclerView.Adapter<ProfileAppoinmentAdapter.MyViewHolder>  {

        private String[] mDataset;
    public static ArrayList<ProfileappointmentData> apnmnt;
    private Context context;
    private SMLocalStore smLocalStore;
  //  private ArrayList<ProfileappointmentData> apmntDataSet;
    public ProfileAppoinmentAdapter(ArrayList<ProfileappointmentData> services) {

        this.apnmnt = services;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public String apt_id;
        TextView id;
        TextView date;
        TextView workshop;
        TextView status;
        TextView bookedon;
        TextView pricetotal;
        TextView type;
        LinearLayout total_lyt;
           public MyViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();

            this.id = (TextView) itemView.findViewById(R.id.appointment_id);
            this.date = (TextView) itemView.findViewById(R.id.appointment_dateon);
            this.workshop = (TextView) itemView.findViewById(R.id.appointment_workshop);
            this.status = (TextView) itemView.findViewById(R.id.appointment_status);
            this.bookedon = (TextView) itemView.findViewById(R.id.appointment_bookedon);
            this.pricetotal = (TextView) itemView.findViewById(R.id.appointment_total);
               this.total_lyt = (LinearLayout) itemView.findViewById(R.id.appointment_total_lyt);

            // this.Booknow = (Button) itemView.findViewById(R.id.booknow);
            itemView.setOnClickListener(this);
            //  Booknow.setOnClickListener(this);
        }

        public void setItem(String item) {
            apt_id = item.toString();
        }

        @Override
        public void onClick(View v) {

            smLocalStore = new SMLocalStore(context);
            smLocalStore.setProfileAppointment(apt_id);

            Intent intent1 = new Intent(context, ProfileAppointmentDetail.class);
            context.startActivity(intent1);
            //SMAdapteronclick smAdapteronclick = new SMAdapteronclick();
            //smAdapteronclick.onclick_nav();
            //  workshoplist.new_activity_launch(mItem);

            //this.activity = SMAdapter.this;


        }
    }

        // Create new views (invoked by the layout manager)
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_appoinment_view, parent, false);

            //  view.setOnClickListener(MainAcotivity.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            TextView id1=holder.id;
            TextView date1=holder.date;
            TextView workshop1=holder.workshop;
            TextView status1=holder.status;
            TextView bookedon1=holder.bookedon;
            TextView pricetotal1=holder.pricetotal;
            id1.setText(": "+apnmnt.get(listPosition).getApmntids());
            date1.setText(": "+apnmnt.get(listPosition).getDates());
            workshop1.setText(": "+apnmnt.get(listPosition).getPartner_ids());
            status1.setText(": "+apnmnt.get(listPosition).getStatuss());
            bookedon1.setText(": "+apnmnt.get(listPosition).getAppointment_ons());
            pricetotal1.setText(": ₹ " + apnmnt.get(listPosition).getprice_total());
            holder.setItem(apnmnt.get(listPosition).getApmntids());
            String types = apnmnt.get(listPosition).gettype();
            if(types.equals("rm"))
            {
                pricetotal1.setVisibility(View.GONE);
                holder.total_lyt.setVisibility(View.GONE);
            }

        }

    @Override
    public int getItemCount() {
        return apnmnt.size();
    }
    }

