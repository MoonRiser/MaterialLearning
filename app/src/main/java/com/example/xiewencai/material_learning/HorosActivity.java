package com.example.xiewencai.material_learning;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class HorosActivity extends BaseActivity {

    public  static  final String  EN_NAME ="eNname";
    public  static  final String  CN_NAME ="cNname";
    public static  final  String   IMAGE_ID="imageId";
     Bundle bundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将12个星座故事传入bundle键值对，方便取用
        bundle.putInt("双鱼座",R.string.Pisces);bundle.putInt("巨蟹座",R.string.Cancro);bundle.putInt("天蝎座",R.string.Scorpio);bundle.putInt("水瓶座",R.string.Aquarius);
        bundle.putInt("双子座",R.string.Gemini);bundle.putInt("天秤座",R.string.Libra);bundle.putInt("白羊座",R.string.Aries);bundle.putInt("狮子座",R.string.Leo);
        bundle.putInt("射手座",R.string.Sagit);bundle.putInt("金牛座",R.string.Taurus);bundle.putInt("处女座",R.string.Virgo);bundle.putInt("摩羯座",R.string.Capricorn);

        //获取各视图的对象，准备
        setContentView(R.layout.activity_horos);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView horoImage=(ImageView) findViewById(R.id.horo_image2);
        TextView horoStory = (TextView) findViewById(R.id.horo_story);
//从intent中取出图片id和字符
        Intent intent= getIntent();
        String horoName=  intent.getStringExtra(EN_NAME);
        int  horoImageId = intent.getIntExtra(IMAGE_ID,0);
        String cnName= intent.getStringExtra(CN_NAME);

//设置工具栏 返回键
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //加载图片文字
        collapsingToolbarLayout.setTitle(horoName);
        Glide.with(this).load(horoImageId).into(horoImage);
        horoStory.setText(bundle.getInt(cnName));

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case  android.R.id.home:
                finishAfterTransition();
            //    finish();
                return  true;
        }
        return  super.onOptionsItemSelected(item);
    }


}
