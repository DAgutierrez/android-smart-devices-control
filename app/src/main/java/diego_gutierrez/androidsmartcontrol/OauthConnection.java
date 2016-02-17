package diego_gutierrez.androidsmartcontrol;

import com.google.api.client.auth.oauth2.PasswordTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

/**
 * Created by diego_gutierrez on 17/12/15.
 */
public class OauthConnection {

    public String requestAccessToken() throws IOException {

        try {
            System.out.println("Try Get Access Token...");

            TokenResponse response =
                    new PasswordTokenRequest(new NetHttpTransport(), new JacksonFactory(),
                            new GenericUrl(Config.Server+"/oauth/token"), "diego.gutierrez684@gmail.com", "123")
                            .setClientAuthentication(
                                    new BasicAuthentication("s6BhdRkqt45", "gX4fBat3bV5")).execute();
            System.out.println("Access Token Succesfull!!");


            String accessToken = "Bearer "+response.getAccessToken();
            return accessToken;

        } catch (TokenResponseException e) {
            System.out.println("Get access token Failed!");
            if (e.getDetails() != null) {

                System.err.println("Error: " + e.getDetails().getError());
                if (e.getDetails().getErrorDescription() != null) {
                    System.err.println(e.getDetails().getErrorDescription());
                }
                if (e.getDetails().getErrorUri() != null) {
                    System.err.println(e.getDetails().getErrorUri());
                }
            } else {
                System.err.println(e.getMessage());
            }
            return "Get Access Token Failed";
        }
    }


}
