package com.example.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment.ALLReminderFragment;
import com.example.fragment.AddPrayerFragment;
import com.example.fragment.AnsweredListFragment;
import com.example.fragment.CreateGroupFragment;
import com.example.fragment.PrayerFragment;
import com.example.fragment.ViewGroupFragment;
import com.example.liftupyourheart.R;
import com.example.model.HomeListModel;

import java.util.ArrayList;

public class HomeGridAdapter extends RecyclerView.Adapter<HomeGridAdapter.ViewHolder> {
    Context context;
    private final ArrayList<HomeListModel> mValues;

    public HomeGridAdapter(Context context, ArrayList<HomeListModel> items) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        HomeListModel homeListModel = mValues.get(position);
        holder.imgAction.setImageResource(homeListModel.getmResources());
        holder.listItem.setBackgroundColor(context.getResources().getIntArray(R.array.itemColor)[position]);
        holder.nameAction.setText(context.getResources().getStringArray(R.array.item)[position]);
        holder.parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    loadFragment(PrayerFragment.getInstance(), "PrayerFragment");
                } else if (position == 1) {
                    loadFragment(AddPrayerFragment.getInstance(), "AddPrayerFragment");
                } else if (position == 2) {
                    loadFragment(ViewGroupFragment.getInstance(), "ViewGroupFragment");
                } else if (position == 3) {
                    loadFragment(CreateGroupFragment.getInstance(), "CreateGroupFragment");
                } else if (position == 4) {
                    loadFragment(ALLReminderFragment.getInstance(), "ReminderFragment");
                } else if (position == 5) {
                    loadFragment(AnsweredListFragment.getInstance(), "AnsweredListFragment");
                }
            }
        });
       /* if (holder.mItem.isSelected()) {
            holder.selectIV.setImageResource(R.drawable.ic_check);
        } else
            holder.selectIV.setImageResource(0);
        holder.parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AlarmDao alarmDao : mValues) {

                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void loadFragment(Fragment fragment, String type) {
        // create a FragmentManager
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
       // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
      // replace the FrameLayout with new Fragment
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.addToBackStack(type);
        fragmentTransaction.commit(); // save the changes

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameAction;
        public LinearLayout parentLL;
        public CardView listItem;
        public ImageView imgAction;

        public ViewHolder(View view) {
            super(view);

            parentLL = (LinearLayout) view.findViewById(R.id.parentLL);
            listItem = (CardView) view.findViewById(R.id.listItem);
            imgAction = (ImageView) view.findViewById(R.id.imgAction);
            nameAction = (TextView) view.findViewById(R.id.nameAction);
        }

    }
}