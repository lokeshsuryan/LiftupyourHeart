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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.activity.databinding.FragmentViewGroupBinding;
import com.liftupmyheart.adapter.ViewGroupListAdapter;
import com.liftupmyheart.model.GroupDetailDao;
import com.liftupmyheart.model.ServerResponseHeart;
import com.liftupmyheart.utills.AppConstant;
import com.liftupmyheart.viewModel.ViewGroupModel;

import java.util.List;

public class ViewGroupFragment extends Fragment implements View.OnClickListener {
    FragmentViewGroupBinding binding;
    ViewGroupModel viewGroupModel;
    ViewGroupListAdapter viewGroupListAdapter;
    private static ViewGroupFragment instance = null;

    public static ViewGroupFragment getInstance() {
        if (instance == null) {
            instance = new ViewGroupFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_group, null, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        setActionBar();
        viewGroupModel = ViewModelProviders.of(this).get(ViewGroupModel.class);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.groupRv.setLayoutManager(layoutManager);
        binding.newGroup.setOnClickListener(this);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                binding.groupRv.getContext(),
                DividerItemDecoration.VERTICAL
        );
        binding.groupRv.addItemDecoration(mDividerItemDecoration);
        binding.loaderLayout.setVisibility(View.VISIBLE);
        viewGroupModel.getGroupList(AppConstant.userId).observe(this, new Observer<ServerResponseHeart<List<GroupDetailDao>>>() {
            @Override
            public void onChanged(@Nullable ServerResponseHeart<List<GroupDetailDao>> listServerResponseHeart) {

                try {
                    List<GroupDetailDao> groupList = listServerResponseHeart.getData();

                    if(groupList.size()>0) {
                        viewGroupListAdapter = new ViewGroupListAdapter(getActivity(), groupList);
                        binding.groupRv.setAdapter(viewGroupListAdapter);
                    }
                    else{
                        Toast.makeText(getActivity(),"No group found",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e){

                }
            }


        });
        binding.loaderLayout.setVisibility(View.GONE);
    }

    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.all_group), R.color.actionBarColor);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newGroup:
                loadFragment(new CreateGroupFragment(), "CreateGroupFragment");
                break;
        }

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
