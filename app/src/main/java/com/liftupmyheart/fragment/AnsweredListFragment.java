package com.liftupmyheart.fragment;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.activity.databinding.FragmentAnsweredListItemBinding;
import com.liftupmyheart.adapter.AnsweredListRecyclerViewAdapter;
import com.liftupmyheart.model.AnswerListDao;
import com.liftupmyheart.model.ServerResponseHeart;
import com.liftupmyheart.utills.Utills;
import com.liftupmyheart.viewModel.SignUpViewModel;

import java.util.List;

/*import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.OnBoomListener;*/

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {}
 * interface.
 */
public class AnsweredListFragment  extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    FragmentAnsweredListItemBinding binding;
    AnsweredListRecyclerViewAdapter answeredListRecyclerViewAdapter;
    private static AnsweredListFragment instance = null;
    int mColumnCount = 1;
    SignUpViewModel signUpViewModel;
    public static AnsweredListFragment getInstance() {
        if (instance == null) {
            instance = new AnsweredListFragment();
        }
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_answered_list_item, null, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {

        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        if (mColumnCount <= 1) {
            binding.allReminder.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            binding.allReminder.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
        }

    }

    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.answered_prayer), R.color.actionBarColor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
                loadFragment(AddPrayerFragment.getInstance(),"AddPrayerFragment");
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
        Utills.showProgressbar(binding.loaderLayoutPrayer);
        signUpViewModel.answerPrayerList().observe(this, new Observer<ServerResponseHeart<List<AnswerListDao>>>() {
            @Override
            public void onChanged(@Nullable ServerResponseHeart<List<AnswerListDao>> serverResponse) {
                Utills.hideProgressbar(binding.loaderLayoutPrayer);
                if (serverResponse != null) {
                    if (serverResponse.getResponse() == 1) {
                        if(serverResponse.getData().size()>0) {
                            List<AnswerListDao> answerList = serverResponse.getData();
                            answeredListRecyclerViewAdapter = new AnsweredListRecyclerViewAdapter(answerList, getActivity());
                            binding.allReminder.setAdapter(answeredListRecyclerViewAdapter);

                        }else {
                            Toast.makeText(getActivity(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();

                        }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void loadFragment(Fragment fragment , String type) {
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