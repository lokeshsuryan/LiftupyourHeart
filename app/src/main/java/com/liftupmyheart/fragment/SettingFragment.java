package com.liftupmyheart.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.liftupmyheart.activity.MainActivity;
import com.liftupmyheart.activity.R;
import com.liftupmyheart.activity.databinding.FragmentSettingBinding;
import com.liftupmyheart.model.Data;
import com.liftupmyheart.utills.PreferanceUtils;
import com.liftupmyheart.utills.Utills;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class SettingFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    FragmentSettingBinding binding;
    public static SettingFragment instance = null;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingFragment.
     */
    public static SettingFragment getInstance() {
        if (instance == null)
            instance = new SettingFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        setActionBar();
        binding.showDesc.setText(PreferanceUtils.getSettingDetail(getActivity(),"des"));
        binding.prayerTime.setText(PreferanceUtils.getSettingDetail(getActivity(),"des"));
        binding.setOrder.setText(PreferanceUtils.getSettingDetail(getActivity(),"prayerOrder"));
        binding.reminderType.setText(PreferanceUtils.getSettingDetail(getActivity(),"reminderType"));
        binding.showDescLL.setOnClickListener(this);
        binding.prayerTimeLL.setOnClickListener(this);
        binding.setOrderLL.setOnClickListener(this);
        binding.reminderTypeLL.setOnClickListener(this);
        binding.changeName.setOnClickListener(this);
        binding.changePassword.setOnClickListener(this);
        binding.legal.setOnClickListener(this);
        binding.logout.setOnClickListener(this);
        binding.shareFacebook.setOnClickListener(this);
        binding.shareTwitter.setOnClickListener(this);
        binding.shareMail.setOnClickListener(this);
        binding.whatNew.setOnClickListener(this);
        Data data=PreferanceUtils.getLoginDetail(getActivity()).getData();
        binding.changeName.setText(data.getFirst_name()+" "+data.getLast_name());
    }
    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitleBgColor(true, getString(R.string.settings), R.color.actionBarColor);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showDescLL:
                loadFragment(DescriptionFragment.getInstance(),"DescriptionFragment");
                break;
            case R.id.prayerTimeLL:
                loadFragment(PrayerTimeFragment.getInstance(),"PrayerTimeFragment");
                break;
            case R.id.setOrderLL:
                loadFragment(ShortOrderFragment.getInstance(),"ShortOrderFragment");
                break;
            case R.id.reminderTypeLL:
                loadFragment(ReminderTypeFragment.getInstance(),"ReminderTypeFragment");
                break;
            case R.id.changeName:
                loadFragment(ChangeNameFragment.getInstance(),"ChangeNameFragment");
                break;
            case R.id.changePassword:
                loadFragment(ChangePasswordFragment.getInstance(),"ChangePasswordFragment");
                break;
            case R.id.legal:
                loadFragment(LegalFragment.getInstance(),"LegalFragment");
                break;
            case R.id.logout:
                Utills.logOut(getActivity());
                break;
            case R.id.shareFacebook:
                shareFaceBook("LiftHeeart");
                break;
            case R.id.shareTwitter:
                shareTwitter("Lift Heat");
                break;
            case R.id.shareMail:
                shareEmail("Lift Heat");
                break;
            case R.id.whatNew:
                break;
        }

    }

    private void shareEmail(String lift_heat) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","abc@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void shareFaceBook(String liftHeeart) {
        String urlToShare = "https://stackoverflow.com/questions/7545254";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

// See if official Facebook app is found
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

// As fallback, launch sharer.php in a browser
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }

        startActivity(intent);
    }

    private void loadFragment(Fragment fragment,String type) {
// create a FragmentManager

        FragmentManager fm =getActivity().getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.addToBackStack(type);
        fragmentTransaction.commit(); // save the changes
    }

    private void shareTwitter(String message) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, "This is a Test.");
        tweetIntent.setType("text/plain");

        PackageManager packManager = getActivity().getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
            startActivity(i);
            Toast.makeText(getActivity(), "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("LiftHeart", "UTF-8 should always be supported", e);
            return "";
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
        return true;
    }
}
