package com.liftupmyheart.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.liftupmyheart.custom.AlphaBaticSortContact;
import com.liftupmyheart.model.ContactDao;
import com.liftupmyheart.model.ServerResponseHeart;
import com.liftupmyheart.receiver.NetworkReceiver;
import com.liftupmyheart.rest.Singleton;
import com.liftupmyheart.utills.AppContactList;
import com.liftupmyheart.utills.PreferanceUtils;
import com.liftupmyheart.utills.Utills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Iball on 6/15/2017.
 */

public class HomeIntentService extends IntentService {

    AppContactList contactListObject;
    private HashMap<String, String> hashMap;
    private List<ContactDao> contactList;
    public static Boolean serviceRunning = false;
    List<ContactDao> conList;
    private Callback<ServerResponseHeart<List<ContactDao>>> syncContactCallBack = new Callback<ServerResponseHeart<List<ContactDao>>>() {
        @Override
        public void onResponse(Call<ServerResponseHeart<List<ContactDao>>> call, Response<ServerResponseHeart<List<ContactDao>>> response) {
            try {
                Log.d("under_service","success on contact sync"+response.code());
                if (response.code() == 200)
                {
                    List<ContactDao> data = response.body().getData();
                    if (response.body().getResponse() == 1) {
                        setListview(data);
                        serviceRunning=false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call<ServerResponseHeart<List<ContactDao>>> call, Throwable t) {
            Log.d("under_service","error on contact sync");
            serviceRunning=false;
        }
    };

    public HomeIntentService() {
        super("MyWebRequestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //String requestString = intent.getStringExtra(REQUEST_STRING);
        serviceRunning=true;
        // callCategory();
        Log.d("under_service", "intent home 0");

      init();
    }
    public void init() {

        Log.d("under_service", "intent home 1");
        contactListObject = PreferanceUtils.getContactListFromPreferance(this);
        if (contactListObject == null
                || contactListObject.getContactList() == null
                || contactListObject.getContactList().isEmpty()||contactListObject.getContactList().size() == 0) {
            callAPI(true);
        } else {
            setListview(contactListObject.getContactList());
            }

    }

    public void callAPI(boolean flag) {
        if (NetworkReceiver.isConnected) {
            if (flag)
                try {
                    ContactSyncTask task = new ContactSyncTask();
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } catch (Exception e) {
                }
        }
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
                            obj.setName(Utills.getDisplayNameByPhoneNumber(this,
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
                PreferanceUtils.setContactListIntoPreferance(this, contactListObject);
            }

        } else {
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
                Cursor cursor = getContentResolver().query(uri,
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

                hashMap.put("number", PreferanceUtils.getLoginDetail(HomeIntentService.this).getData().getPhone());
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactList = contactListObject.getContactList();
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
                    Log.d("under_service", "intent home 2");
                    String user_id=PreferanceUtils.getLoginDetail(HomeIntentService.this).getData().getId();
                    Call<ServerResponseHeart<List<ContactDao>>> call =
                            Singleton.getInstance().getRestOkClient().
                                    syncContactToServer(user_id, hashMap);
                    call.enqueue(syncContactCallBack);
                } else {
                    // hideRefreshLayout();
                }
            } catch (Exception e) {
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
