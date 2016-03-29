package com.horn.workshop;

import android.util.Log;

/**
 * Created by Sariga on 2/5/2016.
 */
public class ProfileappointmentData {

    String partner_ids;
    String apmntids;
    String dates;
    String statuss;
    String appointment_ons;
    String price_totals;
    String type;

//int id_;

    public ProfileappointmentData(String partner_ids, String apmntids, String dates, String statuss, String appointment_ons,String price_totals,String type) {
        this.partner_ids = partner_ids;
                this.apmntids = apmntids;
                this.dates=dates;
                this.statuss=statuss;
                this.appointment_ons=appointment_ons;
                this.price_totals=price_totals;
                this.type = type;

    }


    public String getPartner_ids() {
        return partner_ids;
    }


    public String getApmntids() {
        return apmntids;
    }

    public String getDates() {
        return dates;
    }

    public String getStatuss() {
        return statuss;
    }

    public String getAppointment_ons() {
        return appointment_ons;
    }
    public String getprice_total() {
        return price_totals;
    }
public String gettype(){return type;}


}
