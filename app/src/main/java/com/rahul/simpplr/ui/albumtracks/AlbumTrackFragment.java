package com.rahul.simpplr.ui.albumtracks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rahul.simpplr.R;
import com.rahul.simpplr.base.BaseFragment;
import com.rahul.simpplr.databinding.FragmentAlbumBinding;
import com.rahul.simpplr.ui.album.AlbumTracksResponseModel;
import com.rahul.simpplr.ui.album.AlbumViewModel;
import com.rahul.simpplr.utility.Listeners;
import com.rahul.simpplr.utility.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class AlbumTrackFragment extends BaseFragment<FragmentAlbumBinding, AlbumViewModel> implements Listeners.ItemClickListener {

    public static final String TAG = AlbumTrackFragment.class.getSimpleName();
    private static final String PLAYLIST_ID = "PLAYLIST_ID";

    @Inject
    ViewModelFactory factory;

    private AlbumViewModel viewModel;
    private FragmentAlbumBinding mBinding;
    private List<AlbumTracksResponseModel.AlbumTracksData> albumTrackList;
    private AlbumTrackAdapter adapter;
    private Context context;
    private Listeners.MainListener listener;
    private String playlistId;


    public static AlbumTrackFragment newInstance(String playlistId) {
        Bundle args = new Bundle();
        args.putString(PLAYLIST_ID, playlistId);
        AlbumTrackFragment fragment = new AlbumTrackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_album;
    }

    @Override
    public AlbumViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this,factory).get(AlbumViewModel.class);
        return viewModel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        listener = (Listeners.MainListener)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent();
        subscribeObserver();

        if(playlistId!=null) {
            viewModel.fetchAlbumTrackInfo(playlistId, false, null);
        }
    }

    private void getIntent() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            playlistId = bundle.getString(PLAYLIST_ID, null);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = getViewDataBinding();
        setAdapter();
        return mBinding.getRoot();
    }

    private void setAdapter() {
        albumTrackList =new ArrayList<>();
        adapter = new AlbumTrackAdapter(albumTrackList,this, getContext());
        mBinding.rvAlbum.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvAlbum.setAdapter(adapter);

    }

    private void subscribeObserver() {
        viewModel.getToastObservable().observe(this, this::showToast);
        viewModel.getAlbumTrackObservable().observe(this, albumTrackData -> {

            if(albumTrackData !=null){
                List<AlbumTracksResponseModel.AlbumTracksData> albumList =  albumTrackData.getItems();
                albumTrackList.clear();
                albumTrackList.addAll(albumList);
                adapter.notifyDataSetChanged();
                }else{
                    if(listener!=null){
                    listener.onLogout(null);
                }
            }
        });
    }

    @Override
    public void onItemClick(Object object, Object object2) {

    }
}
