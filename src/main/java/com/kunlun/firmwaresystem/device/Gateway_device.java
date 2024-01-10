package com.kunlun.firmwaresystem.device;

public class Gateway_device {

    String address;
    int rssi;
    long time;


    public Gateway_device() {

    }




    public Gateway_device(
            String address,
            int rssi,
            long time
          ) {
        this.address = address;
        this.time=time;
        this.rssi = rssi;


    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public int getRssi() {
        return rssi;
    }

    public String getAddress() {
        return address;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
