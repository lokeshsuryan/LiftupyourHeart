package com.liftupmyheart.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.custom.CustomTextViewBlack;
import com.liftupmyheart.fragment.GroupDetailFragment;
import com.liftupmyheart.model.GroupDetailDao;

import java.util.List;

public class ViewGroupListAdapter extends RecyclerView.Adapter<ViewGroupListAdapter.Holder> {
    private List<GroupDetailDao> lists;
    private Activity mContext;

    public ViewGroupListAdapter(Activity context, List<GroupDetailDao> lists) {
        try {
            this.lists = lists;
            mContext = context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public ViewGroupListAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_group_listitem, parent, false);
        return new ViewGroupListAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewGroupListAdapter.Holder holder, int position) {
        GroupDetailDao groupDetailDao = lists.get(position);
        holder.parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(GroupDetailFragment.newInstance(), "GroupDetailFragment", groupDetailDao);
            }
        });
        holder.bind(groupDetailDao);
    }
    private void loadFragment(Fragment fragment, String type, GroupDetailDao groupDetailDao) {
// create a FragmentManager
        FragmentManager fm = ((MainActivity)mContext).getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment

        Bundle bundle=new Bundle();
        bundle.putSerializable("groupDetail",groupDetailDao);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.addToBackStack(type);
        fragmentTransaction.commit(); // save the changes
    }




    class Holder extends RecyclerView.ViewHolder {
        private CustomTextViewBlack txtGroupName;
        private LinearLayout parentLL;

        public Holder(View convertView) {

            super(convertView);

            txtGroupName = convertView.findViewById(R.id.txtGroupName);
            parentLL = convertView.findViewById(R.id.parentLL);
        }

        public void bind(GroupDetailDao groupDetailDao) {
            txtGroupName.setText(groupDetailDao.getName());
        }
    }


}