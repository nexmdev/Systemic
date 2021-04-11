package com.nexm.iupacnomenclatureclassxii;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.nexm.iupacnomenclatureclassxii.fragments.ExplanationFragment;
import com.nexm.iupacnomenclatureclassxii.fragments.PracticeFragment;
import com.nexm.iupacnomenclatureclassxii.fragments.ReactionQuestionFragment;
import com.nexm.iupacnomenclatureclassxii.fragments.TestFragment;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity implements

            PracticeFragment.OnFragmentInteractionListener,
            TestFragment.OnFragmentInteractionListener,
            ExplanationFragment.OnFragmentInteractionListener,
            ReactionQuestionFragment.OnFragmentInteractionListener{


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private int current_status,next_status,startQ,noQ;
    private InterstitialAd mInterstitialAd;
    private String callerActivity,unit,Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        boolean isTab = getResources().getBoolean(R.bool.isTab);
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {

            if(!isTab)this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6219444241621852/2403159767");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        callerActivity = getIntent().getStringExtra("Caller");
        SectionsPagerAdapter mSectionsPagerAdapter = null;
        if(callerActivity.matches("Iupac")){
            String topic = getIntent().getStringExtra("TOPIC");
            String rules_table = getIntent().getStringExtra("TABLE");
            int rule_no = getIntent().getIntExtra("RULE_NO", 1);
            current_status = getIntent().getIntExtra("CURRENT_STATUS",1);
            next_status = getIntent().getIntExtra("NEXT_STATUS",0);
            startQ = getIntent().getIntExtra("START_QUESTION",0);
            noQ = getIntent().getIntExtra("NO_OF_QUESTIONS",0);

            getSupportActionBar().setTitle(rules_table + " - "+ rule_no);

            mSectionsPagerAdapter= new SectionsPagerAdapter(getSupportFragmentManager(), rules_table, rule_no, topic,"x");
        }else{
            String unitTable = getIntent().getStringExtra("UnitTableName");
            String eTableID = getIntent().getStringExtra("ETable");
            String qTableID = getIntent().getStringExtra("QTable");
            Title = getIntent().getStringExtra("Title");
            unit = getIntent().getStringExtra("Unit");
            int explNo =  getIntent().getIntExtra("ExplanationNo",1);
            getSupportActionBar().setTitle(unitTable);
            mSectionsPagerAdapter= new SectionsPagerAdapter(getSupportFragmentManager(), unitTable, explNo, eTableID,qTableID);
        }



        mViewPager = findViewById(R.id.container);
        tabLayout = findViewById(R.id.details_tab_layout);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_lightbulb_outline_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_school_black_24dp);

      mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          @Override
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

          }

          @Override
          public void onPageSelected(int position) {
              tabLayout.getTabAt(position).select();
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                  for (int i = 0; i < tabLayout.getTabCount(); i++) {
                      Drawable tabicon = tabLayout.getTabAt(i).getIcon();
                      Drawable wrappedDrawable = DrawableCompat.wrap(tabicon);
                      DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.tab_default));
                      //tabLayout.getTabAt(i).getIcon().setTint(getResources().getColor(R.color.tab_default));
                  }
                  Drawable tabicon = tabLayout.getTabAt(position).getIcon();
                  Drawable wrappedDrawable = DrawableCompat.wrap(tabicon);
                  DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.colorAccent));
              }
          }

          @Override
          public void onPageScrollStateChanged(int state) {

          }
      });
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,AboutActivity.class);
            intent.putExtra("selected","About");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTestFragmentSelection(Uri uri) {

    }

    @Override
    public void onPracticeSelection() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void onExplanationContinue() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void onQuestionFinished(Uri uri) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private String rules_table_name = "",qTableID = "";
        private String tests_table_name = "";
        private final int rule_no;

        SectionsPagerAdapter(FragmentManager fm, String rules_table_name, int r_no, String test_table_name,String qTable) {
            super(fm);

            if(callerActivity.matches("Iupac")){
                this.tests_table_name = test_table_name+"_Tests";
                this.rules_table_name = rules_table_name;
                this.qTableID = qTable;
            }else{
                this.tests_table_name = test_table_name;
                this.rules_table_name = rules_table_name;
                this.qTableID = qTable;
            }

            rule_no = r_no;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch (position){

                case 0:
                    if(callerActivity.matches("Iupac")){
                        fragment = PracticeFragment.newInstance(rules_table_name,rule_no);
                    }else{
                        fragment = ExplanationFragment.newInstance(tests_table_name,qTableID ,unit,Title );
                    }
                    break;
                case 1:

                    if(callerActivity.matches("Iupac")){
                        fragment = TestFragment.newInstance(rules_table_name,rule_no,current_status ,next_status,tests_table_name,startQ ,noQ );
                    }else{
                        if(unit.matches("Haloalkane")){
                            fragment = ReactionQuestionFragment.newInstance(rules_table_name,rule_no,qTableID,unit);
                        }else{
                           // Cursor cursor = IUPAC_APPLICATION.reaction_database.rawQuery("SELECT * FROM  " + rules_table_name + " WHERE number='"+rule_no+"'",null);
                            fragment = ReactionQuestionFragment.newInstance(rules_table_name,rule_no,qTableID,unit);
                        }

                    }
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

    }
}
