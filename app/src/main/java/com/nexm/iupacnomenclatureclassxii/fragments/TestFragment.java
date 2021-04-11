package com.nexm.iupacnomenclatureclassxii.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.nexm.iupacnomenclatureclassxii.IUPAC_APPLICATION;
import com.nexm.iupacnomenclatureclassxii.R;

import java.util.Arrays;
import java.util.Collections;


public class TestFragment extends Fragment implements RewardedVideoAdListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "TABLE";
    private InterstitialAd mInterstitialAd;

    // TODO: Rename and change types of parameters
    private String rule_table,test_table,selectedAnswer;
    private int rule_no,selectedOption,status_current,status_next,first_q,no_q,i=0;
    private Cursor cursor;
    private TextView questionNoTextView,questionTextTextView,right,wrong,next,option1,
            option2,option3,option4,unlock_answer;
    private ImageView question_image;
    private AnimationDrawable myFrameAnimation1;
    AnimationDrawable myFrameAnimation2;
    private OnFragmentInteractionListener mListener;
    private RewardedVideoAd mRewardedVideoAd;


    public TestFragment() {
        // Required empty public constructor
    }


    public static TestFragment newInstance(String rules_table_name, int rule_no, int current_status, int next_status, String test_table_name, int startQuestion, int noOfQuestions) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, rules_table_name);
        args.putInt("RULE_NO",rule_no);
        args.putInt("STATUS_CURRENT",current_status);
        args.putInt("STATUS_NEXT",next_status);
        args.putString("TEST_TABLE_NAME",test_table_name);
        args.putInt("START_QUESTION",startQuestion);
        args.putInt("NO_QUESTIONS",noOfQuestions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rule_table = getArguments().getString(ARG_PARAM1);
            rule_no = getArguments().getInt(("RULE_NO"));
            status_current = getArguments().getInt(("STATUS_CURRENT"));
            status_next = getArguments().getInt(("STATUS_NEXT"));
            no_q = getArguments().getInt(("NO_QUESTIONS"));
            test_table = getArguments().getString("TEST_TABLE_NAME");
            first_q = getArguments().getInt(("START_QUESTION"));
        }
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-6219444241621852/4563393019");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        if(!mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.loadAd("ca-app-pub-6219444241621852/2141721766",
                    new AdRequest.Builder().build());
        }
       // test_table = "Alkane_Test_1";
       // first_q = (5*rule_no)-5;
       // no_q = (first_q == 35) ? 10 : 5;
        cursor = IUPAC_APPLICATION.database.rawQuery("SELECT * FROM  " + test_table + " ",null);
        cursor.moveToFirst();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_test, container, false);

        questionNoTextView = view.findViewById(R.id.test_q_no);
        questionTextTextView = view.findViewById(R.id.test_q_text);
        right = view.findViewById(R.id.test_right);
        right.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.ic_done_black_24dp),null,null,null);
        wrong = view.findViewById(R.id.test_wrong);
        wrong.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.ic_close_black_24dp),null,null,null);
        next = view.findViewById(R.id.test_next);
        question_image = view.findViewById(R.id.test_image);
        if(rule_table.matches("Cyanide_Rules")){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0,40,0,40);
            question_image.setLayoutParams(layoutParams);
            question_image.setPadding(0,0,0,0);

        }
        option1 = view.findViewById(R.id.test_option1);
        option2 = view.findViewById(R.id.test_option2);
        option3 = view.findViewById(R.id.test_option_3);
        option4 = view.findViewById(R.id.test_option_4);
        unlock_answer = view.findViewById(R.id.test_unlock_answer_view);
        unlock_answer.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.ic_action_movie),null,null,null);
        if(cursor != null && !cursor.isAfterLast())cursor.moveToPosition(first_q);
        setQuestion();
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processOptionClick(R.color.tab_background, R.color.tab_default, option1,option2,option3,option4);
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processOptionClick(R.color.tab_background, R.color.tab_default, option2,option1,option3,option4);

            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processOptionClick(R.color.tab_background, R.color.tab_default, option3,option1,option2,option4);

            }
        });
        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processOptionClick(R.color.tab_background, R.color.tab_default, option4,option1,option3,option2);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
               // scrollview();
                switch(next.getText().toString()){
                    case "CHECK":
                        String answer = cursor.getString(cursor.getColumnIndex("A"));
                        if(answer.matches(selectedAnswer)){
                            right.setVisibility(View.VISIBLE);
                            animateRight();
                            wrong.setVisibility(View.GONE);
                            byte[] b = cursor.getBlob(cursor.getColumnIndex("AI"));
                            Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
                            question_image.setImageBitmap(bitmap);
                            TextView textView = view.findViewById(selectedOption);
                            textView.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getActivity(),R.drawable.led),null);

                           /* if(selectedOption == option1.getId()){
                                option1.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.led),null);
                            }else{
                                option2.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.led),null);
                            }*/

                            animateLeds(textView);
                            next.setText("CONTINUE");
                        }else{

                            unlock_answer.setVisibility(View.VISIBLE);
                            right.setVisibility(View.GONE);
                            wrong.setVisibility(View.VISIBLE);
                            animateWrong();
                            TextView textView = view.findViewById(selectedOption);
                            textView.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getActivity(),R.drawable.led_red),null);

                            /*if(selectedOption == option1.getId()){
                                option1.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.led_red),null);
                            }else{
                                option2.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.led_red),null);
                            }*/
                            animateLeds(textView);
                            next.setText("TRY AGAIN");
                        }
                        break;
                    case "TRY AGAIN":
                        setDefaultViews();
                        shuffle();
                        break;
                    case "CONTINUE":
                        cursor.moveToNext();
                        i++;
                        if(cursor.isAfterLast()|| i == no_q){
                            if(IUPAC_APPLICATION.database.isOpen()){
                                if(status_current != 2){
                                    IUPAC_APPLICATION.database.execSQL("UPDATE `"+rule_table+"` SET `Status`=2  WHERE RuleNo='"+rule_no+"'");
                                }
                                rule_no++;
                                if(status_next == 0 || status_next == 11){
                                    IUPAC_APPLICATION.database.execSQL("UPDATE `"+rule_table+"` SET `Status`=1  WHERE RuleNo='"+rule_no+"'");
                                }
                                rule_no++;
                                if(status_next == 0 && rule_no < 6){
                                    IUPAC_APPLICATION.database.execSQL("UPDATE `"+rule_table+"` SET `Status`=11  WHERE RuleNo='"+rule_no+"'");
                                }
                            }
                            cursor.close();
                            getActivity().setResult(Activity.RESULT_OK);
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                            getActivity().finish();
                        }else{
                            setQuestion();
                            setDefaultViews();
                        }
                        break;
                }
            }
        });

        unlock_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        });

        return view;
    }



    private void scrollview() {

        NestedScrollView nestedScrollView = getActivity().findViewById(R.id.nested);

        int toolbarHeight = getActivity().findViewById(R.id.toolbar).getHeight();

        nestedScrollView.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
        nestedScrollView.dispatchNestedPreScroll(0, toolbarHeight, null, null);
        nestedScrollView.dispatchNestedScroll(0, 0, 0, 0, new int[]{0, -toolbarHeight});
       // nestedScrollView.scrollTo(0, nestedScrollView.getBottom());
    }

    private void shuffle() {
        String[] options = {    option1.getText().toString(),
                                option2.getText().toString(),
                                option3.getText().toString(),
                                option4.getText().toString()};
        Collections.shuffle(Arrays.asList(options));
        Collections.shuffle(Arrays.asList(options));
        option1.setText(options[0]);
        option2.setText(options[1]);
        option3.setText(options[2]);
        option4.setText(options[3]);
    }

    private void processOptionClick(int tab_background, int tab_default, TextView selectedOption,
                                    TextView otherOption1,TextView otherOption2,TextView otherOption3) {
        next.setVisibility(View.VISIBLE);
        selectedOption.setBackgroundColor(ContextCompat.getColor(getActivity(),tab_background));
        otherOption1.setBackgroundColor(ContextCompat.getColor(getActivity(),tab_default));
        otherOption2.setBackgroundColor(ContextCompat.getColor(getActivity(),tab_default));
        otherOption3.setBackgroundColor(ContextCompat.getColor(getActivity(),tab_default));
        selectedAnswer = selectedOption.getText().toString().trim();
        this.selectedOption = selectedOption.getId();
    }

    private void animateLeds(TextView selectedOption) {

        Drawable[] d1 =  selectedOption.getCompoundDrawables();
        if(d1[2]!=null){
            myFrameAnimation1 = (AnimationDrawable) d1[2];
            myFrameAnimation1.start();
        }
    }

    private void setDefaultViews() {
        unlock_answer.setVisibility(View.GONE);
        option1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.tab_default));
        option2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.tab_default));
        option3.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.tab_default));
        option4.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.tab_default));
        option1.getCompoundDrawables();
        option1.setCompoundDrawables(null,null,null,null);
        option2.getCompoundDrawables();
        option2.setCompoundDrawables(null,null,null,null);
        option3.getCompoundDrawables();
        option3.setCompoundDrawables(null,null,null,null);
        option4.getCompoundDrawables();
        option4.setCompoundDrawables(null,null,null,null);
       if(myFrameAnimation1!=null){
           if(myFrameAnimation1.isRunning()){
               myFrameAnimation1.stop();
           }

       }

        if(right.getVisibility()==View.VISIBLE){
            right.setTranslationX(next.getWidth());
            right.setVisibility(View.GONE);
        }

        if(wrong.getVisibility()==View.VISIBLE){
           wrong.setTranslationX(next.getWidth());
            wrong.setVisibility(View.GONE);
        }

        next.setText("CHECK");
        next.setVisibility(View.GONE);
    }
    private void animateRight(){

        float a=next.getWidth();
        right.animate()
                .translationX(-a)
                .setDuration(100);

    }
    private void animateWrong(){

        float a=next.getWidth();
        wrong.animate()
                .translationX(-a)
                .setDuration(100);

    }

    private void setQuestion() {

        if(cursor!=null && !cursor.isAfterLast() ){

            questionNoTextView.setText(String.valueOf(i+1) +"/ "+no_q);
            questionTextTextView.setText(cursor.getString(cursor.getColumnIndex("QT")));
            byte[] b = cursor.getBlob(cursor.getColumnIndex("QI"));
            Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
            question_image.setImageBitmap(bitmap);
            option1.setText(cursor.getString(cursor.getColumnIndex("O1")));
            option2.setText(cursor.getString(cursor.getColumnIndex("O2")));
            option3.setText(cursor.getString(cursor.getColumnIndex("O3")));
            option4.setText(cursor.getString(cursor.getColumnIndex("O4")));
        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTestFragmentSelection(uri);
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
    public void onResume() {
        mRewardedVideoAd.resume(getActivity());
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(getActivity());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(getActivity());
        super.onDestroy();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
       // cursor.close();
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        if(!mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.loadAd("ca-app-pub-6219444241621852/2141721766",
                    new AdRequest.Builder().build());
        }

    }

    @Override
    public void onRewarded(RewardItem reward) {
        //Toast.makeText(getActivity(), "onRewarded! currency: " + reward.getType() + "  amount: " +
              //  reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.
        byte[] b = cursor.getBlob(cursor.getColumnIndex("AI"));
        Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
        question_image.setImageBitmap(bitmap);
        setDefaultViews();
        String answer = cursor.getString(cursor.getColumnIndex("A"));
        if(answer.matches(option1.getText().toString())){
            processOptionClick(R.color.tab_background, R.color.tab_default, option1,option4,option3,option2);
            option1.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getActivity(),R.drawable.led),null);
            animateLeds(option1);
            next.setText("CONTINUE");
        }else if(answer.matches(option2.getText().toString())){
            processOptionClick(R.color.tab_background, R.color.tab_default, option2,option4,option3,option1);
            option2.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getActivity(),R.drawable.led),null);
            animateLeds(option2);
            next.setText("CONTINUE");
        }else if(answer.matches(option3.getText().toString())){
            processOptionClick(R.color.tab_background, R.color.tab_default, option3,option4,option2,option1);
            option3.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getActivity(),R.drawable.led),null);
            animateLeds(option3);
            next.setText("CONTINUE");
        }else if(answer.matches(option4.getText().toString())){
            processOptionClick(R.color.tab_background, R.color.tab_default, option4,option3,option2,option1);
            option4.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getActivity(),R.drawable.led),null);
            animateLeds(option4);
            next.setText("CONTINUE");
        }
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        if(unlock_answer.getVisibility() == View.VISIBLE){
            unlock_answer.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRewardedVideoCompleted() {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTestFragmentSelection(Uri uri);
    }
}
