package com.kunlun.firmwaresystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.ArrayList;

//欧孚的CAT工卡
public class FWordcard {
        int id;
        int bt;
       double x;
       double y;
        double latitude;
        double longitude;
        int location_type;
        String customer_key;
        String user_key;
        String project_key;
        long create_time;
        long lastTime;
        String mac;
        @TableField(exist=false)
        ArrayList<Beacon_tag> beaconTags;
        @TableField(exist=false)
        String  map_key;

        int online;
        @TableField(exist=false)
        int sos;

    public void setMap_key(String map_key) {
        this.map_key = map_key;
    }

    public String getMap_key() {
        return map_key;
    }

    public void setSos(int sos) {
        this.sos = sos;
    }

    public int getSos() {
        return sos;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getOnline() {
        return online;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public void setMac(String imei) {
        this.mac = imei;
    }

    public String getMac() {
        return mac;
    }

    public int getBt() {
        return bt;
    }

    public void setBt(int bt) {
        this.bt = bt;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getLocation_type() {
        return location_type;
    }

    public void setLocation_type(int location_type) {
        this.location_type = location_type;
    }
    public String getCustomer_key() {
        return customer_key;
    }
    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getProject_key() {
        return project_key;
    }
    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public void setLastTime(long last_time) {
        this.lastTime = last_time;
    }

    public long getLastTime() {
        return lastTime;
    }

    @Override
    public String toString() {
        return "FWordcard{" +
                ", online=" + online +
                ",id=" + id +
                ", bt=" + bt +
                ", x=" + x +
                ", y=" + y +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", location_type=" + location_type +
                ", customer_key='" + customer_key + '\'' +
                ", user_key='" + user_key + '\'' +
                ", project_key='" + project_key + '\'' +
                ", create_time=" + create_time +
                ", last_time=" + lastTime +
                ", imei='" + mac + '\'' +
                ", beaconTags=" + beaconTags +
                ", map_key='" + map_key + '\'' +
                ", sos=" + sos +
                '}';
    }
}

