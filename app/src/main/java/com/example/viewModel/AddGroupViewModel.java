package com.example.viewModel;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.adapter.GroupAdapter;
import com.example.custom.AlphaBaticSortContact;
import com.example.fragment.CreateGroupFragment;
import com.example.liftupyourheart.R;
import com.example.model.ContactDao;
import com.example.model.CreateGroupDao;
import com.example.model.ServerResponseHeart;
import com.example.receiver.NetworkReceiver;
import com.example.rest.Singleton;
import com.example.utills.AppConstant;
import com.example.utills.AppContactList;
import com.example.utills.PreferanceUtils;
import com.example.utills.Utills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Pintu Riontech on 8/8/16.
 */
public class AddGroupViewModel {

    boolean addMembers = true;
    private CreateGroupFragment createGroupFragment;
    private Context mContext;
    private SwipeRefreshLayout mRefreshLayout;
    private HashMap<String, String> hashMap;
    private List<ContactDao> contactList;
    List<ContactDao> conList;
    Boolean callApiOnce = false;
    int count = 1;



    public AddGroupViewModel(CreateGroupFragment fragment,SwipeRefreshLayout refreshLayout) {

        // add dummy line
        mRefreshLayout=refreshLayout;
        createGroupFragment = fragment;
        mContext = fragment.getActivity();
    }

    public void setSearchFilter(EditText editText, final GroupAdapter adapter) {
        try {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if (adapter != null)
                        //adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } catch (Exception e) {
            // AppLog.e(TAG, e.getMessage(), e);
        }
    }

    public void init() {
        new ContactSyncTask().execute();
    }
    public void setListview(List<ContactDao> data) {
        if (data != null && !data.isEmpty()) {
            AppContactList contactListObject = new AppContactList();

            int index = 0;

            List<ContactDao> list = new ArrayList<>();
            list.addAll(data);

            Collections.sort(list, new AlphaBaticSortContact());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getRegisteration().equalsIgnoreCase("1")) {
                    try {
                        ContactDao obj = list.get(i);
                        if (Utills.isValidString(obj.getPhone())) {
                            obj.setName(Utills.getDisplayNameByPhoneNumber(mContext,
                                    obj.getPhone()));
                            list.remove(i);
                            list.add(index, obj);
                            index++;
                        }
                    } catch (Exception e) {
                    }
                }
            }

            // add code to handle nullpoint exception
            if (list.size() == 0) {
                callAPI(true);
            } else {
                contactListObject.setContactList(list);
                PreferanceUtils.setContactListIntoPreferance(mContext, contactListObject);
                createGroupFragment.setDataList(list);

            }

        } else {
        }
    }
    /**
     *
     */
    public void callAPI(boolean flag) {
        if (NetworkReceiver.isConnected) {
                try {
                    ContactSyncTask task = new ContactSyncTask();
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } catch (Exception e) {
                }
        } else {
            Utills.showSnackToast(mContext.getResources().getString(R.string.no_internet_connection),mContext);
        }
    }




    /**
     * get device contact
     */

    private class ContactSyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            hashMap = new HashMap<String, String>();
            try {
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                Cursor cursor = mContext.getContentResolver().query(uri,
                        new String[]{"display_name", "sort_key", ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI, ContactsContract.CommonDataKinds.Email.ADDRESS}, null, null, "sort_key");
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(0);
                        String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (phone.length() > 9) {
                            if (phone.charAt(0) == '0') {
                                phone = phone.substring(1, phone.length());
                            } else if (phone.charAt(0) == '+')
                                phone = phone.substring(3, phone.length());
                            hashMap.put(phone, name);
                        }
                    } while (cursor.moveToNext());
                }

                hashMap.put("number", PreferanceUtils.getLoginDetail(mContext).getData().getPhone());
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactList = PreferanceUtils.getContactListFromPreferance(mContext).getContactList();
            conList = new ArrayList<>();
            conList.addAll(contactList);
            try {
                if (hashMap.size() != contactList.size()) {

                    if (contactList.size() > 0)
                        for (ContactDao contactDao : contactList) {
                            if (hashMap.containsKey(contactDao.getPhone()) && contactDao.getRegisteration().equalsIgnoreCase("1")) {
                                hashMap.remove(contactDao.getPhone());
                            } else {
                                conList.remove(contactDao);
                            }
                        }


                    //conList.clear();
                    Call<ServerResponseHeart<List<ContactDao>>> call =
                            Singleton.getInstance().getRestOkClient().
                                    syncContactToServer(AppConstant.userId, hashMap);
                    call.enqueue(syncContactCallBack);
                } else {
                    // hideRefreshLayout();
                }
            } catch (Exception e) {
            }
        }
    }


 public void createGroup(String id,CreateGroupDao createGroupDao){
     Call<ServerResponseHeart> call =
             Singleton.getInstance().getRestOkClient().
                     createGroup(id,createGroupDao);
     call.enqueue(createGroup);

 }


    private Callback<ServerResponseHeart> createGroup = new Callback<ServerResponseHeart>() {
        @Override
        public void onResponse(Call<ServerResponseHeart> call, Response<ServerResponseHeart> response) {
            try {
                if (response.code() == 200) {
                    if (response.body().getResponse() == 1) {
                        Utills.showSnackToast(response.body().getMessage(), mContext);
                    }
                }else{
                    Utills.showSnackToast(mContext.getResources().getString(R.string.server_error),mContext);

                }

            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ServerResponseHeart> call, Throwable t) {
        Utills.showSnackToast(mContext.getResources().getString(R.string.server_error),mContext);
    }
};


    private Callback<ServerResponseHeart<List<ContactDao>>> syncContactCallBack = new Callback<ServerResponseHeart<List<ContactDao>>>() {
        @Override
        public void onResponse(Call<ServerResponseHeart<List<ContactDao>>> call, Response<ServerResponseHeart<List<ContactDao>>> response) {
            try {
                mRefreshLayout.setRefreshing(false);
                if (response.code() == 200) {
                    List<ContactDao> data = response.body().getData();
                    if (response.body().getResponse() == 1) {
                        addMembers = true;
                        setListview(data);
                        Log.d("service shutdown....", "HomeIntentService.class");
                        //new DeviceContactSyncWithServerTask(data, mListDeviceContact).execute();
                    } else {
                        if (Utills.isValidString(response.body().getMessage())) {
                            Utills.showSnackToast(response.body().getMessage(),mContext);
                        } else {
                            Utills.showSnackToast(mContext.getResources().getString(R.string.server_error),mContext);
                        }

                    }
                } else {
                    if (Utills.isValidString(response.body().getMessage())) {
                        Utills.showSnackToast(response.body().getMessage(),mContext);
                    } else {
                        Utills.showSnackToast(mContext.getResources().getString(R.string.server_error),mContext);
                    }
                }
            } catch (Exception e) {
                AppConstant.stopProgress();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call<ServerResponseHeart<List<ContactDao>>> call, Throwable t) {
            mRefreshLayout.setRefreshing(false);
            AppConstant.stopProgress();
            Utills.showSnackToast(mContext.getResources().getString(R.string.server_error),mContext);
        }
    };
}