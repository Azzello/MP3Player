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
 * Created by Toni on 25.2.2015..
 */
public class ArtistAdapter extends ArrayAdapter{
    Context context;
    int layoutResourceID;
    ArrayList<Artist> ArtistData = null;
    public ArtistAdapter(Context context, int layoutResourceID, ArrayList<Artist> ArtistData) {
        super(context, layoutResourceID, ArtistData);
        this.context  =context;
        this.layoutResourceID = layoutResourceID;
        this.ArtistData = ArtistData;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ArtistHolder holder;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID,parent,false);
            holder = new ArtistHolder();
            holder.songCount = (TextView)row.findViewById(R.id.textViewArtist);
            holder.artistInfo = (TextView)row.findViewById(R.id.textViewSong);
            row.setTag(holder);
        }
        else
        {
            holder = (ArtistHolder)row.getTag();
        }
        Artist artist = ArtistData.get(position);
        String artistName = artist.ArtistName;
        if(artistName.length()>34)
        {
            artistName = artistName.substring(0,31);
            artistName+="...";
        }

        holder.songCount.setText(artist.SongCount()+" Song(s)");
        holder.artistInfo.setText(artistName);
        return row;
    }
    static class ArtistHolder
    {
        TextView songCount;
        TextView artistInfo;
    }
}
