package com.horn.workshop;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 14-04-2015.
 */
public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.VersionViewHolder> {
    List<String> versionModels;
    Boolean isHomeList = false;
    Boolean isWheelList = false;
    Boolean isElectricalList = false;
    Boolean isBsList = false;
    Boolean isTransList = false;
    Boolean isEngineList = false;
    Boolean isBodyList = false;

    public static List<String> homeActivitiesList = new ArrayList<String>();
    public static List<String> homeActivitiesSubList = new ArrayList<String>();

    public static List<String> wservicesArrayList = new ArrayList<String>();
    public static List<String> wpriceArrayList = new ArrayList<String>();
    public static List<String> wqantityArrayList = new ArrayList<String>();

    public static List<String> eservicesArrayList = new ArrayList<String>();
    public static List<String> epriceArrayList = new ArrayList<String>();
    public static List<String> eqantityArrayList = new ArrayList<String>();

    public static List<String> bservicesArrayList = new ArrayList<String>();
    public static List<String> bpriceArrayList = new ArrayList<String>();
    public static List<String> bqantityArrayList = new ArrayList<String>();

    public static List<String> tservicesArrayList = new ArrayList<String>();
    public static List<String> tpriceArrayList = new ArrayList<String>();
    public static List<String> tqantityArrayList = new ArrayList<String>();

    public static List<String> gservicesArrayList = new ArrayList<String>();
    public static List<String> gpriceArrayList = new ArrayList<String>();
    public static List<String> gqantityArrayList = new ArrayList<String>();

    public static List<String> dservicesArrayList = new ArrayList<String>();
    public static List<String> dpriceArrayList = new ArrayList<String>();
    public static List<String> dqantityArrayList = new ArrayList<String>();

    Context context;
    String service;
    OnItemClickListener clickListener;


    public void setHomeActivitiesList(Context context) {
        String[] listArray = context.getResources().getStringArray(R.array.home_activities);
        String[] subTitleArray = context.getResources().getStringArray(R.array.home_activities_subtitle);
        for (int i = 0; i < listArray.length; ++i) {
            homeActivitiesList.add(listArray[i]);
            homeActivitiesSubList.add(subTitleArray[i]);
        }
    }
    public void setWheelList(Context context) {
        String[] services = context.getResources().getStringArray(R.array.wheel_services);
        String[] price = context.getResources().getStringArray(R.array.price);
        String[] quantity = context.getResources().getStringArray(R.array.quantity);
        for (int i = 0; i < services.length; ++i) {
            wservicesArrayList.add(services[i]);
            wpriceArrayList.add(price[i]);
            wqantityArrayList.add(quantity[i]);
        }
    }
    public void setElectricalList(Context context) {
        String[] services = context.getResources().getStringArray(R.array.electrical_services);
        String[] price = context.getResources().getStringArray(R.array.price);
        String[] quantity = context.getResources().getStringArray(R.array.quantity);
        for (int i = 0; i < services.length; ++i) {
            eservicesArrayList.add(services[i]);
            epriceArrayList.add(price[i]);
            eqantityArrayList.add(quantity[i]);
        }
    }
    public void setBsList(Context context) {
        String[] services = context.getResources().getStringArray(R.array.bs_services);
        String[] price = context.getResources().getStringArray(R.array.price);
        String[] quantity = context.getResources().getStringArray(R.array.quantity);
        for (int i = 0; i < services.length; ++i) {
            bservicesArrayList.add(services[i]);
            bpriceArrayList.add(price[i]);
            bqantityArrayList.add(quantity[i]);
        }
    }
    public void setTransList(Context context) {
        String[] services = context.getResources().getStringArray(R.array.trans_services);
        String[] price = context.getResources().getStringArray(R.array.price);
        String[] quantity = context.getResources().getStringArray(R.array.quantity);
        for (int i = 0; i < services.length; ++i) {
            tservicesArrayList.add(services[i]);
            tpriceArrayList.add(price[i]);
            tqantityArrayList.add(quantity[i]);
        }
    }
    public void setEngineList(Context context) {
        String[] services = context.getResources().getStringArray(R.array.engine_services);
        String[] price = context.getResources().getStringArray(R.array.price);
        String[] quantity = context.getResources().getStringArray(R.array.quantity);
        for (int i = 0; i < services.length; ++i) {
            gservicesArrayList.add(services[i]);
            gpriceArrayList.add(price[i]);
            gqantityArrayList.add(quantity[i]);
        }
    }
    public void setBodyList(Context context) {
        String[] services = context.getResources().getStringArray(R.array.body_services);
        String[] price = context.getResources().getStringArray(R.array.price);
        String[] quantity = context.getResources().getStringArray(R.array.quantity);
        for (int i = 0; i < services.length; ++i) {
            dservicesArrayList.add(services[i]);
            dpriceArrayList.add(price[i]);
            dqantityArrayList.add(quantity[i]);
        }
    }

    private void makeArrayClear(){
                wservicesArrayList.clear();
                wpriceArrayList.clear();
                wqantityArrayList .clear();
                eservicesArrayList.clear();
                epriceArrayList.clear();
                eqantityArrayList.clear();
                bservicesArrayList.clear();
                bpriceArrayList.clear();
                bqantityArrayList.clear();
                tservicesArrayList.clear();
                tpriceArrayList.clear();
                tqantityArrayList.clear();
                gservicesArrayList.clear();
                gpriceArrayList.clear();
                gqantityArrayList .clear();
                dservicesArrayList.clear();
                dpriceArrayList.clear();
                dqantityArrayList.clear();
    }

    public SimpleRecyclerAdapter(String service, Context context) {

        isHomeList = false;
        this.context = context;
        this.service = service;
        if(service.equals("wheel")){
            Toast.makeText(context, "Herefffffffffffffffffffffff", Toast.LENGTH_LONG).show();
            isWheelList = true;
            wservicesArrayList.clear();
            wpriceArrayList.clear();
            wqantityArrayList .clear();
            setWheelList(context);
        }else if(service.equals("eletrical")){
            isElectricalList = true;
            eservicesArrayList.clear();
            epriceArrayList.clear();
            eqantityArrayList.clear();
            setElectricalList(context);
        }else if(service.equals("bs")){
            isBsList = true;
            bservicesArrayList.clear();
            bpriceArrayList.clear();
            bqantityArrayList.clear();
            setBsList(context);
        }else if(service.equals("trans")){
            isTransList = true;
            tservicesArrayList.clear();
            tpriceArrayList.clear();
            tqantityArrayList.clear();
            setTransList(context);
        }else if(service.equals("engine")){
            isEngineList = true;
            gservicesArrayList.clear();
            gpriceArrayList.clear();
            gqantityArrayList .clear();
            setEngineList(context);
        }else{
            isBodyList = true;
            dservicesArrayList.clear();
            dpriceArrayList.clear();
            dqantityArrayList.clear();
             setBodyList(context);
        }
        //setHomeActivitiesList(context);
    }

    public SimpleRecyclerAdapter(List<String> versionModels) {
        isHomeList = false;
        this.versionModels = versionModels;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i) {
        if (isWheelList) {
//            versionViewHolder.title.setText(homeActivitiesList.get(i));
//            versionViewHolder.subTitle.setText(homeActivitiesSubList.get(i));
            versionViewHolder.service_name.setText(wservicesArrayList.get(i));
            versionViewHolder.price.setText("₹ "+wpriceArrayList.get(i));
            versionViewHolder.quantity_value.setText(wqantityArrayList.get(i));
        } else if(isElectricalList){
            versionViewHolder.service_name.setText(eservicesArrayList.get(i));
            versionViewHolder.price.setText("₹ "+epriceArrayList.get(i));
            versionViewHolder.quantity_value.setText(eqantityArrayList.get(i));
        } else if(isBsList){
            versionViewHolder.service_name.setText(bservicesArrayList.get(i));
            versionViewHolder.price.setText("₹ "+bpriceArrayList.get(i));
            versionViewHolder.quantity_value.setText(bqantityArrayList.get(i));
        } else if(isTransList){
            versionViewHolder.service_name.setText(tservicesArrayList.get(i));
            versionViewHolder.price.setText("₹ "+tpriceArrayList.get(i));
            versionViewHolder.quantity_value.setText(tqantityArrayList.get(i));
        } else if(isEngineList){
            versionViewHolder.service_name.setText(gservicesArrayList.get(i));
            versionViewHolder.price.setText("₹ "+gpriceArrayList.get(i));
            versionViewHolder.quantity_value.setText(gqantityArrayList.get(i));
        }else if(isBodyList){
            versionViewHolder.service_name.setText(dservicesArrayList.get(i));
            versionViewHolder.price.setText("₹ "+dpriceArrayList.get(i));
            versionViewHolder.quantity_value.setText(dqantityArrayList.get(i));
        }
    }
    @Override
    public int getItemCount() {
        if (isWheelList||isElectricalList||isBsList|| isTransList||isEngineList||isBodyList)
            return wservicesArrayList == null ? 0 : wservicesArrayList.size();
        else
            return versionModels == null ? 0 : versionModels.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardItemLayout;
        TextView title,service_name,price,quantity_value;
        TextView subTitle;

        public VersionViewHolder(View itemView) {
            super(itemView);

            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
//            title = (TextView) itemView.findViewById(R.id.listitem_name);
//            subTitle = (TextView) itemView.findViewById(R.id.listitem_subname);

            service_name = (TextView) itemView.findViewById(R.id.service_name);
            price = (TextView) itemView.findViewById(R.id.price);
            quantity_value = (TextView) itemView.findViewById(R.id.quanty_value);

            if (isHomeList) {
                itemView.setOnClickListener(this);
            } else {
              //  subTitle.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}
