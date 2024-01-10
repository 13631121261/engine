package com.kunlun.firmwaresystem.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Moffline {
    //用于简单的记录网关，信标，工卡设备的在线离线
    int id;
    String address;
    int type;
    int online;
    long createtime;
    long lasttime;
    String userkey;
    String project_key;
    public Moffline(){

    }

    public Moffline( String address,
            int type,
            int online,
                     long lasttime,String userkey,String project_key){
        this.address=address;
        this.userkey=userkey;
        Date date = new Date();// 获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        this.createtime = System.currentTimeMillis()/1000;
        this.lasttime=lasttime;
        this.online=online;
        this.type=type;
        this.project_key=project_key;
    }

    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getProject_key() {
        return project_key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public long getLasttime() {
        return lasttime;
    }

    public void setLasttime(long lasttime) {
        this.lasttime = lasttime;
    }
}
