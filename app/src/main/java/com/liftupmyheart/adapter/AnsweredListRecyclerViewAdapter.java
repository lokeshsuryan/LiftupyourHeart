package com.liftupmyheart.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.custom.CustomTextViewBlack;
import com.liftupmyheart.fragment.AgainAnswerFragment;
import com.liftupmyheart.model.AnswerListDao;

import java.util.List;

public class AnsweredListRecyclerViewAdapter extends RecyclerView.Adapter<AnsweredListRecyclerViewAdapter.ViewHolder> {

    private final List<AnswerListDao> mValues;
    Context context;
    public AnsweredListRecyclerViewAdapter(List<AnswerListDao> items,Context context) {
        mValues = items;
        this.context=context;
    }

    @Override
    public AnsweredListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prayer_list_item, parent, false);
        return new AnsweredListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnsweredListRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.txtGroupName.setText(holder.mItem.getTitle());
        //holder.mContentView.setText(mValues.get(position).content);
        holder.parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AgainAnswerFragment(),"AgainAnswerFragment",context,holder.mItem);


            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTextViewBlack txtGroupName;
        private RelativeLayout parentLL;
        public AnswerListDao mItem;
        ImageView handle;

        public ViewHolder(View view) {
            super(view);
            txtGroupName = (CustomTextViewBlack) view.findViewById(R.id.txtGroupName);
            parentLL = (RelativeLayout) view.findViewById(R.id.parentLL);
            handle = (ImageView) view.findViewById(R.id.handle);

        }
    }

    private void loadFragment(Fragment fragment, String type, Context context, AnswerListDao answerListDao) {
// create a FragmentManager
        FragmentManager fm = ((MainActivity)context).getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        Bundle bundle=new Bundle();
        bundle.putString("pid",answerListDao.getId());
        bundle.putString("title",answerListDao.getTitle());
        bundle.putString("desc",answerListDao.getDescription());
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.addToBackStack(type);
        fragmentTransaction.commit(); // save the changes
    }
}
