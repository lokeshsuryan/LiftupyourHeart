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
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.liftupyourheart.databinding.FragmentAddPrayerBinding;
import com.example.model.AddPrayerDao;
import com.example.model.ServerResponse;
import com.example.utills.AppConstant;
import com.example.utills.Utills;
import com.example.viewModel.SignUpViewModel;


public class AddPrayerFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    SignUpViewModel signUpViewModel;
    FragmentAddPrayerBinding binding;

    public AddPrayerFragment() {
        // Required empty public constructor
    }
    public static AddPrayerFragment getInstance() {
        AddPrayerFragment fragment = new AddPrayerFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem itemNotification=menu.findItem(R.id.notification);
        MenuItem itemHomeMenu=menu.findItem(R.id.homeMenu);
        if(itemNotification!=null)
            itemNotification.setVisible(false);
            itemHomeMenu.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_prayer, null, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        ((MainActivity) getActivity()).getSupportActionBar().hide();
        binding.toolbarTitle.setText("Add Prayer");
        binding.description.setText("");
        binding.title.setText("");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        binding.toolbar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                ((MainActivity)getActivity()).showActionBarTitle(getResources().getColor(R.color.red));
            }
        });
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

    }

    public void onButtonPressed() {

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        binding.toolbar.inflateMenu(R.menu.prayer_menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_done:
                AddPrayerDao addPrayerDao = new AddPrayerDao();
                String desc = binding.description.getText().toString();
                String title = binding.title.getText().toString();
                if (title.isEmpty())
                    binding.title.setError("Enter Prayer title");
                else if (title.isEmpty())
                    binding.description.setError("Enter Prayer description");
                else {
                    Utills.showProgressbar(binding.loaderLayoutAP);
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
                            else {
                                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                            }
                            Utills.hideProgressbar(binding.loaderLayoutAP);
                        }
                    });
                    break;

                }
        }
        return true;
    }


}
