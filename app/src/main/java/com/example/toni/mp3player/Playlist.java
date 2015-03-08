package com.example.toni.mp3player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toni on 5.3.2015..
 */
public class Playlist {
    public ArrayList<Song> playlistSongs;
    String playlistName;
    public Playlist(String _playlistName)
    {
        playlistName = _playlistName;
        playlistSongs = new ArrayList<Song>();
    }
    public void AddSong(Song s)
    {
        playlistSongs.add(s);
    }
    public int GetSongCount()
    {
        return playlistSongs.size();
    }
}
