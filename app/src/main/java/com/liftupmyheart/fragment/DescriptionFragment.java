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
import androidx.fragment.app.FragmentManager;

import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.activity.databinding.FragmentDescriptionSettingBinding;
import com.liftupmyheart.utills.PreferanceUtils;


public class DescriptionFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    FragmentDescriptionSettingBinding dataBiding;
    public static DescriptionFragment instance = null;
    String des="";
    public DescriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DescriptionFragment.
     */
    public static DescriptionFragment getInstance() {
        if (instance == null)
            instance = new DescriptionFragment();

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

        dataBiding = DataBindingUtil.inflate(inflater, R.layout.fragment_description_setting, null, false);
        View view = dataBiding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        setActionBar();
        dataBiding.alwaysRL.setOnClickListener(this);
        dataBiding.neverRL.setOnClickListener(this);
        dataBiding.askRL.setOnClickListener(this);
        String des=PreferanceUtils.getSettingDetail(getActivity(),"des");
        if(des.equalsIgnoreCase("always"))
            dataBiding.alwaysIV.setImageResource(R.drawable.ic_check);
        else if(des.equalsIgnoreCase("never"))
            dataBiding.neverIV.setImageResource(R.drawable.ic_check);
       else if(des.equalsIgnoreCase("ask"))
            dataBiding.askIV.setImageResource(R.drawable.ic_check);

    }

    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.Desc), R.color.actionBarColor);

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
        dataBiding.alwaysIV.setImageResource(0);
        dataBiding.neverIV.setImageResource(0);
        dataBiding.askIV.setImageResource(0);
        switch (v.getId()){
            case R.id.alwaysRL:
                dataBiding.alwaysIV.setImageResource(R.drawable.ic_check);
                 des="always";
                break;
                case R.id.neverRL:
                    dataBiding.neverIV.setImageResource(R.drawable.ic_check);
                    des="never";
                break;
                case R.id.askRL:
                    dataBiding.askIV.setImageResource(R.drawable.ic_check);
                    des="ask";
                break;
        }
        PreferanceUtils.setSettingDetail(getActivity(),des,"","","","");
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
