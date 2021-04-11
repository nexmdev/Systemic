package com.nexm.iupacnomenclatureclassxii.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.nexm.iupacnomenclatureclassxii.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LegalFragment extends Fragment {


    public LegalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_legal, container, false);
        WebView webView = view.findViewById(R.id.legal_webview);
        webView.loadUrl("file:///android_asset/eula_uturn.html");
        return view;
    }

}
