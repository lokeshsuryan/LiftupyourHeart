package com.example.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adapter.RepartAlarmAdapter;
import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.liftupyourheart.databinding.FragmentRingTimePerPrayerBinding;
import com.example.model.NextPrayerListItem;

import java.util.ArrayList;

public class RepartAlarmFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    /**
     * 0 for never
     * 1 for every day
     * 2 for everyweek day
     * 3 for every week tuesday
     * 4 for every month 16th
     * 5 for every month 3rd tuesday
     * 6 for every year 16 th
     */
    int[] stringId = {
            0,
            1,
            2,
            3,
            4,
            5,
            6

    };
    int[] stringArray = {
            R.string.never,
            R.string.every_day,
            R.string.every_weekday,
            R.string.every_week_on_tuesday,
            R.string.every_month,
            R.string.every_month_rd,
            R.string.every_year

    };
    FragmentRingTimePerPrayerBinding dataBiding;
    static RepartAlarmFragment instance;
    ArrayList<NextPrayerListItem> nextPrayerList=new ArrayList<>();
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RepartAlarmFragment() {
    }

    @SuppressWarnings("unused")
    public static RepartAlarmFragment getInstance() {
        if(instance==null)
            instance = new RepartAlarmFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataBiding = DataBindingUtil.inflate(inflater, R.layout.fragment_ring_time_per_prayer, null, false);
        View view = dataBiding.getRoot();
        initView();
        return view;

    }

    private void initView() {
        setActionBar();
        nextPrayerList.clear();
        for(int i=0;i<stringArray.length;i++) {
            NextPrayerListItem nextPrayerListItem = new NextPrayerListItem();
            nextPrayerListItem.setName(stringArray[i]);
            nextPrayerListItem.setAlarmId(stringId[i]);
            nextPrayerList.add(nextPrayerListItem);
        }
        if (mColumnCount <= 1) {
            dataBiding.listRingTime.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            dataBiding.listRingTime.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
        }
        dataBiding.listRingTime.setAdapter(new RepartAlarmAdapter(getActivity(),nextPrayerList));
        //dataBiding.vibrationTB.set
    }

    private void setActionBar() {

        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.ringing_time), R.color.actionBarColor);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
        return true;
    }
}
