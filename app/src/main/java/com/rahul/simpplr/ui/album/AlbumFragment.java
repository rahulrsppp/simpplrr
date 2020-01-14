package com.rahul.simpplr.ui.album;

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
import com.rahul.simpplr.utility.Listeners;
import com.rahul.simpplr.utility.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;



public class AlbumFragment extends BaseFragment<FragmentAlbumBinding, AlbumViewModel> implements Listeners.ItemClickListener {

    public static final String TAG = AlbumFragment.class.getSimpleName();

    @Inject
    ViewModelFactory factory;

    private AlbumViewModel viewModel;
    private FragmentAlbumBinding mBinding;
    private List<AlbumResponseModel.AlbumData> albumDataList ;
    private AlbumAdapter adapter;
    private Context context;
    private Listeners.MainListener listener;


    public static AlbumFragment newInstance() {
        Bundle args = new Bundle();
        AlbumFragment fragment = new AlbumFragment();
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

        subscribeObserver();
        viewModel.fetchAlbumInfo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = getViewDataBinding();
        setAdapter();
        return mBinding.getRoot();
    }

    private void setAdapter() {
        albumDataList =new ArrayList<>();
        adapter = new AlbumAdapter(albumDataList,this, getContext());
        mBinding.rvAlbum.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvAlbum.setAdapter(adapter);

    }

    private void subscribeObserver() {
        viewModel.getToastObservable().observe(this, this::showToast);
        viewModel.getAlbumObservable().observe(this, albumData -> {

            if(albumData !=null){
                List<AlbumResponseModel.AlbumData> albumList =  albumData.getItems();
                albumDataList.clear();
                albumDataList.addAll(albumList);
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
        AlbumResponseModel.AlbumData albumData = (AlbumResponseModel.AlbumData) object;

        if(albumData!=null){
            String playlistId = albumData.getId();
            if(listener!=null){
                listener.loadFragment(playlistId);
            }
        }

    }
}
