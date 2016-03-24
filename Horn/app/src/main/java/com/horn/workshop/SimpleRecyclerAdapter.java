package com.horn.workshop;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.AppConfig;
import app.AppController;

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
    String service,carid_rma;
    OnItemClickListener clickListener;
    SMLocalStore smLocalStore;
   public static String [] idArray,areaArray,qtyArray,unitpriceArray,itemArray,laborArray;
    String status;


public void getDbdetails(final String carid_rms, String service, final VolleyCallback callback)
{
    StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_RM_DATA, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("RM_rreponse", "RM_home Response: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject != null) {
                    int len = jsonObject.length();
                    status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONArray idArrayj=jsonObject.getJSONArray("id");
                        JSONArray areaArrayj=jsonObject.getJSONArray("area");
                        JSONArray qtyArrayj=jsonObject.getJSONArray("quantity");
                        JSONArray unitpriceArrayj=jsonObject.getJSONArray("unitprice");
                        JSONArray itemArrayj=jsonObject.getJSONArray("items");
                        JSONArray laborArrayj=jsonObject.getJSONArray("labor");

                        idArray = new String[idArrayj.length()];
                        areaArray= new String[areaArrayj.length()];
                        qtyArray=new String[qtyArrayj.length()];
                        unitpriceArray =new String[unitpriceArrayj.length()];
                        itemArray=new String[itemArrayj.length()];
                        laborArray=new String[laborArrayj.length()];
                        for (int i = 0; i < idArrayj.length(); i++)
                        {
                            idArray[i] = idArrayj.getString(i);
                            areaArray[i]= areaArrayj.getString(i);
                            qtyArray[i]= qtyArrayj.getString(i);
                            unitpriceArray[i] = unitpriceArrayj.getString(i);
                            itemArray[i]= itemArrayj.getString(i);
                            laborArray[i]= laborArrayj.getString(i);

                        }

                        callback.onSuccess(status);
                    }

                    }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

        @Override
        protected Map<String, String> getParams() {
            smLocalStore = new SMLocalStore(context);
            // Posting parameters to login url

            Map<String, String> params = new HashMap<String, String>();
            params.put("carid_rm", carid_rms);
            params.put("rm_home", "rm_home");

            return params;
        }

    };

    AppController.getInstance().addToRequestQueue(stringRequest, "fff");


}
    public interface VolleyCallback{
        void onSuccess(String result);
    }
    public void setHomeActivitiesList(Context context) {
        String[] listArray = context.getResources().getStringArray(R.array.home_activities);
        String[] subTitleArray = context.getResources().getStringArray(R.array.home_activities_subtitle);
        for (int i = 0; i < listArray.length; ++i) {
            homeActivitiesList.add(listArray[i]);
            homeActivitiesSubList.add(subTitleArray[i]);
        }
    }
    public void setWheelList(final Context context, String carid_rm) {

        getDbdetails(carid_rm,"test",new VolleyCallback(){
            @Override
            public void onSuccess(String result){
Log.d("success",result);
                String[] services = context.getResources().getStringArray(R.array.wheel_services);
                String[] price = context.getResources().getStringArray(R.array.price);
                String[] quantity = context.getResources().getStringArray(R.array.quantity);
                for (int i = 0; i < services.length; ++i) {

                    wservicesArrayList.add(services[i]);
                    wpriceArrayList.add(price[i]);
                    wqantityArrayList.add(quantity[i]);

                }
           }
        });



    }
    public void setElectricalList(Context context) {
        String[] services = context.getResources().getStringArray(R.array.electrical_services);
        String[] price = context.getResources().getStringArray(R.array.price);
        String[] quantity = context.getResources().getStringArray(R.array.quantity);
//        String[] services = itemArray;
//        String[] price = unitpriceArray;
//        String[] quantity = qtyArray;
//        String[] area = areaArray;
        for (int i = 0; i < services.length; ++i) {
//            if (area[i].equals("Electrical")) {

                eservicesArrayList.add(services[i]);
                epriceArrayList.add(price[i]);
                eqantityArrayList.add(quantity[i]);
//            }
        }
    }
    public void setBsList(Context context) {
        String[] services = context.getResources().getStringArray(R.array.bs_services);
        String[] price = context.getResources().getStringArray(R.array.price);
        String[] quantity = context.getResources().getStringArray(R.array.quantity);
//        String[] services = itemArray;
//        String[] price = unitpriceArray;
//        String[] quantity = qtyArray;
//        String[] area = areaArray;
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
//        String[] services = itemArray;
//        String[] price = unitpriceArray;
//        String[] quantity = qtyArray;
//        String[] area = areaArray;
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
//        String[] services = itemArray;
//        String[] price = unitpriceArray;
//        String[] quantity = qtyArray;
//        String[] area = areaArray;
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
//        String[] services = itemArray;
//        String[] price = unitpriceArray;
//        String[] quantity = qtyArray;
//        String[] area = areaArray;
        for (int i = 0; i < services.length; ++i) {

                dservicesArrayList.add(services[i]);
                dpriceArrayList.add(price[i]);
                dqantityArrayList.add(quantity[i]);

        }
    }
    public SimpleRecyclerAdapter(final String service, String carid_rm, final Context context) {

        isHomeList = false;
        this.context = context;
        this.service = service;
        this.carid_rma = carid_rm;

        if (service.equals("wheel")) {
            // Toast.makeText(context, "Herefffffffffffffffffffffff", Toast.LENGTH_LONG).show();
            isWheelList = true;
            wservicesArrayList.clear();
            wpriceArrayList.clear();
            wqantityArrayList.clear();
            setWheelList(context, carid_rma);
        } else if (service.equals("eletrical")) {
            isElectricalList = true;
            eservicesArrayList.clear();
            epriceArrayList.clear();
            eqantityArrayList.clear();
            setElectricalList(context);
        } else if (service.equals("bs")) {
            isBsList = true;
            bservicesArrayList.clear();
            bpriceArrayList.clear();
            bqantityArrayList.clear();
            setBsList(context);
        } else if (service.equals("trans")) {
            isTransList = true;
            tservicesArrayList.clear();
            tpriceArrayList.clear();
            tqantityArrayList.clear();
             setTransList(context);
        } else if (service.equals("engine")) {
            isEngineList = true;
            gservicesArrayList.clear();
            gpriceArrayList.clear();
            gqantityArrayList.clear();
             setEngineList(context);
        } else {
            isBodyList = true;
            dservicesArrayList.clear();
            dpriceArrayList.clear();
            dqantityArrayList.clear();
            setBodyList(context);
        }

        // setHomeActivitiesList(context);

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
        if (isWheelList)
            return wservicesArrayList == null ? 0 : wservicesArrayList.size();
        else if (isElectricalList)
            return eservicesArrayList == null ? 0 : eservicesArrayList.size();
        else if (isBsList)
            return bservicesArrayList == null ? 0 : bservicesArrayList.size();
        else if (isTransList)
            return tservicesArrayList == null ? 0 : tservicesArrayList.size();
        else if (isEngineList)
            return gservicesArrayList == null ? 0 : gservicesArrayList.size();
        else if (isBodyList)
            return dservicesArrayList == null ? 0 : dservicesArrayList.size();
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
