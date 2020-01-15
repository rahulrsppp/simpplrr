package com.rahul.simpplr.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.rahul.simpplr.R;
import com.rahul.simpplr.databinding.AdapterAlbumBinding;
import com.rahul.simpplr.databinding.ListGroupParentBinding;
import com.rahul.simpplr.ui.album.AlbumResponseModel;
import com.rahul.simpplr.ui.album.AlbumTracksResponseModel;
import com.rahul.simpplr.utility.Listeners;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DrawerViewAdapter extends RecyclerView.Adapter<DrawerViewAdapter.DrawerAlbumViewHolder>{

    private final Context context;
    private List<AlbumResponseModel.AlbumData> albumList;
    private List<AlbumTracksResponseModel.AlbumTracksData> trackList;
    private Listeners.ItemClickListener listener;


    public DrawerViewAdapter(List<AlbumResponseModel.AlbumData> albumList, List<AlbumTracksResponseModel.AlbumTracksData> trackList, Listeners.ItemClickListener listener, Context context) {
        this.albumList = albumList;
        this.listener= listener;
        this.context= context;
        this.trackList= trackList;
    }

    @NonNull
    @Override
    public DrawerViewAdapter.DrawerAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListGroupParentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_group_parent, parent, false);
            return new DrawerAlbumViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull DrawerViewAdapter.DrawerAlbumViewHolder holder, int position) {

        AlbumResponseModel.AlbumData albumData = albumList.get(holder.getAdapterPosition());
        if (albumData != null) {

            String albumName = albumData.getName();
            int isSelected = albumData.getIsSelected();

            String nameToSet = (albumName != null && albumName.length() > 0 ? albumName : context.getString(R.string.na));
            holder.rowBinding.tvAlbumName.setText(nameToSet);

            if (albumData.getImages() != null) {
                AlbumResponseModel.AlbumData images = albumData.getImages().get(0);

                if (images.getImageUrl() != null)
                    Glide.with(context).load(images.getImageUrl()).placeholder(R.drawable.placeholder).into(holder.rowBinding.ivAlbum);
            }

            if(isSelected == 1 && trackList!=null){
               RecyclerView.Adapter adapter =  holder.rowBinding.recyclerViewTrack.getAdapter();
               if(adapter!=null){
                   DrawerTrackAdapter drawerTrackAdapter = (DrawerTrackAdapter) adapter;
                   drawerTrackAdapter.setList(trackList);
               }else{
                   DrawerTrackAdapter  drawerTrackAdapter = new DrawerTrackAdapter(trackList, context);
                   holder.rowBinding.recyclerViewTrack.setAdapter(drawerTrackAdapter);
                   holder.rowBinding.recyclerViewTrack.setLayoutManager(new LinearLayoutManager(context));
                   holder.rowBinding.recyclerViewTrack.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
               }

            }

            holder.rowBinding.recyclerViewTrack.setVisibility(isSelected == 1 && trackList!=null? View.VISIBLE:View.GONE);

            holder.rowBinding.getRoot().setOnClickListener(v -> {
                if(listener!=null) {
                    albumData.setIsSelected(albumData.getIsSelected()== 1?0:1);
                    notifyDataSetChanged();
                    listener.onItemClick(albumData, MainActivity.FROM_ALBUM_ADAPTER);
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return albumList.size();
    }


    class DrawerAlbumViewHolder extends RecyclerView.ViewHolder{

        ListGroupParentBinding rowBinding;

        private DrawerAlbumViewHolder(ListGroupParentBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding=rowBinding;

        }

    }

}
