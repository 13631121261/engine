package com.kunlun.firmwaresystem.entity;

import java.io.Serializable;

public class Ble_firmware implements Serializable {
    String version;
    int id;

    long uploadtime;
    String user_key;
    String url;
    String project_key;
    String remake;

    public Ble_firmware(String url, String remake, long uploadtime,String user_key, String version, String project_key) {
        this.version = version;
        this.user_key = user_key;
        this.remake = remake;
        this.uploadtime = uploadtime;
        this.url = url;
        this.project_key=project_key;
    }
    public Ble_firmware(){

    }
    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getProject_key() {
        return project_key;
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

    public long getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(long uploadtime) {
        this.uploadtime = uploadtime;
    }


    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }





    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Ble_firmware{" +
                "version='" + version + '\'' +
                ", id=" + id +
                ", remake='" + remake + '\'' +
                ", uploadtime='" + uploadtime + '\'' +
                ", user_key='" + user_key + '\'' +
                ", url='" + url + '\'' +
                ", project_key='" + project_key + '\'' +
                '}';
    }
}
