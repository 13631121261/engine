package com.kunlun.firmwaresystem.location_util.backup;

import java.util.ArrayList;

import static com.kunlun.firmwaresystem.NewSystemApplication.println;

public class Point {
    public String mac;
    String map_key;
     ArrayList<Gateway_device> list;
     int a;
     double n;
     double d;
    public Point(double x, double y, String mac,String map_key) {
        this.x = x;
        this.y = y;
        this.mac = mac;
        this.map_key=map_key;
       // println("点 X="+x+ " Y="+y);
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getD() {
        return d;
    }

    public void setA(int a) {
        this.a = a;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public int getA() {
        return a;
    }

    public void setList(ArrayList<Gateway_device> list) {
        this.list = list;
    }

    public ArrayList<Gateway_device> getList() {
        return list;
    }

    public void setMap_key(String map_key) {
        this.map_key = map_key;
    }

    public String getMap_key() {
        return map_key;
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
