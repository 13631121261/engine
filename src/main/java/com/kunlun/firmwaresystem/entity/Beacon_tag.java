package com.kunlun.firmwaresystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;

//这个适用于给转发类型的定位工卡
public class Beacon_tag {
    int id;
    int major;
    int minor;
    String project_key;
    String project_name;
    String user_key;
    double x;
    double y;
    int rssi;
    String name;
    double n;
    String customer_key;
    long create_time;
    int online;
    long last_time;
    String map_key;
    String map_name;
    public Beacon_tag() {

    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getOnline() {
        return online;
    }

    public void setMap_key(String map_key) {
        this.map_key = map_key;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public String getMap_key() {
        return map_key;
    }

    public String getMap_name() {
        return map_name;
    }

    public void setLast_time(Long last_time) {
        this.last_time = last_time;
    }

    public Long getLast_time() {
        return last_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getCreate_time() {
        return create_time;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public Beacon_tag(String name, int major,
                      int minor,
                      String project_key,
                      String user_key,
                      double x,
                      double y
            , String project_name, String customer_key) {
        this.major = major;
        this.minor = minor;
        this.project_key = project_key;
        this.user_key = user_key;
        this.x = x;
        this.y = y;
        this.project_name = project_name;
        this.name = name;
        this.customer_key=customer_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }


    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public Beacon_tag(int id, String name, int major,
                      int minor,
                      String project_key,
                      String user_key,
                      double x,
                      double y
            , String project_name,String customer_key) {
        this.id = id;
        this.major = major;
        this.minor = minor;
        this.project_key = project_key;
        this.user_key = user_key;
        this.x = x;
        this.y = y;
        this.project_name = project_name;
        this.name = name;
        this.customer_key=customer_key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void settRssi(int rssi) {
        this.rssi = rssi;
    }

    public int gettRssi() {
        return rssi;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_name() {
        return project_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public String getProject_key() {
        return project_key;
    }

    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getCustomer_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
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
}
