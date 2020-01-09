package com.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adapter.SetPrayerReminderAdapter;
import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.liftupyourheart.databinding.FragmentSetPrayerForReminderBinding;
import com.example.model.PrayerDao;
import com.example.model.ServerResponseHeart;
import com.example.utills.PreferanceUtils;
import com.example.utills.Utills;
import com.example.viewModel.SignUpViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SetPrayerForReminderFragment extends Fragment implements View.OnClickListener {
    int mColumnCount = 1;
    String prayerOrder;
    List<PrayerDao> prayerDaoList;
    FragmentSetPrayerForReminderBinding binding;
    SignUpViewModel signUpViewModel;
    SetPrayerReminderAdapter setPrayerReminderAdapter;

    public SetPrayerForReminderFragment() {
        // Required empty public constructor
    }

    public static SetPrayerForReminderFragment getInstance() {
        SetPrayerForReminderFragment fragment = new SetPrayerForReminderFragment();
        Bundle args = new Bundle();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_set_prayer_for_reminder, null, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        setActionBar();
        prayerDaoList = new ArrayList<>();
        prayerOrder = PreferanceUtils.getSettingDetail(getActivity(), "prayerOrder");
        if(PreferanceUtils.getPrayerName(getActivity())!=null)
            binding.prayerForReminder.setText(PreferanceUtils.getPrayerName(getActivity()));
          binding.selectRandomIV.setOnClickListener(this);
        if (mColumnCount <= 1) {
            binding.allPrayer.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            binding.allPrayer.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
        }
    }
    private void setActionBar() {
        ((MainActivity) getActivity()).getSupportActionBar().show();
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.you_prayer), R.color.actionBarColor);
       /* AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);*/

    }
    @Override
    public void onResume() {
        super.onResume();
        prayerDaoList = PreferanceUtils.getFirstPrayer(getActivity());
        if (prayerDaoList == null || prayerOrder.equalsIgnoreCase("newest")) {
            signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
            Utills.showProgressbar(binding.loaderLayoutPrayer);
            signUpViewModel.prayerList().observe(this, new Observer<ServerResponseHeart<List<PrayerDao>>>() {
                @Override
                public void onChanged(@Nullable ServerResponseHeart<List<PrayerDao>> serverResponse) {
                    Utills.hideProgressbar(binding.loaderLayoutPrayer);
                    if (serverResponse != null) {
                        if (serverResponse.getResponse() == 1) {
                            prayerDaoList = serverResponse.getData();
                            if (prayerOrder.equalsIgnoreCase("alpha")) {
                                shortAlphabetically(prayerDaoList);
                            }
                            //dataBiding.newestIV.setImageResource(R.drawable.ic_check);
                            else if (prayerOrder.equalsIgnoreCase("oldest")) {

                                Collections.reverse(prayerDaoList);
                            }
                            setAdapterValue();
                            //dataBiding.oldestIV.setImageResource(R.drawable.ic_check);
                        } else {
                            Toast.makeText(getActivity(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }else {
                        Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                    }

                }
            });
        } else {
            if (prayerOrder.equalsIgnoreCase("alpha")) {
                shortAlphabetically(prayerDaoList);
            } else if (prayerOrder.equalsIgnoreCase("oldest")) {
                Collections.reverse(prayerDaoList);
            }
            setAdapterValue();
        }
    }


    private void setAdapterValue() {
        setPrayerReminderAdapter = new SetPrayerReminderAdapter(prayerDaoList, getActivity());
        binding.allPrayer.setAdapter(setPrayerReminderAdapter);
    }

    private void shortAlphabetically(List<PrayerDao> prayerList) {
        Collections.sort(prayerList, new Comparator<PrayerDao>() {
            public int compare(PrayerDao v1, PrayerDao v2) {
                return v1.getTitle().compareTo(v2.getTitle());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectRandomIV:
                if (binding.selectRandomIV.getVisibility() == View.VISIBLE)
                    for (PrayerDao prayerDao : prayerDaoList) {
                        prayerDao.setSelected(false);
                        setPrayerReminderAdapter.notifyDataSetChanged();
                    }
                break;
        }


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
