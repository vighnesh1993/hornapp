package com.horn.workshop;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by vighnu on 12/10/2015.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIME_OUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://crabfolk.comli.com/";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallBack userCallback) {
        progressDialog.show();
     //   new StoreUserdataAsyncTask(user, userCallback).execute();

    }

    public void fetchUserDataInBackground(User user, GetUserCallBack userCallback) {
        progressDialog.show();
     //   new fetchUserDataAsyncTask(user,userCallback).execute();

    }
}