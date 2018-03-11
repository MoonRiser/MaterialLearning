package com.example.xiewencai.material_learning.db;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by Xie Wencai on 2018/3/1.
 */

public class NoteRemote extends BmobObject {

    private String identify;
    private String title;
    private String content;
    private Date date;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }


}
