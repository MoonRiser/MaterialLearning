package com.example.xiewencai.material_learning.bean;

/**
 * Created by Xie Wencai on 2017/8/30.
 */

public class Msg {
    public static  final  int TYPE_RECEIVED=0;
    public static final int  TYPE_SEND = 1;
    private  String content;
    private  int type;

    public Msg(String content,int type){
        this.type=type;
        this.content=content;
    }

    public String  getContent(){
        return content;
    }

    public int getType(){
        return  type;
    }
}
