package com.rahul.simpplr.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ExpandableListView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.rahul.simpplr.R;
import com.rahul.simpplr.base.BaseActivity;
import com.rahul.simpplr.databinding.ActivityMainBinding;
import com.rahul.simpplr.db.AppPreferencesHelper;
import com.rahul.simpplr.db.PreferencesHelper;
import com.rahul.simpplr.ui.album.AlbumFragment;
import com.rahul.simpplr.ui.album.AlbumViewModel;
import com.rahul.simpplr.ui.album.CompleteInfoModel;
import com.rahul.simpplr.ui.albumtracks.AlbumTrackFragment;
import com.rahul.simpplr.ui.login.LoginActivity;
import com.rahul.simpplr.utility.Listeners;
import com.rahul.simpplr.utility.ToastAndErrorMessage;
import com.rahul.simpplr.utility.ViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<ActivityMainBinding, AlbumViewModel>
        implements NavigationView.OnNavigationItemSelectedListener, Listeners.MainListener {

    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesHelper preferenceHelper;

    private AlbumViewModel viewModel;
    private ActivityMainBinding mBinding;
    private List<CompleteInfoModel> completeInfoList = new ArrayList<>();
    private HashMap<CompleteInfoModel, List<CompleteInfoModel.CompleteInfoData>> childList = new HashMap<>();
    private ExpandableListAdapter expandableListAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected AlbumViewModel setViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlbumViewModel.class);
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = getViewBinding();
        setSupportActionBar(mBinding.includeAppBarLayout.toolbar);
        setDrawer();
        setAdapter();
        subscribeObserver();
        setListener();
        AlbumFragment albumFragment= AlbumFragment.newInstance();
        setFragment(albumFragment);
    }

    private void setListener() {

        mBinding.expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            if (completeInfoList.get(groupPosition)!=null) {
                CompleteInfoModel model = completeInfoList.get(groupPosition);
                if (model!=null) {
                   loadFragment(model.getAlbumId());
                }
            }

            return false;
        });
    }

    private void setAdapter() {
        expandableListAdapter = new ExpandableListAdapter(this, completeInfoList, childList);
        mBinding.expandableListView.setAdapter(expandableListAdapter);
    }

    private void subscribeObserver() {
        viewModel.getCompleteDataObservable().observe(this, newData -> {
            completeInfoList.clear();
            completeInfoList.addAll(newData);

            for (CompleteInfoModel completeInfoModel : completeInfoList) {
                childList.put(completeInfoModel, completeInfoModel.getTracks());
            }
            expandableListAdapter.notifyDataSetChanged();
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(fragment.getClass().getCanonicalName());

        if(fragment.getClass() == AlbumTrackFragment.class){
            transaction.replace(R.id.screenContainer, fragment, fragment.getClass().getCanonicalName());
        }else{
            transaction.add(R.id.screenContainer, fragment, fragment.getClass().getCanonicalName());
        }
        transaction.commit();
    }

    private void setDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout, mBinding.includeAppBarLayout.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
       // mBinding.navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {
            AlbumTrackFragment albumTrackFragment = AlbumTrackFragment.newInstance("2");
            setFragment(albumTrackFragment);
        } else if (id == R.id.nav_send) {
            logout();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(){
        preferenceHelper.setStringData(AppPreferencesHelper.PREF_KEY_ACCESS_TOKEN,null);
        preferenceHelper.setBooleanData(AppPreferencesHelper.PREF_KEY_IS_LOGGED_IN,false);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    @Override
    public void onLogout(Object object) {
        logout();
    }

    @Override
    public void loadFragment(Object object) {
        String playlistId = (String)object;

        if(playlistId==null){
            showToast(ToastAndErrorMessage.AN_ERROR_OCCURRED);
        }else {
            AlbumTrackFragment albumTrackFragment = AlbumTrackFragment.newInstance(playlistId);
            setFragment(albumTrackFragment);
        }
    }
}
