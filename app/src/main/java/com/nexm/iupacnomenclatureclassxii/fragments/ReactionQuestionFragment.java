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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;

import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.nexm.iupacnomenclatureclassxii.IUPAC_APPLICATION;
import com.nexm.iupacnomenclatureclassxii.R;
import com.nexm.iupacnomenclatureclassxii.util.CONSTANTS;

import java.util.Arrays;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReactionQuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReactionQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReactionQuestionFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private InterstitialAd mInterstitialAd;
    private FullScreenContentCallback fullScreenContentCallback;

    // TODO: Rename and change types of parameters
    private String selectedAnswer;
    private int selectedOption;
    private int no_q;
    private int i=0;
    private String unitTable,Unit;
    private String qTable;
    private int explNo,qStart,qEnd,firstQ;
    private Cursor cursor;
    private TextView questionNoTextView,questionTextTextView,right,wrong,next,option1,
            option2,option3,unlock_answer;
    private ImageView question_image;
    private AnimationDrawable myFrameAnimation1;
    private Bitmap bm;
    private RewardedAd mRewardedVideoAd;
    private Boolean isRunning = false;

    private OnFragmentInteractionListener mListener;

    public ReactionQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.

     * @return A new instance of fragment ReactionQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReactionQuestionFragment newInstance(String param1, int mexplNo, String param2, String munit) {
        ReactionQuestionFragment fragment = new ReactionQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, mexplNo);
        args.putString(ARG_PARAM4, munit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            unitTable = getArguments().getString(ARG_PARAM1);
            qTable = getArguments().getString(ARG_PARAM2);
            explNo = getArguments().getInt(ARG_PARAM3);
            Unit =  getArguments().getString(ARG_PARAM4);

        }
        fullScreenContentCallback =
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Code to be invoked when the ad showed full screen content.
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        mRewardedVideoAd = null;
                        // Code to be invoked when the ad dismissed full screen content.
                    }
                };
        if(Unit.matches("Haloalkane")){
            cursor = IUPAC_APPLICATION.reaction_database.rawQuery("SELECT * FROM  " + qTable + " ",null);
            no_q = cursor.getCount();
        }else if (Unit.matches("Practice")){
            cursor = IUPAC_APPLICATION.practiceDatabase.rawQuery("SELECT * FROM  Topics ",null);
            cursor.moveToPosition(explNo);
            qStart = cursor.getInt(cursor.getColumnIndex("Present_Q"));
            qEnd = cursor.getInt(cursor.getColumnIndex("Last_Q"));
            firstQ = cursor.getInt(cursor.getColumnIndex("First_Q"));
            cursor.close();
            cursor = IUPAC_APPLICATION.practiceDatabase.rawQuery("SELECT * FROM  Questions WHERE Qnumber BETWEEN '"+qStart+"' AND '"+qEnd+ "' " ,null);
            no_q = cursor.getCount();
        }else{
            cursor = IUPAC_APPLICATION.reaction_database.rawQuery("SELECT * FROM  " + unitTable + " ",null);
            cursor.moveToPosition(explNo -1);
            qStart = cursor.getInt(cursor.getColumnIndex("q_start"));
            qEnd = cursor.getInt(cursor.getColumnIndex("q_end"));
            no_q = qEnd - qStart;
            no_q++;
            cursor.close();
            cursor = IUPAC_APPLICATION.reaction_database.rawQuery("SELECT * FROM  Questions WHERE Qnumber BETWEEN '"+qStart+"' AND '"+qEnd+ "' " ,null);
            int c = cursor.getCount();
        }

        loadInterstitialAd();
        loadRewaredAd();
    }
    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getActivity(), CONSTANTS.INTERSTITIAL_ID_TEST_FRAGMENT, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error

                mInterstitialAd = null;
            }
        });
    }

    private void loadRewaredAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(getActivity(), CONSTANTS.REWARDED_ID_REACTION_Q_FRAGMENT,
                adRequest, new RewardedAdLoadCallback(){
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.

                        mRewardedVideoAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedVideoAd = rewardedAd;
                        mRewardedVideoAd.setFullScreenContentCallback(fullScreenContentCallback);
                    }
                });
    }



    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause() {

        if (Unit.matches("Practice") && !isRunning){

            if(IUPAC_APPLICATION.practiceDatabase.isOpen()){
                unitTable = "Topics";
                i = i+ qStart;
                explNo++;
                IUPAC_APPLICATION.practiceDatabase.execSQL("UPDATE `"+unitTable+"` SET `Present_Q`="+i+"  WHERE Topic_No='"+explNo+"'");
                //cursor.close();
                }}
        super.onPause();
    }

    @Override
    public void onDestroy() {

        cursor.close();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_reaction_question, container, false);
        questionNoTextView = view.findViewById(R.id.question_noView);
        questionTextTextView = view.findViewById(R.id.question_text);
        right = view.findViewById(R.id.question_right);
        right.setCompoundDrawablesWithIntrinsicBounds(null,null, AppCompatResources.getDrawable(getActivity(),R.drawable.ic_done_black_24dp),null);
        wrong = view.findViewById(R.id.question_wrong);
        wrong.setCompoundDrawablesWithIntrinsicBounds(null,null,AppCompatResources.getDrawable(getActivity(),R.drawable.ic_close_black_24dp),null);
        next = view.findViewById(R.id.question_check);
        question_image = view.findViewById(R.id.question_image);
        option1 = view.findViewById(R.id.question_option1);
        option2 = view.findViewById(R.id.question_option2);
        option3 = view.findViewById(R.id.question_option3);
        option1.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);

        option2.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);

        option3.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);

        unlock_answer = view.findViewById(R.id.question_unlock);
        unlock_answer.setCompoundDrawablesWithIntrinsicBounds(null,AppCompatResources.getDrawable(getActivity(),R.drawable.ic_action_movie),null,null);
        cursor.moveToFirst();
        setQuestion();


        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processOptionClick(R.drawable.reaction_option_selected, R.drawable.reaction_option_back, option1,option2,option3);
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processOptionClick(R.drawable.reaction_option_selected, R.drawable.reaction_option_back, option2,option1,option3);

            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processOptionClick(R.drawable.reaction_option_selected, R.drawable.reaction_option_back, option3,option1,option2);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                switch(next.getText().toString()){
                    case "CHECK":
                        String answer = cursor.getString(cursor.getColumnIndex("AnswerText"));
                        if(answer.matches(selectedAnswer)){
                            right.setVisibility(View.VISIBLE);
                            animateRight();
                            wrong.setVisibility(View.GONE);
                            byte[] b = cursor.getBlob(cursor.getColumnIndex("AnswerImage"));
                            bm = BitmapFactory.decodeByteArray(b,0,b.length);
                            //Bitmap useThisBitmap = Bitmap.createScaledBitmap(bm,bm.getWidth(),bm.getHeight(), true);
                            question_image.setImageBitmap(bm);

                            TextView textView = view.findViewById(selectedOption);
                            textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(),R.drawable.led),null,null,null);

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
                            textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(),R.drawable.led_red),null,null,null);

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
                        if(cursor.isAfterLast()){
                            if (Unit.matches("Practice")){
                                if(IUPAC_APPLICATION.practiceDatabase.isOpen()){
                                   // unitTable = "Topics";
                                   // i = i+ qStart;
                                   // IUPAC_APPLICATION.practiceDatabase.execSQL("UPDATE `"+unitTable+"` SET `Present_Q`="+i+"  WHERE Topic_No='"+explNo+"'");
                                   // cursor.close();
                                    getActivity().onBackPressed();
                                }
                            }else{
                                if(IUPAC_APPLICATION.reaction_database.isOpen()){

                                    IUPAC_APPLICATION.reaction_database.execSQL("UPDATE `"+unitTable+"` SET `noofa`="+i+"  WHERE number='"+explNo+"'");

                                }
                                cursor.close();
                                getActivity().setResult(Activity.RESULT_OK);
                                if (mInterstitialAd != null) {
                                    mInterstitialAd.show(getActivity());
                                }
                                getActivity().finish();
                            }

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
                showRewardedAd();

            }
        });
        return view;
    }
    private void shuffle() {
        String[] options = {option1.getText().toString(),
                option2.getText().toString(),
                option3.getText().toString()
        };
        Collections.shuffle(Arrays.asList(options));
        Collections.shuffle(Arrays.asList(options));
        option1.setText(options[0]);
        option2.setText(options[1]);
        option3.setText(options[2]);

    }
    private void animateLeds(TextView selectedOption) {

        Drawable[] d1 =  selectedOption.getCompoundDrawables();
        if(d1[0]!=null){
            myFrameAnimation1 = (AnimationDrawable) d1[0];
            myFrameAnimation1.start();
        }
    }
    private void animateRight(){

        float a=next.getWidth();
        right.animate()
                .translationX(a)
                .setDuration(100);

    }
    private void animateWrong(){

        float a=next.getWidth();
        wrong.animate()
                .translationX(a)
                .setDuration(100);

    }
    private void processOptionClick(int tab_background, int tab_default, TextView selectedoption,
                                    TextView otherOption1,TextView otherOption2) {
        next.setVisibility(View.VISIBLE);
        selectedoption.setBackgroundResource(tab_background);
        selectedoption.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_yellow),null,null,null);
        otherOption1.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);
        otherOption2.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);
        otherOption1.setBackgroundResource(tab_default);
        otherOption2.setBackgroundResource(tab_default);
        selectedAnswer = selectedoption.getText().toString().trim();
        this.selectedOption = selectedoption.getId();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onQuestionFinished(uri);
        }
    }
    private void setDefaultViews() {
        unlock_answer.setVisibility(View.GONE);
        option1.setBackgroundResource(R.drawable.reaction_option_back);
        option2.setBackgroundResource(R.drawable.reaction_option_back);
        option3.setBackgroundResource(R.drawable.reaction_option_back);

        option1.getCompoundDrawables();
        option1.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);
        option2.getCompoundDrawables();
        option2.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);
        option3.getCompoundDrawables();
        option3.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getActivity(),R.drawable.led_default),null,null,null);

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
    private void showRewardedAd() {
        if (mRewardedVideoAd != null) {
            isRunning = true;
            Activity activityContext = getActivity();
            mRewardedVideoAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    byte[] b = cursor.getBlob(cursor.getColumnIndex("AnswerImage"));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
                    question_image.setImageBitmap(bitmap);
                    setDefaultViews();
                    String answer = cursor.getString(cursor.getColumnIndex("AnswerText"));
                    if(answer.matches(option1.getText().toString())){
                        processOptionClick(R.drawable.reaction_option_selected, R.drawable.reaction_option_back, option1,option3,option2);
                        option1.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(),R.drawable.led),null,null,null);
                        animateLeds(option1);
                        next.setText("CONTINUE");
                    }else if(answer.matches(option2.getText().toString())){
                        processOptionClick(R.drawable.reaction_option_selected, R.drawable.reaction_option_back, option2,option3,option1);
                        option2.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(),R.drawable.led),null,null,null);
                        animateLeds(option2);
                        next.setText("CONTINUE");
                    }else if(answer.matches(option3.getText().toString())){
                        processOptionClick(R.drawable.reaction_option_selected, R.drawable.reaction_option_back, option3,option2,option1);
                        option3.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(),R.drawable.led),null,null,null);
                        animateLeds(option3);
                        next.setText("CONTINUE");

                    }
                    mRewardedVideoAd.setFullScreenContentCallback(null);
                    mRewardedVideoAd=null;

                    loadRewaredAd();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Network problem ! Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void setQuestion() {

        if(cursor!=null && !cursor.isAfterLast() ){

            if (Unit.matches("Practice")){
                int noOfQuestions = qEnd-firstQ;
                noOfQuestions++;
                int currentQ = qStart-firstQ;
                currentQ++;
                currentQ = currentQ + i;
                questionNoTextView.setText(currentQ +" / "+noOfQuestions);
            }else{
                questionNoTextView.setText((i + 1) +"/ "+no_q);
            }

            questionTextTextView.setText(cursor.getString(cursor.getColumnIndex("Qtext")));
            byte[] b = cursor.getBlob(cursor.getColumnIndex("Qimage"));
            bm = BitmapFactory.decodeByteArray(b,0,b.length);
           // Bitmap useThisBitmap = Bitmap.createScaledBitmap(bm,bm.getWidth(),bm.getHeight(), true);
            question_image.setImageBitmap(bm);

            option1.setText(cursor.getString(cursor.getColumnIndex("Option1")));
            option2.setText(cursor.getString(cursor.getColumnIndex("Option2")));
            option3.setText(cursor.getString(cursor.getColumnIndex("Option3")));

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
        bm.recycle();
        bm = null;
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
        void onQuestionFinished(Uri uri);
    }
}

