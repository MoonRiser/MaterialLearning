package com.example.xiewencai.material_learning.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import org.litepal.LitePal;

/**
 * Created by Xie Wencai on 2017/11/30.
 */

public class MyApplication extends Application {

    private static Context context;
    SharedPreferences preferences;
  //  int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isNightMode=preferences.getBoolean("pref_theme_dark",false);
        if(isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base); MultiDex.install(this);
    }

}
