package com.kunlun.firmwaresystem.location_util.backup;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Location implements Serializable {
    String tMac;
    double x;
    double y;
    String map_key;
    List<Gateway_device> dataMap;
    String mac;
    double d;
  //  Area area;
    String name;
    int type = 3;
    String gAddress;
    /*//参数定位的三个beacon设备的mac
    String iMac1;
    String iMac2;
    String iMac3;*/
    public Location() {

    }

    public void setgAddress(String gAddress) {
        this.gAddress = gAddress;
    }

    public String getgAddress() {
        return gAddress;
    }

    public void setDataMap(List<Gateway_device> dataMap) {
        this.dataMap = dataMap;
    }

    public List<Gateway_device> getDataMap() {
        return dataMap;
    }

    public Location(Point point, String mac, String map_key) {
        this.map_key=map_key;
       // println("point.getX())"+point.getX());
        DecimalFormat df1 = new DecimalFormat("#.00");
        this.x = Double.parseDouble(df1.format(point.x));
        this.y = Double.parseDouble(df1.format(point.y));
       // println("point.getX())"+this.y);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        this.mac = mac;
        this.gAddress=point.getgAddress();
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

    public void setMap_key(String map_key) {
        this.map_key = map_key;
    }

    public String getMap_key() {
        return map_key;
    }

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
        DecimalFormat df1 = new DecimalFormat("#.00");
        this.x = Double.parseDouble(df1.format(x));

    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        DecimalFormat df1 = new DecimalFormat("#.00");
        this.y = Double.parseDouble(df1.format(y));
    }

    @Override
    public String toString() {
        return "Location{" +
                "tMac='" + tMac + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", map_key='" + map_key + '\'' +
                ", dataMap=" + dataMap +
                ", mac='" + mac + '\'' +
                ", d=" + d +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
