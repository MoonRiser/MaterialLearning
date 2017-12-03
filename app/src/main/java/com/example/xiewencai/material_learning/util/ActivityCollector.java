package com.example.xiewencai.material_learning.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xie Wencai on 2017/10/7.
 */

public class  ActivityCollector{

    public  static List<Activity> activities = new ArrayList<>();

    public  static  void addActivity(Activity activity){
        activities.add(activity);
    }

    public  static  void removeActivity(Activity activity){
        activities.remove(activity);
    }
    // 把列表中的所有活动都结束
    public  static void finishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing())
                activity.finish();
        }
    }

}