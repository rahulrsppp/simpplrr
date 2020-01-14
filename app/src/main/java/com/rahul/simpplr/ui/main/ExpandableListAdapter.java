package com.rahul.simpplr.ui.main;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rahul.simpplr.R;
import com.rahul.simpplr.ui.album.CompleteInfoModel;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<CompleteInfoModel> listDataHeader;
    private HashMap<CompleteInfoModel, List<CompleteInfoModel.CompleteInfoData>> listDataChild;

    public ExpandableListAdapter(Context context, List<CompleteInfoModel> listDataHeader,
                                 HashMap<CompleteInfoModel, List<CompleteInfoModel.CompleteInfoData>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public CompleteInfoModel.CompleteInfoData getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(this.listDataChild.get(this.listDataHeader.get(groupPosition))).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String trackName = getChild(groupPosition, childPosition).getTrackName();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(inflater).inflate(R.layout.list_group_child, parent, false);
        }
        TextView txtListChild = convertView.findViewById(R.id.tvTrackName);
        txtListChild.setText(trackName);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return Objects.requireNonNull(this.listDataChild.get(this.listDataHeader.get(groupPosition))).size();
    }

    @Override
    public CompleteInfoModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String albumName = getGroup(groupPosition).getAlbumName();
        String albumImage = getGroup(groupPosition).getAlbumImage();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(inflater).inflate(R.layout.list_group_parent, parent,false);
        }

        TextView  tvAlbumName= convertView.findViewById(R.id.tvAlbumName);
        ImageView ivAlbum= convertView.findViewById(R.id.ivAlbum);

        tvAlbumName.setText(albumName);

        if(albumImage!=null && albumImage.length()>0)
        Glide.with(context).load(albumImage).placeholder(R.drawable.placeholder).into(ivAlbum);


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}