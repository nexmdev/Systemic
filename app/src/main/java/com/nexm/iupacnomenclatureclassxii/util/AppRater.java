package com.nexm.iupacnomenclatureclassxii.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.nexm.iupacnomenclatureclassxii.R;

import java.lang.ref.WeakReference;

public class AppRater {
    private final static String APP_TITLE = "IUPAC";
    private static String PACKAGE_NAME = "your_package_name";
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private static Activity activity;


    public  static void app_launched(Activity activity1) {
        //activity = activity1;

        WeakReference<Activity> weakactivity = new WeakReference<>(activity1);
        activity = weakactivity.get();
        //Configs.sendScreenView("Avaliando App", activity);

        PACKAGE_NAME = activity.getPackageName();

        prefs = activity.getSharedPreferences("apprater", Context.MODE_PRIVATE);
        /*if (prefs.getBoolean("dontshowagain", false))
            return;*/

        editor = prefs.edit();

        long EXTRA_DAYS = prefs.getLong("extra_days", 0);
        long EXTRA_LAUCHES = prefs.getLong("extra_launches", 0);

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        int DAYS_UNTIL_PROMPT = 3;
        int LAUNCHES_UNTIL_PROMPT = 3;
       if (launch_count >= (LAUNCHES_UNTIL_PROMPT + EXTRA_LAUCHES))
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000) + EXTRA_DAYS)
                showRateDialog();

        editor.apply();
        weakactivity.clear();
    }

    private static void showRateDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.setTitle("Enjoing " + APP_TITLE + "?");
        dialog.setCancelable(false);


        LinearLayout ll = new LinearLayout(activity);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(16, 16, 16, 16);
        ll.setBackgroundResource(R.color.gray);

        TextView tv = new TextView(activity);
        tv.setTextColor(ContextCompat.getColor(activity,  R.color.tab_background));
        tv.setText("*****Give us 5 star !*****");
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(32, 32, 32, 32);
        ll.addView(tv,0);

        Button b1 = new Button(activity);
        b1.setTextColor(ContextCompat.getColor(activity, android.R.color.black));
        b1.setBackgroundResource(R.drawable.brown_oval);

       // b1.setTextColor(android.R.color.white);
        b1.setText("Rate now !");
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Configs.sendHitEvents(Configs.APP_RATER, Configs.CATEGORIA_ANALYTICS, "Clique", "Avaliar", activity);

                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
                delayDays(45);
                delayLaunches(45);
                dialog.dismiss();

            }
        });


        Button b2 = new Button(activity);
        b2.setTextColor(ContextCompat.getColor(activity, android.R.color.black));
        b2.setBackgroundResource(R.drawable.brown_oval);
       // b2.setTextColor(Color.BLACK);
        b2.setText("Later");
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Configs.sendHitEvents(Configs.APP_RATER, Configs.CATEGORIA_ANALYTICS, "Clique", "Avaliar Mais Tarde", activity);

                delayDays(3);
                delayLaunches(3);
                dialog.dismiss();


            }
        });


      /*  Button b3 = new Button(activity);
        b3.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
        b3.setBackgroundResource(R.drawable.brown_oval);
       // b3.setTextColor(Color.BLACK);
        b3.setText("Don't show again");
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Configs.sendHitEvents(Configs.APP_RATER, Configs.CATEGORIA_ANALYTICS, "Clique", "Não Avaliar", activity);

                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }

                dialog.dismiss();

            }
        });
        ll.addView(b3, 1);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) b3.getLayoutParams();
        params.setMargins(5, 3, 5, 0);
        b3.setLayoutParams(params);*/

        ll.addView(b2,1);
        LinearLayout.LayoutParams params ;
        params = (LinearLayout.LayoutParams) b2.getLayoutParams();
        params.setMargins(16, 16, 16, 8);
        b2.setLayoutParams(params);

        ll.addView(b1, 2);
        params = (LinearLayout.LayoutParams) b1.getLayoutParams();
        params.setMargins(16, 8, 16, 16);
        b1.setLayoutParams(params);

        dialog.setContentView(ll);
        dialog.show();


    }

    private static void delayLaunches(int numberOfLaunches) {
        long extra_launches = prefs.getLong("extra_launches", 0) + numberOfLaunches;
        editor.putLong("extra_launches", extra_launches);
        editor.commit();
    }

    private static void delayDays(int numberOfDays) {
        Long extra_days = prefs.getLong("extra_days", 0) + (numberOfDays * 1000 * 60 * 60 * 24);
        editor.putLong("extra_days", extra_days);
        editor.commit();
    }
}

