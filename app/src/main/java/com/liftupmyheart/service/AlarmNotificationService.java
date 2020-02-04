package com.liftupmyheart.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.liftupmyheart.activity.R;
import com.liftupmyheart.activity.SplashActivity;
import com.liftupmyheart.model.AlarmDao;
import com.liftupmyheart.utills.GmailSender;
import com.liftupmyheart.utills.PreferanceUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.liftupmyheart.fragment.AddReminderFragment.ALARM_REQUEST_CODE;
import static com.liftupmyheart.utills.AppConstant.myEmailId;
import static com.liftupmyheart.utills.AppConstant.password;

/**
 * Created by sonu on 10/04/17.
 */

public class AlarmNotificationService extends IntentService {
    private MediaPlayer mediaPlayer;
    String prayerName;
    String prayerDesc;
    String prayerId;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    //Notification ID for Alarm
    public static final int NOTIFICATION_ID = 1;

    public AlarmNotificationService() {
        super("AlarmNotificationService");
    }

    @Override
    public void onHandleIntent(Intent intent) {

        //Send notification

       // int rowidFirst = PreferanceUtils.getSettingPrayerTime(this, "timerFinished");
       // int rowidNext = PreferanceUtils.getSettingPrayerTime(this, "nextPrayerNoti");
        /*if (rowidFirst != 0) {
            mediaPlayer = MediaPlayer.create(this, rowidFirst);

        } else if (rowidNext != 0) {
            mediaPlayer = MediaPlayer.create(this, rowidNext);

        } else if (rowidNext == 0 && rowidFirst == 0) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm1);

        }*/

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mediaPlayer = MediaPlayer.create(this, notification);

        mediaPlayer.start();
        mediaPlayer.setLooping(true);//set looping true to run it infinitely
        int ringTime = PreferanceUtils.getSettingPrayerTime(this, "prayerRingTime");
        List<AlarmDao> reminderList = PreferanceUtils.getReminderList(AlarmNotificationService.this);
        for (AlarmDao alarmDao : reminderList) {
            if (getDateTime().compareTo(alarmDao.getAlarmTime()) == 0) {
                prayerName = alarmDao.getPrayerName();
                prayerDesc = alarmDao.getDescription();
                prayerId = alarmDao.getPrayerId();
                break;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground(AlarmNotificationService.this, getString(R.string.app_name), prayerName);
        } else {
            showNotification(AlarmNotificationService.this, getString(R.string.app_name), prayerName);
            //startForeground(1, new Notification());
        }
        SendEmailTask sendEmailTask = new SendEmailTask();
        sendEmailTask.execute();
        if (ringTime > 0) {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        mediaPlayer.setLooping(false);
                        mediaPlayer.stop();
                        stopAlarmManager();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, ringTime);
        } else {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        mediaPlayer.setLooping(false);
                        mediaPlayer.stop();
                        stopAlarmManager();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 2 * 60 * 1000);
        }
       // sendNotification("Wake Up! Wake Up! Alarm started!!");
    }
    public void stopAlarmManager() {
        Intent alarmIntent = new Intent(this, BroadcastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);//cancel the alarm manager of the pending intent


        //Stop the Media Player Service to stop sound
        stopService(new Intent(this, AlarmSoundService.class));

        //remove the notification from notification tray

        Toast.makeText(this, "Alarm Canceled/Stop by given time in setting.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //On destory stop and release the media player
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }
    /* //handle notification
     private void sendNotification(String msg) {
         alarmNotificationManager = (NotificationManager) this
                 .getSystemService(Context.NOTIFICATION_SERVICE);

         //get pending intent
         PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                 new Intent(this, SplashActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

         //Create notification
         NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                 this).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
                 .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                 .setContentText(msg).setAutoCancel(true);
         alamNotificationBuilder.setContentIntent(contentIntent);

         //notiy notification manager about new notification
         alarmNotificationManager.notify(NOTIFICATION_ID, alamNotificationBuilder.build());
     }*/
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void showNotification(Context mContext, String title, String body) {
        Intent resultIntent = new Intent(this, SplashActivity.class);
        resultIntent.putExtra("prayerId", prayerId);
        resultIntent.putExtra("prayerName", prayerName);
        resultIntent.putExtra("prayerDesc", prayerDesc);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(prayerDesc)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher, getString(R.string.cancel),
                        pendingIntent);
        builder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.actionBarColor));

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationId is a unique int for each notification that you must define
        mNotificationManager.notify(1, builder.build());
    }

    private Intent setAction(Context mContext) {

        Intent notificationIntent = new Intent(mContext, SplashActivity.class);
        notificationIntent.putExtra("closeAlarm", "yes");
        return notificationIntent;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(Context context, String body, String title) {

       /* android.app.TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(SplashActivity.class);
        stackBuilder.addNextIntent(setAction(context));

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);*/

        Intent resultIntent = new Intent(this, SplashActivity.class);
        resultIntent.putExtra("prayerId", prayerId);
        resultIntent.putExtra("prayerName", prayerName);
        resultIntent.putExtra("prayerDesc", prayerDesc);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String NOTIFICATION_CHANNEL_ID = "100001";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(prayerDesc))
                .setContentText(prayerDesc)
                .setAutoCancel(true)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    class SendEmailTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("Email sending", "sending start");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                GmailSender sender = new GmailSender(myEmailId, password);
                //subject, body, sender, to
                sender.sendMail(getString(R.string.app_name)+""+prayerName,
                        prayerDesc,
                        myEmailId,
                        PreferanceUtils.getLoginDetail(AlarmNotificationService.this).getData().getEmail());

                Log.i("Email sending", "send");
            } catch (Exception e) {
                Log.i("Email sending", "cannot send");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }


}
