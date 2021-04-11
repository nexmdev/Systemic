package com.nexm.iupacnomenclatureclassxii;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nexm.iupacnomenclatureclassxii.fragments.AboutFragment;
import com.nexm.iupacnomenclatureclassxii.fragments.LegalFragment;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        boolean isTab = getResources().getBoolean(R.bool.isTab);
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {

            if(!isTab)this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        String topic = getIntent().getStringExtra("selected");

        //getSupportActionBar().setTitle(topic);
        if(topic.matches("Legal")){
            LegalFragment fragment = new LegalFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.about_frame,fragment)
                    .commit();
        }else{
            AboutFragment fragment = new AboutFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.about_frame,fragment)
                    .commit();
        }

    }
}
