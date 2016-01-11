package activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.horn.workshop.MainActivity;
import com.horn.workshop.R;
import com.horn.workshop.UserLocalStore;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import helper.SQLiteHandler;

/**
 * Created by vighnu on 12/4/2015.
 */
public class FacebookLoginSetup extends AppCompatActivity implements View.OnClickListener {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileTracker mProfileTracker;
    private AccessTokenTracker mTokenTracker;
    private TextView info;
    private UserLocalStore userLocalStore;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setupTokenTracker();
        setupProfileTracker();

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();

        setContentView(R.layout.facebook_login);
        info = (TextView) findViewById(R.id.status);
        userLocalStore = new UserLocalStore(this);
        db = new SQLiteHandler(getApplicationContext());

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                try{
                    AccessToken accessToken = loginResult.getAccessToken();
                    Profile profile = Profile.getCurrentProfile();
                    Toast.makeText(getApplicationContext(), R.string.fg_login_success, Toast.LENGTH_LONG).show();
                    userLocalStore.setFBUserLoggedIn(true);
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    try {

                                        String userID = (String) object.get("id");
                                        String userName = (String) object.get("name");
                                        String userEmail = (String) object.get("email");
                                        String pictureUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                        db.storeFBuser(userName, userEmail);
                                        userLocalStore.StoreFBuserID(pictureUrl);
                                        startActivity(new Intent(FacebookLoginSetup.this, MainActivity.class));
                                        finish();
                                        Log.d("o/p", "name");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,birthday,picture");
                    request.setParameters(parameters);
                    request.executeAsync();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
                info.setText(R.string.fb_login_cancel);
            }

            @Override
            public void onError(FacebookException e) {
                info.setText(R.string.fb_login_failed);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("vighnu", "" + currentAccessToken);
                if (currentAccessToken == null) {
                    //User logged out
                    Toast.makeText(getApplicationContext(), R.string.fb_logedout, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d("vighnu", "" + currentProfile);
                info.setText(constructWelcomeMessage(currentProfile));

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    public String constructWelcomeMessage(Profile profile) {
        StringBuilder stringBuffer;
        stringBuffer = new StringBuilder();
        if (profile != null) {
            stringBuffer.append("" + profile.getName());
        }
        return stringBuffer.toString();
    }
    @Override
    public void onClick(View v) {

    }
}
