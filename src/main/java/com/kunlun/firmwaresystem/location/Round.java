package com.kunlun.firmwaresystem.location;

public class Round {
    public double r, x, y;
    public String mac;

    public Round(double x, double y, double r, String mac) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.mac = mac;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
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
