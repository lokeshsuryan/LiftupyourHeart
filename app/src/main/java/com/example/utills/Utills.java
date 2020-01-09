package com.example.utills;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.liftupyourheart.SignUpGuideLineActivity;

public class Utills {

    /**
     * Check Server data is Valid or not
     *
     * @param str
     * @return
     */
    public static boolean isValidString(String str) {
        try {
            return str != null && str.length() > 0 && !TextUtils.isEmpty(str);
        } catch (Exception e) {

            return false;
        }
    }
    /**
     * @param ctx
     * @param phoneNumber
     * @return
     */
    public static String getDisplayNameByPhoneNumber(Context ctx, String phoneNumber) {
        String displayName = phoneNumber;
        try {
            ContentResolver localContentResolver = ctx.getContentResolver();
            Cursor contactLookupCursor =
                    localContentResolver.query
                            (Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber)),
                                    new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                                    null, null, null);
            if (contactLookupCursor != null && contactLookupCursor.getCount() > 0) {
                contactLookupCursor.moveToLast();
                displayName = contactLookupCursor.getString
                        (contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
            }

            if (contactLookupCursor != null) {
                contactLookupCursor.close();
            }
        } catch (Exception e) {

        }
        return displayName;
    }
    /**
     * @param message
     * @param context
     */
    public static void showSnackToast(final String message, final Context context) {
        try {
            //  show simple toast
            String message_ = message + " ";
            Toast.makeText(context, message_,
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }

    /****
     * hide keyBoard
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
   /* private static void loadFragment(Fragment fragment, String type) {
// create a FragmentManager
        FragmentManager fm =getApli.getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.addToBackStack(type);
        fragmentTransaction.commit(); // save the changes
    }*/
   public static void logOut(Context activity){
       SharedPreferences preferences = activity.getSharedPreferences("logindetailHeart", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = preferences.edit();
       editor.clear();
       editor.commit();
       Intent intent=new Intent(activity, SignUpGuideLineActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       activity.startActivity(intent);
       ActivityCompat.finishAffinity((AppCompatActivity)activity);
   }

    public static void hideProgressbar(View view) {
        view.setVisibility(View.GONE);
    }

    public static void showProgressbar(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static class LibraryObject {

        private String mTitle;
        private int mRes;

        public LibraryObject(final int res, final String title) {
            mRes = res;
            mTitle = title;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(final String title) {
            mTitle = title;
        }

        public int getRes() {
            return mRes;
        }

        public void setRes(final int res) {
            mRes = res;
        }
    }
}
