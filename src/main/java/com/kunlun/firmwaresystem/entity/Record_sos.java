package com.kunlun.firmwaresystem.entity;

public class Record_sos {
    int id;
    int sos;
    long time;
    String handle_time;
    int handle = 0;
    String mac;
    String gateway_name;
    String gateway_mac;
    String sn_idcard;
    String name;
    String type;
    String customer_key;
    String userkey;

    public Record_sos() {

    }

    public Record_sos(int sos,
                      long time,
                      String mac,
                      String gateway_mac,
                      String gateway_name,
                      String name,
                      String sn_idcard,
                      String type,    String customer_key,String userkey
                    ) {
        this.mac = mac;
        this.userkey=userkey;
        this.sos= sos;
        this.time = time;
        this.gateway_mac = gateway_mac;
        this.gateway_name = gateway_name;
        this.name=name;
        this.sn_idcard=sn_idcard;
        this.type=type;
        this.customer_key=customer_key;

    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSn_idcard(String sn_idcard) {
        this.sn_idcard = sn_idcard;
    }

    public String getSn_idcard() {
        return sn_idcard;
    }

    public String getType() {
        return type;
    }

    public void setGateway_name(String gateway_name) {
        this.gateway_name = gateway_name;
    }

    public void setGateway_mac(String gateway_mac) {
        this.gateway_mac = gateway_mac;
    }

    public String getGateway_name() {
        return gateway_name;
    }

    public String getGateway_mac() {
        return gateway_mac;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getSos() {
        return sos;
    }

    public void setSos(int sos) {
        this.sos = sos;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getHandle_time() {
        return handle_time;
    }

    public void setHandle_time(String handle_time) {
        this.handle_time = handle_time;
    }

    public int getHandle() {
        return handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
