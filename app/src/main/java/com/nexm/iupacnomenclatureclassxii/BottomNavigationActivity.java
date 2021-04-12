package com.nexm.iupacnomenclatureclassxii;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nexm.iupacnomenclatureclassxii.fragments.IupacFragment;
import com.nexm.iupacnomenclatureclassxii.fragments.PracticeTopicsFragment;
import com.nexm.iupacnomenclatureclassxii.fragments.ReactionQuestionFragment;
import com.nexm.iupacnomenclatureclassxii.fragments.ReactionsTopicsFragment;
import com.nexm.iupacnomenclatureclassxii.fragments.ReactionsUnitsFragments;

import java.util.Objects;

public class BottomNavigationActivity extends AppCompatActivity implements
        IupacFragment.OnFragmentInteractionListener,
         ReactionsUnitsFragments.OnFragmentInteractionListener,

        ReactionQuestionFragment.OnFragmentInteractionListener,
        PracticeTopicsFragment.OnFragmentInteractionListener {

    private SharedPreferences.Editor editor;
    private static final int LEVEL_COMPLETE = 1;
    public static int complete = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    IupacFragment fragment = new IupacFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.iupac_framelayout,fragment)
                            .commit();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().popBackStack("ReactionUnits", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.iupac_framelayout,new ReactionsUnitsFragments())
                            .addToBackStack("ReactionUnits")
                            .commit();
                        return true;
                case R.id.navigation_notifications:
                    getSupportFragmentManager().popBackStack("ReactionUnits",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.iupac_framelayout,new PracticeTopicsFragment())
                            .addToBackStack("ReactionUnits")
                            .commit();
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        boolean isTab = getResources().getBoolean(R.bool.isTab);
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {

            if(!isTab)this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getSupportActionBar().setElevation(0);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        IupacFragment fragment = new IupacFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.iupac_framelayout,fragment)
                .addToBackStack("ReactionUnits")
                .commit();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("RUNNING_STATUS", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Boolean run = sharedPreferences.getBoolean("FIRST", true);

        if(run){
            editor.putBoolean("FIRST", false);
            editor.apply();
            showPrivacyPolicy();
        }


    }
    private void showPrivacyPolicy() {
        final Dialog dialog;

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.privacy_layout);

        final TextView exit = (dialog).findViewById(R.id.privacy_exit);
        final TextView agree = (dialog).findViewById(R.id.privacy_agree);
        final TextView showPolicy = (dialog).findViewById(R.id.privacy_see);

        showPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("FIRST", true);
                editor.apply();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://iupac-nomenclature.firebaseapp.com/"));
                String title = "Open page Using";
                Intent chooser = Intent.createChooser(intent, title);
                startActivity(chooser);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("FIRST", true);
                editor.apply();
                finish();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    @Override
    public void onIupacSelected(Uri uri) {

    }

    @Override
    public void onReactonsUnitSelected(String unitName, String topicName) {
        getSupportFragmentManager().popBackStack("ReactionUnits",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.iupac_framelayout, new ReactionsUnitsFragments())
                .addToBackStack("ReactionUnits")
                .commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.iupac_framelayout, ReactionsTopicsFragment.newInstance(unitName ,topicName))
                .addToBackStack("ReactionUnits")
                .commit();
    }



    @Override
    public void onQuestionFinished(Uri uri) {

    }

    @Override
    public void onPracticeTopicsSelected(int topicNo) {
        topicNo --;

        final Cursor cursor = IUPAC_APPLICATION.practiceDatabase.rawQuery("SELECT * FROM  Topics ",null);
        cursor.moveToPosition(topicNo);
        final int qStart = cursor.getInt(cursor.getColumnIndex("Present_Q"));
        final int qEnd = cursor.getInt(cursor.getColumnIndex("Last_Q"));
        final int firstQ = cursor.getInt(cursor.getColumnIndex("First_Q"));
        cursor.close();
        if(qStart > qEnd){
            showResetDialog("Topics",firstQ,topicNo);
        }else{
            getSupportFragmentManager().popBackStack("ReactionUnits",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.iupac_framelayout,new PracticeTopicsFragment())
                    .addToBackStack("ReactionUnits")
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.iupac_framelayout, ReactionQuestionFragment.newInstance("x",topicNo ,"Questions","Practice"))
                    .addToBackStack("ReactionUnits")
                    .commit();
        }


    }

    private void showResetDialog(final String unit, final int first, final int exp) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Reset Questions")
                .setMessage("You have answered all questions . Do you want to reset and answer again ?.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(IUPAC_APPLICATION.practiceDatabase.isOpen()){
                            final String unitTable = unit;
                            int explNo = exp;
                            final int firstQ = first;
                            explNo++;
                            IUPAC_APPLICATION.practiceDatabase.execSQL("UPDATE `"+unitTable+"` SET `Present_Q`="+firstQ+"  WHERE Topic_No='"+explNo+"'");
                            getSupportFragmentManager().popBackStack("ReactionUnits",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.iupac_framelayout,new PracticeTopicsFragment())
                                    .addToBackStack("ReactionUnits")
                                    .commit();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.iupac_framelayout, ReactionQuestionFragment.newInstance("x",exp ,"Questions","Practice"))
                                    .addToBackStack("ReactionUnits")
                                    .commit();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }
    @Override
    public void onActivityResult(int reuestCode,int resultCode,Intent data){
        super.onActivityResult(reuestCode,resultCode,data);
        if(reuestCode == LEVEL_COMPLETE){
            if(resultCode == RESULT_OK){
                complete = 1;

            }
        }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() == 0 ){
            //this.finish();
            final BottomNavigationView navView = findViewById(R.id.nav_view);
            if(navView.getSelectedItemId() != R.id.navigation_home){
                navView.setSelectedItemId(R.id.navigation_home);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Exit ")
                        .setMessage("Do you want to exit ?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BottomNavigationActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                navView.setSelectedItemId(R.id.navigation_home);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.dash_menu_action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                String[] blacklist = new String[]{"com.nexm.iupacnomenclatureclassxii"};
                String link = "https://play.google.com/store/apps/details?id=com.nexm.iupacnomenclatureclassxii";
                shareIntent.setType("text/plain")
                        .putExtra(Intent.EXTRA_SUBJECT,"IUPAC Nomenclature")
                        .putExtra(Intent.EXTRA_TEXT,link);
                startActivity(Intent.createChooser(shareIntent, "Share via : "));
                // startActivity(generateCustomChooserIntent(shareIntent, blacklist));
                // startActivity(shareIntent);
                return true;
            case R.id.dash_menu_legal:
                Intent intent2 = new Intent(this.getApplicationContext(),AboutActivity.class);
                intent2.putExtra("selected","Legal");
                startActivity(intent2);
                return true;
            case R.id.dash_menu_rate:
                showRateDialogForRate(this);
                return true;
            case R.id.main_menu_about:
                Intent intent = new Intent(this,AboutActivity.class);
                intent.putExtra("selected","About");
                startActivity(intent);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
    private static void showRateDialogForRate(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Rate application")
                .setMessage("Please, rate the app at Google Play")
                .setPositiveButton("RATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (context != null) {
                            ////////////////////////////////
                            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            // To count with Play market back stack, After pressing back button,
                            // to taken back to our application, we need to add following flags to intent.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            }
                            try {
                                context.startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                            }


                        }
                    }
                })
                .setNegativeButton("Later", null);
        builder.show();

    }
}
