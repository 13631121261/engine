package com.kunlun.firmwaresystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;

public class Tag_log {
   long  id;
   String type;
   String       beacon_address;
   String gateway_address;
   String  gateway_name;
   int rssi;
   String    bt;
   int keys1;
   int run;
   long    create_time;
   String project_key;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeacon_address() {
        return beacon_address;
    }

    public void setBeacon_address(String beacon_address) {
        this.beacon_address = beacon_address;
    }

    public String getGateway_address() {
        return gateway_address;
    }

    public void setGateway_address(String gateway_address) {
        this.gateway_address = gateway_address;
    }

    public String getGateway_name() {
        return gateway_name;
    }

    public void setGateway_name(String gateway_name) {
        this.gateway_name = gateway_name;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public int getKeys1() {
        return keys1;
    }

    public void setKeys1(int keys1) {
        this.keys1 = keys1;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getProject_key() {
        return project_key;
    }

    @Override
    public String toString() {
        return "Tag_log{" +
                "id=" + id +
                ", beacon_address='" + beacon_address + '\'' +
                ", gateway_address='" + gateway_address + '\'' +
                ", gateway_name='" + gateway_name + '\'' +
                ", rssi=" + rssi +
                ", bt=" + bt +
                ", key=" + keys1 +
                ", run=" + run +
                ", create_time=" + create_time +
                ", project_key='" + project_key + '\'' +
                '}';
    }
}
