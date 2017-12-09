package com.example.xiewencai.material_learning.bean;

/**
 * Created by Xie Wencai on 2017/12/8.
 */

public class Album {
    private int albumImage;
    private String  albumName;
    private String albumDate;
    private String singer;


    public Album(int albumImage, String albumName, String albumDate, String singer) {
        this.albumImage = albumImage;
        this.albumName = albumName;
        this.albumDate = albumDate;
        this.singer = singer;
    }

    public int getAlbumImage() {
        return albumImage;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumDate() {
        return albumDate;
    }

    public String getSinger() {
        return singer;
    }
}

