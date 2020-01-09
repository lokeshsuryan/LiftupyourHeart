package com.example.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.custom.CustomTextViewBlack;
import com.example.fragment.AnswerPrayerFragment;
import com.example.helper.ItemTouchHelperAdapter;
import com.example.helper.OnStartDragListener;
import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.model.PrayerDao;
import com.example.utills.PreferanceUtils;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

public class PrayerListRecyclerViewAdapter extends RecyclerView.Adapter<PrayerListRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private final List<PrayerDao> mValues;
    Context context;
    boolean activate=false;
    private final OnStartDragListener mDragStartListener;
    public PrayerListRecyclerViewAdapter(List<PrayerDao> items, Context context,OnStartDragListener dragStartListener) {
        mValues = items;
        this.context=context;
        mDragStartListener = dragStartListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prayer_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.txtGroupName.setText(holder.mItem.getTitle());
        //holder.mContentView.setText(mValues.get(position).content);

        holder.parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AnswerPrayerFragment(),"AnswerPrayerFragment",context,holder.mItem);


            }
        });

        if(activate) {
            holder.handleView.setVisibility(View.VISIBLE);
        }
        else{
            holder.handleView.setVisibility(View.GONE);
        }

        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }
    public void activateButtons(boolean activate) {
        this.activate = activate;
        notifyDataSetChanged(); //need to call it for the child views to be re-created with buttons.
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mValues, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        saveList();
        return true;
    }

    private void saveList() {
        Gson gson = new Gson();
        PreferanceUtils.setFirstPrayer(context, gson.toJson(mValues));

    }

    @Override
    public void onItemDismiss(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTextViewBlack txtGroupName;
        private RelativeLayout parentLL;
        private  ImageView handleView;
        public PrayerDao mItem;


        public ViewHolder(View view) {
            super(view);
            txtGroupName = (CustomTextViewBlack) view.findViewById(R.id.txtGroupName);
            parentLL = (RelativeLayout) view.findViewById(R.id.parentLL);
            handleView = (ImageView) view.findViewById(R.id.handle);

        }

    }

    private void loadFragment(Fragment fragment, String type, Context context,PrayerDao prayerDao) {
// create a FragmentManager
        FragmentManager fm = ((MainActivity)context).getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        Bundle bundle=new Bundle();
        bundle.putString("pid",prayerDao.getId());
        bundle.putString("title",prayerDao.getTitle());
        bundle.putString("desc",prayerDao.getDescription());
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.addToBackStack(type);
        fragmentTransaction.commit(); // save the changes
    }
}
