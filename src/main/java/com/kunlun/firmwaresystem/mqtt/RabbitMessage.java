package com.kunlun.firmwaresystem.mqtt;

import com.google.gson.Gson;

public class RabbitMessage {
    String pubTopic;
    String msg;
    String udp;

    public RabbitMessage() {

    }

    public RabbitMessage(String pubTopic, String msg) {
        this.msg = msg;
        this.pubTopic = pubTopic;
    }

    public void setUdp(String udp) {
        this.udp = udp;
    }

    public String getUdp() {
        return udp;
    }

    public String getPubTopic() {
        return pubTopic;
    }

    public void setPubTopic(String pubTopic) {
        this.pubTopic = pubTopic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
