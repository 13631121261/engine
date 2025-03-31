package com.kunlun.firmwaresystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;

public class Bracelet {
    int id;
    String   mac;
    int heart_rate;
    int        steps;
    int spo;
    String      temp;
    int type;
    int online;
    String    user_key;
    String project_key;
    String map_key;
    int is_bind;
    String    idcard;
    String person_name;
    int bt;
    int sos;
    long     create_time;
    long last_time;
    String customer_key;
    int rssi=0;
    int calorie=0;
    @TableField(exist = false)
    double x;
    @TableField(exist = false)
    double y;

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public int getCalorie() {
        return calorie;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getRssi() {
        return rssi;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(int heart_rate) {
        this.heart_rate = heart_rate;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getSpo() {
        return spo;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getOnline() {
        return online;
    }

    public void setSpo(int spo) {
        this.spo = spo;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp() {
        return temp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getMap_key() {
        return map_key;
    }

    public void setMap_key(String map_key) {
        this.map_key = map_key;
    }

    public int getIs_bind() {
        return is_bind;
    }

    public void setIs_bind(int is_bind) {
        this.is_bind = is_bind;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public int getBt() {
        return bt;
    }

    public void setBt(int bt) {
        this.bt = bt;
    }

    public int getSos() {
        return sos;
    }

    public void setSos(int sos) {
        this.sos = sos;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getLast_time() {
        return last_time;
    }

    public void setLast_time(long last_time) {
        this.last_time = last_time;
    }
    public Bracelet(String mac){
        this.mac=mac;
    }

    @Override
    public String toString() {
        return "Bracelet{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", heart_rate=" + heart_rate +
                ", steps=" + steps +
                ", spo=" + spo +
                ", temp='" + temp + '\'' +
                ", type=" + type +
                ", online=" + online +
                ", user_key='" + user_key + '\'' +
                ", project_key='" + project_key + '\'' +
                ", map_key='" + map_key + '\'' +
                ", is_bind=" + is_bind +
                ", idcard='" + idcard + '\'' +
                ", person_name='" + person_name + '\'' +
                ", bt=" + bt +
                ", sos=" + sos +
                ", create_time=" + create_time +
                ", last_time=" + last_time +
                ", customer_key='" + customer_key + '\'' +
                ", rssi=" + rssi +
                ", calorie=" + calorie +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
