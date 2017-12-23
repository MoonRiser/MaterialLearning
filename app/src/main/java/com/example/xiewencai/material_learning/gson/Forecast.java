package com.example.xiewencai.material_learning.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Xie Wencai on 2017/12/21.
 */

public class Forecast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more ;

    public class Temperature {
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }

}
