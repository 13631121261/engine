package com.kunlun.firmwaresystem.device.cmd;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DisConnect {
    final String pkt_type = "command";
    String gw_addr;
    String time;
    DisConnectDetail data;

    public DisConnect() {
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        this.time = sdf.format(date);
    }

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

    public DisConnectDetail getData() {
        return data;
    }

    public void setData(DisConnectDetail data) {
        this.data = data;
    }
}
