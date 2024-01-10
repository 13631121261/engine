package com.kunlun.firmwaresystem.gatewayJson.state;

public class ConnectDetail extends StateHead {
    String device_addr;
    String device_state;

    public String getDevice_addr() {
        return device_addr;
    }

    public void setDevice_addr(String device_addr) {
        this.device_addr = device_addr;
    }

    public String getDevice_state() {
        return device_state;
    }

    public void setDevice_state(String device_state) {
        this.device_state = device_state;
    }
}
