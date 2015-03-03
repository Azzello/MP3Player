package com.example.toni.mp3player;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class Playing extends ActionBarActivity {

    final int MEDIAPLAYERPLAYING = 1;
    final int MEDIAPLAYERPAUSED = 0;

    boolean activityClosing;
    boolean seekBarPositionTouched;
    Song currentSong;

    SeekBar seekBarSongPosition;
    TextView textViewSong;
    TextView textViewArtist;
    TextView textViewAlbum;
    TextView textViewLyrics;
    ImageButton imgButtonNextSong;
    ImageButton imgButtonPreviousSong;
    ImageButton imgButtonPlayPauseSong;
    ImageView imgViewAlbumArt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//hides action bar
        //Set our TextViews and other controls
        textViewSong = (TextView)findViewById(R.id.textViewArtist);
        textViewArtist = (TextView)findViewById(R.id.textViewSong);
        textViewAlbum = (TextView)findViewById(R.id.textViewAlbum);
        textViewLyrics = (TextView)findViewById(R.id.textViewLyrics);
        seekBarSongPosition = (SeekBar)findViewById(R.id.seekBarSongPosition);
        imgButtonNextSong = (ImageButton)findViewById(R.id.imageButtonNextSong);
        imgButtonPreviousSong = (ImageButton)findViewById(R.id.imageButtonPreviousSong);
        imgButtonPlayPauseSong = (ImageButton)findViewById(R.id.imageButtonPlayPause);
        imgViewAlbumArt = (ImageView)findViewById(R.id.imageViewAlbumCover);
        //Check song state and set imgButtonPlayPauseSong button image.
        if(MediaPlayerHelper.GetMusicPlayerState() == MEDIAPLAYERPLAYING)
        {
            imgButtonPlayPauseSong.setImageResource(R.drawable.pauseicon);
        }
        else if(MediaPlayerHelper.GetMusicPlayerState() == MEDIAPLAYERPAUSED)
        {
            imgButtonPlayPauseSong.setImageResource(R.drawable.playicon);
        }
        activityClosing = false;
        seekBarPositionTouched = false;
        //Set onSeekBarChangeListener which will be used to move through song
        seekBarSongPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarPositionTouched = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekBarPercentage = seekBar.getProgress();
                MediaPlayerHelper.SetSongPositionAtPercentage(seekBarPercentage);
                seekBarPositionTouched = false;
            }
        });



        //Create thread which will update our seekBar every second and get currently playing song.
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(activityClosing==false)//Keep running this thread until activity is closed.
                {
                    try
                    {
                        if(currentSong != MediaPlayerHelper.GetCurrentSong()) //Check with media player if song has changed. If it did run UpdateLabels() in UI Thread.
                        {
                            currentSong = MediaPlayerHelper.GetCurrentSong();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UpdateUI();
                                }
                            });
                        }
                        if(!seekBarPositionTouched)
                        {
                            int songPositionPercentage = MediaPlayerHelper.GetSeekPercentage();//Get song position percentage which we will use in our seekBar progress.
                            seekBarSongPosition.setProgress(songPositionPercentage);
                        }
                        Thread.sleep(1000);//Sleep for 1000ms(1 second) before repeating
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        //Lyrics click listener
        textViewLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lyricsIntent = new Intent(Playing.this,LyricsActivity.class);
                lyricsIntent.putExtra("Artist Name",currentSong.artistName);
                lyricsIntent.putExtra("Song Name",currentSong.songName);
                startActivity(lyricsIntent);
            }
        });
        //Buttons onClick events
        imgButtonPlayPauseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MediaPlayerHelper.GetMusicPlayerState() == MEDIAPLAYERPLAYING)
                {
                    MediaPlayerHelper.PauseSong();
                    imgButtonPlayPauseSong.setImageResource(R.drawable.playicon);
                }
                else if(MediaPlayerHelper.GetMusicPlayerState() == MEDIAPLAYERPAUSED)
                {
                    MediaPlayerHelper.UnpauseSong();
                    imgButtonPlayPauseSong.setImageResource(R.drawable.pauseicon);
                }
            }
        });
        imgButtonNextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayerHelper.PlayNextSong();
            }
        });
        imgButtonPreviousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayerHelper.PlayPreviousSong();
            }
        });
    }
    //Update our textviews containing song information.
    void UpdateUI()
    {
        String songName = currentSong.songName;
        String artistName = currentSong.artistName;
        String albumName = currentSong.albumName;

        if(songName.length() > 27)
        {
            songName = songName.substring(0,24);
            songName+="...";
        }
        if(artistName.length() > 27)
        {
            artistName = artistName.substring(0,24);
            artistName+="...";
        }
        if(albumName.length() > 27)
        {
            albumName = albumName.substring(0,24);
            albumName+="...";
        }
        textViewSong.setText(songName);
        textViewArtist.setText(artistName);
        textViewAlbum.setText(albumName);
        if(currentSong.albumArt != null)
        {
            imgViewAlbumArt.setImageBitmap(currentSong.albumArt);
        }
        else
        {
            imgViewAlbumArt.setImageResource(R.drawable.albumcover);
        }
    }
    @Override
    public void onBackPressed() {
        activityClosing = true;//Lets our thread know that we are closing activity, otherwise he would keep running and on reopening it app would crash.
        finish();//Closes activity
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
