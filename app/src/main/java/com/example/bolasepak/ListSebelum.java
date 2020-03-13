package com.example.bolasepak;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolasepak.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListSebelum.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListSebelum#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListSebelum extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recycler;
    private CardViewAdapter adapter;
    private ArrayList<SoccerMatch> list = new ArrayList<>();
    private SQLiteDatabase mydatabase;
    private String home_id,away_id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListSebelum() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListSebelum.
     */
    // TODO: Rename and change types and number of parameters
    public static ListSebelum newInstance(String param1, String param2) {
        ListSebelum fragment = new ListSebelum();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ListSebelum newInstance() {
        return new ListSebelum();
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
        View rootView = inflater.inflate(R.layout.fragment_list_sekarang, container, false);
        int orientation = getResources().getConfiguration().orientation; //check whether is it portrait or landscape
        int col = 1;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            col = 2;
        }
        recycler = rootView.findViewById(R.id.recycler_view);
        adapter = new CardViewAdapter(getActivity());
        list.addAll(getData());
        recycler.setLayoutManager(new GridLayoutManager(getContext(), col));
        adapter.setListMatch(list);
        recycler.setAdapter(adapter);

        return rootView;
    }

    private ArrayList<SoccerMatch> getData(){
        TeamDetail activity = (TeamDetail) getActivity();
        mydatabase = getActivity().openOrCreateDatabase("Bola.db",MODE_PRIVATE,null); //need to change database name
        Cursor resultSet = mydatabase.rawQuery("SELECT * FROM past WHERE homeID = ?", new String[] {activity.sendTeamID()}); //need to change query

        resultSet.moveToFirst();
        SoccerMatch match = null;
        ArrayList<SoccerMatch> list = new ArrayList<>();
        while (!resultSet.isAfterLast()) {
            match = new SoccerMatch();
            match.setName1(resultSet.getString(4));
            match.setName2(resultSet.getString(5));
            match.setDateMatch(resultSet.getString(3));
            match.setScore1(resultSet.getString(8));
            match.setScore2(resultSet.getString(9));
            Log.d("name", match.getName1());
            Log.d("name2", match.getName2());
            match.setImg1(resultSet.getString(6));
            match.setImg2(resultSet.getString(7));
            match.setEventID(resultSet.getString(0));

            list.add(match);
            resultSet.moveToNext();
        }
        return list;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int col = 1;
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            col = 2;
        }
        recycler.setLayoutManager(new GridLayoutManager(getContext(), col));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
