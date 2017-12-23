package com.example.xiewencai.material_learning.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Xie Wencai on 2017/12/21.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }

}
