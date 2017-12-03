package com.example.xiewencai.material_learning.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import org.litepal.LitePal;

/**
 * Created by Xie Wencai on 2017/11/30.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base); MultiDex.install(this);
    }

}
