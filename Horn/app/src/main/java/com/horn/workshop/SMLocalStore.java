package com.horn.workshop;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

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

    public void setSMhome(String vehicle, String km, String varient)
    {
        editor.putString("sm_vehicle", vehicle);
        editor.putString("sm_kms", km);
        editor.putString("sm_varient",varient);
        editor.commit();
    }

    public void setSMdesc(String description)
    {
        editor.putString("sm_description", description);
        editor.commit();
    }

    public void setSMservice(String labour, String total, String selectedstring)
    {
        editor.putString("sm_labour", labour);
        editor.putString("sm_total",total);
        editor.putString("sm_Selectedservice", selectedstring);
        editor.commit();
    }

    public void setSMworkshoplist(String workshopid)
    {
        editor.putString("sm_workshopid", workshopid);
        editor.commit();
    }

    public String getSMhome_vehicle()
    {
        return pref.getString("sm_vehicle", null);
    }

    public String getSMworkshopdetail_id()
    {
        return pref.getString("sm_workshopid", null);
    }

    public String getSMhome_kms()
    {
        return pref.getString("sm_kms",null);
    }

    public String getSMhome_varient()
    {
        return pref.getString("sm_varient",null);
    }

    public String getSMdesc()
    {
        return pref.getString("sm_description", null);
    }
}
