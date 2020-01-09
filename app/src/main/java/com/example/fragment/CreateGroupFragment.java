package com.example.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.adapter.GroupAdapter;
import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.liftupyourheart.databinding.CreateGroupFragmentBinding;
import com.example.model.ContactDao;
import com.example.model.CreateGroupDao;
import com.example.model.Data;
import com.example.model.GroupItemDao;
import com.example.utills.AppConstant;
import com.example.utills.PreferanceUtils;
import com.example.utills.Utills;
import com.example.viewModel.AddGroupViewModel;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CreateGroupFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "paymentExample";
    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    private static final int REQUEST_PHONE_STATE_PERMISSION = 123;
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AVET0sqWDZqiBwWJ6218CofNsyTpK1BYlsEqSJnGuNpbIqgAmmMs0ssWd4zaZqghhF3VWef4z9mrtxJd";
    String amount;
    String trnId;
    List<GroupItemDao> selectedMember;
    String groupName;
    Data dataUser;
    AddGroupViewModel addGroupViewModel;
    CreateGroupFragmentBinding binding;
    List<ContactDao> duplicatList = new ArrayList<>();
    List<ContactDao> contactList;
    GroupAdapter groupAdapter;

    private static CreateGroupFragment instance = null;

    public static CreateGroupFragment getInstance() {
        if (instance == null) {
            instance = new CreateGroupFragment();
        }
        return instance;
    }

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("LiftUpYoursHeart")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem itemNotification=menu.findItem(R.id.notification);
        MenuItem itemHomeMenu=menu.findItem(R.id.homeMenu);
        if(itemNotification!=null)
            itemNotification.setVisible(false);
        itemHomeMenu.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.create_group_fragment, null, false);
        View view = binding.getRoot();
        initView();

        return view;
    }

    private void initView() {

        //set toolbar appearance

        setActionBar();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.contactRv.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                binding.contactRv.getContext(),
                DividerItemDecoration.VERTICAL
        );
        binding.contactRv.addItemDecoration(mDividerItemDecoration);
        addGroupViewModel = new AddGroupViewModel(CreateGroupFragment.this, binding.mSwipeRefreshLayout);
        binding.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items

                if (checkCallPermission()) {
                    showProgressDialog();
                    addGroupViewModel.init();

                }

            }
        });

        binding.newGroup.setOnClickListener(this);
        getDataFromPref();
    }

    public void showProgressDialog(){
        binding.loaderLayout.setVisibility(View.VISIBLE);
    }
    public void hideProgressDialog(){
        binding.loaderLayout.setVisibility(View.GONE);
    }

    private boolean checkCallPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPhoneStatePermission();
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            addGroupViewModel.init();
        } else {
            requestPhoneStatePermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPhoneStatePermission() {
        requestPermissions(
                new String[]{
                        Manifest.permission.READ_CONTACTS},
                REQUEST_PHONE_STATE_PERMISSION);
    }

    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.create_group), R.color.actionBarColor);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(myActionMenuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                Log.d("searchResult", query);
                //UserFeedback.show( "SearchOnQueryTextSubmit: " + query);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                MenuItemCompat.collapseActionView(myActionMenuItem);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                if (s != null && contactList != null&& contactList.size()>0) {
                    if (duplicatList.size() == 0) {
                        duplicatList.addAll(contactList);

                    }
                    contactList.clear();
                    for (ContactDao contactDao : duplicatList) {
                        if (contactDao.getName().toLowerCase().startsWith(s.toString().toLowerCase()) || contactDao.getPhone().startsWith(s.toString())) {
                            contactList.add(contactDao);
                        }
                    }
                    groupAdapter.notifyDataSetChanged();
                } else {
                    contactList.addAll(duplicatList);
                    duplicatList.clear();
                    groupAdapter.notifyDataSetChanged();
                }
                Log.d("searchResult", s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void getDataFromPref() {
        contactList = PreferanceUtils.getContactListFromPreferance(getActivity()).getContactList();
        if (contactList != null) {
            if (contactList.size() > 0) {

                groupAdapter = new GroupAdapter(getActivity(), contactList);
                binding.contactRv.setAdapter(groupAdapter);
            } else {

                if (checkCallPermission()) {
                    showProgressDialog();
                    addGroupViewModel.init();

                }
            }
        } else {
            if (checkCallPermission()) {
                showProgressDialog();
                addGroupViewModel.init();

            }
        }
    }


    public void setDataList(List<ContactDao> contactList) {
         hideProgressDialog();
        this.contactList = contactList;
        if (groupAdapter == null) {
            groupAdapter = new GroupAdapter(getActivity(), contactList);
            binding.contactRv.setAdapter(groupAdapter);
        } else {
            groupAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.newGroup:
                groupName = binding.groupnameET.getText().toString();
                selectedMember = new ArrayList<>();
                dataUser = PreferanceUtils.getLoginDetail(getActivity()).getData();
                for (ContactDao contactDao : contactList) {
                    if (contactDao.isSelected()) {
                        GroupItemDao groupItemDao = new GroupItemDao();
                        groupItemDao.setId(contactDao.getId());
                        groupItemDao.setName(contactDao.getName());
                        selectedMember.add(groupItemDao);
                    }
                }
                if(selectedMember.size()>0) {
                    GroupItemDao groupItemDao = new GroupItemDao();
                    groupItemDao.setId(dataUser.getId());
                    groupItemDao.setName(dataUser.getName());
                    selectedMember.add(groupItemDao);
                    if (groupName.isEmpty())
                        binding.groupnameET.setError("Enter Group Name");
                    else if (selectedMember.size() == 0)
                        Utills.showSnackToast("Atleast select one member", getActivity());
                    else {
                        createMemberGroup();

                        //amount = 2 * (selectedMember.size() - 1) + "";

                        //makePayment(amount);
                    }
                }
                else{
                    Utills.showSnackToast("Atleast select one member", getActivity());
                }

        }

    }


    protected void displayResultText(String result) {

        //((TextView)findViewById(R.id.txtResult)).setText("Result : " + result);
        Toast.makeText(
                getActivity(),
                result, Toast.LENGTH_LONG)
                .show();
    }



    /*  public void makePayment(String amountValue) {

              amount=amountValue;
              Intent intent = new Intent(getActivity(), PayPalService.class);
              intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
              getActivity().startService(intent);
              onBuyPressed();
      }


      public void onBuyPressed() {
          *//*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         *//*
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        *//*
         * See getStuffToBuy(..) for examples of some available payment options.
         *//*

        Intent intent = new Intent(getActivity(), PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(amount), "USD", "Plan",
                paymentIntent);
    }*/

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        final JSONObject obj = new JSONObject(confirm.toJSONObject().toString(4)).getJSONObject("response"); ;
                        trnId=obj.getString("id");
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        /**
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         *
                        createMemberGroup();
                        //displayResultText("PaymentConfirmation info received from PayPal");


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        displayResultText("Future Payment code received from PayPal");

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        displayResultText("Profile Sharing code received from PayPal");

                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }*/

    private void createMemberGroup() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String currentDate = df.format(cal.getTime());
        CreateGroupDao createGroupDao = new CreateGroupDao();
        createGroupDao.setParticipant(selectedMember);
        createGroupDao.setGroupName(groupName);
        createGroupDao.setInvoice_number(trnId);
        createGroupDao.setRency_code(AppConstant.currencyCode);
        createGroupDao.setAmount(amount);
        createGroupDao.setCreate_time(currentDate);
        createGroupDao.setType("android");
        addGroupViewModel.createGroup(AppConstant.userId, createGroupDao);
    }

   /* private void sendAuthorizationToServer(PayPalAuthorization authorization) {

        *//**
         * exchange the authorization code for OAuth access and refresh tokens.
         *
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         *
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
         *//*

    }*/



    @Override
    public void onDestroy() {
        // Stop service when done
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }
}