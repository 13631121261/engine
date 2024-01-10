package com.kunlun.firmwaresystem.gatewayJson.type_response;

import com.alibaba.fastjson.JSONObject;

public class Adv_paramsDetail extends ResponseHead {
    String resp;
    boolean result;
    boolean adv_onoff;
    String adv_dev_name;
    int adv_interval;
    String adv_ibcn_uuid;
    int adv_ibcn_major;
    int adv_ibcn_minor;
    int adv_ibcn_rssi;
    int adv_txpwr;

    public Adv_paramsDetail(String json) {
        analysis(json);
    }

    @Override
    public String getResp() {
        return resp;
    }

    @Override
    public void setResp(String resp) {
        this.resp = resp;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isAdv_onoff() {
        return adv_onoff;
    }

    public void setAdv_onoff(boolean adv_onoff) {
        this.adv_onoff = adv_onoff;
    }

    public String getAdv_dev_name() {
        return adv_dev_name;
    }

    public void setAdv_dev_name(String adv_dev_name) {
        this.adv_dev_name = adv_dev_name;
    }

    public int getAdv_interval() {
        return adv_interval;
    }

    public void setAdv_interval(int adv_interval) {
        this.adv_interval = adv_interval;
    }

    public String getAdv_ibcn_uuid() {
        return adv_ibcn_uuid;
    }

    public void setAdv_ibcn_uuid(String adv_ibcn_uuid) {
        this.adv_ibcn_uuid = adv_ibcn_uuid;
    }

    public int getAdv_ibcn_major() {
        return adv_ibcn_major;
    }

    public void setAdv_ibcn_major(int adv_ibcn_major) {
        this.adv_ibcn_major = adv_ibcn_major;
    }

    public int getAdv_ibcn_minor() {
        return adv_ibcn_minor;
    }

    public void setAdv_ibcn_minor(int adv_ibcn_minor) {
        this.adv_ibcn_minor = adv_ibcn_minor;
    }

    public int getAdv_ibcn_rssi() {
        return adv_ibcn_rssi;
    }

    public void setAdv_ibcn_rssi(int adv_ibcn_rssi) {
        this.adv_ibcn_rssi = adv_ibcn_rssi;
    }

    public int getAdv_txpwr() {
        return adv_txpwr;
    }

    public void setAdv_txpwr(int adv_txpwr) {
        this.adv_txpwr = adv_txpwr;
    }

    private void analysis(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        result = jsonObject.getBoolean("result");
        adv_dev_name = jsonObject.getJSONObject("adv_dev_name").getString("value");
        adv_onoff = jsonObject.getJSONObject("adv_onoff").getBoolean("enable");
        adv_interval = jsonObject.getJSONObject("adv_onoff").getIntValue("value");
        adv_ibcn_uuid = jsonObject.getJSONObject("adv_ibcn_uuid").getString("value");
        adv_ibcn_major = jsonObject.getJSONObject("adv_ibcn_major").getIntValue("value");
        adv_ibcn_minor = jsonObject.getJSONObject("adv_ibcn_minor").getIntValue("value");
        adv_ibcn_rssi = jsonObject.getJSONObject("adv_ibcn_rssi").getIntValue("value");
        adv_txpwr = jsonObject.getJSONObject("adv_txpwr").getIntValue("value");
    }
}
