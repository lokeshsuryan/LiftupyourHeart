package com.example.adapter;

import android.content.Intent;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.liftupyourheart.BrowsePlan;
import com.example.liftupyourheart.PayPal;
import com.example.liftupyourheart.R;


/**
 * Created by GIGAMOLE on 7/27/16.
 */
public class VerticalPagerAdapter extends PagerAdapter {

    private LayoutInflater mLayoutInflater;
    private BrowsePlan context;

    public VerticalPagerAdapter(final BrowsePlan context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {


        View view = null;

        if (position == 0) {
            view = mLayoutInflater.inflate(R.layout.payment_item_free, container, false);
            TextView plan = view.findViewById(R.id.free);
            /*SpannableString ss1 = new SpannableString("$2.95 Per Month");
            ss1.setSpan(new RelativeSizeSpan(2f), 0, 5, 0); // set size
            plan.setText(ss1);*/
        }
        if (position == 1) {
            view = mLayoutInflater.inflate(R.layout.payment_first_plan, container, false);
            TextView plan = view.findViewById(R.id.free);
            SpannableString ss1 = new SpannableString("$2.95 Per Month");
            ss1.setSpan(new RelativeSizeSpan(2f), 0, 5, 0); // set size
            plan.setText(ss1);
        }
        if (position == 2) {
            view = mLayoutInflater.inflate(R.layout.payment_second_plan, container, false);
            TextView plan = view.findViewById(R.id.free);
            SpannableString ss1 = new SpannableString("$35 Per Month");
            ss1.setSpan(new RelativeSizeSpan(2f), 0, 3, 0); // set size
            plan.setText(ss1);
        }
        container.addView(view);
        //setupItem(view, TWO_WAY_LIBRARIES[position]);

        Button itemButton = view.findViewById(R.id.getStarted);
        itemButton.setOnClickListener((View v) -> {
            int planType = -1;
            int planId = -1;
            if (position == 0) {
                planId = 0;
                planType = 0;
                context.makePayment("0", String.valueOf(planId), String.valueOf(planType), "Heart One monthly");
            } else if (position == 1) {
                planId = 2;
                planType = 0;
                context.makePayment("30", String.valueOf(planId), String.valueOf(planType), "Parish Plan monthly");

                // context.launchDropIn("30",planId,planType,"Parish Plan monthly");
                //$30 payment yearly
            } else if (position == 2) {
                planId = 2;
                planType = 1;

                Intent intent = new Intent(context, PayPal.class);
                intent.putExtra("amount", "350");
                intent.putExtra("planId", planId);
                intent.putExtra("planType", planType);
                intent.putExtra("planName", "Parish Plan Yearly");
                context.startActivity(intent);

                context.makePayment("350", String.valueOf(planId), String.valueOf(planType), "Parish Plan Yearly");

                //context.launchDropIn("350",planId,planType,"Parish Plan Yearly");
                //$350 payment yearly
            }


        });
        Button itemMonthly = view.findViewById(R.id.monthPlan);
        itemMonthly.setOnClickListener((View v) -> {
            int planType = -1;
            int planId = -1;
            if (position == 1) {
                planId = 1;
                planType = 0;

                context.makePayment("2.95", String.valueOf(planId), String.valueOf(planType), "Heart One monthly");

            } else if (position == 2) {
                planId = 2;
                planType = 1;
                context.makePayment("35", String.valueOf(planId), String.valueOf(planType), "Heart One yearly");


                //context.launchDropIn("2.95",planId,planType,"Heart One yearly");
                //$35 payment yearly
            }

        });
        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
}
