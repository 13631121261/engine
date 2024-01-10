package com.kunlun.firmwaresystem.gatewayJson.state;

//{"pkt_type":"state","gw_addr":"f0aa21b4a1f3",\time":"2021-12-04 11:00:38",
// "data":{"msgId":12,"state":"sta_device_state","device_addr":"d028989ce2b6","device_state":"searching"}}
public class StateHead {
    int msgId;
    String state;


    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
