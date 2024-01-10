package com.kunlun.firmwaresystem.entity;

import java.io.Serializable;

public class Ble_firmware implements Serializable {
    String version;
    int id;
    String remake;
    String uploadtime;

    String user_key;
    String company_name;
    String author;
    String url;
    String customer_key;


    public Ble_firmware(String url, String remake, String uploadtime, String company_name, String user_key, String version, String customer_key) {
        this.version = version;
        this.user_key = user_key;
        this.company_name = company_name;
        this.remake = remake;
        this.uploadtime = uploadtime;
        this.url = url;
        this.customer_key=customer_key;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }


    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
