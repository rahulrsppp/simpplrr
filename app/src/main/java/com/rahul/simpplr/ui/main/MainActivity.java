package com.rahul.simpplr.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rahul.simpplr.R;
import com.rahul.simpplr.base.BaseActivity;
import com.rahul.simpplr.databinding.ActivityMainBinding;
import com.rahul.simpplr.db.AppPreferencesHelper;
import com.rahul.simpplr.db.PreferencesHelper;
import com.rahul.simpplr.ui.album.AlbumFragment;
import com.rahul.simpplr.ui.album.AlbumResponseModel;
import com.rahul.simpplr.ui.album.AlbumTracksResponseModel;
import com.rahul.simpplr.ui.album.AlbumViewModel;
import com.rahul.simpplr.ui.albumtracks.AlbumTrackFragment;
import com.rahul.simpplr.ui.login.LoginActivity;
import com.rahul.simpplr.utility.Listeners;
import com.rahul.simpplr.utility.ToastAndErrorMessage;
import com.rahul.simpplr.utility.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<ActivityMainBinding, AlbumViewModel>
        implements  Listeners.MainListener, Listeners.ItemClickListener {

    public static final int FROM_TRACK_ADAPTER = 1;
    public static final int FROM_ALBUM_ADAPTER = 2;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesHelper preferenceHelper;

    private AlbumViewModel viewModel;
    private ActivityMainBinding mBinding;
    private List<AlbumResponseModel.AlbumData> albumList = new ArrayList<>();
    private List<AlbumTracksResponseModel.AlbumTracksData> trackList = new ArrayList<>();
    private DrawerViewAdapter multipleViewAdapter;

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

        AlbumFragment albumFragment = AlbumFragment.newInstance();
        setFragment(albumFragment);
    }

    private void setListener() {
        View view = mBinding.navView.getHeaderView(0);
        ImageView ivLogout = view.findViewById(R.id.ivLogout);
        ivLogout.setOnClickListener(v ->{
            onLogout(null);
        });
    }


    private void setAdapter() {
        albumList = new ArrayList<>();
        multipleViewAdapter = new DrawerViewAdapter(albumList, trackList, this, this);
        mBinding.recyclerView.setAdapter(multipleViewAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void subscribeObserver() {
        viewModel.getAlbumTrackObservable().observe(this, newData -> {
            trackList.clear();
            trackList.addAll(newData.getItems());
            multipleViewAdapter.notifyDataSetChanged();
        });
    }


    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(fragment.getClass().getCanonicalName());
        transaction.replace(R.id.screenContainer, fragment, fragment.getClass().getCanonicalName());
        transaction.commit();
    }

    private void setDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout, mBinding.includeAppBarLayout.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // setting User Email and Name on Drawer
        String userName = preferenceHelper.getStringData(AppPreferencesHelper.PREF_KEY_USER_NAME);
        String userEmail = preferenceHelper.getStringData(AppPreferencesHelper.PREF_KEY_USER_EMAIL);
        View view = mBinding.navView.getHeaderView(0);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        tvName.setText(userName);
        tvEmail.setText(userEmail);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                System.exit(0);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onLogout(Object object) {
        preferenceHelper.setStringData(AppPreferencesHelper.PREF_KEY_ACCESS_TOKEN, null);
        preferenceHelper.setBooleanData(AppPreferencesHelper.PREF_KEY_IS_LOGGED_IN, false);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void loadFragment(Object object) {
        String playlistId = (String) object;

        if (playlistId == null) {
            showToast(ToastAndErrorMessage.AN_ERROR_OCCURRED);
        } else {
            AlbumTrackFragment albumTrackFragment = AlbumTrackFragment.newInstance(playlistId);
            setFragment(albumTrackFragment);
        }
    }

    @Override
    public void onItemClick(Object object, Object object2) {
        int type = (int) object2;
        if (type == MainActivity.FROM_ALBUM_ADAPTER) {
            AlbumResponseModel.AlbumData albumData = (AlbumResponseModel.AlbumData) object;
            if (albumData != null) {
                String playlistId = albumData.getId();
                viewModel.fetchAlbumTrackInfo(playlistId);
            }
        } else if (type == MainActivity.FROM_TRACK_ADAPTER) {
            AlbumTracksResponseModel.AlbumTracksData albumTrackData = (AlbumTracksResponseModel.AlbumTracksData) object;

            if (albumTrackData != null) {
                setFragment(AlbumTrackFragment.newInstance(albumTrackData.getAlbumId()));
            }
            onBackPressed();
        }
    }

    public void updateDrawer(AlbumResponseModel albumData) {
        List<AlbumResponseModel.AlbumData> albumDataList = albumData.getItems();
        albumList.clear();
        albumList.addAll(albumDataList);
        multipleViewAdapter.notifyDataSetChanged();
    }

}
