package com.liftupmyheart.fragment;

import android.content.Context;
import android.net.Uri;
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

import com.google.gson.Gson;
import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.activity.databinding.FragmentChangeNameBinding;
import com.liftupmyheart.model.ServerResponse;
import com.liftupmyheart.model.SignUpDao;
import com.liftupmyheart.utills.PreferanceUtils;
import com.liftupmyheart.utills.Utills;
import com.liftupmyheart.viewModel.SignUpViewModel;

public class ChangeNameFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    SignUpViewModel signUpViewModel;
    FragmentChangeNameBinding dataBiding;
    public static ChangeNameFragment instance = null;

    public ChangeNameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangeNameFragment.
     */
    public static ChangeNameFragment getInstance() {
        if (instance == null)
            instance = new ChangeNameFragment();
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

        dataBiding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_name, null, false);
        View view = dataBiding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        setActionBar();
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        dataBiding.submitBtn.setOnClickListener(this);
    }
    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.change_name), R.color.actionBarColor);

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
            case R.id.submitBtn:
                Utills.hideKeyboard(getActivity());
                //String userId = PreferanceUtils.getLoginDetail(getActivity()).getData().getId();
                String firstName = dataBiding.firstNameET.getText().toString();
                String lastName = dataBiding.lastNameET.getText().toString();
                if (firstName.isEmpty()) {
                    dataBiding.firstNameET.setError("enter first name");
                }
                if (lastName.isEmpty()) {
                    dataBiding.lastNameET.setError("enter last name");
                } else {
                    SignUpDao signUpDao = new SignUpDao();
                    signUpDao.setFirst_name(firstName);
                    signUpDao.setLast_name(lastName);
                    Utills.showProgressbar(dataBiding.loaderLayoutCN);
                    signUpViewModel.editProfile(signUpDao).observe(this, new Observer<ServerResponse>() {
                        @Override
                        public void onChanged(@Nullable ServerResponse serverResponse) {
                            Utills.hideProgressbar(dataBiding.loaderLayoutCN);
                            if (serverResponse != null) {
                                if (serverResponse.getResponse() == 1) {
                                    Toast.makeText(getActivity(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                                    Gson gson = new Gson();
                                    String jsonObject = gson.toJson(serverResponse);
                                    PreferanceUtils.setLoginDetail(getActivity(), jsonObject);
                                    //Log.d("loginDetail",PreferanceUtils.getLoginDetail(SignUpGuideLineActivity.this));
                                    getActivity().getSupportFragmentManager().popBackStack();
                                } else {
                                    Toast.makeText(getActivity(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }
                            else {
                                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utills.hideKeyboard(getActivity());
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                break;
        }
        return true;
    }
}
