package com.liftupmyheart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.liftupmyheart.activity.R;
import com.liftupmyheart.fragment.MainPrayerFragment;
import com.liftupmyheart.model.PrayerDao;
import com.liftupmyheart.utills.PreferanceUtils;

import java.util.List;

public class PrayerPagerAdapter extends PagerAdapter {

    private Context context;
    List<PrayerDao> prayerDaoList;

    public PrayerPagerAdapter(Context context, List<PrayerDao> prayerDaoList) {
        this.context = context;
        this.prayerDaoList = prayerDaoList;
    }

    public void notfyAdapter(){
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        PrayerDao prayerDao = prayerDaoList.get(position);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_pager_item_prayer, collection, false);
        TextView praayerTitle = layout.findViewById(R.id.prayerTitle);
        TextView prayerDesc = layout.findViewById(R.id.prayerDesc);
        praayerTitle.setText(prayerDao.getTitle());
        showDes(prayerDesc);
        prayerDesc.setText(prayerDao.getDescription());
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
    }
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    @Override
    public int getCount() {
        return this.prayerDaoList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    private void showDes(TextView prayerDesc) {
        String des = PreferanceUtils.getSettingDetail(context, "des");
        if (des.equalsIgnoreCase("always")) {
            prayerDesc.setVisibility(View.VISIBLE);
        } else if (des.equalsIgnoreCase("never")) {
            prayerDesc.setVisibility(View.GONE);
        } else if (des.equalsIgnoreCase("ask")) {
            if (MainPrayerFragment.isdesVisible)
                prayerDesc.setVisibility(View.VISIBLE);
            else {
                prayerDesc.setVisibility(View.GONE);
            }
        }
    }

}
