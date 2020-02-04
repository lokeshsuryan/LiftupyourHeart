package com.liftupmyheart.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.liftupmyheart.activity.R;
import com.liftupmyheart.model.NextPrayerListItem;
import com.liftupmyheart.utills.PreferanceUtils;

import java.util.List;

public class RingTimePrayerAdapter extends RecyclerView.Adapter<RingTimePrayerAdapter.ViewHolder> {
    Context context;
    private final List<NextPrayerListItem> mValues;
    MediaPlayer mediaPlayer;

    public RingTimePrayerAdapter(Context context, List<NextPrayerListItem> items) {
        mValues = items;
        this.context = context;
    }

    @Override
    public RingTimePrayerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new RingTimePrayerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RingTimePrayerAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.prayerItem.setText(holder.mItem.getName());
        if (holder.mItem.isSelected()) {
            holder.selectIV.setImageResource(R.drawable.ic_check);
        } else
            holder.selectIV.setImageResource(0);
        holder.parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (NextPrayerListItem nextPrayerListItem : mValues) {
                    if (nextPrayerListItem.isSelected())
                        nextPrayerListItem.setSelected(false);
                }
                holder.mItem.setSelected(true);
                PreferanceUtils.setSettingPrayerTime(context,0,0,holder.mItem.getRingTime(),0,holder.mItem.getName());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView prayerItem;
        public RelativeLayout parentLL;
        public ImageView selectIV;

        public NextPrayerListItem mItem;

        public ViewHolder(View view) {
            super(view);

            parentLL = (RelativeLayout) view.findViewById(R.id.parentLL);
            selectIV = (ImageView) view.findViewById(R.id.selectIV);
            prayerItem = (TextView) view.findViewById(R.id.prayerItem);
        }

    }
}