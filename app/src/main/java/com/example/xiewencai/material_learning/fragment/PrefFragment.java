package com.example.xiewencai.material_learning.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.activity.MainActivity;
import com.example.xiewencai.material_learning.util.ActivityCollector;

import static com.example.xiewencai.material_learning.activity.MainActivity.BROADCAST_FLAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrefFragment extends PreferenceFragment  implements Preference.OnPreferenceChangeListener,Preference.OnPreferenceClickListener{

    Context context;
    SharedPreferences sharedPreferences;
    Preference userInFo;
    Preference logout;
    Preference themeDark;
    String nickname;
   // int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);
        userInFo=findPreference("pref_user_info");
        logout=findPreference("pref_user_logout");
        themeDark=findPreference("pref_theme_dark");


        context=getActivity();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        logout.setOnPreferenceClickListener(this);
        themeDark.setOnPreferenceChangeListener(this);

        String user=sharedPreferences.getString("account",null);
        nickname  =sharedPreferences.getString("pref_user_name",null);
        if(nickname==null){
            if(user==null){
                userInFo.setTitle("游客状态");
                userInFo.setSummary("请在登陆界面勾选 保存账号密码");
            }else {
                userInFo.setTitle("尊敬的"+user);
                userInFo.setSummary("用户，你好");}
        }else {
            userInFo.setTitle("尊敬的"+nickname);
            userInFo.setSummary("用户，你好");}
        }



    public boolean onPreferenceChange(Preference preference, Object newValue) {
    switch (preference.getKey()){
        case "pref_user_name":
            userInFo.setTitle("尊敬的"+"“"+nickname+"”");
            userInFo.setSummary("欢迎使用本应用");
            break;

        case"pref_theme_dark":
            if((boolean)newValue) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else  {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            ActivityCollector.activities.get(0).recreate();//返回栈的首节点应该是主活动，让它重启
            getActivity().recreate();

            //Toast.makeText(getActivity(), "都说了功能还在开发中"+newValue, Toast.LENGTH_SHORT).show();
            break;
    }

        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        switch (preference.getKey()){
            case "pref_user_logout":
                Intent intent=new Intent(BROADCAST_FLAG);
               context.sendBroadcast(intent);
                break;
        }
        return true;
    }


}
