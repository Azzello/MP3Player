package com.example.toni.mp3player;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Toni on 5.3.2015..
 */
public class PlaylistMakerAdapter extends ArrayAdapter{
    Context context;
    int layoutResourceID;
    ArrayList<Song> SongData = null;
    public PlaylistMakerAdapter(Context context, int layoutResourceID,ArrayList<Song> SongData) {
        super(context, layoutResourceID, SongData);
        this.context = context;
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
            holder.checkBoxSong = (CheckBox)row.findViewById(R.id.checkBoxSong);
            holder.checkBoxSong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = (Integer)buttonView.getTag();
                    SongData.get(pos).checked = buttonView.isChecked();
                }
            });
            row.setTag(holder);
        }
        else
        {
            holder = (SongHolder)row.getTag();
        }
        Song song = SongData.get(position);
        String songName = song.songName;

        if(songName.length()>31)
        {
            songName = songName.substring(0,28);
            songName+="...";
        }
        holder.checkBoxSong.setText(songName);
        holder.checkBoxSong.setTag(position);
        holder.checkBoxSong.setChecked(SongData.get(position).checked);
        return row;
    }
    static class SongHolder
    {
        CheckBox checkBoxSong;
    }
}
