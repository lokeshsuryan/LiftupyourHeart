package com.example.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.model.GroupDetailDao;
import com.example.model.Participants;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class GroupDetailFragment extends Fragment {
    RecyclerView recyclerView;
    GroupDetailDao groupDetailDao;
    public static GroupDetailFragment newInstance() {
        GroupDetailFragment fragment = new GroupDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_detail_list_item, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle=getArguments();
        groupDetailDao = (GroupDetailDao)bundle.getSerializable("groupDetail");
        ArrayList<Participants> participantsArrayList=groupDetailDao.getParticipants();
        recyclerView.setAdapter(new GroupDetailAdapterAdapter(getActivity(),participantsArrayList));
        setActionBar();
    }
    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.group_detail), R.color.actionBarColor);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}
