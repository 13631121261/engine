package com.kunlun.firmwaresystem.device.cmd;

public class Connect {
    final String pkt_type = "command";
    String gw_addr;
    String time;
    ConnectDetail data;

    public String getPkt_type() {
        return pkt_type;
    }

    public String getGw_addr() {
        return gw_addr;
    }

    public void setGw_addr(String gw_addr) {
        this.gw_addr = gw_addr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ConnectDetail getData() {
        return data;
    }

    public void setData(ConnectDetail data) {
        this.data = data;
    }
}
