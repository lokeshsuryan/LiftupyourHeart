package com.liftupmyheart.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetworkReceiver extends BroadcastReceiver {
    public static final String EXTRA_DATA_NAME_NETWORK_CONNECTED = "com.liftupmyheart.liftupyourheart.NetworkConnected";
    public static ConnectivityReceiverListener connectivityReceiverListener;
    public static boolean isConnected;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        isConnected = conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();


        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(NetworkReceiver.isConnected);
        }
    }

    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            isConnected=conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected();
            return isConnected;
        } catch (Exception e) {

            return false;
        }

    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}