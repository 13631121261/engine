package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Area;
import com.kunlun.firmwaresystem.entity.Person;

import java.util.List;

public class PageArea {
    List<Area> areaList;
    long page;
    long total;

    public PageArea(List<Area> areaList,
                    long page,
                    long total) {
        this.areaList = areaList;
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

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    @Override
    public String toString() {
        return "PageArea{" +
                "areaList=" + areaList +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}