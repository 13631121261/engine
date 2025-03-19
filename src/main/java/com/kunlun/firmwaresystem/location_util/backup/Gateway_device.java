package com.kunlun.firmwaresystem.location_util.backup;

public class Gateway_device {

    String gAddress;
    String dAddress;
    double x, y;
    int rssi;
   /* double n=2.67;*/
   double n=3.34;
   int a_rssi;
    long time;
    String map_key;
    double d;
    double z;
    int used;
    int l_type;
    public Gateway_device() {

    }

    public void setL_type(int l_type) {
        this.l_type = l_type;
    }

    public int getL_type() {
        return l_type;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getUsed() {
        return used;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getZ() {
        return z;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getD() {
        return d;
    }

    public void setA_rssi(int a_rssi) {
        this.a_rssi = a_rssi;
    }

    public int getA_rssi() {
        return a_rssi;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public Gateway_device(  String gAddress,
                            String dAddress ,   int rssi,double x,double y,long time,String map_key,int arssi,double n,double z,int used){
        this.dAddress = dAddress;
        this.map_key=map_key;
        this.gAddress = gAddress;
        this.rssi = rssi;

        this.x=x;
        this.y=y;
        this.time=time;
        this.a_rssi=arssi;
        this.n=n;
        this.z=z;
        this.used=used;
    }

    public void setMap_key(String map_key) {
        this.map_key = map_key;
    }

    public String getMap_key() {
        return map_key;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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
