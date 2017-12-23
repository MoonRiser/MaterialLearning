package com.example.xiewencai.material_learning.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.gson.Forecast;
import com.example.xiewencai.material_learning.gson.Weather;
import com.example.xiewencai.material_learning.util.HttpUtil;
import com.example.xiewencai.material_learning.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity {


    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private LinearLayout titleLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    private SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;
    private Button button;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusTransparent();
        setContentView(R.layout.activity_weather);

        //初始化各控件
        bingPicImg=findViewById(R.id.bing_pic_img);
        weatherLayout=findViewById(R.id.weather_layout);
        titleCity=findViewById(R.id.title_city);
        titleUpdateTime=findViewById(R.id.title_update_time);
        degreeText=findViewById(R.id.degree_text);
        weatherInfoText=findViewById(R.id.weather_info_text);
        forecastLayout=findViewById(R.id.forecast_layout);
        aqiText=findViewById(R.id.aqi_text);
        pm25Text=findViewById(R.id.pm25_text);
        comfortText=findViewById(R.id.comfort_text);
        carWashText=findViewById(R.id.car_wash_text);
        sportText=findViewById(R.id.sport_text);
        swipeRefresh=findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(R.color.colorPrimary);
        button=findViewById(R.id.back_button);
        titleLayout=findViewById(R.id.title_layout);
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);

        titleLayout.setTranslationY(getStatusBarHeight(this));


        String bingPic=preferences.getString("bing_pic",null);
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }


        mWeatherId=getIntent().getStringExtra("weather_id");
        String weatherString = preferences.getString("weather",null);
        String savedWeatherId=preferences.getString("weather_id",null);
        if(mWeatherId==savedWeatherId){
            Weather weather= Utility.handleWeatherResponse(weatherString);
            mWeatherId=weather.basic.weatherId;
            showWeatherInfo(weather);//有缓存时，读取缓存中的数据
        }else {
        //    mWeatherId=getIntent().getStringExtra("weather_id");
           // String weatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                              @Override
                                              public void onRefresh() {
                                                  requestWeather(mWeatherId);
                                              }
                                          }
        );

        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          WeatherActivity.this.finish();
                                      }
                                  }
        );

    }


    public void requestWeather(final String weatherId){

        String url="http://guolin.tech/api/weather?cityid="+weatherId+"&key=6e3f510f79334f7ea15bffc10168f420";
        Log.i("天气url",url);
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      showToast("获取天气信息失败OnFailure");
                                      swipeRefresh.setRefreshing(false);
                                  }
                              }
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      if(weather!=null &&"ok".equals(weather.status)){
                                          SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                                          editor.putString("weather",responseText);
                                          editor.putString("weather_id",weatherId);
                                          editor.apply();
                                          showWeatherInfo(weather);
                                      }else {
                                          showToast("获取天气信息失败onResponse");
                                      }
                                      swipeRefresh.setRefreshing(false);
                                  }
                              }
                );
            }
        });
        loadBingPic();

    }

    public void showWeatherInfo(Weather weather){

        String cityName=weather.basic.cityName;
      //  Log.i("WeatherActivity",weather.basic.update.updateTime);
        String[] temp=weather.basic.update.updateTime.split("\\s");
        String updateTime=temp[1];
  //      Log.i("updateTime",updateTime);
        String degree=weather.now.temperature+"℃";
        String weatherInfo=weather.now.more.info;

        titleUpdateTime.setText(updateTime);
        titleCity.setText(cityName);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText= view.findViewById(R.id.date_text);
            TextView infoText= view.findViewById(R.id.info_text);
            TextView maxText= view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort="舒适度:"+weather.suggestion.comfort.info;
        String carWash="洗车指数:"+weather.suggestion.carWash.info;
        String sport="运动建议:"+weather.suggestion.sport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

    }

    private void loadBingPic(){
        String requestBIngPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBIngPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                                  }
                              }
                );

            }
        });
    }

    public void setStatusTransparent(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);


    }


}
