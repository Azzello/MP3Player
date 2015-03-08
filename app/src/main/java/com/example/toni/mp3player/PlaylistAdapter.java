package com.example.toni.mp3player;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Toni on 8.3.2015..
 */
public class PlaylistAdapter extends ArrayAdapter{
    Context context;
    int layoutResourceID;
    ArrayList<Playlist> PlaylistData = null;
    public PlaylistAdapter(Context context, int layoutResourceID, ArrayList<Playlist> PlaylistData) {
        super(context, layoutResourceID, PlaylistData);
        this.context  =context;
        this.layoutResourceID = layoutResourceID;
        this.PlaylistData = PlaylistData;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        SongHolder holder;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID,parent,false);
            holder = new SongHolder();
            holder.songInfo = (TextView)row.findViewById(R.id.textViewSong);
            holder.artistInfo = (TextView)row.findViewById(R.id.textViewArtist);
            row.setTag(holder);
        }
        else
        {
            holder = (SongHolder)row.getTag();
        }
        Playlist playlist = PlaylistData.get(position);
        String playlistName = playlist.playlistName;
        int playlistSongs = playlist.GetSongCount();

        //Check if song or artist name is too long and make it shorter
        if(playlistName.length()>36)
        {
            playlistName = playlistName.substring(0,36);
            playlistName +="...";
        }

        holder.songInfo.setText(playlistName);
        holder.artistInfo.setText(playlistSongs+" song(s)");
        return row;
    }
    static class SongHolder
    {
        TextView songInfo;
        TextView artistInfo;
    }
}
