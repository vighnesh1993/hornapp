package com.horn.workshop;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sariga on 1/5/2016.
 */
public class SMLocalStore {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    public static final String SM_vehicle = "SM Vehicle";

    public SMLocalStore(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(SM_vehicle, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void setRMhome(String vehicle, String varient,String make,String model) {
        editor.putString("rm_vehicle", vehicle);
        editor.putString("rm_varient", varient);
        editor.putString("rm_make", make);
        editor.putString("rm_model", model);
        editor.commit();
    }

    public void setSMhome(String vehicle, String km, String varient,String make,String model) {
        editor.putString("sm_vehicle", vehicle);
        editor.putString("sm_kms", km);
        editor.putString("sm_varient", varient);
        editor.putString("sm_make", make);
        editor.putString("sm_model", model);
        editor.commit();
    }

    public void setSMdesc(String description) {
        editor.putString("sm_description", description);
        editor.commit();
    }

    public void setRMdesc(String description) {
        editor.putString("rm_description", description);
        editor.commit();
    }

    public void setSMservice(String labour, String total, String selectedstring) {
        editor.putString("sm_labour", labour);
        editor.putString("sm_total", total);
        editor.putString("sm_Selectedservice", selectedstring);
        editor.commit();
    }
    public void setRMservice(String selectedstring) {

        editor.putString("rm_Selectedservice", selectedstring);
        editor.commit();
    }

    public void setSMworkshoplist(String workshopid) {
        editor.putString("sm_workshopid", workshopid);
        editor.commit();
    }
    public void setRMworkshoplist(String workshopid) {
        editor.putString("rm_workshopid", workshopid);
        editor.commit();
    }
    public void setProfileAppointment(String apt_id){
        editor.putString("profile_view_apt_id", apt_id);
        editor.commit();
    }
    public void setSmwCurrentLatlng(String lat, String log) {
        editor.putString("hw_lat", lat);
        editor.putString("hw_log", log);
        editor.commit();
    }
    public void setSmdcoordinates(String lat, String log) {
        editor.putString("smd_lat", lat);
        editor.putString("smd_log", log);
        editor.commit();
    }
    public void setBpimages(String s) {

        editor.putString("selectImages", s);
        editor.commit();
    }
    public void clearBpimages() {

        editor.putString("selectImages","");
        editor.commit();
    }
    public String getSMhome_vehicle() {
        return pref.getString("sm_vehicle", null);
    }
    public String getRMhome_vehicle() {
        return pref.getString("rm_vehicle", null);
    }
    public String getProfileAppointment() {
        return pref.getString("profile_view_apt_id", null);
    }

    public String getSMworkshopdetail_id() {
        return pref.getString("sm_workshopid", null);
    }
    public String getRMworkshopdetail_id() {
        return pref.getString("rm_workshopid", null);
    }

    public String getSMhome_kms() {
        return pref.getString("rm_kms", null);
    }
    public String getRMhome_kms() {
        return pref.getString("rm_kms", null);
    }

    public String getSMhome_varient() {
        return pref.getString("sm_varient", null);
    }
    public String getSMhome_make() {
        return pref.getString("sm_make", null);
    }
    public String getRMhome_make() {
        return pref.getString("rm_make", null);
    }
    public String getSMhome_model() {
        return pref.getString("sm_model", null);
    }
    public String getRMhome_model() {
        return pref.getString("rm_model", null);
    }

    public String getSMworkshop_name()
    {
        return pref.getString("sm_workshop_name",null);
    }
    public String getRMworkshop_name()
    {
        return pref.getString("rm_workshop_name",null);
    }
    public void setSMworkshopname(String workshopname)
    {
        editor.putString("sm_workshop_name", workshopname);
        editor.commit();
    }
    public void setRMworkshopname(String workshopname)
    {
        editor.putString("rm_workshop_name", workshopname);
        editor.commit();
    }
    public void setProfileAddCar(String car_id)
    {
        editor.putString("Profile_Add_Car", car_id);
        editor.commit();
    }
    public void setProfileMyCar(String mycar_id)
    {
        editor.putString("Profile_My_Car", mycar_id);
        editor.commit();
    }
    public void setService_total(String service_total)
    {
        editor.putString("Service_total", service_total);
        editor.commit();
    }
    public void setOffer_total(String offer_total)
    {
        editor.putString("offer_total", offer_total);
        editor.commit();
    } public void setRMOffer_total(String offer_total)
    {
        editor.putString("RMoffer_total", offer_total);
        editor.commit();
    }
    public String getProfileMyCar()
    {
        return pref.getString("Profile_My_Car", null);
    }
    public String getService_total()
    {
        return pref.getString("Service_total", null);
    }
    public String getOffer_total()
    {
        return pref.getString("offer_total", null);
    }
    public String getRMOffer_total()
    {
        return pref.getString("RMoffer_total", null);
    }

    public String getProfileAddCar()
    {
        return pref.getString("Profile_Add_Car", null);
    }
    public String getSMservices()
    {
        return pref.getString("sm_Selectedservice", null);
    }
    public String getRMservices()
    {
        return pref.getString("rm_Selectedservice", null);
    }

    public String getSMdesc() {
        return pref.getString("sm_description", null);
    }
    public String getRMdesc() {
        return pref.getString("rm_description", null);
    }

    public String getBpimages() {
        return pref.getString("selectImages", null);
    }

    public HashMap<String, String> getSmwCurrentLatlng() {
        HashMap<String, String> latlog = new HashMap<String, String>();
        latlog.put("lat", pref.getString("hw_lat", null));
        latlog.put("log", pref.getString("hw_log", null));
        return latlog;
    }
    public HashMap<String, String> getSmdcoordinates() {
        HashMap<String, String> smdlatlog = new HashMap<String, String>();
        smdlatlog.put("smdlat", pref.getString("smd_lat", null));
        smdlatlog.put("smdlog", pref.getString("smd_log", null));
        return smdlatlog;
    }
    public void bpDescription(String s) {

        editor.putString("bpdesc", s);
        editor.commit();
    }
    public void clrbpDescription() {

        editor.putString("bpdesc","");
        editor.commit();
    }
    public String getbpDescription() {
        return pref.getString("bpdesc", null);
    }
}
