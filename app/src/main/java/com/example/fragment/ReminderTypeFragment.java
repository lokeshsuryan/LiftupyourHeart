package com.example.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.liftupyourheart.databinding.FragmentReminderTypeBinding;
import com.example.utills.PreferanceUtils;

public class ReminderTypeFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static ReminderTypeFragment instance = null;
    FragmentReminderTypeBinding dataBiding;
    String reminderType="";
    Boolean reciveingEmails=false;
    public ReminderTypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReminderTypeFragment.
     */
    public static ReminderTypeFragment getInstance() {
        if (instance == null)
            instance = new ReminderTypeFragment();

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataBiding = DataBindingUtil.inflate(inflater, R.layout.fragment_reminder_type, container, false);
        View view = dataBiding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        setActionBar();
        dataBiding.pushNotiRL.setOnClickListener(this);
        dataBiding.emailReminderRL.setOnClickListener(this);
        dataBiding.bothReminderRL.setOnClickListener(this);
        reminderType = PreferanceUtils.getSettingDetail(getActivity(), "reminderType");
        reciveingEmails = PreferanceUtils.getEmailRecevingSetting(getActivity());
        if (reminderType.equalsIgnoreCase("Push Notification"))
            dataBiding.pushIV.setImageResource(R.drawable.ic_check);
        else if (reminderType.equalsIgnoreCase("Email"))
            dataBiding.emailIV.setImageResource(R.drawable.ic_check);
        else if (reminderType.equalsIgnoreCase("Both"))
            dataBiding.bothIV.setImageResource(R.drawable.ic_check);
        if(reciveingEmails)
            dataBiding.recevingEmailTB.setChecked(reciveingEmails);
        dataBiding.recevingEmailTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    PreferanceUtils.setEmailRecevingSetting(getActivity(),true);
                else
                    PreferanceUtils.setEmailRecevingSetting(getActivity(),false);

            }
        });

    }
    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.reminder_), R.color.actionBarColor);

    }
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View v) {
        dataBiding.pushIV.setImageResource(0);
        dataBiding.emailIV.setImageResource(0);
        dataBiding.bothIV.setImageResource(0);
        switch (v.getId()) {
            case R.id.pushNotiRL:
                dataBiding.pushIV.setImageResource(R.drawable.ic_check);
                reminderType = "Push Notification";
                break;
            case R.id.emailReminderRL:
                dataBiding.emailIV.setImageResource(R.drawable.ic_check);
                reminderType = "Email";
                break;
            case R.id.bothReminderRL:
                dataBiding.bothIV.setImageResource(R.drawable.ic_check);
                reminderType = "Both";
                break;
        }
        PreferanceUtils.setSettingDetail(getActivity(), "", "", "", reminderType, "");


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return true;
    }
}
