package com.nexm.iupacnomenclatureclassxii.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.nexm.iupacnomenclatureclassxii.R;
import com.nexm.iupacnomenclatureclassxii.adapters.ReactionsAdapter;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReactionsUnitsFragments.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ReactionsUnitsFragments extends Fragment {
    private final String[] topics = {"Haloalkane","Alcohol","Phenol","Ether","Aldehyde","Acid"};
    private OnFragmentInteractionListener mListener;

    public ReactionsUnitsFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Organic Reactions Notes");
        View view = inflater.inflate(R.layout.fragment_reactions_units_fragments, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.reactions_recyclerView);
        final RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        final ReactionsAdapter adapter = new ReactionsAdapter(topics);
        adapter.setOnItemClickListener(new ReactionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String unitName, String topicName) {
                if(mListener != null){
                   /* if(unitName.matches("Haloalkane") || unitName.matches("Alcohol")
                            ||unitName.matches("Phenol")||unitName.matches("Ether")
                            ||unitName.matches("Aldehyde")){*/
                        mListener.onReactonsUnitSelected(unitName,topicName);
                   // }else{
                  //      showComingDialog();
                  //  }

                }
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String unitName, String topicName) {
        if (mListener != null) {
            mListener.onReactonsUnitSelected(unitName,topicName );
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onReactonsUnitSelected(String unitName, String topicName);
    }


}
