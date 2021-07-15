package com.nexm.iupacnomenclatureclassxii.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.nexm.iupacnomenclatureclassxii.IUPAC_APPLICATION;
import com.nexm.iupacnomenclatureclassxii.R;
import com.nexm.iupacnomenclatureclassxii.adapters.ExplanationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExplanationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExplanationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExplanationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    // TODO: Rename and change types of parameters
    private String eTable,eEnd,unit,Title;
    private Cursor cursor;
    private ExplanationAdapter adapter;
    private RecyclerView recyclerView;
   // private ProgressBar progressBar;
    public static final int NUMBER_OF_ADS = 0;

    // The AdLoader used to load ads.
    private AdLoader adLoader;

    // List of native ads that have been successfully loaded.
    private final List<UnifiedNativeAd> mNativeAds = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public ExplanationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param eTableID Parameter 1.


     * @param eEnd
     * @param unit
     * @param title
     * @return A new instance of fragment ExplanationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExplanationFragment newInstance(String eTableID, String eEnd, String unit, String title) {
        ExplanationFragment fragment = new ExplanationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, eTableID);
        args.putString(ARG_PARAM2, eEnd);
        args.putString(ARG_PARAM3, unit);
        args.putString(ARG_PARAM4, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eTable = getArguments().getString(ARG_PARAM1);
            eEnd = getArguments().getString(ARG_PARAM2);
            unit = getArguments().getString(ARG_PARAM3);
            Title = getArguments().getString(ARG_PARAM4);
        }
        if(unit.matches("Haloalkane")){
            cursor = IUPAC_APPLICATION.reaction_database.rawQuery("SELECT * FROM  " + eTable + " ",null);
        }else{
            int start,end;
            try {
                start = Integer.parseInt(eTable);
                end = Integer.parseInt(eEnd);
            }
            catch (NumberFormatException e)
            {
                start = 0;
                end = 0;
            }
            cursor = IUPAC_APPLICATION.reaction_database.rawQuery("SELECT * FROM  Explanation WHERE number BETWEEN '"+start+"' AND '"+end+ "' " ,null);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_explanation, container, false);
        TextView textView = view.findViewById(R.id.explanation_title_textview);
        textView.setText(Title);
        final Button continueButton = view.findViewById(R.id.explanation_continue_button);
         recyclerView = view.findViewById(R.id.explanation_recyclerView);
         recyclerView.setFocusable(false);
        // progressBar = view.findViewById(R.id.explanation_progressbar);
        // progressBar.setVisibility(View.VISIBLE);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setNestedScrollingEnabled(false);
       // loadNativeAds();
        adapter = new ExplanationAdapter(cursor,mNativeAds);
       // progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onExplanationContinue();
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onExplanationContinue();
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
        adapter.setBitmapNull();
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
        void onExplanationContinue();
    }
    private void loadNativeAds() {

        AdLoader.Builder builder = new AdLoader.Builder(getActivity(), getString(R.string.ad_unit_id));
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            //insertAdsInMenuItems();
                            adapter = new ExplanationAdapter(cursor,mNativeAds);
                           // progressBar.setVisibility(View.GONE);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    //@Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            //insertAdsInMenuItems();
                            adapter = new ExplanationAdapter(cursor,mNativeAds);
                            //progressBar.setVisibility(View.GONE);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }).build();

        // Load the Native Express ad.
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
    }
}
