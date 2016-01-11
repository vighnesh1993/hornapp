package activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.horn.workshop.R;
import com.horn.workshop.UserLocalStore;


/**
 * Created by vighnu on 11/29/2015.
 */
public class ChoiceLogin extends AppCompatActivity implements View.OnClickListener {

    Button hornLogin, fbLobin, gpLogin;
    TextView guestLogin, hornSignup, textView01;
    private UserLocalStore userLocalStore;
    //Google signIn
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_login);

        hornLogin = (Button) findViewById(R.id.horn_login);
        fbLobin = (Button) findViewById(R.id.fb_login);
        gpLogin = (Button) findViewById(R.id.gp_login);
        hornSignup = (TextView) findViewById(R.id.horn_signUp);
        guestLogin = (TextView) findViewById(R.id.horn_guest_login);

        hornLogin.setOnClickListener(this);
        fbLobin.setOnClickListener(this);
        gpLogin.setOnClickListener(this);
        hornSignup.setOnClickListener(this);
        guestLogin.setOnClickListener(this);


        userLocalStore = new UserLocalStore(this);
        checkVerified();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.horn_login:
                startActivity(new Intent(ChoiceLogin.this, Login.class));
                checkVerified();
                break;
            case R.id.fb_login:
                startActivity(new Intent(ChoiceLogin.this, FacebookLoginSetup.class));
                checkVerified();
                break;
            case R.id.gp_login:
                startActivity(new Intent(ChoiceLogin.this, GoogleLoginSetup.class));
                checkVerified();
                break;
            case R.id.horn_signUp:
                startActivity(new Intent(ChoiceLogin.this, Register.class));
                checkVerified();
                break;
            case R.id.horn_guest_login:
                startActivity(new Intent(ChoiceLogin.this, GuestLogin.class));
                break;
        }
    }

    private void checkVerified(){
        if(userLocalStore.isWaitingForSms()){
            startActivity(new Intent(ChoiceLogin.this, OtpVerification.class));
        }
    }

    @Override
    public void onBackPressed() {
        boolean r = moveTaskToBack(true);
        //r is false !! using API LEVEL 8
    }
}