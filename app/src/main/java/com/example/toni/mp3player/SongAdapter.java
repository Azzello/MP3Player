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
 * Created by Toni on 12.2.2015..
 * Song adapter used in our Listview in MusicFragment.java
 */
public class SongAdapter extends ArrayAdapter<Song>{
    Context context;
    int layoutResourceID;
    ArrayList<Song> SongData = null;
    public SongAdapter(Context context, int layoutResourceID, ArrayList<Song> SongData) {
        super(context, layoutResourceID, SongData);
        this.context  =context;
        this.layoutResourceID = layoutResourceID;
        this.SongData = SongData;
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
        Song song = SongData.get(position);
        String songName = song.songName;
        String artistName = song.artistName;

        //Check if song or artist name is too long and make it shorter
        if(songName.length()>36)
        {
            songName = songName.substring(0,36);
            songName +="...";
        }
        if(artistName.length()>36)
        {
            artistName = artistName.substring(0,36);
            artistName +="...";
        }
        holder.songInfo.setText(songName);
        holder.artistInfo.setText(artistName);
        return row;
    }
    static class SongHolder
    {
        TextView songInfo;
        TextView artistInfo;
    }
}
