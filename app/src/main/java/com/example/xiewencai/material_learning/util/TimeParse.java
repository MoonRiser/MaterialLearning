package com.example.xiewencai.material_learning.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Xie Wencai on 2018/3/1.
 */

public class TimeParse {//通过Date返回时间日期信息字符串
    public static String  getTimeByDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        return  simpleDateFormat.format(date);

    }


    public static Date getCurrentDate(){
        Date date = new Date(System.currentTimeMillis());
        return date;
    }
    //获取当前时间
 //
  //time1.setText("Date获取当前日期时间"+simpleDateFormat.format(date));


}
