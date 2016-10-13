package com.gymtime.kalyank.gymtime.session;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fivehundredpx.android.blur.BlurringView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gymtime.kalyank.gymtime.GymTimeActivity;
import com.gymtime.kalyank.gymtime.R;
import com.gymtime.kalyank.gymtime.common.Constants;
import com.gymtime.kalyank.gymtime.communication.HTTPClient;
import com.gymtime.kalyank.gymtime.communication.HTTPResponse;
import com.gymtime.kalyank.gymtime.dao.User;

import java.util.ArrayList;
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
    private String gymJson;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        final LinearLayout root = (LinearLayout) findViewById(R.id.login_view);
        BlurringView blurringView = (BlurringView) findViewById(R.id.blurring_view);
        blurringView.setBlurredView(root);


        _loginButton.setOnClickListener(new View.OnClickListener()

                                        {

                                            @Override
                                            public void onClick(View v) {
                                                login();
                                            }
                                        }

        );

        _signupLink.setOnClickListener(new View.OnClickListener()

                                       {

                                           @Override
                                           public void onClick(View v) {
                                               // Start the Signup activity
                                               Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                                               startActivityForResult(intent, REQUEST_SIGNUP);
                                           }
                                       }

        );
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("");
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = ProgressDialog.show(LoginActivity.this, null, "Authenticating...");

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        new LoginTask().execute(new HashMap.SimpleEntry<String, String>("method", "GET"),
                new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_login_url)),
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
            if (response.getCode() == HttpsURLConnection.HTTP_NOT_FOUND) {
                onLoginFailed(getString(R.string.EMAIL_OR_PASSWORD_INVALID));
            } else {
                onLoginSuccess(response.getMessage());

            }
        }

    }


    private void onLoginSuccess(String _user) {
        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(_user, User.class);
        _loginButton.setEnabled(true);
        progressDialog.dismiss();
        sessionManager.setPreference(LoginActivity.this, Constants.USER.toString(), _user);
        Intent myIntent = new Intent(LoginActivity.this, GymTimeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getString(R.string.gym_bundle), (ArrayList) user.getFavorites());
        myIntent.putExtras(bundle);
        LoginActivity.this.startActivity(myIntent);
        finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    public void onLoginFailed(String errorMessage) {
        Log.d(LoginActivity.class.getCanonicalName(), "Error"+errorMessage);
        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
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