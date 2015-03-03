package com.example.toni.mp3player;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class PlaylistSongs extends ActionBarActivity {

    ArrayList<Song> playlistSongs;
    ListView listViewPlaylistSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_songs);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        playlistSongs = MediaPlayerHelper.clickedList;
        listViewPlaylistSongs = (ListView) findViewById(R.id.listViewPlaylistSongs);
        SongAdapter songAdapter = new SongAdapter(this,R.layout.listview_row,playlistSongs);
        listViewPlaylistSongs.setAdapter(songAdapter);
        //set listview item click listener
        listViewPlaylistSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaPlayerHelper.SetPlaylist(MediaPlayerHelper.clickedList);
                MediaPlayerHelper.PlaySongAtIndex(position);
                Intent playingIntent = new Intent(getApplicationContext(),Playing.class);
                startActivity(playingIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playlist_songs, menu);
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
