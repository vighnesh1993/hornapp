package com.horn.workshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.facebook.Profile;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * Created by vighnu on 12/5/2015.
 */
public class UserLocalStore {


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    public static final String SP_NAME = "userDetails";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_MOBILE_NUMBER = "mobile_number";

    public UserLocalStore(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(SP_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUserLoggedIn(boolean loggedIn) {
        editor.putBoolean("loggedIn", loggedIn);
        editor.commit();

    }

    public void setGuestUserLoggedIn(boolean loggedIn) {
        editor.putBoolean("gstLoggedIn", loggedIn);
        editor.commit();

    }

    public void setFBUserLoggedIn(boolean fbloggedIn) {
        editor.putBoolean("fbloggedIn", fbloggedIn);
        editor.commit();

    }

    public void setGoogleUserLoggedIn(boolean googleloggedIn) {
        editor.putBoolean("googleloggedIn", googleloggedIn);
        editor.commit();

    }

    public boolean getGoogleUserLoggedIn() {
        if (pref.getBoolean("googleloggedIn", false)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getUserLoggedIn() {
        if (pref.getBoolean("loggedIn", false)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getGuestUserLoggedIn() {
        if (pref.getBoolean("gstLoggedIn", false)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getFBUserLoggedIn() {
        if (pref.getBoolean("fbloggedIn", false)) {
            return true;
        } else {
            return false;
        }
    }

    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }

    public String getMobileNumber() {
        return pref.getString(KEY_MOBILE_NUMBER, null);
    }

    public void setSmsVerified(boolean verified) {
        editor.putBoolean("verified", verified);
        editor.commit();

    }

    public void storeEmailForLogin(String email) {
        editor.putString("emailforlogin", email);
        editor.commit();
    }

    public String getEmailForLogin() {
        String email = pref.getString("emailforlogin", null);
        return email;
    }

    public void StoreFBuserID(String userId) {
        editor.putString("fbuserid", userId);
        editor.commit();
    }

    public String getFBuserProfileUrl() {
        String proPicUrl = pref.getString("fbuserid", null);
        return proPicUrl;
    }

    public void storeGoogleUserProfileUrl(String gUrl) {
        editor.putString("gUserUrl", gUrl);
        editor.commit();
    }

    public String getGoogleUserProfileUrl() {
        String gUrl = pref.getString("gUserUrl", null);
        return gUrl;
    }

    public void storeGuestUser(String name, String email) {
        editor.putString("guesName", name);
        editor.putString("guestEmail", email);
        editor.commit();
    }

    public HashMap<String, String> getGuestUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("name", pref.getString("guesName", null));
        user.put("email", pref.getString("guestEmail", null));
        return user;
    }

    public void storeUserForPasswordChange(String email, String phone) {
        editor.putString("fpUserEmail", email);
        editor.putString("fpUserPhone", phone);
        editor.commit();
    }

    public HashMap<String, String> getUserForPasswordChange() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("fpUserEmail", pref.getString("fpUserEmail", null));
        user.put("fpUserPhone", pref.getString("fpUserPhone", null));
        return user;
    }

    public void setUserRequestForChangePassword(boolean requsted) {
        editor.putBoolean("passwordRequest", requsted);
        editor.commit();
    }

    public boolean getUserRequestForChangePassword() {
        if (pref.getBoolean("passwordRequest", false)) {
            return true;
        } else {
            return false;
        }
    }
    public void setManualLocationLatlong(String mLatlong) {
        editor.putString("mLatLong", mLatlong);
        editor.commit();
    }
    public String getManualLocationLatlong() {
        return pref.getString("mLatLong","");
    }
    public void setManualLocation(String mLocation) {
        editor.putString("mLocation", mLocation);
        editor.commit();
    }

    public String getManualLocation() {
        return pref.getString("mLocation","");
    }
    public boolean getManualLocationChoosen() {
        if (pref.getBoolean("gLchoosen", false)) {
            return true;
        } else {
            return false;
        }
    }
    public void setManualLocationChoosen(boolean mLocation) {
        editor.putBoolean("gLchoosen", mLocation);
        editor.commit();

    }
    public void setMylocationLatlog(String myLocation) {
        editor.putString("myLocation", myLocation);
        editor.commit();

    }public void setMyManuallocationLatlog(String mymLocation) {
        editor.putString("mymLocation", mymLocation);
        editor.commit();

    }public String getMyManuallocationLatlog() {
        return pref.getString("mymLocation", "");

    }public String getMylocationLatlog() {
        return pref.getString("myLocation","");

    }

    public void setGPSLatlong(String mLatlong) {
        editor.putString("mGpsLatLong", mLatlong);
        editor.commit();
    }
    public String getGPSLatlong() {
        return pref.getString("mGpsLatLong","");
    }

    public boolean getPlaceActivityGPS() {
        if (pref.getBoolean("placegps", false)) {
            return true;
        } else {
            return false;
        }
    }
    public void setPlaceActivityGPS(boolean placeGps) {
        editor.putBoolean("placegps", placeGps);
        editor.commit();
    }
}

