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

public class NextPrayerRecyclerViewAdapter extends RecyclerView.Adapter<NextPrayerRecyclerViewAdapter.ViewHolder> {
    Context context;
    private final List<NextPrayerListItem> mValues;
    MediaPlayer mediaPlayer;
    int type;

    public NextPrayerRecyclerViewAdapter(Context context, List<NextPrayerListItem> items, int type) {
        mValues = items;
        this.context = context;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.prayerItem.setText(holder.mItem.getName());
        if (holder.mItem.isSelected()) {
            holder.selectIV.setImageResource(R.drawable.ic_check);
        } else
            holder.selectIV.setImageResource(0);
        holder.parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    for (NextPrayerListItem nextPrayerListItem : mValues) {
                        if (nextPrayerListItem.isSelected())
                            nextPrayerListItem.setSelected(false);
                    }
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        mediaPlayer.release();
                    }
                    if (holder.mItem.getRawId() != 1) {

                        mediaPlayer = MediaPlayer.create(context, holder.mItem.getRawId());
                        mediaPlayer.start();
                    }
                    if (type == 1)
                        PreferanceUtils.setSettingPrayerTime(context, holder.mItem.getRawId(), 0, 0, 0, holder.mItem.getName());
                    else if (type == 2)
                        PreferanceUtils.setSettingPrayerTime(context, 0, 0, 0, holder.mItem.getRawId(), holder.mItem.getName());
                    holder.mItem.setSelected(true);
                    notifyDataSetChanged();
                }
                catch(Exception e){

                }

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

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.release();
        }

    }
}
