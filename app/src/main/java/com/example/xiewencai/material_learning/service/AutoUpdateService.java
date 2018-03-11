package com.example.xiewencai.material_learning.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.activity.WeatherActivity;
import com.example.xiewencai.material_learning.gson.Weather;
import com.example.xiewencai.material_learning.util.HttpUtil;
import com.example.xiewencai.material_learning.util.NotificationUtils;
import com.example.xiewencai.material_learning.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Xie Wencai on 2017/12/24.
 */

public class AutoUpdateService extends Service {


    private SharedPreferences preferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        updateWeather();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hours = Integer.parseInt(preferences.getString("weather_update_time", "0"));
        int times = hours * 60 * 60 * 1000;
        if (times != 0) {
           // Log.i("进入判断，times的值为", String.valueOf(times));
            long triggerAtTime = SystemClock.elapsedRealtime() + times;
            Intent i = new Intent(this, AutoUpdateService.class);
            PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
            manager.cancel(pi);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        } else {
            this.stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    private void updateWeather() {


        String weatherString = preferences.getString("weather", null);
        if (weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            assert weather != null;
            String weatherId = weather.basic.weatherId;
            String url = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=6e3f510f79334f7ea15bffc10168f420";
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather1 = Utility.handleWeatherResponse(responseText);
                    if (weather1 != null && "ok".equals(weather1.status)) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("weather", responseText);
                        editor.apply();
                        sentNotification(weather1.basic.cityName, weather1.now.more.info, weather1.now.temperature + "℃");
                    }
                }
            });


        }
    }


    private void sentNotification(String title, String weather, String temperature) {
        Intent intent = new Intent(this, WeatherActivity.class);
        String savedWeatherId = preferences.getString("weather_id", null);
        intent.putExtra("weather_id", savedWeatherId);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationUtils notificationUtils=new NotificationUtils(this);
        notificationUtils.sendNotification(title,weather+" "+temperature,R.mipmap.ic_launcher,pi,2);
        /*
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//获取系统服务：通知管理器
        Notification notification = new NotificationCompat.Builder(this).setContentTitle(title).setContentText(weather + "  " + temperature)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentIntent(pi).setDefaults(NotificationCompat.DEFAULT_ALL).setSmallIcon(R.mipmap.ic_launcher).
                        setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).build();
        notificationManager.notify(3, notification);
        */

    }


}
