package com.kunlun.firmwaresystem.location_util.location;

public class Point {
    public String mac;

    public Point(double x, double y, String mac) {
        this.x = x;
        this.y = y;
        this.mac = mac;
    }

    public Point() {

    }

    //点坐标
    public double x = 0;                //x轴
    public double y = 0; //y轴

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

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
