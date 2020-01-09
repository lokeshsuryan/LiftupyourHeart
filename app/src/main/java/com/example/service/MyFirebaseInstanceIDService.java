package com.example.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/*
*
* Created by Iball on 9/19/2017.
*/
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d("devicetoken",refreshedToken);

        Log.d("fcm_id_value","fcm id "+refreshedToken);
        //PreferanceUtils.setFCMID(this, refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project

        if(token!=null)
        {
            if(token.toString().equalsIgnoreCase("") || token.toString().equalsIgnoreCase("null"))
            {
                FirebaseInstanceId.getInstance().getToken();

                Log.d("fcm_id_value","fcm id token if "+token);
            }
        }

    }
}
