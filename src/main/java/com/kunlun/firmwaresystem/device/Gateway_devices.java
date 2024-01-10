package com.kunlun.firmwaresystem.device;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Gateway_devices {
    ArrayList<Gateway_device> gatewayDevices;

    public ArrayList<Gateway_device> getGatewayDevices() {
        return gatewayDevices;
    }

    public void setGatewayDevices(ArrayList<Gateway_device> gatewayDevices) {
        this.gatewayDevices = gatewayDevices;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
