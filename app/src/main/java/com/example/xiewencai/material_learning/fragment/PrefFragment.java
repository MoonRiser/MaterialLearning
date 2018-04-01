package com.example.xiewencai.material_learning.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.Toast;

import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.activity.MainActivity;
import com.example.xiewencai.material_learning.db.Note;
import com.example.xiewencai.material_learning.service.AutoUpdateService;
import com.example.xiewencai.material_learning.util.ActivityCollector;

import org.litepal.crud.DataSupport;

import static com.example.xiewencai.material_learning.activity.MainActivity.BROADCAST_FLAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrefFragment extends PreferenceFragment  implements Preference.OnPreferenceChangeListener,Preference.OnPreferenceClickListener{

    Context context;
    SharedPreferences sharedPreferences;
    Preference userInFo;
    Preference editUser;
    Preference logout;
    Preference themeDark;
    Preference weatherUpdate;
    String nickname;
   // int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);
        userInFo=findPreference("pref_user_info");
        logout=findPreference("pref_user_logout");
        themeDark=findPreference("pref_theme_dark");
        editUser=findPreference("pref_user_name");
        weatherUpdate=findPreference("weather_update_time");


        context=getActivity();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        editUser.setOnPreferenceChangeListener(this);
        weatherUpdate.setOnPreferenceChangeListener(this);
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
            userInFo.setTitle("尊敬的"+"“"+ newValue +"”");
            userInFo.setSummary("欢迎使用本应用");
            break;

        case"pref_theme_dark":
            if((boolean)newValue) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else  {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            Activity activity= ActivityCollector.activities.get(0);//返回栈的首节点应该是主活动，让它重启
            if(activity instanceof MainActivity){
                activity.finish();
            }
            getActivity().recreate();
            //Toast.makeText(getActivity(), "都说了功能还在开发中"+newValue, Toast.LENGTH_SHORT).show();
            break;

        case "weather_update_time":
                 int flag=  Integer.parseInt(String.valueOf(newValue));
            if(flag!=0){
                Toast.makeText(context, "天气更新服务已启动", Toast.LENGTH_SHORT).show();
               // Log.i("测试Preference值改变回调",(String)newValue);
                Intent intent=new Intent(getActivity(), AutoUpdateService.class);
                getActivity().startService(intent);
            }


    }

        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        switch (preference.getKey()){
            case "pref_user_logout":

               final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
               builder.setTitle("退出当前账号");
               builder.setCancelable(false);
               builder.setMessage("回到登陆界面，但是不清除本地账号密码数据");
               builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Intent intent=new Intent(BROADCAST_FLAG);
                       DataSupport.deleteAll(Note.class);
                       context.sendBroadcast(intent);
                   }
               });
               builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });
                builder.show();
                break;
        }
        return true;
    }


}
