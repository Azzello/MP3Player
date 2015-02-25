package com.example.toni.mp3player;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Toni on 25.2.2015..
 */
public class Album {
    ArrayList<Song> songs;
    String albumName;
    String artistName;
    Bitmap coverImage;
    public Album(String _albumName,String _artistName)
    {
        albumName = _albumName;
        artistName = _artistName;

        songs = new ArrayList<Song>();
    }
    public void AddSong(Song song)
    {
        songs.add(song);
    }
    public void setCoverImage(Bitmap bmpImage)
    {
        coverImage = bmpImage;
    }
}
