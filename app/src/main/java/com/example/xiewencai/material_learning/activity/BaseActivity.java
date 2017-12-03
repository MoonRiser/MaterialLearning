package com.example.xiewencai.material_learning.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.xiewencai.material_learning.util.ActivityCollector;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;


/**
 * Created by Xie Wencai on 2017/10/7.
 */

public class BaseActivity  extends AppCompatActivity{

    private ForceOfflineReceiver receiver;
    private Toast toast;

    protected  void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        Bmob.initialize(this,"d214b4eddf59772a1d00b7383c093d64");//初始化bmob
        ActivityCollector.addActivity(this);
    }

    //自定义Toast方法，
    public void showToast(String content){
        if(toast==null){
       toast= Toast.makeText(this, content, Toast.LENGTH_SHORT);
        }else {
            toast.setText(content);
        }
        toast.show();
    }

    //在活动生命周期的onResume()可见时动态注册广播，到onPause()不可见时取消广播注册

    protected void  onResume(){
        super.onResume();;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MainActivity.BROADCAST_FLAG);
        receiver=new ForceOfflineReceiver();
        registerReceiver(receiver,intentFilter);
    }

    protected void onPause(){
        super.onPause();
        if(receiver!= null) {
            unregisterReceiver(receiver);
            receiver=null;
            }
        }

    protected  void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);

    }




    class  ForceOfflineReceiver extends BroadcastReceiver{
        public void onReceive(final Context context, Intent intent){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("FBI WARNING(翻车现场)");
            builder.setMessage("You are force to be offline, please login again(你不是真正的老司机，请重考驾照)");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogInterface,int which){
                    ActivityCollector.finishAll();
                    BmobUser.logOut();
                    Intent intent=new Intent(context,LoginActivity.class);
                    startActivity(intent);
                }
            });
            builder.show();
        }

    }


}
