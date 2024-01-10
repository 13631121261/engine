package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Map;

import java.util.List;

public class PageMap {
    List<Map> mapList;
    long page;
    long total;

    public PageMap(List<Map> mapList,
                   long page,
                   long total) {
        this.mapList = mapList;
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

    public void setMapList(List<Map> mapList) {
        this.mapList = mapList;
    }

    public List<Map> getMapList() {
        return mapList;
    }

    @Override
    public String toString() {
        return "PageMap{" +
                "recordList=" + mapList +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}