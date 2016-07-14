package com.gymtime.kalyank.gymtime.session;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gymtime.kalyank.gymtime.GymDetailActivity;
import com.gymtime.kalyank.gymtime.GymTimeActivity;
import com.gymtime.kalyank.gymtime.R;
import com.gymtime.kalyank.gymtime.communication.HTTPClient;
import com.gymtime.kalyank.gymtime.communication.HTTPResponse;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    SessionManager sessionManager;
    private String userId;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager();
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
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


    private void generateUserId(String _userId) {
        userId = _userId;
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

        new SignUpTask().execute(new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_signup_url)),
                new HashMap.SimpleEntry<String, String>("name", name),
                new HashMap.SimpleEntry<String, String>("email", email),
                new HashMap.SimpleEntry<String, String>("password", password));

        return "";
    }

    public class SignUpTask extends AsyncTask<Map.Entry, Void, HTTPResponse> {

        @Override
        protected HTTPResponse doInBackground(Map.Entry... urls) {

            return HTTPClient.getData(urls);
        }

        @Override
        protected void onPostExecute(HTTPResponse response) {

            Log.d(SignUpActivity.class.getCanonicalName(), response.getMessage());
            Log.d(SignUpActivity.class.getCanonicalName(), getString(R.string.email_already_exists));
            if (response.getMessage().toString() == getString(R.string.email_already_exists)) {
                generateUserId(response.getMessage());
                onSignupSuccess();
            } else {
                Log.d(SignUpActivity.TAG, response.getMessage());
                onSignupFailed(response.getMessage());
            }
        }

    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        progressDialog.dismiss();
        setResult(RESULT_OK, null);
        sessionManager.setPreferences(SignUpActivity.this, "user", userId);
        Intent intent = new Intent(SignUpActivity.this, GymTimeActivity.class);
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
