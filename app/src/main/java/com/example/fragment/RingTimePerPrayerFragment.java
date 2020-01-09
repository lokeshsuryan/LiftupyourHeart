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

import com.example.adapter.RingTimePrayerAdapter;
import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.liftupyourheart.databinding.FragmentRingTimePerPrayerBinding;
import com.example.model.NextPrayerListItem;

import java.util.ArrayList;

public class RingTimePerPrayerFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    int[] mResources = {
            0*1000,
            5*1000,
            10*1000,
            20*1000,
            30*1000,
            40*1000,
            50*1000,
            60*1000,
            2*60*1000,
            5*60*1000,
            10*60*1000,
            20*60*1000,
            30*60*1000,
            40*60*1000,
            50*60*1000,
            60*60*1000

    };
    int[] stringArray = {
            R.string.none,
            R.string.second_5,
            R.string.second_10,
            R.string.second_20,
            R.string.second_30,
            R.string.second_40,
            R.string.second_50,
            R.string.second_60,
            R.string.min_2,
            R.string.min_5,
            R.string.min_10,
            R.string.min_20,
            R.string.min_30,
            R.string.min_40,
            R.string.min_50,
            R.string.hours_1

    };
    FragmentRingTimePerPrayerBinding dataBiding;
    static RingTimePerPrayerFragment instance;
    ArrayList<NextPrayerListItem> nextPrayerList=new ArrayList<>();
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RingTimePerPrayerFragment() {
    }

    @SuppressWarnings("unused")
    public static RingTimePerPrayerFragment getInstance() {
        if(instance==null)
            instance = new RingTimePerPrayerFragment();
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
            nextPrayerListItem.setRingTime(mResources[i]);
            nextPrayerList.add(nextPrayerListItem);
        }
        if (mColumnCount <= 1) {
            dataBiding.listRingTime.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            dataBiding.listRingTime.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
        }
        dataBiding.listRingTime.setAdapter(new RingTimePrayerAdapter(getActivity(),nextPrayerList));
        //dataBiding.vibrationTB.set
    }

    private void setActionBar() {

        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.empty), R.color.actionBarColor);

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
