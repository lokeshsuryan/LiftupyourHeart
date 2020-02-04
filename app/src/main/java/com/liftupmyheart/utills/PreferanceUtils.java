package com.liftupmyheart.utills;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.liftupmyheart.model.AlarmDao;
import com.liftupmyheart.model.PrayerDao;
import com.liftupmyheart.model.ServerResponse;

import java.util.List;

/**
 * Created by riontech1 on 26/1/16.
 */
public class PreferanceUtils {


    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Get Data from preferance
     *
     * @param context
     * @param defaultValue
     * @param key
     * @return
     */
    public static String getStringFromPreferences(final Context context,
                                                  final String defaultValue, final String key) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences(AppConstant.PREFERENCE_NAME,
                        Context.MODE_PRIVATE);
        final String temp = sharedPreferences.getString(key, defaultValue);
        return temp;
    }

    public static boolean setLoginDetail(Context context, String logindetail) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("logindetailHeart",
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logindetail", logindetail);
        editor.commit();
        return true;
    }

    public static ServerResponse getLoginDetail(Context context) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("logindetailHeart",
                        Context.MODE_PRIVATE);

        final String empty_value_field = sharedPreferences.getString("logindetail", null);
        if (empty_value_field != null)
            return new Gson().fromJson(empty_value_field, ServerResponse.class);
        else
            return null;
    }
    public static boolean setFirstPrayer(Context context, String prayerDao) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("firstPrayer",
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstPrayer", prayerDao);
        editor.commit();
        return true;
    }
    public static boolean setPrayerName(Context context, String prayerName) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("Prayer",
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("prayerName", prayerName);
        editor.commit();
        return true;
    }

    public static String getPrayerName(Context context) {

        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("Prayer",
                        Context.MODE_PRIVATE);
        final String empty_value_field = sharedPreferences.getString("prayerName", null);
        return empty_value_field;

    }
    public static List<PrayerDao> getFirstPrayer(Context context) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("firstPrayer",
                        Context.MODE_PRIVATE);
        final String empty_value_field = sharedPreferences.getString("firstPrayer", null);
        if (empty_value_field != null)
          return new Gson().fromJson(empty_value_field, new TypeToken<List<PrayerDao>>() {
                }.getType());
        else
            return null;
    }
    public static boolean setEmailRecevingSetting(Context context, Boolean EmailReceving) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("emailRecSettingdetailHeart",
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("emailDetail", EmailReceving);
        editor.commit();
        return true;
    }

    public static boolean getEmailRecevingSetting(Context context) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("emailRecSettingdetailHeart",
                        Context.MODE_PRIVATE);

        final Boolean empty_value_field = sharedPreferences.getBoolean("emailDetail", false);
        if (empty_value_field != null)
            return empty_value_field;
        else
            return false;
    }


    public static boolean getPaymentStatus(Context context) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("paymentStatus",
                        Context.MODE_PRIVATE);

        Boolean isPayment = sharedPreferences.getBoolean("isPayment", false);

        return isPayment;//30 days over, update user to non-vip
    }

    public static boolean setPaymentStatus(Context context, Boolean status) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences("paymentStatus",
                Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isPayment", status);
        editor.commit();
        return true;
    }
    public static boolean setSettingDetail(Context context, String des, String prayerType, String prayerOrder, String reminderType, String dayLight) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("settingdetailHeart",
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!des.isEmpty())
            editor.putString("des", des);
        if (!prayerType.isEmpty())
            editor.putString("prayerType", prayerType);
        if (!prayerOrder.isEmpty())
            editor.putString("prayerOrder", prayerOrder);
        if (!reminderType.isEmpty())
            editor.putString("reminderType", reminderType);
        if (!dayLight.isEmpty())
            editor.putString("dayLight", dayLight);
        editor.commit();
        return true;
    }

    public static boolean setSettingPrayerTime(Context context, int timerFinishd, int auto, int prayerRingTime, int nextPrayerNoti, int name) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("settingdetailPrayerTimeHeart",
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (timerFinishd != 0) {
            editor.putInt("timerFinished", timerFinishd);
            editor.putInt("timerFinishedName", name);
        } else if (auto != 0)
            editor.putInt("auto", auto);
        else if (prayerRingTime != 0) {
            editor.putInt("prayerRingTime", prayerRingTime);
            editor.putInt("prayerRingTimeName", name);
        } else if (nextPrayerNoti != 0) {
            editor.putInt("nextPrayerNoti", nextPrayerNoti);
            editor.putInt("nextPrayerNotiName", name);
        }

        editor.commit();
        return true;
    }
    public static boolean setPrayerAlaram(Context context, String alarmDao) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("settingalarmDetailHeart",
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (alarmDao != null) {
            editor.putString("alarmType", alarmDao);
        }

        editor.commit();
        return true;
    }

    public static AlarmDao getPrayerAlaram(Context context, String alarmDao) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("settingalarmDetailHeart",
                        Context.MODE_PRIVATE);
        final String empty_value_field = sharedPreferences.getString("alarmType", null);
        if (empty_value_field != null)
            return new Gson().fromJson(empty_value_field, AlarmDao.class);
        else
            return null;
    }
    public static int getSettingPrayerTimeName(Context context, String type) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("settingdetailPrayerTimeHeart",
                        Context.MODE_PRIVATE);
        int empty_value_field;
        if (type.equalsIgnoreCase("timerFinishedName")) {
            empty_value_field = sharedPreferences.getInt("timerFinishedName", 0);
            return empty_value_field;
        } else if (type.equalsIgnoreCase("prayerRingTimeName")) {
            empty_value_field = sharedPreferences.getInt("prayerRingTimeName", 0);
            return empty_value_field;
        } else if (type.equalsIgnoreCase("nextPrayerNotiName")) {
            empty_value_field = sharedPreferences.getInt("nextPrayerNotiName", 0);
            return empty_value_field;
        } else
            return 0;
    }

    public static int getSettingPrayerTime(Context context, String type) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("settingdetailPrayerTimeHeart",
                        Context.MODE_PRIVATE);
        int empty_value_field = 0;
        if (type.equalsIgnoreCase("timerFinished")) {
            empty_value_field = sharedPreferences.getInt("timerFinished", 0);
            return empty_value_field;
        } else if (type.equalsIgnoreCase("auto")) {
            empty_value_field = sharedPreferences.getInt("auto", 0);
            return empty_value_field;
        } else if (type.equalsIgnoreCase("prayerRingTime")) {
            empty_value_field = sharedPreferences.getInt("prayerRingTime", 0);
            return empty_value_field;
        } else if (type.equalsIgnoreCase("nextPrayerNoti")) {
            empty_value_field = sharedPreferences.getInt("nextPrayerNoti", 0);
            return empty_value_field;
        } else
            return 0;
    }

    public static String getSettingDetail(Context context, String type) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("settingdetailHeart",
                        Context.MODE_PRIVATE);
        String empty_value_field = "";
        if (type.equalsIgnoreCase("des")) {
            empty_value_field = sharedPreferences.getString("des", "");
            return empty_value_field;
        } else if (type.equalsIgnoreCase("prayerType")) {
            empty_value_field = sharedPreferences.getString("prayerType", "");
            return empty_value_field;
        } else if (type.equalsIgnoreCase("prayerOrder")) {
            empty_value_field = sharedPreferences.getString("prayerOrder", "");
            return empty_value_field;
        } else if (type.equalsIgnoreCase("reminderType")) {
            empty_value_field = sharedPreferences.getString("reminderType", "");
            return empty_value_field;
        } else if (type.equalsIgnoreCase("dayLight")) {
            empty_value_field = sharedPreferences.getString("dayLight", "");
            return empty_value_field;
        } else if (type.equalsIgnoreCase("reciveingEmails")) {
            empty_value_field = sharedPreferences.getString("reciveingEmails", "");
            return empty_value_field;
        } else
            return "";
    }

    public static boolean setVibration(final Context context, int type, boolean vibration) {

        final SharedPreferences sharedPreferences = context.getSharedPreferences("vibrationValue",
                Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (type == 1)
            editor.putBoolean("vibrationFirst", vibration);
        else if (type == 2)
            editor.putBoolean("vibrationNext", vibration);
        editor.commit();

        return true;
    }

    public static boolean getVibration(Context context, int type) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("vibrationValue",
                        Context.MODE_PRIVATE);
        boolean vibration = false;
        if (type == 1)
            vibration = sharedPreferences.getBoolean("vibrationFirst", false);
        else if (type == 2) {
            vibration = sharedPreferences.getBoolean("vibrationNext", false);
            return vibration;//30 days over, update user to non-vip
        }

        return vibration;
    }

    public static boolean setPaymentTime(final Context context,Boolean paymetStatus) {

        final SharedPreferences sharedPreferences = context.getSharedPreferences("payexpire",
                Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("starttime", (int) System.currentTimeMillis() / 1000);
        editor.putBoolean("paymentStatus", paymetStatus);
        editor.commit();
        return true;
    }


    public static boolean getPaymentTime(Context context) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("payexpire",
                        Context.MODE_PRIVATE);

        int startTime = sharedPreferences.getInt("starttime", 0);
        boolean status = sharedPreferences.getBoolean("paymentStatus", false);
        int currentTime = (int) System.currentTimeMillis() / 1000;
        int timeofVip = currentTime - startTime; //calculate the time of his VIP-being time
        if(status){
            if (timeofVip >= 2592000)  //2592000 is 30 days in seconds
                return true;//30 days over, update user to non-vip
            else
                return false;
        }else {
            if (timeofVip >= 864000)  //864000 is 10 days in seconds
                return true;//30 days over, update user to non-vip
            else
                return false;
        }

    }


    public static String getLoginUserPhone(Context context) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("userId",
                        Context.MODE_PRIVATE);

        final String empty_value_field = sharedPreferences.getString("phone", null);

        return empty_value_field;
    }

    public static boolean setFCMID(Context context, String id) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("fcm_key",
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fcm_value", id);
        editor.commit();
        return true;
    }

    public static String getFCMID(Context context) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("fcm_key",
                        Context.MODE_PRIVATE);

        final String empty_value_field = sharedPreferences.getString("fcm_value", null);

        return empty_value_field;
    }

    /**
     * Insert getUser ID in preferance
     *
     * @param context
     * @param value
     * @param key
     * @return
     */
    public static boolean putUserIDPreferences(Context context,
                                               final boolean value, final String key) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences(AppConstant.PREFERENCE_NAME,
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
        return true;
    }

    /**
     * @param context
     * @return
     */
    public static AppContactList getContactListFromPreferance(Context context) {
        try {
            String json = PreferanceUtils.getStringFromPreferences(context, null,
                    AppConstant.PREFERANCE_CONTACTS);
            if (json != null) {
                return new Gson().fromJson(json, AppContactList.class);
            }
        } catch (JsonSyntaxException e) {

        }
        return new AppContactList();
    }

    /**
     * @param context
     * @param object
     */
    public static void setContactListIntoPreferance(Context context, AppContactList object) {
        try {
            String contactListInString = new Gson().toJson(object).toString();
            PreferanceUtils.putStringInPreferences(context, contactListInString,
                    AppConstant.PREFERANCE_CONTACTS);
        } catch (Exception e) {

        }
    }

    /**
     * Insert string img_4 in Shared Preferences
     *
     * @param context of application
     * @param value   to store in preferences
     * @param key     using which img_4 is mapped
     * @return
     */
    public static boolean putStringInPreferences(final Context context,
                                                 final String value, final String key) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences(AppConstant.PREFERENCE_NAME,
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }

    /**
     *
     *set ReminderList
     */

    public static boolean setReminderList(Context context, String reminderString) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("reminderList",
                        Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("reminderListData", reminderString);
        editor.commit();
        return true;
    }
    /**
     *
     *get ReminderList
     */

    public static List<AlarmDao> getReminderList(Context context) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("reminderList",
                        Context.MODE_PRIVATE);
        final String empty_value_field = sharedPreferences.getString("reminderListData", null);
        if (empty_value_field != null)
            return new Gson().fromJson(empty_value_field, new TypeToken<List<AlarmDao>>() {
            }.getType());
        else
            return null;
    }
}
