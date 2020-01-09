package com.example.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.liftupyourheart.R;
import com.example.listner.SignUpListner;

/**
 * Created by kittybee on 30/04/17.
 */

public class GuidelineAdapter extends PagerAdapter {

    Context mContext;
    SignUpListner listner;
    int[] mResources;
    public GuidelineAdapter(Context context, int[] mResources,SignUpListner listner) {
        mContext = context;
        this.mResources=mResources;
        this.listner=listner;
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View currentPage = null;
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
                currentPage = LayoutInflater.from(mContext).inflate(R.layout.pager_item, null);
                ImageView imageView = (ImageView) currentPage.findViewById(R.id.img_pager_item);
                imageView.setImageResource(mResources[position]);
                container.addView(currentPage);
                break;
            case 4:
                currentPage = LayoutInflater.from(mContext).inflate(R.layout.signup_popup, null);
                ///////////// This page will be default ////////////////////
                Button signUp=(Button) currentPage.findViewById(R.id.signupBtn);
                TextView textView=(TextView) currentPage.findViewById(R.id.liftUpMyHeart);

                SpannableString spannableString = new SpannableString("LIFT UP MY HEART");
                spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.dark_blue)), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.red)), 11, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableString);
                Button loginBtn=(Button) currentPage.findViewById(R.id.loginBtn);
                signUp.setOnClickListener(view-> {
                        listner.signUpMethod();
                });
                loginBtn.setOnClickListener(view-> {
                        listner.loginMethod();
                });
                ((ViewPager) container).setCurrentItem(position);
                container.addView(currentPage);
                ////////////////////////////////////////////////////////////
                break;
        }
        return currentPage;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}