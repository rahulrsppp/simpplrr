package com.rahul.simpplr.ui.album;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rahul.simpplr.R;
import com.rahul.simpplr.base.BaseFragment;
import com.rahul.simpplr.databinding.FragmentAlbumBinding;
import com.rahul.simpplr.ui.main.MainActivity;
import com.rahul.simpplr.utility.Listeners;
import com.rahul.simpplr.utility.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = getViewDataBinding();
        setAdapter();
        setListener();
        return mBinding.getRoot();
    }

    private void setListener() {
        mBinding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.fetchAlbumInfo();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.fetchAlbumInfo();
    }

    private void setAdapter() {
        albumDataList =new ArrayList<>();
        adapter = new AlbumAdapter(albumDataList,this, getContext());
        mBinding.rvAlbum.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvAlbum.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
         Objects.requireNonNull(((AppCompatActivity) context).getSupportActionBar()).setTitle(context.getString(R.string.album));
             }

    private void subscribeObserver() {
        viewModel.getToastObservable().observe(this, this::showToast);
        viewModel.getAlbumObservable().observe(this, albumData -> {

            mBinding.swipeRefresh.setRefreshing(false);
            if(albumData !=null){

                ((MainActivity) context).updateDrawer(albumData);
                List<AlbumResponseModel.AlbumData> albumList =  albumData.getItems();
                albumDataList.clear();
                albumDataList.addAll(albumList);
                adapter.notifyDataSetChanged();

                mBinding.swipeRefresh.setRefreshing(false);
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
