package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.location_util.backup.Gateway_device;

import java.io.Serializable;

public class Device implements Serializable {

    String dAddress;
    int payload_len_max;
    String state;
    int timeout;
    Gateway_device gateway_device;


    public Device() {

    }

    public Device(
            String dAddress, String state, int timeOut, Gateway_device gateway_device) {
        this.dAddress = dAddress;
        this.state = state;
        this.timeout = timeOut;
        this.gateway_device = gateway_device;
    }

    public Gateway_device getGateway_device() {
        return gateway_device;
    }

    public void setGateway_device(Gateway_device gateway_device) {
        this.gateway_device = gateway_device;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }


    public String getdAddress() {
        return dAddress;
    }

    public void setdAddress(String dAddress) {
        this.dAddress = dAddress;
    }

    public int getPayload_len_max() {
        return payload_len_max;
    }

    public void setPayload_len_max(int payload_len_max) {
        this.payload_len_max = payload_len_max;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
