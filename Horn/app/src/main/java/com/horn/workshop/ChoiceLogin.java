package com.horn.workshop;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;


/**
 * Created by vighnu on 11/29/2015.
 */
public class ChoiceLogin extends AppCompatActivity implements View.OnClickListener {

    Button hornLogin, fbLobin, gpLogin;
    TextView guestLogin, hornSignup, textView01;

    //Google signIn
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_login);

        hornLogin = (Button) findViewById(R.id.horn_login);
        fbLobin = (Button) findViewById(R.id.fb_login);
        gpLogin = (Button) findViewById(R.id.gp_login);

        hornLogin.setOnClickListener(this);
        fbLobin.setOnClickListener(this);
        gpLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.horn_login:
                startActivity(new Intent(ChoiceLogin.this, Login.class));
                break;
            case R.id.fb_login:
                startActivity(new Intent(ChoiceLogin.this, FacebookLoginSetup.class));
                break;
            case R.id.gp_login:
                startActivity(new Intent(ChoiceLogin.this, GoogleLoginSetup.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        boolean r = moveTaskToBack(true);
        //r is false !! using API LEVEL 8
    }
}