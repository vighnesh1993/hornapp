package activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.horn.workshop.MainActivity;
import com.horn.workshop.R;
import com.horn.workshop.UserLocalStore;

public class GuestLogin extends AppCompatActivity {

    private TextInputLayout inputLayoutName, inputLayoutEmail;
    private EditText inputEmail, inputName;
    private Button guesLoginBtn;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_login);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        getWindow().setLayout((int) (width * .8), (int) (height * .5));

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_guestName);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_guestEmail);
        inputEmail = (EditText) findViewById(R.id.input_guestEmail);
        inputName = (EditText) findViewById(R.id.input_guestName);
        guesLoginBtn = (Button) findViewById(R.id.btn_guestLogin);

        userLocalStore = new UserLocalStore(this);

        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputName.addTextChangedListener(new MyTextWatcher(inputName));

        guesLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // guesLoginBtn.setBackgroundResource(R.color.accent);
                submitForm();
            }
        });

    }

    private void guestLogin(String name, String email) {
        userLocalStore.storeGuestUser(name, email);
        userLocalStore.setGuestUserLoggedIn(true);
        startActivity(new Intent(GuestLogin.this, MainActivity.class));
        finish();
    }

    private void submitForm() {

        if (!validateEmail() || !validateName()) {
            return;
        } else {
            String email = inputEmail.getText().toString().trim();
            String name = inputName.getText().toString().trim();

            guestLogin(name, email);
            Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
                case R.id.input_guestName:
                    validateName();
                    break;
                case R.id.input_guestEmail:
                    validateEmail();
                    break;
            }
        }
    }
}
