package com.liftupmyheart.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.activity.databinding.FragmentShortOrderBinding;
import com.liftupmyheart.utills.PreferanceUtils;


public class ShortOrderFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static ShortOrderFragment instance = null;
    FragmentShortOrderBinding dataBiding;

    public ShortOrderFragment() {
        // Required empty public constructor
    }

    String prayerOrder = "";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ShortOrderFragment.
     */
    public static ShortOrderFragment getInstance() {
        if (instance == null)
            instance = new ShortOrderFragment();
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
        dataBiding = DataBindingUtil.inflate(inflater, R.layout.fragment_short_order, null, false);
        View view = dataBiding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        setActionBar();
        dataBiding.alphaRL.setOnClickListener(this);
        dataBiding.newestRL.setOnClickListener(this);
        dataBiding.oldestRL.setOnClickListener(this);
        dataBiding.manualRL.setOnClickListener(this);
        prayerOrder = PreferanceUtils.getSettingDetail(getActivity(), "prayerOrder");
        if (prayerOrder.equalsIgnoreCase("alpha"))
            dataBiding.alphaIV.setImageResource(R.drawable.ic_check);
        if (prayerOrder.equalsIgnoreCase("newest"))
            dataBiding.newestIV.setImageResource(R.drawable.ic_check);
        if (prayerOrder.equalsIgnoreCase("oldest"))
            dataBiding.oldestIV.setImageResource(R.drawable.ic_check);
        if (prayerOrder.equalsIgnoreCase("manual"))
            dataBiding.oldestIV.setImageResource(R.drawable.ic_check);
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

    }

    @Override
    public void onClick(View v) {
        dataBiding.alphaIV.setImageResource(0);
        dataBiding.newestIV.setImageResource(0);
        dataBiding.oldestIV.setImageResource(0);
        dataBiding.manualIV.setImageResource(0);
        switch (v.getId()) {
            case R.id.alphaRL:
                dataBiding.alphaIV.setImageResource(R.drawable.ic_check);
                prayerOrder = "alpha";
                break;
            case R.id.newestRL:
                dataBiding.newestIV.setImageResource(R.drawable.ic_check);
                prayerOrder = "newest";
                break;
            case R.id.oldestRL:
                dataBiding.oldestIV.setImageResource(R.drawable.ic_check);
                prayerOrder = "oldest";
                break;
            case R.id.manualRL:
                dataBiding.manualIV.setImageResource(R.drawable.ic_check);
                prayerOrder = "manual";
                break;
        }
        PreferanceUtils.setSettingDetail(getActivity(), "", "", prayerOrder, "", "");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
        return true;
    }
}
