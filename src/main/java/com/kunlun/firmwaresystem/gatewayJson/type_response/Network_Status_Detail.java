package com.kunlun.firmwaresystem.gatewayJson.type_response;

import com.alibaba.fastjson.JSONObject;

public class Network_Status_Detail extends ResponseHead {
    String up_time;
    String wlan_mac;
    String wifi_onoff;
    String wlan_ssid;
    String wlan_ip;
    String wlan_mask;
    String op_mode;
    String wan_mode;
    String wan_ip;
    String wan_mask;
    String wan_gw;
    String CRC_FLAG;
    public Network_Status_Detail(String json){
            analysis(json);
    }
    private void analysis(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        msgId = jsonObject.getIntValue("msgId");
        resp = jsonObject.getString("resp");
        wifi_onoff=jsonObject.getJSONObject("status").getString("wifi_onoff");
        wlan_ssid=jsonObject.getJSONObject("status").getString("wlan_ssid");
        wlan_ip=jsonObject.getJSONObject("status").getString("wlan_ip");
        wlan_mask=jsonObject.getJSONObject("status").getString("wlan_mask");
        op_mode=jsonObject.getJSONObject("status").getString("op_mode");
        wan_mode=jsonObject.getJSONObject("status").getString("wan_mode");
        wan_ip=jsonObject.getJSONObject("status").getString("wan_ip");
        wan_mask=jsonObject.getJSONObject("status").getString("wan_mask");
        wan_gw=jsonObject.getJSONObject("status").getString("wan_gw");
        wlan_mac=jsonObject.getJSONObject("status").getString("wlan_mac");
        up_time=jsonObject.getJSONObject("status").getString("up_time");
        CRC_FLAG=jsonObject.getJSONObject("status").getString("CRC_FLAG");

    }

    public String getCRC_FLAG() {
        return CRC_FLAG;
    }

    public void setUp_time(String up_time) {
        this.up_time = up_time;
    }

    public String getUp_time() {
        return up_time;
    }

    public void setWlan_mac(String wlan_mac) {
        this.wlan_mac = wlan_mac;
    }

    public String getWlan_mac() {
        return wlan_mac;
    }

    public String getWifi_onoff() {
        return wifi_onoff;
    }

    public void setWifi_onoff(String wifi_onoff) {
        this.wifi_onoff = wifi_onoff;
    }

    public String getWlan_ssid() {
        return wlan_ssid;
    }

    public void setWlan_ssid(String wlan_ssid) {
        this.wlan_ssid = wlan_ssid;
    }

    public String getWlan_ip() {
        return wlan_ip;
    }

    public void setWlan_ip(String wlan_ip) {
        this.wlan_ip = wlan_ip;
    }

    public String getWlan_mask() {
        return wlan_mask;
    }

    public void setWlan_mask(String wlan_mask) {
        this.wlan_mask = wlan_mask;
    }

    public String getOp_mode() {
        return op_mode;
    }

    public void setOp_mode(String op_mode) {
        this.op_mode = op_mode;
    }

    public String getWan_mode() {
        return wan_mode;
    }

    public void setWan_mode(String wan_mode) {
        this.wan_mode = wan_mode;
    }

    public String getWan_ip() {
        return wan_ip;
    }

    public void setWan_ip(String wan_ip) {
        this.wan_ip = wan_ip;
    }

    public String getWan_mask() {
        return wan_mask;
    }

    public void setWan_mask(String wan_mask) {
        this.wan_mask = wan_mask;
    }

    public String getWan_gw() {
        return wan_gw;
    }

    public void setWan_gw(String wan_gw) {
        this.wan_gw = wan_gw;
    }

    @Override
    public String toString() {
        return "Network_Status_Detail{" +
                "up_time='" + up_time + '\'' +
                ", wlan_mac='" + wlan_mac + '\'' +
                ", wifi_onoff='" + wifi_onoff + '\'' +
                ", wlan_ssid='" + wlan_ssid + '\'' +
                ", wlan_ip='" + wlan_ip + '\'' +
                ", wlan_mask='" + wlan_mask + '\'' +
                ", op_mode='" + op_mode + '\'' +
                ", wan_mode='" + wan_mode + '\'' +
                ", wan_ip='" + wan_ip + '\'' +
                ", wan_mask='" + wan_mask + '\'' +
                ", wan_gw='" + wan_gw + '\'' +
                '}';
    }
}
