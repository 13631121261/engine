package com.kunlun.firmwaresystem.location_util.location;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Location implements Serializable {
    String tMac;
    double x;
    double y;

    String mac;
    double d;
  //  Area area;
    String name;
    int type = 3;

    /*//参数定位的三个beacon设备的mac
    String iMac1;
    String iMac2;
    String iMac3;*/
    public Location() {

    }

    public Location(Point point, String mac) {
        double x = 0, y = 0;
//        for (Point m : points) {
//            println("每个ID计算的坐标="+m.toString());
//            x=x+m.x;
//            y=y+m.y;
//        }
        DecimalFormat df1 = new DecimalFormat("#.00");



       /* x = point.x;
        y = point.y;*/
        this.x = Double.valueOf(df1.format(point.x));
        this.y = Double.valueOf(df1.format(point.y));
       /* this.x = x;
        this.y = y;*/
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
     /*   time = df.format(new Date());// new Date()为获取当前系统时间*/
        this.mac = mac;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

  /*  public void setArea(Area area) {
        this.area = area;
    }

    public Area getArea() {
        return area;
    }
*/
    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String gettMac() {
        return tMac;
    }

    public void settMac(String tMac) {
        this.tMac = tMac;
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
