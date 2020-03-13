package com.example.bolasepak;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListSekarang#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListSekarang extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recycler;
    private CardViewAdapter adapter;
    private ArrayList<SoccerMatch> list = new ArrayList<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListSekarang() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListSekarang.
     */
    // TODO: Rename and change types and number of parameters
    public static ListSekarang newInstance(String param1, String param2) {
        ListSekarang fragment = new ListSekarang();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ListSekarang newInstance() {
        return new ListSekarang();
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
        adapter = new CardViewAdapter(getContext());
        list.addAll(MockData.getListData());
        recycler.setLayoutManager(new GridLayoutManager(getContext(), col));
        adapter.setListMatch(list);
        recycler.setAdapter(adapter);

        return rootView;
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
}
