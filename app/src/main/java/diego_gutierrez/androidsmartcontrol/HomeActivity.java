package diego_gutierrez.androidsmartcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    Context mContext;
    String userEmail;

    public static final String MyPREFERENCES = "MyPrefs" ;
    HashMap<String, String> user = new HashMap<String, String>();

    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        user = User.getUserDetails(mContext);
        userEmail = user.get("Email");
        System.out.println(userEmail);


    }

    public  void logout(View view){
        SharedPreferences sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
