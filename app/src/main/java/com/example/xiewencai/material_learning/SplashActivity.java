package com.example.xiewencai.material_learning;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Random;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class SplashActivity extends BaseActivity {

    private  int[] splashs = {R.drawable.splash00,R.drawable.splash01,R.drawable.splash02,R.drawable.splash03};
    final int TIME=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView=(ImageView) findViewById(R.id.splashImage);


        //随机选择一张图片作为启动图
        Random random=new Random();
        int index = random.nextInt(4);
        Glide.with(this).load(splashs[index]).into(imageView);

        //用于隐藏导航栏和状态栏的标识符
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(option);


        //利用handler的postDelayed方法延时开启子线程
        new android.os.Handler().postDelayed(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     if(!isLoginBefore()){
                                                         Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                                                         startActivity(intent);
                                                         SplashActivity.this.finish();
                                                     }else {
                                                         Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                                                         startActivity(intent);
                                                         SplashActivity.this.finish();
                                                     }


                                                 }
                                             }
                , TIME);

    }

    private Boolean isLoginBefore(){
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            return true;
        }else{
            return false;
        }
    }
}
