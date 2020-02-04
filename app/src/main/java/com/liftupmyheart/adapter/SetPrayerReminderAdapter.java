package com.liftupmyheart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.custom.CustomTextViewBlack;
import com.liftupmyheart.model.PrayerDao;
import com.liftupmyheart.utills.AppConstant;
import com.liftupmyheart.utills.PreferanceUtils;

import java.util.List;

public class SetPrayerReminderAdapter extends RecyclerView.Adapter<SetPrayerReminderAdapter.ViewHolder> {

    private final List<PrayerDao> mValues;
    Context context;
    boolean activate = false;

    public SetPrayerReminderAdapter(List<PrayerDao> items, Context context) {
        mValues = items;
        this.context = context;
    }

    @Override
    public SetPrayerReminderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.set_reminder_list_item, parent, false);
        return new SetPrayerReminderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SetPrayerReminderAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.txtPrayerName.setText(holder.mItem.getTitle());
        //holder.mContentView.setText(mValues.get(position).content);
        if (holder.mItem.isSelected())
            holder.selectIV.setVisibility(View.VISIBLE);
        else {
            holder.selectIV.setVisibility(View.GONE);
        }
        holder.parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (PrayerDao prayerDao : mValues) {
                    prayerDao.setSelected(false);
                }
                AppConstant.prayerName=holder.mItem.getTitle();
                AppConstant.prayerDesc=holder.mItem.getDescription();
                holder.mItem.setSelected(true);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    private void saveList() {
        Gson gson = new Gson();
        PreferanceUtils.setFirstPrayer(context, gson.toJson(mValues));

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTextViewBlack txtPrayerName;
        private RelativeLayout selectIV;
        RelativeLayout parentLL;
        private ImageView handleView;
        public PrayerDao mItem;


        public ViewHolder(View view) {
            super(view);
            txtPrayerName = (CustomTextViewBlack) view.findViewById(R.id.txtPrayerName);
            parentLL = (RelativeLayout) view.findViewById(R.id.parentLL);
            selectIV = (RelativeLayout) view.findViewById(R.id.selectIV);

        }

    }

}