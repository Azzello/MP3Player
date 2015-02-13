package com.example.toni.mp3player;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;


public class Playing extends ActionBarActivity {

    boolean activityClosing;
    SeekBar seekBarSongPosition;
    Song currentSong;
    TextView textViewSong;
    TextView textViewArtist;
    TextView textViewAlbum;
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
        seekBarSongPosition = (SeekBar)findViewById(R.id.seekBarSongPosition);

        //Set onSeekBarChangeListener which will be used to move through song
        seekBarSongPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekBarPercentage = seekBar.getProgress();
                MediaPlayerHelper.SetSongPositionAtPercentage(seekBarPercentage);
            }
        });



        //Create thread which will update our seekbar every second and get currently playing song.
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
                                    UpdateLabels();
                                }
                            });
                        }
                        int songPositionPercentage = MediaPlayerHelper.GetSeekPercentage();//Get song position precentage which we will use in our seekBar progress.
                        seekBarSongPosition.setProgress(songPositionPercentage);
                        Thread.sleep(1000);//Sleep for 1000ms(1 second) before repeating
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    //Update our textviews containing song information.
    void UpdateLabels()
    {
        textViewSong.setText(currentSong.songName);
        textViewArtist.setText(currentSong.artistName);
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
