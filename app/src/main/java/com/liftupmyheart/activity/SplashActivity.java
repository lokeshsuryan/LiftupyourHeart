package com.liftupmyheart.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.liftupmyheart.model.PaymentConfirmationResponce;
import com.liftupmyheart.service.AlarmSoundService;
import com.liftupmyheart.service.HomeIntentService;
import com.liftupmyheart.utills.AppConstant;
import com.liftupmyheart.utills.PreferanceUtils;
import com.liftupmyheart.viewModel.SignUpViewModel;

import static com.liftupmyheart.fragment.AddReminderFragment.ALARM_REQUEST_CODE;

public class SplashActivity extends AppCompatActivity {
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.signup_popup);
        LinearLayout buttonLL = (LinearLayout) findViewById(R.id.buttonLL);
        TextView textView = (TextView) findViewById(R.id.liftUpMyHeart);
        SpannableString spannableString = new SpannableString("LIFT UP MY HEART");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_blue)), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 11, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        buttonLL.setVisibility(View.INVISIBLE);
        if ( getIntent().hasExtra("prayerId")) {
            stopAlarmManager();
        }

        new Handler().postDelayed(() -> {

            proceedToTheNextActivity();
        }, 100);
    }

    public void stopAlarmManager() {
        Intent alarmIntent = new Intent(this, BroadcastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);//cancel the alarm manager of the pending intent


        //Stop the Media Player Service to stop sound
        stopService(new Intent(this, AlarmSoundService.class));

        //remove the notification from notification tray

    }

    private void proceedToTheNextActivity() {
        //PreferanceUtils.setLoginUserID(this,"27","7341112202");
      /*  Intent registerIntent = new Intent(this, MainActivity.class);
        startActivity(registerIntent);
        finish();*/
        if (PreferanceUtils.getLoginDetail(SplashActivity.this) != null && PreferanceUtils.getLoginDetail(SplashActivity.this) != null) {
            AppConstant.userId = PreferanceUtils.getLoginDetail(SplashActivity.this).getData().getId();
            checkPaymentDetail();
        } else {
            Intent registerIntent = new Intent(this, SignUpGuideLineActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }

    private void checkPaymentDetail() {
        AppConstant.showProgressDialog(SplashActivity.this, getString(R.string.please_wait));
        SignUpViewModel signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        signUpViewModel.paymentDetail().observe(this, new Observer<PaymentConfirmationResponce>() {
            @Override
            public void onChanged(@Nullable PaymentConfirmationResponce serverResponse) {
                AppConstant.stopProgress();
                if (serverResponse != null) {
                    Intent msgIntent = new Intent(SplashActivity.this, HomeIntentService.class);
                    startService(msgIntent);
                    Intent registerIntent;

                    if (serverResponse.getResponse() == 1 && serverResponse.getIs_active() == 1) {
                        registerIntent = new Intent(SplashActivity.this, MainActivity.class);

                    } else {
                        registerIntent = new Intent(SplashActivity.this, BrowsePlan.class);

                    }
                    if (getIntent().hasExtra("prayerId")) {
                        registerIntent.putExtra("prayerId", getIntent().getStringExtra("prayerId"));
                        registerIntent.putExtra("prayerName", getIntent().getStringExtra("prayerName"));
                        registerIntent.putExtra("prayerDesc", getIntent().getStringExtra("prayerDesc"));
                    }
                    startActivity(registerIntent);
                    finish();
                } else {
                    AppConstant.stopProgress();
                    Toast.makeText(SplashActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}

