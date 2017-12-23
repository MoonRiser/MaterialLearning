package com.example.xiewencai.material_learning.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Xie Wencai on 2017/12/18.
 */

public class HttpUtil {//封装OKHTTP网络连接请求
    public static void sendOkHttpRequest(String adress,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(adress).build();
        client.newCall(request).enqueue(callback);
    }

}
