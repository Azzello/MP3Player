package com.example.toni.mp3player;

import java.util.ArrayList;

/**
 * Created by Toni on 25.2.2015..
 */
public class Artist{
    public String ArtistName;
    public ArrayList<Song> Songs;
    public Artist(String _ArtistName)
    {
        ArtistName = _ArtistName;
        Songs = new ArrayList<Song>();
    }
    public int SongCount()
    {
        return Songs.size();
    }
    public void addSong(Song song)
    {
        Songs.add(song);
    }
}
