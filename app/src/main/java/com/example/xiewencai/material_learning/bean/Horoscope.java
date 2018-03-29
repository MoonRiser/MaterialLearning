package com.example.xiewencai.material_learning.bean;

/**
 * Created by Xie Wencai on 2017/8/22.
 */

public class Horoscope {
    //字段属性
    private String eNname;
    private String cNname;
    private int imageId;
    private  String dateHoros;
    private int circleImg;

    public Horoscope(String eNname, String cNname, int imageId, String dateHoros, int circleImg) {
        this.eNname = eNname;
        this.cNname = cNname;
        this.imageId = imageId;
        this.dateHoros = dateHoros;
        this.circleImg = circleImg;
    }

    public String getDateHoros() {

        return dateHoros;
    }

    public void setDateHoros(String dateHoros) {
        this.dateHoros = dateHoros;
    }

    public int getCircleImg() {
        return circleImg;
    }

    public void setCircleImg(int circleImg) {
        this.circleImg = circleImg;
    }

    //构造方法
    public  Horoscope(String eNname,String cNname,int  imageId){
        this.eNname=eNname;
        this.cNname=cNname;
        this.imageId=imageId;
    }

    public  String geteNname(){
        return eNname;
    }

    public String getcNname(){
        return  cNname;
    }

    public  int getImageId(){
        return imageId;
    }
}
