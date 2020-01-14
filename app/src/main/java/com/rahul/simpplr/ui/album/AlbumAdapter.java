package com.rahul.simpplr.ui.album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.rahul.simpplr.R;
import com.rahul.simpplr.databinding.AdapterAlbumBinding;
import com.rahul.simpplr.utility.Listeners;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    private final Context context;
    private List<AlbumResponseModel.AlbumData> albumList;

    private Listeners.ItemClickListener listener;


    public AlbumAdapter(List<AlbumResponseModel.AlbumData> albumList,Listeners.ItemClickListener listener, Context context) {
        this.albumList = albumList;
        this.listener= listener;
        this.context= context;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         AdapterAlbumBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_album, parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setView(holder);
    }

    private void setView(final ViewHolder holder) {
        AlbumResponseModel.AlbumData albumData = albumList.get(holder.getAdapterPosition());

        if (albumData != null) {
             String albumName = albumData.getName();
             String description = albumData.getDescription();
             String trackToSet = "Total Tracks: ";
             String nameToSet= "Album: " +  (albumName!=null && albumName.length()>0? albumName: context.getString(R.string.na));
             String descriptionToSet= "Description: " +(description!=null && description.length()>0?description: context.getString(R.string.na));


                if( albumData.getImages()!=null) {
                    AlbumResponseModel.AlbumData images = albumData.getImages().get(0);

                    if(images.getImageUrl()!=null)
                    Glide.with(context).load(images.getImageUrl()).placeholder(R.drawable.placeholder).into(holder.rowBinding.ivAlbum);

                }


                if( albumData.getTracks()!=null) {
                   int totalTracks =  albumData.getTracks().getTotal();
                   trackToSet += totalTracks;

                }

                holder.rowBinding.tvName.setText(nameToSet);
                holder.rowBinding.tvText1.setText(descriptionToSet);
                holder.rowBinding.tvText2.setText(trackToSet);

        }

        holder.rowBinding.getRoot().setOnClickListener(v -> {
          listener.onItemClick(albumData,null);
        });
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        AdapterAlbumBinding rowBinding;

        private ViewHolder(AdapterAlbumBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding=rowBinding;

        }

    }

}
