package com.nexm.iupacnomenclatureclassxii.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nexm.iupacnomenclatureclassxii.BottomNavigationActivity;
import com.nexm.iupacnomenclatureclassxii.DetailActivity;
import com.nexm.iupacnomenclatureclassxii.IUPAC_APPLICATION;
import com.nexm.iupacnomenclatureclassxii.R;
import com.nexm.iupacnomenclatureclassxii.adapters.ReactionsTopicsAdapter;


public class ReactionsTopicsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String tableName,unit,topic;
    private Cursor cursor;
    private static final int LEVEL_COMPLETE = 1;

    private  ReactionsTopicsAdapter adapter;
    private RecyclerView recyclerView;

    public ReactionsTopicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ReactionsTopicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReactionsTopicsFragment newInstance(String unit,String topic) {
        ReactionsTopicsFragment fragment = new ReactionsTopicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, unit);
        args.putString(ARG_PARAM2, topic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            unit = getArguments().getString(ARG_PARAM1);
            topic = getArguments().getString(ARG_PARAM2);
            tableName = unit+topic;
            cursor = IUPAC_APPLICATION.reaction_database.rawQuery("SELECT * FROM  " + tableName + " ",null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reactions_topics, container, false);
        getActivity().setTitle(unit+ "-"+topic);
        recyclerView = view.findViewById(R.id.reactions_topics_recyclerView);
        boolean isTab = getResources().getBoolean(R.bool.isTab);
        if(isTab && getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ){
            final GridLayoutManager manager = new GridLayoutManager(getActivity(),2);
            recyclerView.setLayoutManager(manager);
        }else{
            final LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(manager);
        }

        adapter = new ReactionsTopicsAdapter(cursor,unit,topic);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ReactionsTopicsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String unitTable, int explNo, String eTable, String qTable, String unit, String topicTitle) {
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("Caller","Reactions");
                intent.putExtra("UnitTableName",unitTable);
                intent.putExtra("ExplanationNo",explNo);
                intent.putExtra("ETable",eTable);
                intent.putExtra("QTable",qTable);
                intent.putExtra("Unit",unit);
                intent.putExtra("Title",topicTitle);
                getActivity().startActivityForResult(intent,LEVEL_COMPLETE);
            }
        });
        return view;
    }

    @Override
    public void onResume(){


        super.onResume();
       // mRewardedVideoAd.resume(this);
      /*  mRewardedVideoAd.setRewardedVideoAdListener(this);
        if(!mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                    new AdRequest.Builder().build());
        }*/
        if(BottomNavigationActivity.complete == 1){

            cursor.close();
            cursor = IUPAC_APPLICATION.reaction_database.rawQuery("SELECT * FROM  " + tableName + " ", null);
            adapter.swapCursors(cursor);
            //cursor.moveToFirst();
           // setViews();
        }

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
