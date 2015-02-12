package com.example.toni.mp3player;

/**
 * Created by Toni on 12.2.2015..
 * Song class which will store our songs information
 */
public class Song {
    String songName;
    String artistName;
    String songPath;
    String songDuration;
    public Song(String _songName, String _artistName, String _songPath, String _songDuration)
    {
        super();
        songName = _songName;
        artistName = _artistName;
        songPath = _songPath;
        songDuration = _songDuration;
    }
}
