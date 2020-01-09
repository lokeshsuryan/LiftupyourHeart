package com.example.fragment;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.HomeGridAdapter;
import com.example.liftupyourheart.MainActivity;
import com.example.liftupyourheart.R;
import com.example.liftupyourheart.databinding.FragmentHomeBinding;
import com.example.model.HomeListModel;
import com.example.utills.GridSpacingItemDecoration;
import com.example.utills.Utills;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link HomeFragment#} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    FragmentHomeBinding bind;
    ArrayList<HomeListModel> arryListHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_home, null, false);
        View view = bind.getRoot();
        intiView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //((MainActivity) getActivity()).getSupportActionBar().hide();
        arryListHome=new ArrayList<>();
        for (int list=0 ; list<6; list++) {
            HomeListModel homeListModel=new HomeListModel();
            int drawableID = getResources().getIdentifier("img_"+list, "drawable", getContext().getPackageName());
            homeListModel.setmResources(drawableID);
            arryListHome.add(homeListModel);


        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL); // set Horizontal Orientation
        bind.homeListRV.setLayoutManager(gridLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.common_15_dp);
        bind.homeListRV.addItemDecoration(new GridSpacingItemDecoration(spacingInPixels));
        HomeGridAdapter homeGridAdapter=new HomeGridAdapter(getActivity(),arryListHome);
        bind.homeListRV.setAdapter(homeGridAdapter);
        bind.homeIB.setOnClickListener(this);
        bind.logout.setOnClickListener(this);
        setActionBar();
        Bundle bundle=getArguments();
        if (bundle!=null&&bundle.getString("prayerId") != null&&!bundle.getString("prayerId").equalsIgnoreCase("")) {
            Fragment fragment=new AnswerPrayerFragment();
            Bundle bundleAnswerFragment=new Bundle();
            bundleAnswerFragment.putString("pid",bundle.getString("prayerId"));
            bundleAnswerFragment.putString("title",bundle.getString("prayerName"));
            bundleAnswerFragment.putString("desc",bundle.getString("prayerDesc"));
            fragment.setArguments(bundleAnswerFragment);
            loadFragment(fragment,"AnswerPrayerFragment");
        }
    }
    private void setActionBar() {
        ((MainActivity) getActivity()).showActionBarTitle(getResources().getColor(R.color.red));

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(getArguments()!=null){
            getArguments().clear();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return true;
    }
    private void intiView() {
        isStoragePermissionGranted();

    }


    /**
     * @return
     * permission from storage access for DropIn Payment
     */
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeIB:
                //onBackPressed();
                Fragment fragment=MainPrayerFragment.getInstance();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                loadFragment(fragment, "MainFragment");
                break;
            case R.id.logout:
                Utills.logOut(getActivity());
                break;
        }
    }

    public void loadFragment(Fragment fragment, String type) {
        // create a FragmentManager
        FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment

        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.addToBackStack(type);
        fragmentTransaction.commit(); // save the changes

    }

}