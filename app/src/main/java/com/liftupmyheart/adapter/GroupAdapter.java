package com.liftupmyheart.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.liftupmyheart.activity.R;
import com.liftupmyheart.custom.ButtonTextBlack;
import com.liftupmyheart.custom.CustomTextViewBlack;
import com.liftupmyheart.model.ContactDao;
import com.liftupmyheart.model.Data;
import com.liftupmyheart.model.ReqInvite;
import com.liftupmyheart.utills.PreferanceUtils;
import com.liftupmyheart.utills.Utills;

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
                Uri contentUri = Uri.parse("android.resource://" + mContext.getPackageName() + "/drawable/" + "ic_launcher");

                StringBuilder msg = new StringBuilder();
                msg.append("Hey, Download this awesome app lift up my heart!");
                msg.append("\n");
                msg.append("https://play.google.com/store/apps/details?id=com.liftupmyheart.liftupyourheart&hl=en"); //example :com.package.name

                if (contentUri != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                    shareIntent.setType("*/*");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg.toString());
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    try {
                        mContext.startActivity(Intent.createChooser(shareIntent, "Share via"));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(mContext, "No App Available", Toast.LENGTH_SHORT).show();
                    }
                }
            }


           /* Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "LiftHart");
            share.putExtra(Intent.EXTRA_TEXT, "lifthart.com");
            mContext.startActivity(Intent.createChooser(share, "Share link!"));*/
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