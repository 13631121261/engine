package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Beacon_tag;

import java.util.List;

public class PageBeaconTag {
    List<Beacon_tag> beacon_tags;
    long page;
    long total;

    public PageBeaconTag(List<Beacon_tag> beacon_tags,
                         long page,
                         long total) {
        this.beacon_tags = beacon_tags;
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

    public void setBeacon_tags(List<Beacon_tag> beaconList) {
        this.beacon_tags = beaconList;
    }

    public List<Beacon_tag> getBeacon_tags() {
        return beacon_tags;
    }

    @Override
    public String toString() {
        return "PageBeacon{" +
                "beaconList=" + beacon_tags +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}