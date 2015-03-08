package com.example.toni.mp3player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaylistsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaylistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistsFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button buttonNewPlaylist;
    ListView listViewPlaylists;
    ArrayList<Playlist> listPlaylist;
    PlaylistAdapter playlistAdapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistsFragment newInstance(String param1, String param2) {
        PlaylistsFragment fragment = new PlaylistsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaylistsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View FragmentView = inflater.inflate(R.layout.fragment_playlists, container, false);
        buttonNewPlaylist = (Button)FragmentView.findViewById(R.id.buttonNewPlaylist);
        listViewPlaylists = (ListView)FragmentView.findViewById(R.id.listViewPlaylists);

        buttonNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),PlaylistMaker.class);
                startActivityForResult(intent,1);
            }
        });
        listPlaylist=  readPlaylistsFromFile();
        playlistAdapter = new PlaylistAdapter(getActivity(),R.layout.listview_row,listPlaylist);
        listViewPlaylists.setAdapter(playlistAdapter);
        listViewPlaylists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaPlayerHelper.clickedList = listPlaylist.get(position).playlistSongs;
                Intent i = new Intent(getActivity(),PlaylistSongs.class);
                startActivity(i);
            }
        });
        return FragmentView;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == 1)
            {
                Toast.makeText(getActivity().getApplicationContext(),"New playlist has been added.",Toast.LENGTH_SHORT).show();
                listPlaylist = readPlaylistsFromFile();
                playlistAdapter.clear();
                playlistAdapter.addAll(listPlaylist);
                playlistAdapter.notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(),"Playlist Maker Canceled.",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private ArrayList<Playlist> readPlaylistsFromFile() {
        ArrayList<Playlist> playlists = new ArrayList<Playlist>();
        Playlist tempPlaylist=new Playlist("");
        String songName="";
        String albumName="";
        String artistName="";
        String playlistName="";
        String ret = "";
        try {

            InputStream inputStream = getActivity().openFileInput("playlists.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                    if(receiveString.contains("<-StartPlaylist:"))//New playlist
                    {
                        int startIndex = receiveString.indexOf("<-StartPlaylist:") + 16;
                        int endIndex = receiveString.indexOf("->");
                        Log.d("AppTag", startIndex + " " + endIndex + " " + (endIndex-startIndex));
                        playlistName = receiveString.substring(startIndex,endIndex);
                        tempPlaylist = new Playlist(playlistName);

                    }
                    else if (receiveString.contains("<-EndPlaylist:"))//End of playlist
                    {
                        playlists.add(tempPlaylist);
                    }
                    else//Songs
                    {
                        int startIndex = 0;
                        int endIndex = receiveString.indexOf("<||>");
                        artistName = receiveString.substring(startIndex, endIndex);
                        startIndex = endIndex+4;
                        endIndex = receiveString.indexOf("<||>",startIndex);
                        songName = receiveString.substring(startIndex,endIndex);
                        startIndex = endIndex+4;
                        endIndex = receiveString.indexOf("<||>",startIndex);
                        albumName = receiveString.substring(startIndex,endIndex);
                        Song tempSong = getSongFromAll(songName,artistName,albumName);
                        if(tempSong != null)
                        {
                            tempPlaylist.AddSong(tempSong);
                        }
                    }
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return playlists;
    }
    Song getSongFromAll(String songName, String artistName, String albumName)
    {
        for(int i =0;i<MediaPlayerHelper.allSongs.size();i++)
        {
            Song currentSong = MediaPlayerHelper.allSongs.get(i);
            if(currentSong.songName.equals(songName) && currentSong.artistName.equals(artistName) && currentSong.albumName.equals(albumName))
            {
                return currentSong;
            }
        }
        return null;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
