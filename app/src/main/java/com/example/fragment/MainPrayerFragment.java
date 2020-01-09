package com.example.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.example.adapter.PrayerPagerAdapter;
import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.liftupyourheart.databinding.FragmentMainPrayerBinding;
import com.example.model.PrayerDao;
import com.example.model.ServerResponseHeart;
import com.example.utills.PreferanceUtils;
import com.example.utills.Utills;
import com.example.viewModel.SignUpViewModel;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainPrayerFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    FragmentMainPrayerBinding binding;
    private static MainPrayerFragment instance = null;
    List<PrayerDao> prayerDaoList;
    SignUpViewModel signUpViewModel;
    String prayerOrder;
    PrayerPagerAdapter prayerPagerAdapter;
    public static boolean isdesVisible=false;
    public static MainPrayerFragment getInstance() {
        if (instance == null) {
            instance = new MainPrayerFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_prayer, null, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        setActionBar();
        showDes();
        isdesVisible=false;
        binding.viewDesc.setOnClickListener(this);
        prayerOrder = PreferanceUtils.getSettingDetail(getActivity(), "prayerOrder");
        prayerDaoList = PreferanceUtils.getFirstPrayer(getActivity());
        if (prayerDaoList == null) {
            signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
            Utills.showProgressbar(binding.loaderLayoutPrayerMain);
            signUpViewModel.prayerList().observe(this, new Observer<ServerResponseHeart<List<PrayerDao>>>() {
                @Override
                public void onChanged(@Nullable ServerResponseHeart<List<PrayerDao>> serverResponse) {
                    Utills.hideProgressbar(binding.loaderLayoutPrayerMain);
                    if (serverResponse != null) {
                        if (serverResponse.getResponse() == 1) {
                            prayerDaoList = serverResponse.getData();
                            prayerDaoList = serverResponse.getData();
                            if (prayerOrder.equalsIgnoreCase("alpha"))
                                shortAlphabetically(prayerDaoList);
                            else if (prayerOrder.equalsIgnoreCase("newest")) {

                            } else if (prayerOrder.equalsIgnoreCase("oldest")) {
                                Collections.reverse(prayerDaoList);
                            } else if (prayerOrder.equalsIgnoreCase("manual")) {

                            }

                            setUpViewPager(prayerDaoList);;
                            //dataBiding.oldestIV.setImageResource(R.drawable.ic_check);
                            Gson gson = new Gson();
                            PreferanceUtils.setFirstPrayer(getActivity(), gson.toJson(prayerDaoList));
                        } else {
                            Toast.makeText(getActivity(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                    else {
                        Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else {
            setUpViewPager(prayerDaoList);
        }
    }

    private void setUpViewPager( List<PrayerDao> prayerDaoList) {
        prayerPagerAdapter= new PrayerPagerAdapter(getContext(), prayerDaoList);
        binding.prayerVP.setAdapter(prayerPagerAdapter);

    }

    private void setActionBar() {

        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.empty), R.color.actionBarColor);
    }
    private void showDes() {
        String des=PreferanceUtils.getSettingDetail(getActivity(),"des");
        if(des.equalsIgnoreCase("ask")){
            binding.viewDesc.setVisibility(View.VISIBLE);
        }
    }

    private void shortAlphabetically(List<PrayerDao> prayerList) {

        Collections.sort(prayerList, new Comparator<PrayerDao>() {
            public int compare(PrayerDao v1, PrayerDao v2) {
                return v1.getTitle().compareTo(v2.getTitle());
            }
        });

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
            case R.id.viewDesc:

                 if(isdesVisible) {
                     isdesVisible = false;
                     binding.viewDesc.setText(getString(R.string.view_desc));
                 }else{
                     isdesVisible=true;
                     binding.viewDesc.setText(getString(R.string.hide_desc));
                }
                prayerPagerAdapter.notfyAdapter();
                break;

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        String des=PreferanceUtils.getSettingDetail(getActivity(),"des");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*public void setListview(ArrayList<PrayerDao> data) {
        Utills.hideProgressbar(binding.loaderLayoutPrayerMain);
        PrayerDao prayerDao = data.get(0);
        binding.praayerTitle.setText(prayerDao.getTitle());
        binding.prayerDesc.setText(prayerDao.getDescription());
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
        return true;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

}
