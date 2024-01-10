package com.kunlun.firmwaresystem.location;

public class Data {

    int id;
    //Beacon mac
    String gMac;
    //标签mac
    String iMac;
    //标签信号
    int tRssi;
    //beacon位置
    double x, y;
    //Beacon 电量
    int iBattery;
    //标签电量
    int tCode;
    //时间
    String sTime;
    //时间戳
    long lTime;

    public Data() {
    }

    public Data(String gMac, String iMac, int rssi, double x, double y, int battery, int code) {

        this.gMac = gMac;
        this.iMac = iMac;
        this.tCode = code;
        this.tRssi = rssi;
        this.x = x;
        this.y = y;
        this.iBattery = battery;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getgMac() {
        return gMac;
    }

    public void setgMac(String gMac) {
        this.gMac = gMac;
    }


    public String getiMac() {
        return iMac;
    }

    public void setiMac(String iMac) {
        this.iMac = iMac;
    }

    public int gettRssi() {
        return tRssi;
    }

    public void settRssi(int tRssi) {
        this.tRssi = tRssi;
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

    public int getiBattery() {
        return iBattery;
    }

    public void setiBattery(int iBattery) {
        this.iBattery = iBattery;
    }

    public int gettCode() {
        return tCode;
    }

    public void settCode(int tCode) {
        this.tCode = tCode;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public long getlTime() {
        return lTime;
    }

    public void setlTime(long lTime) {
        this.lTime = lTime;
    }

}
