package com.liftupmyheart.fragment;

import android.content.Context;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.activity.databinding.FragmentAnswerBinding;
import com.liftupmyheart.model.AnswerPrayerDao;
import com.liftupmyheart.model.ServerResponse;
import com.liftupmyheart.utills.AppConstant;
import com.liftupmyheart.utills.Utills;
import com.liftupmyheart.viewModel.SignUpViewModel;


public class AnswerPrayerFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER;
    String pid;
    FragmentAnswerBinding binding;
    SignUpViewModel signUpViewModel;
    String title,desc;
    public AnswerPrayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_answer, null, false);
        View view = binding.getRoot();
        initView();
        return view;
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
    private void initView() {
        setActionBar();
        Bundle bundle = getArguments();
        title = bundle.getString("title");
        desc = bundle.getString("desc");
        pid = bundle.getString("pid");
        getActivity().getIntent().replaceExtras(new Bundle());
        getActivity().getIntent().setAction("");
        getActivity().getIntent().setData(null);
        getActivity().getIntent().setFlags(0);
        setArguments(null);
        binding.title.setText(title);
        binding.description.setText(desc);
        binding.deletePrayer.setOnClickListener(this);
        binding.answerPrayer.setOnClickListener(this);
        binding.addReminder.setOnClickListener(this);
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);


    }
    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.your_prayer), R.color.actionBarColor);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {
        AnswerPrayerDao answerPrayerDao;
        switch (v.getId()) {
            case R.id.addReminder:
                AppConstant.alearmId = 0;
                AppConstant.prayerName = title;
                AppConstant.prayerDesc = desc;
                AppConstant.prayerID = pid;
                loadFragment(AddReminderFragment.getInstance(), "AddReminderFragment");
                break;
            case R.id.answerPrayer:
                Utills.showProgressbar(binding.loaderLayoutAP);
                 answerPrayerDao=new AnswerPrayerDao();
                answerPrayerDao.setAnswer("1");
                answerPrayerDao.setUser_id(AppConstant.userId);
                signUpViewModel.answerPrayer(pid,answerPrayerDao).observe(this, new Observer<ServerResponse>() {
                    @Override
                    public void onChanged(@Nullable ServerResponse serverResponse) {
                        if (serverResponse != null) {
                            if (serverResponse.getResponse() == 1) {
                                Toast.makeText(getActivity(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager().popBackStack();
                            } else {
                                Toast.makeText(getActivity(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                        else {
                            Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                        }
                        Utills.hideProgressbar(binding.loaderLayoutAP);
                    }
                });
                break;
            case R.id.deletePrayer:
                 Utills.showProgressbar(binding.loaderLayoutAP);
                 answerPrayerDao=new AnswerPrayerDao();
                 answerPrayerDao.setUser_id(AppConstant.userId);
                signUpViewModel.deletePrayer(pid,answerPrayerDao).observe(this, new Observer<ServerResponse>() {
                    @Override
                    public void onChanged(@Nullable ServerResponse serverResponse) {
                        if (serverResponse != null) {
                            if (serverResponse.getResponse() == 1) {
                                Toast.makeText(getActivity(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager().popBackStack();
                            } else {
                                Toast.makeText(getActivity(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                        else {
                            Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        }
                        Utills.hideProgressbar(binding.loaderLayoutAP);
                    }
                });
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
}
