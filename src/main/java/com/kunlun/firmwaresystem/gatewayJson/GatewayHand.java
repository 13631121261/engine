package com.kunlun.firmwaresystem.gatewayJson;

import java.io.Serializable;

//全部的类都会继承这个类
public class GatewayHand implements Serializable {
    String pkt_type;
    String gw_addr;
    String time;


    public String getPkt_type() {
        return pkt_type;
    }

    public void setPkt_type(String pkt_type) {
        this.pkt_type = pkt_type;
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
}
