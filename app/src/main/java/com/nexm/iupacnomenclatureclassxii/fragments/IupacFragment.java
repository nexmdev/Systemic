package com.nexm.iupacnomenclatureclassxii.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nexm.iupacnomenclatureclassxii.R;
import com.nexm.iupacnomenclatureclassxii.adapters.Main_Recycler_Adapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IupacFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IupacFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IupacFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String[] topics = {"Alkane","Alkene_Alkyne","Haloalkane","Alcohol","Ether","Aldehyde","CarboxylicAcid","Amines","Cyanide","Amide"};

    private OnFragmentInteractionListener mListener;

    public IupacFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IupacFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IupacFragment newInstance(String param1, String param2) {
        IupacFragment fragment = new IupacFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        View view = inflater.inflate(R.layout.fragment_iupac, container, false);
        getActivity().setTitle("Iupac nomenclature");
        final RecyclerView recyclerView = view.findViewById(R.id.iupac_recyclerView);
        boolean isLarge = getResources().getBoolean(R.bool.isLarge);
        int columns = 2;
        if (isLarge) {
            columns = 3;
        }
        final RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), columns);
        recyclerView.setLayoutManager(manager);
        final Main_Recycler_Adapter adapter = new Main_Recycler_Adapter(topics);
        recyclerView.setAdapter(adapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onIupacSelected(uri);
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
        void onIupacSelected(Uri uri);
    }
}
