package com.liftupmyheart.fragment;

import android.content.Context;
import android.content.Intent;
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

import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.activity.SignUpGuideLineActivity;
import com.liftupmyheart.activity.databinding.FragmentChangePasswordBinding;
import com.liftupmyheart.model.ChangePasswordDao;
import com.liftupmyheart.model.ServerResponse;
import com.liftupmyheart.utills.PreferanceUtils;
import com.liftupmyheart.utills.Utills;
import com.liftupmyheart.viewModel.SignUpViewModel;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    SignUpViewModel signUpViewModel;
    FragmentChangePasswordBinding dataBiding;
    public static ChangePasswordFragment instance;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangePasswordFragment.
     */
    public static ChangePasswordFragment getInstance() {
        if (instance == null)
            instance = new ChangePasswordFragment();
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

        dataBiding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, null, false);
        View view = dataBiding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        setActionBar();
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

        dataBiding.submitPassBtn.setOnClickListener(this);
    }
    private void setActionBar() {

        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.account_password), R.color.actionBarColor);

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
            case R.id.submitPassBtn:
                Utills.hideKeyboard(getActivity());
                //String userId = PreferanceUtils.getLoginDetail(getActivity()).getData().getId();
                String currPass = dataBiding.currentPassET.getText().toString();
                String newPass = dataBiding.newPassET.getText().toString();
                if (currPass.length() <= 5) {
                    dataBiding.currentPassET.setError("enter 6 char minimum");
                }
                if (newPass.length() <= 5) {
                    dataBiding.newPassET.setError("enter 6 char minimum");
                } else {
                    ChangePasswordDao changePasswordDao = new ChangePasswordDao();
                    changePasswordDao.setOldPassword(currPass);
                    changePasswordDao.setNewPassword(newPass);
                    Utills.showProgressbar(dataBiding.loaderLayoutCP);
                    signUpViewModel.changePasswordRespone(changePasswordDao).observe(this, new Observer<ServerResponse>() {
                        @Override
                        public void onChanged(@Nullable ServerResponse serverResponse) {
                            if (serverResponse != null) {
                                if (serverResponse.getResponse() == 1) {
                                    Toast.makeText(getActivity(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                                    PreferanceUtils.setLoginDetail(getActivity(),null);
                                    Intent intent = new Intent(getActivity(), SignUpGuideLineActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                            }
                            Utills.hideProgressbar(dataBiding.loaderLayoutCP);
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
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
        return true;
    }
}
