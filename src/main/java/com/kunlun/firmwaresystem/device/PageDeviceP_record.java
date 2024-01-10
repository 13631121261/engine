package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.device.Devicep_record;

import java.util.List;

public class PageDeviceP_record {
    List<Devicep_record> deviceList;
    long page;
    long total;

    public PageDeviceP_record(List<Devicep_record> deviceList,
                              long page,
                              long total) {
        this.deviceList = deviceList;
        this.page = page;
        this.total = total;
    }


    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setDeviceList(List<Devicep_record> deviceList) {
        this.deviceList = deviceList;
    }

    public List<Devicep_record> getDeviceList() {
        return deviceList;
    }

    @Override
    public String toString() {
        return "PageBeacon{" +
                "deviceList=" + deviceList +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}