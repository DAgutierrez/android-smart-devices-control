package diego_gutierrez.androidsmartcontrol;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String Email = "emailKey";
    public static final String Password = "passwordKey";

    String email;
    String password;

    SharedPreferences sharedpreferences;

    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        //Config Strict Mode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        checkLogin();


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
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(this,R.style.CustomDialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Autorizando. Favor Espere.");
        progressDialog.show();

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        OauthConnection oauth = new OauthConnection();

        try{
            String AccessToken = oauth.requestAccessToken();
            String urlParameters = "email="+email+"&password="+password;
            String url = Config.Server+"/api/users/login?"+urlParameters;

            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", AccessToken);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String jsonText = readAll(in);
            try {
                JSONObject json = new JSONObject(jsonText);

                String response = json.get("res").toString();
                if (response.equals("correct user")) {
                    onLoginSuccess();
                }
                else {
                    onLoginFailed();
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public void onLoginSuccess() {

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(Email, email);
        editor.putString(Password, password);
        editor.commit();

        _loginButton.setEnabled(true);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        progressDialog.dismiss();
        Toast.makeText(getBaseContext(), "Autenticación Fallida.", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);

    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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

    // Get Login State
    public boolean isLoggedIn(){
        return sharedpreferences.getBoolean(IS_LOGIN, false);
    }

    public void checkLogin(){
        // Check login status
        if(this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(Email, sharedpreferences.getString(Email, null));

        // user email id
        user.put(Password, sharedpreferences.getString(Password, null));

        // return user
        return user;
    }
}