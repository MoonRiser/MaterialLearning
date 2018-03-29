package com.example.xiewencai.material_learning.db;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by Xie Wencai on 2018/3/1.
 */

public class Note extends DataSupport{

    private int id;
    private String title;
    private String content;
    private String  date;
    private  Boolean isUpload;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String  getDate() {
        return date;
    }

    public void setDate(String  date) {
        this.date = date;
    }

    public Boolean getUpload() {
        return isUpload;
    }

    public void setUpload(Boolean upload) {
        isUpload = upload;
    }

}
