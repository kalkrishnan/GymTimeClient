package com.gymtime.kalyank.gymtime.session;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fivehundredpx.android.blur.BlurringView;
import com.gymtime.kalyank.gymtime.GymTimeActivity;
import com.gymtime.kalyank.gymtime.R;
import com.gymtime.kalyank.gymtime.common.Constants;
import com.gymtime.kalyank.gymtime.communication.HTTPClient;
import com.gymtime.kalyank.gymtime.communication.HTTPResponse;
import com.gymtime.kalyank.gymtime.service.LocationService;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    SessionManager sessionManager;
    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;
    ProgressDialog progressDialog;
    private final int MY_PERMISSIONS_ACCESS_LOCATION = 111;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager();
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        final LinearLayout root = (LinearLayout) findViewById(R.id.signup_view);
        BlurringView blurringView = (BlurringView) findViewById(R.id.signup_blurringview);
        blurringView.setBlurredView(root);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_ACCESS_LOCATION);
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(GymTimeActivity.class.getCanonicalName(), Integer.toString(requestCode));
        signup();
    }

    private void storeUser(String _user) {
        sessionManager.setPreference(SignUpActivity.this, Constants.USER.toString(), _user);

    }

    public String signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed("Invalid Input");
            return "";
        }

        _signupButton.setEnabled(false);

        progressDialog = ProgressDialog.show(SignUpActivity.this,
                null, "Creating Account...");

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        new SignUpTask().execute(
                new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_signup_url)),
                new HashMap.SimpleEntry<String, String>("name", name),
                new HashMap.SimpleEntry<String, String>("email", email),
                new HashMap.SimpleEntry<String, String>("password", password));

        return "";
    }

    public class SignUpTask extends AsyncTask<Map.Entry, Void, HTTPResponse> {

        @Override
        protected HTTPResponse doInBackground(Map.Entry... urls) {

            return HTTPClient.postData(urls[0].getValue().toString(), "{\"name\":\"" + urls[1].getValue().toString() + "\",\"email\":\"" + urls[2].getValue().toString() + "\",\"password\":\"" + urls[3].getValue().toString() + "\"}");
        }

        @Override
        protected void onPostExecute(HTTPResponse response) {

            Log.d(SignUpActivity.class.getCanonicalName(), response.getMessage());
            if (response.getMessage().toString().equals(getString(R.string.email_already_exists))) {
                Log.d(SignUpActivity.TAG, response.getMessage());
                onSignupFailed(response.getMessage());

            } else {
                storeUser(response.getMessage());
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startLocationService();
                }
                onSignupSuccess();
            }
        }

    }

    private void startLocationService() {
        Intent locationIntent = new Intent(this, LocationService.class);
        startService(locationIntent);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        progressDialog.dismiss();
        setResult(RESULT_OK, null);
        Intent intent = new Intent(SignUpActivity.this, GymTimeActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(intent);
        finish();
    }


    public void onSignupFailed(String error) {
        Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
