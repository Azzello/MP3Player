package com.example.toni.mp3player;

/**
 * Created by Toni on 12.2.2015..
 * Song class which will store our songs information
 */
public class Song {
    public String songName;
    public String artistName;
    public Song(String _songName, String _artistName)
    {
        super();
        songName = _songName;
        artistName = _artistName;
    }
}
