package com.example.fragment;

import android.content.Context;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adapter.NextPrayerRecyclerViewAdapter;
import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.liftupyourheart.databinding.FragmentItemListBinding;
import com.example.model.NextPrayerListItem;
import com.example.utills.PreferanceUtils;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class NextPrayerListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    NextPrayerRecyclerViewAdapter nextPrayerRecyclerViewAdapter;
    int type;
    boolean isCheck = false;
    int[] mResources = {
            1,
            R.raw.alarm1,
            R.raw.alarm2,
            R.raw.alarm3,
            R.raw.alarm4,
            R.raw.alarm5,
            R.raw.alarm6

    };
    int[] stringArray = {
            R.string.none,
            R.string.open,
            R.string.contour,
            R.string.cutaway,
            R.string.resolve,
            R.string.low,
            R.string.vintage,

    };
    FragmentItemListBinding dataBiding;
    static NextPrayerListFragment instance;
    ArrayList<NextPrayerListItem> nextPrayerList = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NextPrayerListFragment() {
    }

    @SuppressWarnings("unused")
    public static NextPrayerListFragment getInstance() {
        if (instance == null)
            instance = new NextPrayerListFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataBiding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_list, null, false);
        View view = dataBiding.getRoot();
        initView();
        return view;

    }

    private void initView() {
        setActionBar();
        dataBiding.vibrationTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(buttonView.isPressed())
                    PreferanceUtils.setVibration(getActivity(),type,isChecked);
            }
        });
        nextPrayerList.clear();
        int timerFinishedName = PreferanceUtils.getSettingPrayerTimeName(getActivity(), "timerFinishedName");
        int nextPrayerNotiName = PreferanceUtils.getSettingPrayerTimeName(getActivity(), "nextPrayerNotiName");
        for (int i = 0; i < stringArray.length; i++) {
            NextPrayerListItem nextPrayerListItem = new NextPrayerListItem();
            nextPrayerListItem.setName(stringArray[i]);
            nextPrayerListItem.setRawId(mResources[i]);
            if (type == 1 && timerFinishedName == mResources[i])
                nextPrayerListItem.setSelected(true);
            else if (type == 2 && nextPrayerNotiName == mResources[i])
                nextPrayerListItem.setSelected(true);
            nextPrayerList.add(nextPrayerListItem);
        }
        if (mColumnCount <= 1) {
            dataBiding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            dataBiding.list.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
        }
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                dataBiding.list.getContext(),
                DividerItemDecoration.VERTICAL
        );
        dataBiding.list.addItemDecoration(mDividerItemDecoration);

        nextPrayerRecyclerViewAdapter = new NextPrayerRecyclerViewAdapter(getActivity(), nextPrayerList, type);
        dataBiding.list.setAdapter(nextPrayerRecyclerViewAdapter);
        //dataBiding.vibrationTB.set
    }

    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.setting), R.color.actionBarColor);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        nextPrayerRecyclerViewAdapter.onDetachedFromRecyclerView(dataBiding.list);
    }

    @Override
    public void onResume() {
        super.onResume();
        isCheck = PreferanceUtils.getVibration(getActivity(), type);
        dataBiding.vibrationTB.setChecked(isCheck);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
