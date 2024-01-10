package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Locator;
import com.kunlun.firmwaresystem.entity.device.Devicep;

import java.util.List;

public class PageLocator {
    List<Locator> locatorList;
    long page;
    long total;

    public PageLocator(List<Locator> locatorList,
                       long page,
                       long total) {
        this.locatorList = locatorList;
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

    public void setLocatorList(List<Locator> locatorList) {
        this.locatorList = locatorList;
    }

    public List<Locator> getLocatorList() {
        return locatorList;
    }

    @Override
    public String toString() {
        return "PageBeacon{" +
                "locatorList=" + locatorList +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}