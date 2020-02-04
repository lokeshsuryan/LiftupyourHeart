package com.liftupmyheart.fragment;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.activity.databinding.FragmentPrayerTimeBinding;
import com.liftupmyheart.utills.PreferanceUtils;

public class PrayerTimeFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    FragmentPrayerTimeBinding databinding;

    public PrayerTimeFragment() {
        // Required empty public constructor
    }

    public static PrayerTimeFragment getInstance() {
        PrayerTimeFragment fragment = new PrayerTimeFragment();
        return fragment;
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
        databinding = DataBindingUtil.inflate(inflater, R.layout.fragment_prayer_time, null, false);
        View view = databinding.getRoot();
        initView();
        return view;
    }

    /**
     * initViews
     */
    private void initView() {
        setActionBar();
        if (PreferanceUtils.getSettingPrayerTime(getActivity(), "auto") == 1) {
            databinding.advanceAutoTB.setChecked(true);
        }
        int timerFinishedName = PreferanceUtils.getSettingPrayerTimeName(getActivity(), "timerFinishedName");
        int nextPrayerNotiName = PreferanceUtils.getSettingPrayerTimeName(getActivity(), "nextPrayerNotiName");
        int prayerRingTimeName = PreferanceUtils.getSettingPrayerTimeName(getActivity(), "prayerRingTimeName");
        if (timerFinishedName != 0)
            databinding.timeFinishNotiTV.setText(timerFinishedName);
        if (nextPrayerNotiName != 0)
            databinding.nextPrayerNotiTV.setText(nextPrayerNotiName);
        if (prayerRingTimeName != 0)
            databinding.howLongTV.setText(prayerRingTimeName);
        databinding.nextPrayerNotiLL.setOnClickListener(this);
        databinding.howLongLL.setOnClickListener(this);
        databinding.tmerfinishNotiLL.setOnClickListener(this);
        databinding.advanceAutoTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferanceUtils.setSettingPrayerTime(getActivity(), 0, 1, 0, 0, 0);
                } else {
                    PreferanceUtils.setSettingPrayerTime(getActivity(), 0, 2, 0, 0, 0);
                }
            }
        });
    }

    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.prayer_time), R.color.actionBarColor);

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
        switch (v.getId()) {
            case R.id.nextPrayerNotiLL:
                loadFragment(NextPrayerListFragment.getInstance(), "NextPrayerListFragment", 2);
                break;
            case R.id.howLongLL:
                loadFragment(RingTimePerPrayerFragment.getInstance(), "RingTimePerPrayerFragment", 1);
                break;
            case R.id.tmerfinishNotiLL:
                loadFragment(NextPrayerListFragment.getInstance(), "NextPrayerListFragment", 1);
                break;

        }
    }

    /**
     * Replace fragment here
     *
     * @param fragment
     * @param type
     * @param fragmentType
     */
    private void loadFragment(Fragment fragment, String type, int fragmentType) {
// create a FragmentManager

        FragmentManager fm = getActivity().getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.addToBackStack(type);
        Bundle bundle = new Bundle();
        bundle.putInt("type", fragmentType);
        fragment.setArguments(bundle);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
        return true;
    }
}
