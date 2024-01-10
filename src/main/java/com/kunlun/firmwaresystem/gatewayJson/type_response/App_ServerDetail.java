package com.kunlun.firmwaresystem.gatewayJson.type_response;

import com.alibaba.fastjson.JSONObject;

public class App_ServerDetail extends ResponseHead {
    String sub;
    String pub;
    String usr, pw;
    String clientId;
    int qos = 0;
    String host;

    public App_ServerDetail(String str) {
        resp = "sys_app_server";

        analysis(str);
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private void analysis(String gson) {
        JSONObject jsonObject = JSONObject.parseObject(gson);
        msgId = jsonObject.getIntValue("msgId");
        JSONObject mqtt = jsonObject.getJSONObject("mqtt");
        pub = mqtt.getString("pub");
        sub = mqtt.getString("sub");
        usr = mqtt.getString("usr");
        pw = mqtt.getString("pw");
        clientId = mqtt.getString("clientId");
        qos = mqtt.getIntValue("qos");
        host = mqtt.getString("host");

    }

    @Override
    public String toString() {
        return "App_ServerDetail{" +
                "sub='" + sub + '\'' +
                ", pub='" + pub + '\'' +
                ", usr='" + usr + '\'' +
                ", pw='" + pw + '\'' +
                ", clientId='" + clientId + '\'' +
                ", qos=" + qos +
                ", host='" + host + '\'' +
                '}';
    }
}
