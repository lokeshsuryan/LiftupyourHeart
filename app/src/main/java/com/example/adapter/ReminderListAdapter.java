package com.example.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragment.AddReminderFragment;
import com.example.liftupyourheart.R;
import com.example.model.AlarmDao;
import com.example.utills.AppConstant;

import java.util.List;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder> {
    Context context;
    private final List<AlarmDao> mValues;

    public ReminderListAdapter(Context context, List<AlarmDao>items) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ReminderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ReminderListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReminderListAdapter.ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.prayerItem.setText(holder.mItem.getPrayerName());
        holder.parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(AddReminderFragment.getInstance(), "AddReminderFragment", holder.mItem,position);
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
    private void loadFragment(Fragment fragment, String type,AlarmDao mItem,int pos) {
// create a FragmentManager
        FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        Bundle bundle=new Bundle();
        AppConstant.alearmId=mItem.getRepeartAlaramType();
        AppConstant.prayerName=mItem.getPrayerName();
        AppConstant.prayerID=mItem.getPrayerId();
        AppConstant.prayerDesc=mItem.getDescription();
        bundle.putString("prayerName",mItem.getPrayerName());
        bundle.putString("alearmTime",mItem.getAlarmTime());
        bundle.putInt("alearmRepeded",mItem.getRepeartAlaramType());
        bundle.putInt("position",pos);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.addToBackStack(type);
        fragmentTransaction.commit(); // save the changes
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView prayerItem;
        public RelativeLayout parentLL;
        public ImageView selectIV;
        public AlarmDao mItem;

        public ViewHolder(View view) {
            super(view);

            parentLL = (RelativeLayout) view.findViewById(R.id.parentLL);
            selectIV = (ImageView) view.findViewById(R.id.selectIV);
            prayerItem = (TextView) view.findViewById(R.id.prayerItem);
        }

    }
}