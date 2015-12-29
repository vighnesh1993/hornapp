package com.horn.workshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.facebook.Profile;

import java.io.ByteArrayOutputStream;

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

    public void setFBUserLoggedIn(boolean fbloggedIn) {
        editor.putBoolean("fbloggedIn", fbloggedIn);
        editor.commit();

    }

    public void setGoogleUserLoggedIn(boolean googleloggedIn) {
        editor.putBoolean("googleloggedIn", googleloggedIn);
        editor.commit();

    }

    public boolean getUserLoggedIn() {
        if (pref.getBoolean("loggedIn", false)) {
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

    public boolean getGoogleUserLoggedIn() {
        if (pref.getBoolean("googleloggedIn", false)) {
            return true;
        } else {
            return false;
        }
    }

    public void storeFacebookUser(Profile profile) {
        editor.putString("FbUsername",profile.getName());
        editor.commit();
    }

    public FacebookUser getFacebookUserData() {
        String name = pref.getString("FbUsername", "");
        FacebookUser fbUser = new FacebookUser(name);
        return fbUser;
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
    public void storeEmailForLogin(String email){
        editor.putString("emailforlogin", email);
        editor.commit();
    }

    public String getEmailForLogin(){
        String email = pref.getString("emailforlogin",null);
        return email;
    }
}

