package com.kunlun.firmwaresystem.device.cmd;

import com.google.gson.Gson;
import com.kunlun.firmwaresystem.gatewayJson.Constant;

public class BleVersion {
    final String pkt_type = Constant.pkt_type_command;
    String gw_addr;

    BleVersionDetail data;

    public String getPkt_type() {
        return pkt_type;
    }


    public String getGw_addr() {
        return gw_addr;
    }

    public void setGw_addr(String gw_addr) {
        this.gw_addr = gw_addr;
    }

    public BleVersionDetail getData() {
        return data;
    }

    public void setData(BleVersionDetail data) {
        this.data = data;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
