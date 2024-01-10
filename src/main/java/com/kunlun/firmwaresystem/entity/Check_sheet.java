package com.kunlun.firmwaresystem.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Check_sheet {
    int id;
    String name;
    int starttime;
    int stoptime;
    int linetime;
    String createtime;
    String userkey;
    String host;
    String sub,pub;
    String port;
    String defaultsub;
    String udp;
    public Check_sheet(){

    }
    public Check_sheet(  String name,
            int starttime,
            int stopttime,
            int linetime,
            String userkey){
        this.name=name;
        this.starttime=starttime;
        this.stoptime=stopttime;
        this.linetime=linetime;
        this.userkey=userkey;
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        this.createtime = sdf.format(date);


    }

    public String getUdp() {
        return udp;
    }

    public void setUdp(String udp) {
        this.udp = udp;
    }

    public String getDefaultsub() {
        return defaultsub;
    }

    public String getPort() {
        return port;
    }

    public String getSub() {
        return sub;
    }

    public String getPub() {
        return pub;
    }

    public String getHost() {
        return host;
    }

    public void setDefaultsub(String defaultsub) {
        this.defaultsub = defaultsub;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public void setStoptime(int stoptime) {
        this.stoptime = stoptime;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public int getStoptime() {
        return stoptime;
    }

    public void setStopttime(int stopttime) {
        this.stoptime = stopttime;
    }

    public int getLinetime() {
        return linetime;
    }

    public void setLinetime(int linetime) {
        this.linetime = linetime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }
}
