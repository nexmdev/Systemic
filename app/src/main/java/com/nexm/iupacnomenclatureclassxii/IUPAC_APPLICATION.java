package com.nexm.iupacnomenclatureclassxii;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.nexm.iupacnomenclatureclassxii.util.ExtDbHelper;
import com.nexm.iupacnomenclatureclassxii.util.ExtDbHelperPractice;
import com.nexm.iupacnomenclatureclassxii.util.ExtDbHelperReactions;

public class IUPAC_APPLICATION extends Application {

    private static final String DB_NAME = "iupacdata.db";
    private static final String DB_NAME_Reactions = "reactions.db";
    private static final String DB_NAME_Practice = "practiceData.db";
    public static SQLiteDatabase database,reaction_database,practiceDatabase;
    private static ExtDbHelper extDbHelper ;
    private static ExtDbHelperReactions extDbHelperReactions;
    private static ExtDbHelperPractice extDbHelperPractice;
    private static SharedPreferences sharedPreferences;

  //  public static Typeface typeface;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        extDbHelper = ExtDbHelper.getInstance(getApplicationContext(), DB_NAME,8);
        extDbHelperReactions = ExtDbHelperReactions.getInstance(getApplicationContext(),DB_NAME_Reactions,6);
        extDbHelperPractice = ExtDbHelperPractice.getInstance(getApplicationContext(),DB_NAME_Practice,1);
        sharedPreferences = getSharedPreferences("IUPACVersion",Context.MODE_PRIVATE);
        MyTask myTask = new MyTask();
        myTask.execute();
       // typeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        super.onCreate();
    }

    private static class MyTask extends AsyncTask<String, String, SQLiteDatabase> {

        @Override
        protected SQLiteDatabase doInBackground(String... params) {

            database = extDbHelper.openDataBase();
            readStatus();
            reaction_database = extDbHelperReactions.openDataBase();
            practiceDatabase = extDbHelperPractice.openDataBase();
            // MobileAds.initialize(getApplicationContext(), "ca-app-pub-6219444241621852~4579409126");


            return database;
        }

        private void readStatus() {
            if(sharedPreferences.getString("Alkane",null)!=null){
                if(IUPAC_APPLICATION.database != null){
                    final String[] topics = {"Alkane","Haloalkane","Alcohol","Ether","Aldehyde","CarboxylicAcid","Amines"};
                    for(int i=0 ; i<topics.length ; i++){
                        String status = sharedPreferences.getString(topics[i],"0");
                        String[] s = status.split(",");
                        for(int j = 0 ; j<s.length ; j++){
                            String rule_table = topics[i]+"_Rules";
                            int ss = Integer.parseInt(s[j]);
                            int rule_no = j+1;
                            IUPAC_APPLICATION.database.execSQL("UPDATE `"+rule_table+"` SET `Status`="+ss+"  WHERE RuleNo='"+rule_no+"'");
                        }
                    }
                }
                sharedPreferences.edit().remove("Alkane").apply();

            }

        }
    }


}
