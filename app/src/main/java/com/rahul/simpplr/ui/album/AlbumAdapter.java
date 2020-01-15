package com.rahul.simpplr.ui.album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rahul.simpplr.R;
import com.rahul.simpplr.databinding.AdapterAlbumBinding;
import com.rahul.simpplr.ui.album.AlbumResponseModel;
import com.rahul.simpplr.utility.Listeners;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter{

    private final Context context;
    private List<AlbumResponseModel.AlbumData> albumList;

    private Listeners.ItemClickListener listener;


    AlbumAdapter(List<AlbumResponseModel.AlbumData> albumList, Listeners.ItemClickListener listener, Context context) {
        this.albumList = albumList;
        this.listener= listener;
        this.context= context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterAlbumBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_album, parent, false);
        return new AlbumViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        AlbumResponseModel.AlbumData albumData = albumList.get(holder.getAdapterPosition());
        if (albumData != null) {
                AlbumViewHolder albumViewHolder = ((AlbumViewHolder) holder);
                String albumName = albumData.getName();
                String description = albumData.getDescription();
                String trackToSet = "Tracks: ";
                String nameToSet = (albumName != null && albumName.length() > 0 ? albumName : context.getString(R.string.na));
                String descriptionToSet = "Description: " + (description != null && description.length() > 0 ? description : context.getString(R.string.na));

                if (albumData.getImages() != null) {
                    AlbumResponseModel.AlbumData images = albumData.getImages().get(0);

                    if (images.getImageUrl() != null)
                        Glide.with(context).load(images.getImageUrl()).placeholder(R.mipmap.placeholder).into(albumViewHolder.rowBinding.ivAlbum);
                }

                if (albumData.getTracks() != null) {
                    int totalTracks = albumData.getTracks().getTotal();
                    trackToSet += totalTracks;
                }

                albumViewHolder.rowBinding.tvName.setText(nameToSet);
                albumViewHolder.rowBinding.tvText1.setText(descriptionToSet);
                albumViewHolder.rowBinding.tvText2.setText(trackToSet);

                albumViewHolder.rowBinding.getRoot().setOnClickListener(v -> listener.onItemClick(albumData, null));
        }
    }



    @Override
    public int getItemCount() {
        return albumList.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder{

        AdapterAlbumBinding rowBinding;

        private AlbumViewHolder(AdapterAlbumBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding=rowBinding;

        }

    }


}
