package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Beacon;

import java.util.List;

public class PageBeacon {
    List<Beacon> beaconList;
    long page;
    long total;

    public PageBeacon(List<Beacon> beaconList,
                      long page,
                      long total) {
        this.beaconList = beaconList;
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

    public void setBeaconList(List<Beacon> beaconList) {
        this.beaconList = beaconList;
    }

    public List<Beacon> getBeaconList() {
        return beaconList;
    }

    @Override
    public String toString() {
        return "PageBeacon{" +
                "beaconList=" + beaconList +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}