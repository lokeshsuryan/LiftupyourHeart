package com.example.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.custom.ButtonTextBlack;
import com.example.custom.CustomTextViewBlack;
import com.example.liftupyourheart.R;
import com.example.model.ContactDao;
import com.example.model.Data;
import com.example.model.ReqInvite;
import com.example.utills.PreferanceUtils;
import com.example.utills.Utills;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.Holder> {
    private List<ContactDao> lists;
    private Activity mContext;

    public GroupAdapter(Activity context, List<ContactDao> lists) {
        try {
            this.lists = lists;
            mContext = context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_contact, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ContactDao contactDao = lists.get(position);
        holder.bind(contactDao,position);
    }


    public void reloadData() {
        notifyDataSetChanged();
    }

    private void inviteMember(ContactDao obj, final int pos) {

        Data data = PreferanceUtils.getLoginDetail(mContext).getData();
        ReqInvite invite = new ReqInvite();
        invite.setPhone(obj.getPhone());
        invite.setName(data.getName());
        invite.setUserId(data.getId());
        if (!lists.get(pos).isInvite()) {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "LiftHart");
            share.putExtra(Intent.EXTRA_TEXT, "lifthart.com");
            mContext.startActivity(Intent.createChooser(share, "Share link!"));
        }
    }


    class Holder extends RecyclerView.ViewHolder {
        private CustomTextViewBlack txtContactName;
        private CustomTextViewBlack txtContactNumber;
        private ButtonTextBlack txtContactInvite;
        private CheckBox checkbox;

        public Holder(View convertView) {

            super(convertView);
            txtContactInvite = (ButtonTextBlack) convertView.findViewById(R.id.txtContactInvite);
            txtContactName = (CustomTextViewBlack) convertView.findViewById(R.id.txtContactName);
            txtContactNumber = (CustomTextViewBlack) convertView.findViewById(R.id.txtContactNumber);
            checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);

            /**
             * move on phone call
             */




        }

        public void bind(final ContactDao contactDao,int position) {
            if (Utills.isValidString(contactDao.getName())) {
                txtContactName.setText(contactDao.getName());
            } else {
                txtContactName.setText("");
            }

            txtContactNumber.setText(contactDao.getPhone());
            if (contactDao.getRegisteration().equalsIgnoreCase("0")) {
                txtContactInvite.setVisibility(View.VISIBLE);
                checkbox.setVisibility(View.GONE);
            }

            if (contactDao.getRegisteration().equalsIgnoreCase("1")) {
                txtContactInvite.setVisibility(View.GONE);
                checkbox.setVisibility(View.VISIBLE);

            }
            if (contactDao.isSelected())
                checkbox.setChecked(true);
            else {
                checkbox.setChecked(false);
            }


            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && !contactDao.isSelected())
                        contactDao.setSelected(true);
                    else if (contactDao.isSelected())
                        contactDao.setSelected(false);


                }
            });

            /**
             * move on Invite
             */
            txtContactInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lists.get(position).getPhone().length() > 9) {
                        inviteMember(lists.get(position), position);
                    } else {
                        Utills.showSnackToast("Phone number not valid can't invite."
                                , mContext);
                    }

                }
            });
        }
    }


}