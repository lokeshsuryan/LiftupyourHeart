package com.example.utills;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by lokeshkumar on 13/03/18.
 */

public class AppConstant {
    public static String BASE_URL_OLD = "http://qrmp.co/app/";
    public static String BASE_URL = "https://app.liftupmyheart.com/";
    public static String myEmailId = "liftupheart@gmail.com";
    public static String password = "lokesh@123";
    public static final int CONNECTION_TIMEOUT = 120000;
    public static final String PRIVACY_POLCY = "http://liftupmyheart.com/privacy.html";
    public static final String TERMS_CONDITION = "http://liftupmyheart.com/terms.html";
    public static  String userId = "";
    public static final String PREFERENCE_NAME = "com.example.liftuoyourHart";
    public static final String PREFERANCE_CONTACTS = PREFERENCE_NAME + "contacts";
    public static  int alearmId =0;
    public static  String prayerName ="";
    public static  String prayerDesc ="";
    public static  String prayerID ="";
    public static String currencyCode="USD";
    static ProgressDialog progressDialog;
    public static void showProgressDialog(Context context, String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
            progressDialog = null;
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }



}
