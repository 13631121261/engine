package com.kunlun.firmwaresystem.device.cmd;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kunlun.firmwaresystem.gatewayJson.Constant.cmd_conn_addr_disconn;


public class DisConnectDetail {

    int msgId;
    final String cmd = cmd_conn_addr_disconn;
    int devices_num;
    ArrayList<HashMap<String, String>> devices;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getCmd() {
        return cmd;
    }

    public int getDevices_num() {
        return devices_num;
    }

    public void setDevices_num(int devices_num) {
        this.devices_num = devices_num;
    }

    public ArrayList<HashMap<String, String>> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<HashMap<String, String>> devices) {
        this.devices = devices;
    }
}
