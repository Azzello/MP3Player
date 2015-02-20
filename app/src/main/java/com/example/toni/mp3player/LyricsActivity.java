package com.example.toni.mp3player;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LyricsActivity extends ActionBarActivity {
    TextView textViewArtistName;
    TextView textViewSongName;
    TextView textViewSongLyrics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent lyricsIntent = getIntent();
        Bundle lyricsIntentExtras = lyricsIntent.getExtras();
        String ArtistName = lyricsIntentExtras.getString("Artist Name");
        String SongName = lyricsIntentExtras.getString("Song Name");

        textViewArtistName = (TextView)findViewById(R.id.textViewArtistName);
        textViewSongName = (TextView)findViewById(R.id.textViewSongName);
        textViewSongLyrics = (TextView)findViewById(R.id.textViewSongLyrics);
        textViewSongLyrics.setMovementMethod(new ScrollingMovementMethod());

        textViewArtistName.setText(ArtistName);
        textViewSongName.setText(SongName);

        //edit strings a bit to remove special characters and spaces from names
        ArtistName = ArtistName.toLowerCase().replace(" ", "");
        ArtistName = ArtistName.toLowerCase().replace("'", "");
        ArtistName = ArtistName.toLowerCase().replace(":", "");
        SongName = SongName.toLowerCase().replace(" ","");
        SongName = SongName.toLowerCase().replace("'","");
        SongName = SongName.toLowerCase().replace(":","");
        String Url = "http://www.azlyrics.com/lyrics/" + ArtistName + "/" + SongName + ".html";
        new GetLyrics().execute(Url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lyrics, menu);
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


    private class GetLyrics extends AsyncTask<String,String,String>
    {
        ProgressDialog progressDialog = new ProgressDialog(LyricsActivity.this);
        String html;
        @Override
        protected String doInBackground(String... params) {
            try {
                html = "No Lyrics Found! Please try another song or check your internet connection!";
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 3000); // 3s max for connection
                HttpConnectionParams.setSoTimeout(httpParameters, 4000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                HttpGet request = new HttpGet(params[0]);
                Log.d("AppTag",params[0]);
                HttpResponse response = client.execute(request);
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String currentLine = null;
                while ((currentLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(currentLine);
                }
                inputStream.close();
                html = stringBuilder.toString();
                int startOfLyrics =0;
                int endOfLyrics = 0;
                startOfLyrics = html.indexOf("<!-- start of lyrics -->") + 24;
                endOfLyrics = html.indexOf("<!-- end of lyrics -->");
                if(startOfLyrics > endOfLyrics)
                {
                    html = "No Lyrics Found! Please try another song or check your internet connection!";
                    return html;
                }
                html = html.substring(startOfLyrics,endOfLyrics);
                //get rid of HTML tags
                html = html.replace("<br />", "\n");
                html = html.replace("<i>","");
                html = html.replace("</i>","");
                return html;
            }
            catch (Exception e)
            {
                Log.d("AppTag",e.toString());
                return null;
            }

        }
        @Override
        protected  void onPreExecute()
        {
            progressDialog.setTitle("Looking for lyrics");
            progressDialog.show();
        }
        @Override
        protected  void onPostExecute(String result)
        {
            textViewSongLyrics.setText(html);
            progressDialog.dismiss();
        }
    }
}
