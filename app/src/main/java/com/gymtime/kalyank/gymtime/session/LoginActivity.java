package com.gymtime.kalyank.gymtime.session;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gymtime.kalyank.gymtime.GymTimeActivity;
import com.gymtime.kalyank.gymtime.R;
import com.gymtime.kalyank.gymtime.communication.HTTPClient;
import com.gymtime.kalyank.gymtime.communication.HTTPResponse;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private String userId;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    AppCompatButton _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = ProgressDialog.show(LoginActivity.this, null, "Authenticating...");

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        new LoginTask().execute(new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_login_url)),
                new HashMap.SimpleEntry<String, String>("email", email),
                new HashMap.SimpleEntry<String, String>("password", password));

    }

    public class LoginTask extends AsyncTask<Map.Entry, Void, HTTPResponse> {

        @Override
        protected HTTPResponse doInBackground(Map.Entry... urls) {

            return HTTPClient.getData(urls);
        }

        @Override
        protected void onPostExecute(HTTPResponse response) {
            if (response.getMessage().toString()== getString(R.string.invalid_login_credentials))
            {
                Log.d(LoginActivity.TAG, response.getMessage());
                onLoginFailed();
            } else {
                generateUserId(response.getMessage());
                onLoginSuccess();

            }
        }

    }


    private void generateUserId(String _userId) {
        userId = _userId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        progressDialog.dismiss();
        sessionManager.setPreferences(LoginActivity.this, "user", userId);
        Intent intent = new Intent(LoginActivity.this, GymTimeActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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