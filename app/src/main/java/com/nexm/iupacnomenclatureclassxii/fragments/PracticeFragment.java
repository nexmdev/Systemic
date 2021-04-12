package com.nexm.iupacnomenclatureclassxii.fragments;

import android.content.Context;

import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.nexm.iupacnomenclatureclassxii.IUPAC_APPLICATION;
import com.nexm.iupacnomenclatureclassxii.R;


public class PracticeFragment extends Fragment {

    private static final String ARG_PARAM1 = "TABLE";
   


    private String table;
    private int rule_no;
    private Cursor cursor1;

    private OnFragmentInteractionListener mListener;

    public PracticeFragment() {
        // Required empty public constructor
    }


    public static PracticeFragment newInstance(String param1,int rule) {
        PracticeFragment fragment = new PracticeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt("RULE",rule);
       
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            table = getArguments().getString(ARG_PARAM1);
           rule_no = getArguments().getInt("RULE",1);
        }
        cursor1 = IUPAC_APPLICATION.database.rawQuery("SELECT * FROM  " + table + " ",null);
        cursor1.moveToFirst();
        cursor1.moveToPosition(rule_no-1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        TextView ruleView1 = view.findViewById(R.id.practice_rule_textView1);
        TextView ruleView2 = view.findViewById(R.id.practice_rule_textView2);
        TextView ruleView3 = view.findViewById(R.id.practice_rule_textView3);
        TextView continueView = view.findViewById(R.id.practice_continue);
        //TextView videoView = view.findViewById(R.id.practice_video_textView);
        ImageView imageView1 = view.findViewById(R.id.practice_imageview1);
        ImageView imageView2 = view.findViewById(R.id.practice_imageview2);
        ImageView imageView3 = view.findViewById(R.id.practice_imageview3);

        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
            if(table.matches("Cyanide_Rules")||table.matches("Amide_Rules")){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT
            );
            int pixels = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
            layoutParams.setMargins(0,pixels,0,pixels);
            imageView1.setLayoutParams(layoutParams);
            imageView2.setLayoutParams(layoutParams);
            imageView3.setLayoutParams(layoutParams);
            imageView1.setPadding(0,0,0,0);
            imageView2.setPadding(0,0,0,0);
            imageView3.setPadding(0,0,0,0);

            }
        }

        if(cursor1 != null && !cursor1.isAfterLast()){
            String t1 = cursor1.getString(cursor1.getColumnIndex("T1"));
            String t2 = cursor1.getString(cursor1.getColumnIndex("T2"));
            String t3 = cursor1.getString(cursor1.getColumnIndex("T3"));
            if(t1 == null){
                ruleView1.setText("Congratulations you have completed all levels , let us take a final test !");
                imageView1.setImageResource(R.drawable.congrat);
            }else{
                ruleView1.setText(t1);
                byte[] b = cursor1.getBlob(cursor1.getColumnIndex("I1"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
                imageView1.setImageBitmap(bitmap);
            }
            if(t2 == null){
                ruleView2.setVisibility(View.GONE);
                imageView2.setVisibility(View.GONE);
            }else{
                ruleView2.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                ruleView2.setText(t2);
                byte[] b = cursor1.getBlob(cursor1.getColumnIndex("I2"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
                imageView2.setImageBitmap(bitmap);
            }
            if(t3 == null){
                imageView3.setVisibility(View.GONE);
                ruleView3.setVisibility(View.GONE);
            }else{
                imageView3.setVisibility(View.VISIBLE);
                ruleView3.setVisibility(View.VISIBLE);
                ruleView3.setText(t3);
                byte[] b = cursor1.getBlob(cursor1.getColumnIndex("I3"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
                imageView3.setImageBitmap(bitmap);
            }


        }
        continueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPracticeSelection();
            }
        });
       /* videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://youtu.be/X-VRvaSFV70")));
               // getActivity().startActivity(new Intent(getActivity().getApplicationContext(), VideoActivity.class));
            }
        });*/
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPracticeSelection();
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
        cursor1.close();
    }

   
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPracticeSelection();
    }
}
