package com.example.toni.mp3player;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by Toni on 13.2.2015..
 */
public  class MediaPlayerHelper{
    static ArrayList<Song> songList;//current playlist
    static ArrayList<Song> allSongs;//all songs on device
    static ArrayList<Song> clickedList;//used to pass playlist to another activity
    static MediaPlayer mediaPlayer;
    static int songIndex;
    static boolean isMediaPrepared;



    public static void InitializePlayer()
    {
        mediaPlayer = new MediaPlayer();
        songList = new ArrayList<Song>();
        songIndex = 0;
        //Set OnCompletionListener which will be called when song is over so we can play next one.
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isMediaPrepared = false;
                PlayNextSong();
            }
        });
        //Set OnPreparedListener to set our bool to true. This is required for some crash fixes.
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isMediaPrepared = true;
            }
        });
    }
    //Set media player playlist
    public static void SetPlaylist(ArrayList<Song> _songList)
    {
        songList = _songList;
    }
    //Plays song at index on current playlist
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
    public static int GetMusicPlayerState()
    {
        if(mediaPlayer.isPlaying())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
    public static void PauseSong()
    {
        mediaPlayer.pause();
    }
    public static void UnpauseSong()
    {
        mediaPlayer.start();
    }
    //Plays next song
    public static void PlayNextSong()
    {
        songIndex++;
        if(songIndex>songList.size()-1)
        {
            songIndex = 0;
        }
        isMediaPrepared = false;
        PlaySongAtIndex(songIndex);
    }
    //Previous next song
    public static void PlayPreviousSong()
    {
        songIndex--;
        if(songIndex<0)
        {
            songIndex = songList.size()-1;
        }
        isMediaPrepared = false;
        PlaySongAtIndex(songIndex);
    }
    //Returns current playlist
    public static ArrayList<Song> GetCurrentPlaylist()
    {
        return songList;
    }
    //Returns currently playing song
    public static Song GetCurrentSong()
    {
        return songList.get(songIndex);
    }
    //Returns percentage of song played. This is used when updating seekBar in PlayingActivity
    public static int GetSeekPercentage()
    {
        if(isMediaPrepared)
        {
            int songDuration = mediaPlayer.getDuration();
            int songCurrentPosition = mediaPlayer.getCurrentPosition();

            float currentPositionPercentage = ((float) songCurrentPosition / (float) songDuration) * 100;//Get percentage of duration
            int finalCurrentPositionPercentage = Math.round(currentPositionPercentage);//Round percentage to int
            return finalCurrentPositionPercentage;
        }
        return 0;
    }
    //Sets song position at percentage we give
    public static void SetSongPositionAtPercentage(int percentage)
    {
        int songDuration = mediaPlayer.getDuration();
        float seekPercentage = (float)percentage/100f;
        int finalSongPosition = Math.round(seekPercentage*songDuration);
        mediaPlayer.seekTo(finalSongPosition);
    }
}
