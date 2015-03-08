package com.example.toni.mp3player;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class PlaylistMaker extends ActionBarActivity {

    ListView listViewPlaylistMaker;
    ArrayList<Song> songList;
    Button buttonMakePlaylist;
    EditText editTextPlaylistName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_maker);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        songList = MediaPlayerHelper.allSongs;
        listViewPlaylistMaker = (ListView)findViewById(R.id.listViewPlaylistMaker);
        buttonMakePlaylist = (Button)findViewById(R.id.buttonMakePlaylist);
        editTextPlaylistName = (EditText)findViewById(R.id.editTextPlaylistName);
        PlaylistMakerAdapter playlistMakerAdapter = new PlaylistMakerAdapter(this,R.layout.listview_row_playlistmaker,songList);
        listViewPlaylistMaker.setAdapter(playlistMakerAdapter);
        listViewPlaylistMaker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkbox = (CheckBox)view.getTag(R.id.checkBoxSong);
                checkbox.setChecked(true);
            }
        });
        buttonMakePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Checked Songs: " + GetChecked(songList).size(),Toast.LENGTH_SHORT).show();
                writeToFilePlaylist();
                setResult(1);
                finish();
            }
        });
    }
    ArrayList<Song> GetChecked(ArrayList<Song> _songList)
    {
        ArrayList<Song> checkedSongs = new ArrayList<Song>();
        for(int i = 0;i<_songList.size();i++)
        {
            if(_songList.get(i).checked)
                checkedSongs.add(_songList.get(i));
        }
        return checkedSongs;
    }
    private void writeToFilePlaylist() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("playlists.txt",Context.MODE_APPEND));
            ArrayList<Song> checkedSongs = GetChecked(songList);
            String playlistString = "<-StartPlaylist:" + editTextPlaylistName.getText() + "->\n";
            for(int i = 0;i<checkedSongs.size();i++)
            {
                playlistString+=checkedSongs.get(i).artistName + "<||>" + checkedSongs.get(i).songName + "<||>" + checkedSongs.get(i).albumName + "<||>\n";
            }
            playlistString += "<-EndPlaylist:" + editTextPlaylistName.getText() + "->\n";
            outputStreamWriter.write(playlistString);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Playlist creation failed!",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playlist_maker, menu);
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
