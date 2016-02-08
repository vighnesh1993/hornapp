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

//int id_;

    public ProfileappointmentData(String partner_ids, String apmntids, String dates, String statuss, String appointment_ons) {
        this.partner_ids = partner_ids;
                this.apmntids = apmntids;
                this.dates=dates;
                this.statuss=statuss;
                this.appointment_ons=appointment_ons;


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



}
