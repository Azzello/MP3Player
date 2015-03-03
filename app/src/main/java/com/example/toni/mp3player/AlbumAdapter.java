package com.example.toni.mp3player;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Toni on 25.2.2015..
 * Album adapter for albums tab listview
 */
public class AlbumAdapter extends ArrayAdapter{
    Context context;
    int layoutResourceID;
    ArrayList<Album> AlbumData = null;
    public AlbumAdapter(Context context, int layoutResourceID, ArrayList<Album> AlbumData) {
        super(context, layoutResourceID, AlbumData);
        this.context  =context;
        this.layoutResourceID = layoutResourceID;
        this.AlbumData = AlbumData;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        AlbumHolder holder;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID,parent,false);
            holder = new AlbumHolder();
            holder.albumInfo = (TextView)row.findViewById(R.id.textViewAlbumName);
            holder.artistInfo = (TextView)row.findViewById(R.id.textViewArtistName);
            holder.albumCover = (ImageView)row.findViewById(R.id.imageViewAlbumCover);
            row.setTag(holder);
        }
        else
        {
            holder = (AlbumHolder)row.getTag();
        }
        Album album = AlbumData.get(position);
        String albumName = album.albumName;
        String artistName = album.artistName;

        if(albumName.length()>31)
        {
            albumName = albumName.substring(0,28);
            albumName+="...";
        }
        if(artistName.length()>36)
        {
            artistName = artistName.substring(0,33);
            artistName+="...";
        }
        holder.albumInfo.setText(albumName);
        holder.artistInfo.setText(artistName);
        //Check if coverImage exists for this album. Without this random images will appear on cover.
        if(album.coverImage != null)
        {
            holder.albumCover.setImageBitmap(album.coverImage);
        }
        else
        {
            holder.albumCover.setImageResource(R.drawable.albumcover);
        }
        return row;
    }
    static class AlbumHolder
    {
        TextView albumInfo;
        TextView artistInfo;
        ImageView albumCover;
    }
}
