package com.example.liftupyourheart;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fragment.AnswerPrayerFragment;
import com.example.fragment.ChangeNameFragment;
import com.example.fragment.ChangePasswordFragment;
import com.example.fragment.DescriptionFragment;
import com.example.fragment.HomeFragment;
import com.example.fragment.LegalFragment;
import com.example.fragment.PrayerTimeFragment;
import com.example.fragment.ReminderTypeFragment;
import com.example.fragment.ShortOrderFragment;
import com.example.liftupyourheart.databinding.ActivityMainBinding;
import com.example.model.Data;
import com.example.utills.PreferanceUtils;
import com.example.utills.Utills;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ActivityMainBinding bind;
    DrawerLayout drawer;
    MenuItem accountName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();

    }

    private void initView() {

        showActionBarTitle(getResources().getColor(R.color.red));
        loadFragment(new HomeFragment(), "homeFragment");
        drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, bind.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();
        bind.navView.setNavigationItemSelectedListener(this);

        Menu menu = bind.navView.getMenu();

        // find MenuItem you want to change
        accountName = menu.findItem(R.id.accountName);

        // set new title to the MenuItem



    }

    @Override
    protected void onResume() {
        super.onResume();
        Data data=PreferanceUtils.getLoginDetail(MainActivity.this).getData();
        accountName.setTitle(data.getFirst_name()+" "+data.getLast_name());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }


    public void showActionBar(Boolean show, String title) {
        bind.appBarMain.toolbar.setTitle("");
        // bind.appBarMain.toolbar.toolbarTitle.setText(title);
        setSupportActionBar(bind.appBarMain.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

    }

    public void showActionBarTitle(int color) {
        bind.appBarMain.toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        bind.appBarMain.toolbar.setTitle(getString(R.string.lift_up_heart));
        setSupportActionBar(bind.appBarMain.toolbar);
        bind.appBarMain.toolbar.setTitleTextColor(color);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().show();
    }

    public void showActionBarTitleBgColor(Boolean show, String title, int color) {
        bind.appBarMain.toolbar.setTitle(title);
        bind.appBarMain.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        bind.appBarMain.toolbar.setBackgroundColor(getResources().getColor(color));
        //bind.appBarMain.toolbarTitle.setText(title);
        setSupportActionBar(bind.appBarMain.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

    }

    private void loadFragment(Fragment fragment, String type) {
// create a FragmentManager
        bind.appBarMain.fragments.setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if (getIntent().hasExtra("prayerId")) {
            Bundle bundle = new Bundle();
            bundle.putString("prayerId", getIntent().getStringExtra("prayerId"));
            bundle.putString("prayerName", getIntent().getStringExtra("prayerName"));
            bundle.putString("prayerDesc", getIntent().getStringExtra("prayerDesc"));
            fragment.setArguments(bundle);
        }
        getIntent().replaceExtras(new Bundle());
        getIntent().setAction("");
        getIntent().setData(null);
        getIntent().setFlags(0);
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.addToBackStack(type);
        fragmentTransaction.commit(); // save the changes
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                break;
        }
        return true;
    }*/

    @Override
    public void onBackPressed() {
        //FragmentManager manager = getSupportFragmentManager();
        Utills.hideKeyboard(MainActivity.this);
        FragmentManager manager = getSupportFragmentManager();
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }
        else if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStackImmediate();
            String topOnStack = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
            if (topOnStack.equalsIgnoreCase("homeFragment")) {
                showActionBarTitle(getResources().getColor(R.color.red));
            }

        } else {
            exitConfirmationDialog();
        }
    }
    private void exitConfirmationDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.exit_title);
        alertDialog.setMessage(R.string.exit_confirmation_msg);

        alertDialog.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });

        alertDialog.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                Utills.hideKeyboard(MainActivity.this);
                FragmentManager manager = getSupportFragmentManager();
                if (manager.getBackStackEntryCount() > 0) {
                    manager.popBackStackImmediate();
                    String topOnStack = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
                    if (topOnStack.equalsIgnoreCase("homeFragment")) {
                        showActionBarTitle(getResources().getColor(R.color.red));
                    }

                }
                return true;
            case R.id.notification:
                return true;
            case R.id.homeMenu:
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.showDesc) {
            loadFragment(DescriptionFragment.getInstance(), "DescriptionFragment");
        } else if (id == R.id.prayerTime) {
            loadFragment(PrayerTimeFragment.getInstance(), "PrayerTimeFragment");
        } else if (id == R.id.setOrders) {
            loadFragment(ShortOrderFragment.getInstance(), "ShortOrderFragment");
        } else if (id == R.id.reminderType) {
            loadFragment(ReminderTypeFragment.getInstance(), "ReminderTypeFragment");
        } else if (id == R.id.accountName) {
            loadFragment(ChangeNameFragment.getInstance(), "ChangeNameFragment");

        } else if (id == R.id.changePassword) {
            loadFragment(ChangePasswordFragment.getInstance(), "ChangePasswordFragment");
        } else if (id == R.id.legal) {
            loadFragment(LegalFragment.getInstance(), "LegalFragment");

        } else if (id == R.id.logout) {
            Utills.logOut(MainActivity.this);
        }

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


}
