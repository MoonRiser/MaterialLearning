package com.example.xiewencai.material_learning.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Xie Wencai on 2017/10/14.
 */

public class Book extends DataSupport {

    private  int id;
    private  String author;
    private double price;
    private int pages;
    private  String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
