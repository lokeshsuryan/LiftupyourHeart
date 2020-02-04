package com.liftupmyheart.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.liftupmyheart.activity.R;
import com.liftupmyheart.model.Participants;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class GroupDetailAdapterAdapter extends RecyclerView.Adapter<GroupDetailAdapterAdapter.ViewHolder> {

    private Context context;
    private final ArrayList<Participants> mValues;

    public GroupDetailAdapterAdapter(Context context, ArrayList<Participants> items){
        this.context = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_detail_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Participants participants = mValues.get(position);
        holder.itemName.setText(participants.getFirst_name()+" "+participants.getLast_name());
        holder.userNumber.setText(participants.getPhone());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public  View mView;
        public  TextView itemName;
        public  TextView userNumber;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemName = (TextView) view.findViewById(R.id.itemName);
            userNumber = (TextView) view.findViewById(R.id.userNumber);
        }
    }
}
