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

import java.util.List;

import static com.liftupmyheart.utills.AppConstant.alearmId;

public class RepartAlarmAdapter  extends RecyclerView.Adapter<RepartAlarmAdapter.ViewHolder> {
    Context context;
    private final List<NextPrayerListItem> mValues;
    MediaPlayer mediaPlayer;

    public RepartAlarmAdapter(Context context, List<NextPrayerListItem> items) {
        mValues = items;
        this.context = context;
    }

    @Override
    public RepartAlarmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new RepartAlarmAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RepartAlarmAdapter.ViewHolder holder, int position) {
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
                alearmId=holder.mItem.getAlarmId();
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