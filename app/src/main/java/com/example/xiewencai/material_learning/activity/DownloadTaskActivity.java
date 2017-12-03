package com.example.xiewencai.material_learning.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.service.DownloadService;

public class DownloadTaskActivity extends BaseActivity implements View.OnClickListener{

    private DownloadService.DownloadBinder downloadBinder;
    EditText inputUr;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {//绑定服务和活动的时候回调该方法
        downloadBinder=(DownloadService.DownloadBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_task);
        inputUr=findViewById(R.id.editText_url);
        Button start=findViewById(R.id.bt_start);
        Button cancel=findViewById(R.id.bt_cancel);
        Button pause=findViewById(R.id.bt_pause);

      //  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        inputUr.setText("http://speed.myzone.cn/WindowsXP_SP2.exe");

        start.setOnClickListener(this);
        cancel.setOnClickListener(this);
        pause.setOnClickListener(this);
        Intent intent=new Intent(this,DownloadService.class);
        startService(intent);//启动下载服务
        bindService(intent,connection,BIND_AUTO_CREATE);//绑定下载服务

    }

    @Override
    public void onClick(View v) {
        if(downloadBinder==null){
            return;
        }
        switch (v.getId()){
            case R.id.bt_start:
                beginDownload();break;
            case R.id.bt_pause:
                downloadBinder.pauseDownload();break;
            case R.id.bt_cancel:
                inputUr.setText("");
                downloadBinder.cancelDownload();break;
                default:
                    break;

        }


    }

    private void beginDownload(){
        String urlString=inputUr.getText().toString();
        if(urlString.length()!=0){
            downloadBinder.startDownload(urlString);
        }else {
            showToast("网址输入为空，请重试");
            return;
        }

    }

    protected  void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

}
