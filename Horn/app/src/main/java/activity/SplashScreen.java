package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.horn.workshop.MainActivity;
import com.horn.workshop.R;
import com.horn.workshop.UserLocalStore;

/**
 * Created by vighnu on 11/29/2015.
 */
public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIMEOUT = 3000;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(this);

        if(userLocalStore.getFBUserLoggedIn()){
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
            Toast.makeText(this, R.string.logged_via_fb,Toast.LENGTH_LONG).show();
        }else if(userLocalStore.getGoogleUserLoggedIn()) {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
            Toast.makeText(this, R.string.signed_via_google,Toast.LENGTH_LONG).show();
        }else if(userLocalStore.getUserLoggedIn()) {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }else if(userLocalStore.getGuestUserLoggedIn()){
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
            Toast.makeText(this, R.string.signed_as_guest,Toast.LENGTH_LONG).show();
        }else{
            setContentView(R.layout.activity_splash);
//        ImageView logoAnimate = (ImageView) findViewById(R.id.logo);

//        ((AnimationDrawable) logoAnimate.getBackground()).start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, PagerActivity.class));
                    finish();
                }
            }, SPLASH_SCREEN_TIMEOUT);
        }
    }
}
