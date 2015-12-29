package activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.horn.workshop.R;

import service.HttpService;

public class OtpVerification extends AppCompatActivity {

    private TextInputLayout inputLayoutPhone;
    private Button getVerify;
    private EditText inputOtp;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        getWindow().setLayout((int) (width * .8), (int) (height * .5));

        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        getVerify = (Button) findViewById(R.id.btn_verify);
        getVerify.setEnabled(false);
        inputOtp = (EditText) findViewById(R.id.input_otp);
        inputOtp.setFocusable(false);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
      //  progressBar.setVisibility(View.GONE);

        getVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });

        Runnable r = new Runnable() {
            @Override
            public void run(){
                manualVerification(); //<-- put your code in here.
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 10000);

    }

    /**
     * sending the OTP to server and activating the user
     */
    private void verifyOtp() {
        String otp = inputOtp.getText().toString().trim();

        if (!otp.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
            grapprIntent.putExtra("otp", otp);
            startService(grapprIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }
    private void manualVerification(){
       inputOtp.setFocusableInTouchMode(true);
        getVerify.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this,"Automatic OTP detection Failed! ",Toast.LENGTH_LONG).show();
    }
    public void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }

}
