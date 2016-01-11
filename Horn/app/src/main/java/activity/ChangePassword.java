package activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.horn.workshop.R;
import com.horn.workshop.UserLocalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;


/**
 * Created by user on 07-01-2016.
 */
public class ChangePassword extends AppCompatActivity {
    private Button pswdResetBtn;
    private TextInputLayout inputLayoutPassword, inputLayoutCPassword;
    private EditText inputPassword, inputConfirmPassword;
    private ProgressDialog pDialog;
    private UserLocalStore userLocalStore;
    private static final String TAG = ChangePassword.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        getWindow().setLayout((int) (width * .8), (int) (height * .5));

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        userLocalStore = new UserLocalStore(this);

        pswdResetBtn = (Button) findViewById(R.id.btn_pswdReset);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutCPassword = (TextInputLayout) findViewById(R.id.input_layout_Cpassword);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputConfirmPassword.addTextChangedListener(new MyTextWatcher(inputConfirmPassword));

        pswdResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }
    private void submitForm() {

         if (!validatePassword()) {
            return;
        } else if (!validateConfrimPassword()) {
            return;
        } else if (matchPassword()) {

             HashMap<String, String> user = userLocalStore.getUserForPasswordChange();
             String fpUserEmail = user.get("fpUserEmail");
             String fpUserPhone = user.get("fpUserPhone");
             String password = inputPassword.getText().toString().trim();
             resetPassword(fpUserEmail, fpUserPhone, password);
            return;
        }

    }

    private boolean validateConfrimPassword() {
        if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
            inputLayoutCPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputLayoutCPassword);
            return false;
        } else {
            inputLayoutCPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }
    private boolean matchPassword() {
        String password = inputPassword.getText().toString().trim();
        String cPassword = inputConfirmPassword.getText().toString().trim();

        if (!cPassword.equals(password)) {
            inputLayoutCPassword.setError(getString(R.string.err_msg_Cpassword));
            requestFocus(inputConfirmPassword);
            return false;
        } else {
            inputLayoutCPassword.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_password:
                    validatePassword();
                    break;
                case R.id.input_confirm_password:
                    validateConfrimPassword();
                    matchPassword();
                    break;
            }
        }
    }

    private void resetPassword(final String email, final String phone, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_reset_password";

        pDialog.setMessage("Processing ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        String message = jObj.getString("message");
                        startActivity(new Intent(ChangePassword.this, Login.class));
                        userLocalStore.setUserRequestForChangePassword(false);
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Reset Password Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);
                return params;
            }

        };

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}






