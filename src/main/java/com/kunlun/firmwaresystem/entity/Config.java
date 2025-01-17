package com.kunlun.firmwaresystem.entity;

public class Config {
    int id;
    String host;
    public Config(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
