package com.example.toni.mp3player;

import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by Toni on 13.2.2015..
 */
public  class MediaPlayerHelper{
    static ArrayList<Song> songList;
    static MediaPlayer mediaPlayer;
    static int songIndex;



    public static void InitializePlayer()
    {
        mediaPlayer = new MediaPlayer();
        songList = new ArrayList<Song>();
        songIndex = 0;
        //Set OnCompletionListener which will be called when song is over so we can play next one.
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                songIndex++;
                PlaySongAtIndex(songIndex);
            }
        });



    }
    public static void SetPlaylist(ArrayList<Song> _songList)
    {
        songList = _songList;
    }
    public static void PlaySongAtIndex(int _songIndex)
    {
        try {
            songIndex = _songIndex;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songList.get(songIndex).songPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static ArrayList<Song> GetCurrentPlaylist()
    {
        return songList;
    }
    public static Song GetCurrentSong()
    {
        return songList.get(songIndex);
    }
}
