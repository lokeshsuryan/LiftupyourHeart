package com.liftupmyheart.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.activity.databinding.FragmentPrayerBinding;
import com.liftupmyheart.adapter.PrayerListRecyclerViewAdapter;
import com.liftupmyheart.helper.OnStartDragListener;
import com.liftupmyheart.helper.SimpleItemTouchHelperCallback;
import com.liftupmyheart.model.PrayerDao;
import com.liftupmyheart.model.ServerResponseHeart;
import com.liftupmyheart.utills.PreferanceUtils;
import com.liftupmyheart.utills.Utills;
import com.liftupmyheart.viewModel.SignUpViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PrayerFragment extends Fragment implements View.OnClickListener, OnStartDragListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    FragmentPrayerBinding binding;
    SignUpViewModel signUpViewModel;
    private static PrayerFragment instance = null;
    int mColumnCount = 1;
    String prayerOrder;
    List<PrayerDao> prayerDaoList;
    PrayerListRecyclerViewAdapter prayerListRecyclerViewAdapter;
    private ItemTouchHelper mItemTouchHelper;
    boolean showHideIcon = false;
    public static PrayerFragment getInstance() {
        if (instance == null) {
            instance = new PrayerFragment();
        }
        return instance;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_prayer, null, false);
        View view = binding.getRoot();
        view.setFocusableInTouchMode(true);
        Log.d("hello", getActivity().getSupportFragmentManager().getBackStackEntryCount() + "");

        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("BackPrass", "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("BackPrass", "onKey Back listener is working!!!");
                    Gson gson = new Gson();
                    PreferanceUtils.setFirstPrayer(getActivity(), gson.toJson(prayerDaoList));
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
        initView();
        return view;
    }

    private void initView() {
        prayerDaoList = new ArrayList<>();
        prayerOrder = PreferanceUtils.getSettingDetail(getActivity(), "prayerOrder");

        binding.newReminderOrPrayer.setText("ADD A NEW PRAYER");
        binding.allReminderDummy.setText("ALL PRAYER");
        binding.newReminderOrPrayer.setOnClickListener(this);
        binding.homeIB.setOnClickListener(this);
        binding.upDownIB.setOnClickListener(this);

        if (mColumnCount <= 1) {
            binding.allReminder.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            binding.allReminder.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
        }


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
            case R.id.newGroup:
                loadFragment(AddPrayerFragment.getInstance(), "AddPrayerFragment");
                break;
            case R.id.newReminderOrPrayer:
                loadFragment(AddPrayerFragment.getInstance(), "AddPrayerFragment");
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
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
                    } else {
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
        prayerListRecyclerViewAdapter = new PrayerListRecyclerViewAdapter(prayerDaoList, getActivity(), this);
        binding.allReminder.setAdapter(prayerListRecyclerViewAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(prayerListRecyclerViewAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.allReminder);

    }

    private void shortAlphabetically(List<PrayerDao> prayerList) {
        Collections.sort(prayerList, new Comparator<PrayerDao>() {
            public int compare(PrayerDao v1, PrayerDao v2) {
                return v1.getTitle().compareTo(v2.getTitle());
            }
        });
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
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.you_prayer), R.color.actionBarColor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;

            case R.id.homeIB:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.upDownIB:
                if (!showHideIcon) {
                    showHideIcon = true;
                    prayerListRecyclerViewAdapter.activateButtons(showHideIcon);
                    //prayerListRecyclerViewAdapter.notifyDataSetChanged();
                } else if (showHideIcon) {
                    showHideIcon = false;
                    prayerListRecyclerViewAdapter.activateButtons(showHideIcon);

                    //prayerListRecyclerViewAdapter.notifyDataSetChanged();
                }
                PreferanceUtils.setSettingDetail(getActivity(), "", "", "manual", "", "");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

}
