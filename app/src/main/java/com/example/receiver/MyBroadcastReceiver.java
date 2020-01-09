package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.example.service.AlarmNotificationService;
import com.example.service.AlarmSoundService;
import com.example.utills.GmailSender;
import com.example.utills.PreferanceUtils;

import static androidx.legacy.content.WakefulBroadcastReceiver.startWakefulService;
import static com.example.utills.AppConstant.myEmailId;
import static com.example.utills.AppConstant.password;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, AlarmSoundService.class));
        } else {
            context.startService(new Intent(context, AlarmSoundService.class));
        }
        //This will send a notification message and show notification in notification tray

        if (PreferanceUtils.getVibration(context, 1)) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
        } else if (PreferanceUtils.getVibration(context, 1)) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
        } else {

            ComponentName comp = new ComponentName(context.getPackageName(),
                    AlarmNotificationService.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));

        }
        //Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
    }


}
