package com.kunlun.firmwaresystem.location_util.location;

public class Gateway_device {

    String gAddress;
    String dAddress;
    double x, y;
    int rssi;
    double n=2.67;
    int rssi_At_1m=-30;

    public Gateway_device() {

    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public Gateway_device(  String gAddress,
                            String dAddress ,   int rssi,double x,double y,double n,int rssi_At_1m){
        this.dAddress = dAddress;
        this.gAddress = gAddress;
        this.rssi = rssi;
        this.n=n;
        this.x=x;
        this.y=y;
        this.rssi_At_1m=rssi_At_1m;
    }

    public int getRssi_At_1m() {
        return rssi_At_1m;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public String getgAddress() {
        return gAddress;
    }

    public void setgAddress(String gAddress) {
        this.gAddress = gAddress;
    }

    public String getdAddress() {
        return dAddress;
    }

    public void setdAddress(String dAddress) {
        this.dAddress = dAddress;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }


}
