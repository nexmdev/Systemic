package com.nexm.iupacnomenclatureclassxii;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        boolean isTab = getResources().getBoolean(R.bool.isTab);
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {

            if(!isTab)this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getSupportActionBar().hide();
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        while (IUPAC_APPLICATION.practiceDatabase==null){
            progressBar.setVisibility(View.VISIBLE);
        }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(getApplicationContext(),BottomNavigationActivity.class);
                    startActivity(i);
                    finish();
                }
            },2000);
    }
}
