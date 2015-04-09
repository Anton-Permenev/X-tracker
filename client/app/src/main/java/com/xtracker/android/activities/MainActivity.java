package com.xtracker.android.activities;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.xtracker.android.R;
import com.xtracker.android.adapters.DrawerAdapter;
import com.xtracker.android.fragments.HomeFragment;
import com.xtracker.android.fragments.ProfileFragment;
import com.xtracker.android.fragments.ScreenThree;
import com.xtracker.android.fragments.ScreenTwo;
import com.xtracker.android.objects.GApiClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    private String[] mScreenTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Toolbar toolbar;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private GoogleApiClient mGoogleApiClient;

    private String privateKey;
    private long userId;
    private DrawerAdapter mDrawerAdapter;

    public GoogleApiClient getApiClient(){
        return this.mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient client){
        this.mGoogleApiClient = client;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeDrawer();
        loadUserInfo();

        // Initialize the first fragment when the application first loads.
        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    private void loadUserInfo() {
        SharedPreferences sPref = getSharedPreferences("credentials", MODE_PRIVATE);
        privateKey = sPref.getString("privateKey", "");
        userId = sPref.getLong("userId", 0);
    }

    private void initializeDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerList = (ListView) findViewById(R.id.navigation_drawer);
        mDrawerAdapter = new DrawerAdapter(this);
        mDrawerList.setAdapter(mDrawerAdapter);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
                mDrawerLayout.closeDrawers();

                mDrawerAdapter.setSelectedItem(position);
            }
        });
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new ScreenTwo();
                break;
            case 2:
                fragment = new ProfileFragment();
                break;
            case 3:
                fragment = new ScreenThree();
                break;
            default:
                break;
        }

        // Insert the fragment by replacing any existing fragment
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content, fragment).commit();

            //setTitle(mScreenTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // Error
            Log.e(this.getClass().getName(), "Error. Fragment is not created");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
