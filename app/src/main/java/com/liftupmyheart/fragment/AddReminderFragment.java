package com.liftupmyheart.fragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.activity.databinding.FragmentReminderBinding;
import com.liftupmyheart.model.AlarmDao;
import com.liftupmyheart.model.PrayerDao;
import com.liftupmyheart.model.ServerResponseHeart;
import com.liftupmyheart.receiver.MyBroadcastReceiver;
import com.liftupmyheart.utills.AppConstant;
import com.liftupmyheart.utills.PreferanceUtils;
import com.liftupmyheart.utills.Utills;
import com.liftupmyheart.viewModel.SignUpViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AddReminderFragment extends Fragment implements View.OnClickListener {

    private int mYear, mMonth, mDay, mHour, mMinute;
    private PendingIntent pendingIntent;
    FragmentReminderBinding binding;
    int dayOfMonthPrayer, monthOfYearPrayer, yearPrayer;
    int hourOfDayPrayer, minutePrayer;
    SignUpViewModel signUpViewModel;
    List<PrayerDao> prayerDaoList;
    public static final int ALARM_REQUEST_CODE = 133;
    String strDate = "";
    String prayerName, alearmRepeded;
    int pos;
    int time;
    int[] stringArray = {
            R.string.never,
            R.string.every_day,
            R.string.every_weekday,
            R.string.every_week_on_tuesday,
            R.string.every_month,
            R.string.every_month_rd,
            R.string.every_year

    };

    public AddReminderFragment() {
        // Required empty public constructor
    }


    public static AddReminderFragment getInstance() {
        AddReminderFragment fragment = new AddReminderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reminder, null, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        ((MainActivity) getActivity()).getSupportActionBar().hide();
        if (getArguments() != null) {
            binding.removeReminder.setVisibility(View.VISIBLE);
            binding.removeReminder.setOnClickListener(this);
            alearmRepeded = getArguments().getString("alearmRepeded");
            //binding.repartingText.setText(alearmRepeded);
            if (!AppConstant.prayerName.equalsIgnoreCase(""))
                binding.prayerForReminder.setText(AppConstant.prayerName);
            pos = getArguments().getInt("position");
        }
        String currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());
        binding.prayerTime.setText(currentDateTimeString);
        binding.cancelBtn.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        binding.doneBtn.setOnClickListener(view ->{
            if (AppConstant.prayerName.equalsIgnoreCase("")) {
                List<PrayerDao> prayerDaoListforprayer = PreferanceUtils.getFirstPrayer(getActivity());
                if(prayerDaoListforprayer!=null&&prayerDaoListforprayer.size()>0) {
                    Random r = new Random();
                    int i1 = r.nextInt(prayerDaoListforprayer.size() - 0) + 0;
                    AppConstant.prayerName = prayerDaoListforprayer.get(i1).getTitle();
                    AppConstant.prayerDesc = prayerDaoListforprayer.get(i1).getDescription();
                    AppConstant.prayerID = prayerDaoListforprayer.get(i1).getId();
                }
                else{
                    Toast.makeText(getActivity(),"Please select any prayer",Toast.LENGTH_SHORT).show();
                }
            }
            if (strDate.equalsIgnoreCase("")) {
                strDate = getDateTime();
            }
            AlarmDao alarmDao = new AlarmDao();
            alarmDao.setAlarmTime(strDate);
            alarmDao.setPrayerName(AppConstant.prayerName);
            alarmDao.setPrayerId(AppConstant.prayerID);
            alarmDao.setDescription(AppConstant.prayerDesc);
            alarmDao.setRepeartAlaramType(AppConstant.alearmId);
            alarmDao.setRequestCode(time);
            if (AppConstant.prayerName.equalsIgnoreCase("")) {
                AppConstant.prayerName = "A Prayer For healing";
            }
            if (AppConstant.alearmId == 0) {
                startAlarmOnce(strDate);
            } else if (AppConstant.alearmId == 1) {
                startAtEveryDAY(hourOfDayPrayer, minutePrayer);
            } else if (AppConstant.alearmId == 2) {
                startAtWeekDays(hourOfDayPrayer, minutePrayer);
            } else if (AppConstant.alearmId == 3) {
                startAtEveryTuesday(hourOfDayPrayer, minutePrayer);
            } else if (AppConstant.alearmId == 4) {
                startAtMonth16(hourOfDayPrayer, minutePrayer);
            } else if (AppConstant.alearmId == 5) {
                startAtEVERY3RD(hourOfDayPrayer, minutePrayer);
            } else if (AppConstant.alearmId == 6) {
                starteveryYear(hourOfDayPrayer, minutePrayer);
            }

            List<AlarmDao> reminderList = PreferanceUtils.getReminderList(getActivity());
            if (reminderList != null) {
                AppConstant.prayerName = "";
                reminderList.add(alarmDao);
                AppConstant.alearmId = 0;
                String reminderString = new Gson().toJson(reminderList);
                PreferanceUtils.setReminderList(getActivity(), reminderString);
            } else {
                reminderList = new ArrayList<>();
                AppConstant.prayerName = "";
                AppConstant.prayerDesc="";
                reminderList.add(alarmDao);
                AppConstant.alearmId = 0;
                String reminderString = new Gson().toJson(reminderList);
                PreferanceUtils.setReminderList(getActivity(), reminderString);
            }
            getActivity().getSupportFragmentManager().popBackStack();
        });

        if (PreferanceUtils.getPrayerName(getActivity()) != null)
            binding.prayerForReminder.setText(PreferanceUtils.getPrayerName(getActivity()));
        prayerDaoList = new ArrayList<>();
        time = (int) System.currentTimeMillis();
        Log.d("current_time_mili", time + "");
        Intent alarmIntent = new Intent(getActivity(), MyBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), ALARM_REQUEST_CODE, alarmIntent, 0);
        binding.prayerTime.setOnClickListener(this);
        binding.repartingText.setOnClickListener(this);
        binding.prayerForReminder.setOnClickListener(this);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!AppConstant.prayerName.equalsIgnoreCase(""))
            binding.prayerForReminder.setText(AppConstant.prayerName);
        if (AppConstant.alearmId == 0) {
            binding.repartingText.setText(stringArray[0]);
        } else if (AppConstant.alearmId == 1) {
            binding.repartingText.setText(stringArray[1]);
        } else if (AppConstant.alearmId == 2) {
            binding.repartingText.setText(stringArray[2]);
        } else if (AppConstant.alearmId == 3) {
            binding.repartingText.setText(stringArray[3]);
        } else if (AppConstant.alearmId == 4) {
            binding.repartingText.setText(stringArray[4]);
        } else if (AppConstant.alearmId == 5) {
            binding.repartingText.setText(stringArray[5]);
        } else if (AppConstant.alearmId == 6) {
            binding.repartingText.setText(stringArray[6]);
        }
        prayerDaoList = PreferanceUtils.getFirstPrayer(getActivity());
        if (prayerDaoList == null) {
            Utills.showProgressbar(binding.loaderLayoutPrayer);
            signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
            signUpViewModel.prayerList().observe(this, new Observer<ServerResponseHeart<List<PrayerDao>>>() {
                @Override
                public void onChanged(@Nullable ServerResponseHeart<List<PrayerDao>> serverResponse) {
                    Utills.hideProgressbar(binding.loaderLayoutPrayer);
                    if (serverResponse != null) {
                        if (serverResponse.getResponse() == 1) {
                            prayerDaoList = serverResponse.getData();
                            Gson gson = new Gson();
                            PreferanceUtils.setFirstPrayer(getActivity(), gson.toJson(prayerDaoList));
                            //dataBiding.oldestIV.setImageResource(R.drawable.ic_check);
                        }
                    }
                    else{
                        Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prayerForReminder:
                loadFragment(SetPrayerForReminderFragment.getInstance(), "SetPrayerForReminderFragment");
                break;
            case R.id.prayerTime:
                datePickerDialog();
                break;
            case R.id.repartingText:
                loadFragment(RepartAlarmFragment.getInstance(), "RepartAlarmFragment");
                break;
            case R.id.removeReminder:

                List<AlarmDao> reminderList = PreferanceUtils.getReminderList(getActivity());
                if (reminderList != null) {
                    int reqCode = reminderList.get(pos).getRequestCode();
                    reminderList.remove(pos);
                    String reminderString = new Gson().toJson(reminderList);
                    PreferanceUtils.setReminderList(getActivity(), reminderString);
                    AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(getActivity(), MyBroadcastReceiver.class);
                    PendingIntent pIntent = PendingIntent.getBroadcast(getActivity(), reqCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    manager.cancel(pIntent);
                }
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }

    private void loadFragment(Fragment fragment, String type) {
// create a FragmentManager

        FragmentManager fm = getActivity().getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.addToBackStack(type);
        fragmentTransaction.commit(); // save the changes
    }

    private void timePickerDialog() {

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String am_pm = "";

                        Calendar datetime = Calendar.getInstance();
                        datetime.set(yearPrayer, monthOfYearPrayer, dayOfMonthPrayer);
                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime.set(Calendar.MINUTE, minute);
                        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                        strDate = format.format(datetime.getTime());
                        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                            am_pm = "PM";

                        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
                        Log.d("timeId",strHrsToShow);
                        hourOfDayPrayer=hourOfDay;
                        minutePrayer=minute;
                        Log.d("", hourOfDay + ":" + minute + "");
                        binding.prayerTime.setText(strDate);
                        //txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    private void datePickerDialog() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        timePickerDialog();
                        dayOfMonthPrayer = dayOfMonth;
                        monthOfYearPrayer = monthOfYear;
                        yearPrayer = year;

                        //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public void startAtEveryDAY(int Hours_Of_day, int MINUTE) {
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.HOUR_OF_DAY, Hours_Of_day);
        calendar.set(Calendar.MINUTE, MINUTE);
        calendar.set(Calendar.SECOND, 0);
        //calendar.set(Calendar.AM_PM, AM_PM);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        /* Repeating on every 20 minutes interval */

    }

    public void startAtWeekDays(int Hours_Of_day, int MINUTE) {
        for (int aleramDays = 2; aleramDays <= 6; aleramDays++) {
            AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            /* Set the alarm to start at 10:30 AM */
            Calendar calendar = Calendar.getInstance();
            //calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.DAY_OF_WEEK, aleramDays);
            calendar.set(Calendar.HOUR_OF_DAY, Hours_Of_day);
            calendar.set(Calendar.MINUTE, MINUTE);
            calendar.set(Calendar.SECOND, 0);
            //calendar.set(Calendar.AM_PM, AM_PM);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        /* Repeating on every 20 minutes interval */

    }

    public void startAlarmOnce(String dateTime) {
        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(getDateTime(dateTime));
        //calendar.set(Calendar.AM_PM, Calendar.AM );
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.DAY_OF_WEEK, 2);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1);
        cal.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR));
        cal.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
        cal.set(Calendar.AM_PM, Calendar.AM);
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        /* Repeating on every 20 minutes interval */

    }

    public void startAtWeekDay(String dateTime) {
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDateTime(dateTime));
        //calendar.set(Calendar.AM_PM, Calendar.AM );
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.DAY_OF_WEEK, 2);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1);
        cal.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR));
        cal.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
        cal.set(Calendar.AM_PM, Calendar.AM);
        //calendar.set(Calendar.AM_PM, AM_PM);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
        /* Repeating on every 20 minutes interval */

    }

    public void startAtEveryTuesday(int Hours_Of_day, int MINUTE) {
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, 3);
        calendar.set(Calendar.HOUR_OF_DAY, Hours_Of_day);
        calendar.set(Calendar.MINUTE, MINUTE);
        calendar.set(Calendar.SECOND, 0);
        //calendar.set(Calendar.AM_PM, AM_PM);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        /* Repeating on every 20 minutes interval */

    }

    public void startAtEVERY3RD(int Hours_Of_day, int MINUTE) {

        for (int i = 1; i <= 12; i++) {
            AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            /* Set the alarm to start at 10:30 AM */
            Calendar calendar = Calendar.getInstance();
            //calendar.setTimeInMillis(System.currentTimeMillis());
            //calendar.set(Calendar.DAY_OF_WEEK, 2);
            calendar.set(Calendar.MONTH, i);

            calendar.set(Calendar.WEEK_OF_MONTH, 4);
            calendar.set(Calendar.HOUR_OF_DAY, Hours_Of_day);
            calendar.set(Calendar.DAY_OF_WEEK, 3);
            calendar.set(Calendar.MINUTE, MINUTE);
            calendar.set(Calendar.SECOND, 0);
            //calendar.set(Calendar.AM_PM, AM_PM);
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 30, pendingIntent);
            /* Repeating on every 20 minutes interval */
        }
    }

    public void startAtMonth16(int Hours_Of_day, int MINUTE) {

        for (int i = 1; i <= 12; i++) {
            AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            /* Set the alarm to start at 10:30 AM */
            Calendar calendar = Calendar.getInstance();
            //calendar.setTimeInMillis(System.currentTimeMillis());
            //calendar.set(Calendar.DAY_OF_WEEK, 2);
            calendar.set(Calendar.MONTH, i);
            calendar.set(Calendar.DAY_OF_MONTH, 16);
            calendar.set(Calendar.HOUR_OF_DAY, Hours_Of_day);
            calendar.set(Calendar.MINUTE, MINUTE);
            calendar.set(Calendar.SECOND, 0);
            //calendar.set(Calendar.AM_PM, AM_PM);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 30, pendingIntent);
            /* Repeating on every 20 minutes interval */
        }
    }

    public void starteveryYear(int Hours_Of_day, int MINUTE) {

        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 16);
        calendar.set(Calendar.HOUR_OF_DAY, Hours_Of_day);
        calendar.set(Calendar.MINUTE, MINUTE);
        calendar.set(Calendar.SECOND, 0);
        //calendar.set(Calendar.AM_PM, AM_PM);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY * 365, pendingIntent);
        /* Repeating on every 20 minutes interval */
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private Date getDateTime(String dateTime)  {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        Date yourDate = null;
        try {
            yourDate = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return yourDate;
    }
}
