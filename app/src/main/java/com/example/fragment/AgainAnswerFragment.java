package com.example.fragment;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.liftupyourheart.databinding.FragmentAginAnswerBinding;
import com.example.model.AddPrayerDao;
import com.example.model.AnswerPrayerDao;
import com.example.model.ServerResponse;
import com.example.utills.AppConstant;
import com.example.utills.Utills;
import com.example.viewModel.SignUpViewModel;

public class AgainAnswerFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER;
    String pid,title,desc;
    FragmentAginAnswerBinding binding;
    SignUpViewModel signUpViewModel;

    public AgainAnswerFragment() {
        // Required empty public constructor
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_agin_answer, null, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        Bundle bundle = getArguments();
        title = bundle.getString("title");
        desc = bundle.getString("desc");
        pid = bundle.getString("pid");
        binding.title.setText(title);
        binding.description.setText(desc);
        binding.deletePrayer.setOnClickListener(this);
        binding.answerPrayer.setOnClickListener(this);
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);


    }
    private void setActionBar() {
        ((MainActivity)getActivity()).showActionBarTitleBgColor(true,getString(R.string.answered_prayer),R.color.actionBarColor);
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
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
        AnswerPrayerDao answerPrayerDao;
        switch (v.getId()) {
            case R.id.answerPrayer:
                Utills.showProgressbar(binding.loaderLayoutAP);
                AddPrayerDao addPrayerDao=new AddPrayerDao();
                addPrayerDao.setTitle(title);
                addPrayerDao.setDescription(desc);
                addPrayerDao.setUser_id(AppConstant.userId);
                Utills.hideKeyboard(getActivity());
                signUpViewModel.addPrayer(addPrayerDao).observe(this, new Observer<ServerResponse>() {
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
}