package diego_gutierrez.androidsmartcontrol;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by diego_gutierrez on 22/12/15.
 */
public class User {



    public static  HashMap<String, String> getUserDetails(Context mContext) {




        String Email = "emailKey";
        String Password = "passwordKey";


        SharedPreferences sharedpreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put("Email", sharedpreferences.getString(Email, null));

        // user email id
        user.put("Password", sharedpreferences.getString(Password, null));

        // return user
        return user;
    }
}
