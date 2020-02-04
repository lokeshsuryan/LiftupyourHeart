package com.liftupmyheart.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.liftupmyheart.service.AlarmNotificationService;
import com.liftupmyheart.service.AlarmSoundService;
import com.liftupmyheart.utills.PreferanceUtils;

import static androidx.legacy.content.WakefulBroadcastReceiver.startWakefulService;

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
