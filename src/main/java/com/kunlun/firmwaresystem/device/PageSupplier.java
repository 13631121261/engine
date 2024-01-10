package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.device.Deviceptype;
import com.kunlun.firmwaresystem.entity.device.Supplier;

import java.util.List;

public class PageSupplier {
    List<Supplier> supplierList;
    long page;
    long total;

    public PageSupplier(List<Supplier> supplierList,
                        long page,
                        long total) {
        this.supplierList = supplierList;
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

    public void setDeviceList(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

    public List<Supplier> getSupplierList() {
        return supplierList;
    }

    @Override
    public String toString() {
        return "PageBeacon{" +
                "supplierList=" + supplierList +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}