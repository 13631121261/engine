package com.kunlun.firmwaresystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;

public class Tag {
    int type;
    String mac;
    double bt;
    String user_key;
    @TableField(exist = false)
    String gateway_address;
    @TableField(exist = false)
    int online;

    long lastTime;
    @TableField(exist = false)
    int rssi;
    public long getLastTime() {
        return lastTime;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getOnline() {
        return online;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public double getBt() {
        return bt;
    }

    public void setBt(double bt) {
        this.bt = bt;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getRssi() {
        return rssi;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public String getGateway_address() {
        return gateway_address;
    }

    public void setGateway_address(String gateway) {
        this.gateway_address = gateway;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "type=" + type +
                ", mac='" + mac + '\'' +
                ", bt=" + bt +
                ", user_key='" + user_key + '\'' +
                ", gateway_address='" + gateway_address + '\'' +
                ", online=" + online +
                ", lastTime=" + lastTime +
                ", rssi=" + rssi +
                '}';
    }
}
