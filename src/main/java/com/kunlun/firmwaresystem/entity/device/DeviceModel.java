package com.kunlun.firmwaresystem.entity.device;

import java.util.HashMap;

public class DeviceModel {
    int id;
    String customer_key;
    String name;
    String uuids;
    String handles;
    String user_key;
    HashMap<String, String> uuids_handles;

    public HashMap<String, String> getUuids_handles() {
        return uuids_handles;
    }

    public void setUuids_handles(HashMap uuids_handles) {
        this.uuids_handles = uuids_handles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuids() {
        return uuids;
    }

    public void setUuids(String uuids) {
        this.uuids = uuids;
    }

    public String getHandles() {
        return handles;
    }

    public void setHandles(String handles) {
        this.handles = handles;
    }

    public String getUser_key() {
        return user_key;
    }

    public String getCustomer_key() {
        return customer_key;
    }

    public void setCustomer_key(String customer_key) {
        this.customer_key = customer_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }


}
