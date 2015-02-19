package com.example.toni.mp3player;

import android.graphics.Bitmap;

/**
 * Created by Toni on 12.2.2015..
 * Song class which will store our songs information
 */
public class Song {
    String songName;
    String artistName;
    String albumName;
    String songPath;
    String songDuration;
    Bitmap albumArt;
    public Song(String _songName, String _artistName,String _albumName,String _songPath, String _songDuration, Bitmap _albumArt)
    {
        super();
        songName = _songName;
        artistName = _artistName;
        albumName = _albumName;
        songPath = _songPath;
        songDuration = _songDuration;
        albumArt = _albumArt;
    }
}
