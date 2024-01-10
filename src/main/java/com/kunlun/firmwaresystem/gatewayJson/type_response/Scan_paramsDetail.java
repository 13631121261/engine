package com.kunlun.firmwaresystem.gatewayJson.type_response;

import com.alibaba.fastjson.JSONObject;

public class Scan_paramsDetail extends ResponseHead {
    boolean result;
    boolean report_onoff;
    boolean request_onoff;
    boolean stuff_card_onoff;
    int report_interval;
    int report_max_num;
    boolean report_rssi_only;
    boolean report_rank_rssi;
    boolean channel_37, channel_38, channel_39;

    public Scan_paramsDetail(String json) {
        analysis(json);
    }

    public boolean isReport_onoff() {
        return report_onoff;
    }

    public boolean isRequest_onoff() {
        return request_onoff;
    }

    public boolean isStuff_card_onoff() {
        return stuff_card_onoff;
    }

    public int getReport_interval() {
        return report_interval;
    }

    public int getReport_max_num() {
        return report_max_num;
    }

    public boolean isReport_rssi_only() {
        return report_rssi_only;
    }

    public boolean isReport_rank_rssi() {
        return report_rank_rssi;
    }

    public boolean isChannel_37() {
        return channel_37;
    }

    public boolean isChannel_38() {
        return channel_38;
    }

    public boolean isChannel_39() {
        return channel_39;
    }

    private void analysis(String gson) {
        //   System.out.println("333");
        JSONObject jsonObject = JSONObject.parseObject(gson);
        //  System.out.println("44");
        msgId = jsonObject.getIntValue("msgId");
        // System.out.println("55");
        result = jsonObject.getBoolean("result");
        //   System.out.println("66");
        resp = jsonObject.getString("resp");
        //  System.out.println("77");
        report_onoff = jsonObject.getJSONObject("scan_report_onoff").getBoolean("enable");
        //  System.out.println("888");
        request_onoff = jsonObject.getJSONObject("scan_request_onoff").getBoolean("enable");
        //  System.out.println("999");
        if (jsonObject.getJSONObject("scan_stuff_card_onoff") != null) {
            stuff_card_onoff = jsonObject.getJSONObject("scan_stuff_card_onoff").getBoolean("enable");
        }
        //  System.out.println("000");
        report_interval = jsonObject.getJSONObject("scan_report_interval").getIntValue("value");
        // System.out.println("aaa");
        report_max_num = jsonObject.getJSONObject("scan_report_max_num").getIntValue("value");
        // System.out.println("bbb");
        report_rssi_only = jsonObject.getJSONObject("scan_report_rssi_only").getBoolean("enable");
        // System.out.println("ccc");
        report_rank_rssi = jsonObject.getJSONObject("scan_report_rank_rssi").getBoolean("enable");
        // System.out.println("ddd");
        channel_37 = jsonObject.getJSONObject("scan_rf_channel").getBoolean("ch37");
      //  System.out.println("eee");
        channel_38 = jsonObject.getJSONObject("scan_rf_channel").getBoolean("ch38");
        // System.out.println("fff");
        channel_39 = jsonObject.getJSONObject("scan_rf_channel").getBoolean("ch39");
    }
}
