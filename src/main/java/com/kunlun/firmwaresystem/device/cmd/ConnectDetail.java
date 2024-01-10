package com.kunlun.firmwaresystem.device.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kunlun.firmwaresystem.gatewayJson.Constant.cmd_conn_addr_request;

public class ConnectDetail {
    int msgId;
    final String cmd = cmd_conn_addr_request;
    int devices_num;
    List<HashMap<String, String>> devices;
    int notify_chars_num;
    ArrayList<Map> notify_chars;
    int indicate_chars_num;
    ArrayList<Map> indicate_chars;
    int keep_time;
    int send_num;
    ArrayList<Map> send_infos;

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

    public List<HashMap<String, String>> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<HashMap<String, String>> devices) {
        this.devices = devices;
    }

    public int getNotify_chars_num() {
        return notify_chars_num;
    }

    public void setNotify_chars_num(int notify_chars_num) {
        this.notify_chars_num = notify_chars_num;
    }

    public ArrayList<Map> getNotify_chars() {
        return notify_chars;
    }

    public void setNotify_chars(ArrayList<Map> notify_chars) {
        this.notify_chars = notify_chars;
    }

    public int getIndicate_chars_num() {
        return indicate_chars_num;
    }

    public void setIndicate_chars_num(int indicate_chars_num) {
        this.indicate_chars_num = indicate_chars_num;
    }

    public ArrayList<Map> getIndicate_chars() {
        return indicate_chars;
    }

    public void setIndicate_chars(ArrayList<Map> indicate_chars) {
        this.indicate_chars = indicate_chars;
    }

    public int getKeep_time() {
        return keep_time;
    }

    public void setKeep_time(int keep_time) {
        this.keep_time = keep_time;
    }

    public int getSend_num() {
        return send_num;
    }

    public void setSend_num(int send_num) {
        this.send_num = send_num;
    }

    public ArrayList<Map> getSend_infos() {
        return send_infos;
    }

    public void setSend_infos(ArrayList<Map> send_infos) {
        this.send_infos = send_infos;
    }
}
