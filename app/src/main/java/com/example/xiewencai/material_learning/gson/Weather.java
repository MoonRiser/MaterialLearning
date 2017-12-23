package com.example.xiewencai.material_learning.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Xie Wencai on 2017/12/21.
 */

public class Weather {

    public String status;
    public Basic basic;
    public Now now;
    public AQI aqi;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
