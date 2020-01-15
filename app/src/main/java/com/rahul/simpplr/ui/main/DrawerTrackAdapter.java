package com.rahul.simpplr.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rahul.simpplr.R;
import com.rahul.simpplr.databinding.ListGroupChildBinding;
import com.rahul.simpplr.ui.album.AlbumTracksResponseModel;
import com.rahul.simpplr.ui.album.TracksResponseData;
import com.rahul.simpplr.utility.Listeners;

import java.util.List;

public class DrawerTrackAdapter extends RecyclerView.Adapter<DrawerTrackAdapter.DrawerTrackViewHolder>{

    private final Context context;
    private List<AlbumTracksResponseModel.AlbumTracksData> trackList;
    private Listeners.ItemClickListener listener;


    DrawerTrackAdapter(List<AlbumTracksResponseModel.AlbumTracksData> trackList, Context context) {
        this.listener=  ( Listeners.ItemClickListener) context;
        this.context= context;
        this.trackList= trackList;
    }

    public void setList(List<AlbumTracksResponseModel.AlbumTracksData> trackList){
        this.trackList = trackList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DrawerTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListGroupChildBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_group_child, parent, false);
        return new DrawerTrackViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull DrawerTrackViewHolder holder, int position) {

        AlbumTracksResponseModel.AlbumTracksData trackData = trackList.get(holder.getAdapterPosition());
        if (trackData != null) {
            TracksResponseData track = trackData.getTrack();
            if (track != null) {
                String trackName = track.getName();
                String artistNameToSet = null;

                List<AlbumTracksResponseModel.AlbumTracksData> artistData = track.getArtists();

                if (artistData != null && artistData.size() > 0) {
                    artistNameToSet = artistData.get(0).getArtistName();
                }

                holder.rowBinding.tvTrackArtist.setVisibility(artistNameToSet != null ? View.VISIBLE : View.GONE);
                holder.rowBinding.tvTrackArtist.setText(artistNameToSet != null ? artistNameToSet : "");
                holder.rowBinding.tvTrackName.setText(trackName != null && trackName.length() > 0 ? trackName : context.getString(R.string.na));
                // holder.rowBinding.tvTracks.setText(trackToSet);

                holder.rowBinding.getRoot().setOnClickListener(v -> {
                    if (listener != null)
                        listener.onItemClick(trackData, MainActivity.FROM_TRACK_ADAPTER);
                });
            }
        }
    }



    @Override
    public int getItemCount() {
        return trackList.size();
    }


    class DrawerTrackViewHolder extends RecyclerView.ViewHolder{

        ListGroupChildBinding rowBinding;

        private DrawerTrackViewHolder(ListGroupChildBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding=rowBinding;

        }

    }

}
