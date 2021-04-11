package com.nexm.iupacnomenclatureclassxii;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.nexm.iupacnomenclatureclassxii.util.AppRater;

public class MenuActivity extends AppCompatActivity implements RewardedVideoAdListener {

    private Cursor cursor;
    private String TOPIC_NAME,TABLE_NAME;
    private static final int LEVEL_COMPLETE = 1;
    private int complete = 0;
    private final int[] status = {0,0,0,0,0,0,0,0,8};
    private int[] startQ = {0,0,0,0,0,0,0,0,8};
    private int[] noQ = {0,0,0,0,0,0,0,0,8};
    private int unlock_rule_position;
    private RewardedVideoAd mRewardedVideoAd;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        boolean isTab = getResources().getBoolean(R.bool.isTab);
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {

            if(!isTab)this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        AppRater.app_launched(this);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd("ca-app-pub-6219444241621852/8749076261",
                new AdRequest.Builder().build());
        TOPIC_NAME = getIntent().getStringExtra("TOPIC");
        TABLE_NAME = TOPIC_NAME+"_Rules";
        cursor = IUPAC_APPLICATION.database.rawQuery("SELECT * FROM  " + TABLE_NAME + " ", null);

        setViews();
    }

    private void setViews() {
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){

            switch(cursor.getInt(cursor.getColumnIndex("RuleNo"))){
                case 1:
                    status[0] = cursor.getInt(cursor.getColumnIndex("Status"));
                    startQ[0] = cursor.getInt(cursor.getColumnIndex("StartQ"));
                    noQ[0] = cursor.getInt(cursor.getColumnIndex("NoQ"));
                    TextView textView = findViewById(R.id.menu_textView_1);
                    textView.setText(cursor.getString(cursor.getColumnIndex("RuleName")));
                    setImage(R.id.menu_imageView_1,R.id.menu_unlockView_2,status[0]);
                    break;
                case 2:
                    status[1] = cursor.getInt(cursor.getColumnIndex("Status"));
                    startQ[1] = cursor.getInt(cursor.getColumnIndex("StartQ"));
                    noQ[1] = cursor.getInt(cursor.getColumnIndex("NoQ"));
                    TextView textView2 = findViewById(R.id.menu_textView_2);
                    textView2.setText(cursor.getString(cursor.getColumnIndex("RuleName")));
                    setImage(R.id.menu_imageView_2,R.id.menu_unlockView_2,status[1]);
                    break;
                case 3:
                    status[2] = cursor.getInt(cursor.getColumnIndex("Status"));
                    startQ[2] = cursor.getInt(cursor.getColumnIndex("StartQ"));
                    noQ[2] = cursor.getInt(cursor.getColumnIndex("NoQ"));
                    TextView textView3 = findViewById(R.id.menu_textView_3);
                    textView3.setText(cursor.getString(cursor.getColumnIndex("RuleName")));
                    setImage(R.id.menu_imageView_3,R.id.menu_unlockView_3,status[2]);
                    break;
                case 4:
                    status[3] = cursor.getInt(cursor.getColumnIndex("Status"));
                    startQ[3] = cursor.getInt(cursor.getColumnIndex("StartQ"));
                    noQ[3] = cursor.getInt(cursor.getColumnIndex("NoQ"));
                    TextView textView4 = findViewById(R.id.menu_textView_4);
                    textView4.setText(cursor.getString(cursor.getColumnIndex("RuleName")));
                    setImage(R.id.menu_imageView_4,R.id.menu_unlockView_4,status[3]);
                    break;
                case 5:
                    status[4] = cursor.getInt(cursor.getColumnIndex("Status"));
                    startQ[4] = cursor.getInt(cursor.getColumnIndex("StartQ"));
                    noQ[4] = cursor.getInt(cursor.getColumnIndex("NoQ"));
                    TextView textView5 = findViewById(R.id.menu_textView_5);
                    textView5.setText(cursor.getString(cursor.getColumnIndex("RuleName")));
                    setImage(R.id.menu_imageView_5,R.id.menu_unlockView_5,status[4]);
                    break;
                case 6:
                    status[5] = cursor.getInt(cursor.getColumnIndex("Status"));
                    startQ[5] = cursor.getInt(cursor.getColumnIndex("StartQ"));
                    noQ[5] = cursor.getInt(cursor.getColumnIndex("NoQ"));
                    TextView textView6 = findViewById(R.id.menu_textView_6);
                    textView6.setText(cursor.getString(cursor.getColumnIndex("RuleName")));
                    setImage(R.id.menu_imageView_6,R.id.menu_unlockView_6,status[5]);
                    break;

            }
            cursor.moveToNext();
        }
    }

    private void setImage(int id,int unlock_id, int status) {

        ImageView imageView = findViewById(id);
        TextView unlock_view = findViewById(unlock_id);
        switch (status){
            case 0 :
                    imageView.setImageResource(R.drawable.default_menu);
                    unlock_view.setVisibility(View.GONE);
                    break;
            case 1 :
                    imageView.setImageResource(R.drawable.current);
                    unlock_view.setVisibility(View.GONE);break;
            case 2 :
                    imageView.setImageResource(R.drawable.complete_b);
                    unlock_view.setVisibility(View.GONE);break;
            case 11 :
                    imageView.setImageResource(R.drawable.default_menu);

                    unlock_view.setVisibility(View.VISIBLE);
                    id = unlock_id;
                    break;

        }
    }




    public void menu_click_handler(View view) {

        switch(view.getId()){
            case R.id.menu_frame_1:
                if(status[0]==0){showDialog();return;}else{
                    goToDetails(1,TABLE_NAME,status[0],status[1],startQ[0] ,noQ[0] );
                }  break;
            case R.id.menu_frame_2:
                if(status[1]==0){showDialog();return;}else if(status[1]==11){unlock_rule_position=1;showUnlockDialog();}else{
                    goToDetails(2,TABLE_NAME,status[1],status[2],startQ[1] ,noQ[1]);
                } break;
            case R.id.menu_frame_3:
                if(status[2]==0){showDialog();return;}else if(status[2]==11){unlock_rule_position=2;showUnlockDialog();}else{
                    goToDetails(3,TABLE_NAME,status[2],status[3],startQ[2] ,noQ[2] );
                }   break;
            case R.id.menu_frame_4:
                if(status[3]==0){showDialog();return;}else if(status[3]==11){unlock_rule_position=3;showUnlockDialog();}else{
                    goToDetails(4,TABLE_NAME,status[3],status[4],startQ[3] ,noQ[3] );
                }   break;
            case R.id.menu_frame_5:
                if(status[4]==0){showDialog();return;}else if(status[4]==11){unlock_rule_position=4;showUnlockDialog();}else{
                    goToDetails(5,TABLE_NAME,status[4],status[5],startQ[4] ,noQ[4] );
                }    break;
            case R.id.menu_frame_6:
                if(status[5]==0){showDialog();return;}else if(status[5]==11){unlock_rule_position=5;showUnlockDialog();}else{
                    goToDetails(6,TABLE_NAME,status[5],status[6],startQ[5] ,noQ[5]);
                }    break;

        }

    }

    private void showUnlockDialog() {
        //Toast.makeText(getApplicationContext(),"Unlock",Toast.LENGTH_SHORT).show();
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }else{
            mRewardedVideoAd.loadAd("ca-app-pub-6219444241621852/8749076261",
                    new AdRequest.Builder().build());
        }
    }

    private void showDialog(){

        Toast.makeText(getApplicationContext(),"Complete previous levels to unlock !",Toast.LENGTH_SHORT).show();
    }
    private void goToDetails(int i, String table_name, int status_current, int status_next, int startQuestion, int noOfQuestions) {
        //cursor.close();
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("Caller","Iupac");
        intent.putExtra("RULE_NO",i);
        intent.putExtra("TABLE",table_name);
        intent.putExtra("CURRENT_STATUS",status_current);
        intent.putExtra("NEXT_STATUS",status_next);
        intent.putExtra("TOPIC",TOPIC_NAME);
        intent.putExtra("START_QUESTION",startQuestion);
        intent.putExtra("NO_OF_QUESTIONS",noOfQuestions);

        startActivityForResult(intent,LEVEL_COMPLETE);
    }
    @Override
    protected void onActivityResult(int reuestCode,int resultCode,Intent data){
        super.onActivityResult(reuestCode,resultCode,data);
        if(reuestCode == LEVEL_COMPLETE){
            if(resultCode == RESULT_OK){
                complete = 1;

            }
        }
    }
    @Override
    public void onResume(){


        super.onResume();
        mRewardedVideoAd.resume(this);
      /*  mRewardedVideoAd.setRewardedVideoAdListener(this);
        if(!mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                    new AdRequest.Builder().build());
        }*/
        if(complete == 1){

            cursor.close();
            cursor = IUPAC_APPLICATION.database.rawQuery("SELECT * FROM  " + TABLE_NAME + " ", null);
            //cursor.moveToFirst();
            setViews();
        }

    }
    @Override
    public void onPause() {
               super.onPause();
        mRewardedVideoAd.pause(this);
    }

    @Override
    public void onDestroy() {
       // mRewardedVideoAd.destroy(this);
        super.onDestroy();
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
            mRewardedVideoAd.loadAd("ca-app-pub-6219444241621852/8749076261",
                    new AdRequest.Builder().build());
        }

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        status[unlock_rule_position]=1;
        switch (unlock_rule_position){
            case 1: setUnlockedMenu(R.id.menu_imageView_2,R.id.menu_unlockView_2);break;
            case 2: setUnlockedMenu(R.id.menu_imageView_3,R.id.menu_unlockView_3);break;
            case 3: setUnlockedMenu(R.id.menu_imageView_4,R.id.menu_unlockView_4);break;
            case 4: setUnlockedMenu(R.id.menu_imageView_5,R.id.menu_unlockView_5);break;
            case 5: setUnlockedMenu(R.id.menu_imageView_6,R.id.menu_unlockView_6);break;

        }
        unlock_rule_position++;
        IUPAC_APPLICATION.database.execSQL("UPDATE `"+TABLE_NAME+"` SET `Status`=1  WHERE RuleNo='"+unlock_rule_position+"'");

    }

    private void setUnlockedMenu(int id,int unlock_id) {
        ImageView imageView = findViewById(id);
        imageView.setImageResource(R.drawable.current);
        TextView textView = findViewById(unlock_id);
        textView.setVisibility(View.GONE);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
       // Toast.makeText(this,"Try again !",Toast.LENGTH_SHORT).show();
        if(id != 0){
            TextView unlock_view = findViewById(id);
            if(unlock_view != null)
            unlock_view.setVisibility(View.GONE);
        }


    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
