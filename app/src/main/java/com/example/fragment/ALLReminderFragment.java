package com.example.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adapter.ReminderListAdapter;
import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.liftupyourheart.databinding.FragmentPrayerBinding;
import com.example.model.AlarmDao;
import com.example.utills.AppConstant;
import com.example.utills.PreferanceUtils;

import java.util.List;

public class ALLReminderFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    FragmentPrayerBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ALLReminderFragment() {
    }

    @SuppressWarnings("unused")
    public static ALLReminderFragment getInstance() {
        ALLReminderFragment fragment = new ALLReminderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_prayer, null, false);
        View view = binding.getRoot();
        initView();
        return view;
        // Set the adapter
    }

    private void initView() {
        setActionBar();
        binding.newReminderOrPrayer.setOnClickListener(this);
        binding.homeIB.setOnClickListener(this);
        binding.upDownIB.setVisibility(View.INVISIBLE);
        if (mColumnCount <= 1) {
            binding.allReminder.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            binding.allReminder.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
        }
    }

    private void setActionBar() {
        ((MainActivity) getActivity()).getSupportActionBar().show();
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.you_prayer), R.color.actionBarColor);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newReminderOrPrayer:
                AppConstant.alearmId = 0;
                AppConstant.prayerName = "";
                AppConstant.prayerID = "";
                AppConstant.prayerDesc = "";
                loadFragment(AddReminderFragment.getInstance(), "AddReminderFragment");
                break;
            case R.id.homeIB:
                getActivity().getSupportFragmentManager().popBackStack();
        }

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        menu.clear();
    }
    @Override
    public void onResume() {
        super.onResume();

        List<AlarmDao> alarmDaoList = PreferanceUtils.getReminderList(getActivity());
        if (alarmDaoList != null)
            binding.allReminder.setAdapter(new ReminderListAdapter(getActivity(), alarmDaoList));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return true;
        }
        return true;
    }
}
