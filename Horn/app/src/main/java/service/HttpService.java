package service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.horn.workshop.UserLocalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activity.ChangePassword;
import activity.Login;
import activity.OtpVerification;
import app.AppConfig;
import app.AppController;

/**
 * Created by vighnu on 12/21/2015.
 */
public class HttpService extends IntentService {
    private UserLocalStore userLocalStore;
    private OtpVerification otpVerification;
    private static String TAG = HttpService.class.getSimpleName();
    public HttpService() {
        super(HttpService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String otp = intent.getStringExtra("otp");
            verifyOtp(otp);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Posting the OTP to server and activating the user
     *
     * @param otp otp received in the SMS
     */
    private void verifyOtp(final String otp) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VERIFY_OTP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    userLocalStore = new UserLocalStore(getApplicationContext());
                    JSONObject responseObj = new JSONObject(response);

                    // Parsing json object response
                    // response will be a json object
                    boolean error = responseObj.getBoolean("error");
                    String message = responseObj.getString("message");

                    if (!error) {
 //                        parsing the user profile information
                        JSONObject profileObj = responseObj.getJSONObject("user");

                       // String name = profileObj.getString("name");
                        String email = profileObj.getString("email");
                      //  String mobile = profileObj.getString("mobile");


                        userLocalStore.storeEmailForLogin(email);
                        userLocalStore.setIsWaitingForSms(false);

                        if (userLocalStore.getUserRequestForChangePassword()){
                            Toast.makeText(getApplicationContext(),"First",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(HttpService.this, ChangePassword.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),"Second",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(HttpService.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "No Network Connection", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", otp);

                Log.e(TAG, "Posting params: " + params.toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

}
