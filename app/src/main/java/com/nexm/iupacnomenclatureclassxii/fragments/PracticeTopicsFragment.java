package com.nexm.iupacnomenclatureclassxii.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nexm.iupacnomenclatureclassxii.IUPAC_APPLICATION;
import com.nexm.iupacnomenclatureclassxii.R;
import com.nexm.iupacnomenclatureclassxii.adapters.PracticeTopicsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PracticeTopicsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PracticeTopicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PracticeTopicsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private Cursor cursor;

    private OnFragmentInteractionListener mListener;

    public PracticeTopicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PracticeTopicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PracticeTopicsFragment newInstance(String param1, String param2) {
        PracticeTopicsFragment fragment = new PracticeTopicsFragment();
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
        if(IUPAC_APPLICATION.practiceDatabase != null)
        cursor = IUPAC_APPLICATION.practiceDatabase.rawQuery("SELECT * FROM  Topics ",null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Iupac Practice");
        View view = inflater.inflate(R.layout.fragment_practice_topics, container, false);
        recyclerView = view.findViewById(R.id.practice_topics_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);

            PracticeTopicsAdapter adapter = new PracticeTopicsAdapter(cursor);
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(false);

            adapter.setOnItemClickListener(new PracticeTopicsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int topicNo) {
                    if (mListener != null) {
                        mListener.onPracticeTopicsSelected(topicNo);
                    }
                }
            });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int last) {
        if (mListener != null) {
            mListener.onPracticeTopicsSelected(last);
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
        void onPracticeTopicsSelected(int topicNo);
    }
}
