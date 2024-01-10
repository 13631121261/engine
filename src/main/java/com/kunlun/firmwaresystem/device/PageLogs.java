package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Logs;
import com.kunlun.firmwaresystem.entity.Map;

import java.util.List;

public class PageLogs {
    List<Logs> mapList;
    long page;
    long total;

    public PageLogs(List<Logs> mapList,
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

    public void setMapList(List<Logs> mapList) {
        this.mapList = mapList;
    }

    public List<Logs> getMapList() {
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