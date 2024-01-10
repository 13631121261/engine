package com.kunlun.firmwaresystem.location;

import com.kunlun.firmwaresystem.device.Gateway;

//网管之间的信号值矫正
public class G_G {
    Gateway gateway1, gateway2;
    int rssi;

    public Gateway getGateway1() {
        return gateway1;
    }

    public void setGateway1(Gateway gateway1) {
        this.gateway1 = gateway1;
    }

    public Gateway getGateway2() {
        return gateway2;
    }

    public void setGateway2(Gateway gateway2) {
        this.gateway2 = gateway2;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public G_G(Gateway gateway1, Gateway gateway2, int rssi) {
        this.gateway1 = gateway1;
        this.gateway2 = gateway2;
        this.rssi = rssi;

    }
}
