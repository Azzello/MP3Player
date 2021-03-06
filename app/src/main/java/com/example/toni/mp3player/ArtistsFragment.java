package com.example.toni.mp3player;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArtistsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArtistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistsFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView listViewArtist;
    ArrayList<Artist> arrayListArtists;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistsFragment newInstance(String param1, String param2) {
        ArtistsFragment fragment = new ArtistsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ArtistsFragment() {
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
        View FragmentView =  inflater.inflate(R.layout.fragment_artists, container, false);
        //FragmentView.setBackgroundColor(Color.rgb(0,0,0));
        listViewArtist = (ListView)FragmentView.findViewById(R.id.listViewArtist);
        arrayListArtists = getArtistList();
        ArtistAdapter artistAdapter = new ArtistAdapter(getActivity(),R.layout.listview_row,arrayListArtists);
        listViewArtist.setAdapter(artistAdapter);
        //set on item click listener for listview
        listViewArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaPlayerHelper.clickedList = arrayListArtists.get(position).Songs;
                Intent i = new Intent(getActivity(),PlaylistSongs.class);
                startActivity(i);
            }
        });
        return FragmentView;
    }


    ArrayList<Artist> getArtistList()
    {
        ArrayList<Artist> arrayListArtist = new ArrayList<Artist>();
        for(int i = 0;i<MediaPlayerHelper.allSongs.size();i++)
        {
            boolean artistExists = false;
            for(int j = 0;j<arrayListArtist.size();j++)
            {

                if(MediaPlayerHelper.allSongs.get(i).artistName.equals(arrayListArtist.get(j).ArtistName))
                {

                    artistExists = true;
                    arrayListArtist.get(j).addSong(MediaPlayerHelper.allSongs.get(i));
                    break;
                }
            }
            if(!artistExists)
            {
                Artist tempArtist = new Artist(MediaPlayerHelper.allSongs.get(i).artistName);
                tempArtist.addSong(MediaPlayerHelper.allSongs.get(i));
                arrayListArtist.add(tempArtist);
            }
        }

        return arrayListArtist;
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
