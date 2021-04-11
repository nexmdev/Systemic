package com.nexm.iupacnomenclatureclassxii.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nexm.iupacnomenclatureclassxii.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about,null);
        TextView contact = view.findViewById(R.id.textview_email);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail(view);
            }
        });
        return view;
    }
    private void mail(View view) {

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "dev.nexm@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Contact Developer");
        email.putExtra(Intent.EXTRA_TEXT, "");

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

}
