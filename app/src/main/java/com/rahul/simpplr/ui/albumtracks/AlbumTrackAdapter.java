package com.rahul.simpplr.ui.albumtracks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rahul.simpplr.R;
import com.rahul.simpplr.databinding.AdapterAlbumBinding;
import com.rahul.simpplr.ui.album.AlbumTracksResponseModel;
import com.rahul.simpplr.ui.album.TracksResponseData;
import com.rahul.simpplr.utility.Util;

import java.util.List;


public class AlbumTrackAdapter extends RecyclerView.Adapter<AlbumTrackAdapter.ViewHolder>{

    private final Context context;
    private List<AlbumTracksResponseModel.AlbumTracksData> albumTrackList;

    AlbumTrackAdapter(List<AlbumTracksResponseModel.AlbumTracksData> albumTrackList, Context context) {
        this.albumTrackList = albumTrackList;
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
        AlbumTracksResponseModel.AlbumTracksData albumTracksData = albumTrackList.get(holder.getAdapterPosition());

        if (albumTracksData != null) {
            TracksResponseData trackData = albumTracksData.getTrack();
            if (trackData != null) {
                String trackName = trackData.getName();
                String duration = trackData.getDuration();
                String artistNameToSet = null;
                String dateToSet = null;

                if(duration !=null && duration.length()>0){
                    dateToSet = "Duration: "+ Util.getCompleteTrackDuration(duration);
                }

                try {
                    List<AlbumTracksResponseModel.AlbumTracksData> artistData = trackData.getArtists();

                    if (artistData != null && artistData.size() > 0) {
                        artistNameToSet = artistData.get(0).getArtistName();
                    }

                    AlbumTracksResponseModel.AlbumTracksData albumData = trackData.getAlbum();
                    if (albumData != null) {
                        List<AlbumTracksResponseModel.AlbumTracksData> imagesData = albumData.getImages();

                        String imageUrl = imagesData.get(0).getImageUrl();
                        if (imageUrl != null) {
                            Glide.with(context).load(imageUrl).placeholder(R.mipmap.placeholder).into(holder.rowBinding.ivAlbum);
                        }
                    }

                } catch (Exception ignored) { }

                holder.rowBinding.tvText2.setVisibility(dateToSet != null ? View.VISIBLE : View.GONE);
                holder.rowBinding.tvText1.setVisibility(artistNameToSet != null ? View.VISIBLE : View.GONE);
                holder.rowBinding.tvText1.setText(artistNameToSet);
                holder.rowBinding.tvName.setText(trackName != null && trackName.length() > 0 ? trackName : context.getString(R.string.na));
                holder.rowBinding.tvText2.setText(dateToSet!=null?dateToSet:"");
            }
        }
    }

    @Override
    public int getItemCount () {
        return albumTrackList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AdapterAlbumBinding rowBinding;

        private ViewHolder(AdapterAlbumBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding = rowBinding;

        }

    }

}
