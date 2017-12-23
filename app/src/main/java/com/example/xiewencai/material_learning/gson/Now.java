package com.example.xiewencai.material_learning.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Xie Wencai on 2017/12/21.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }

}
