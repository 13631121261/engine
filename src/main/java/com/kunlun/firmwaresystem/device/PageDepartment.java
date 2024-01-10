package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Department;

import java.util.List;

public class PageDepartment {
    List<Department> departmentList;
    long page;
    long total;

    public PageDepartment(List<Department> departmentList,
                          long page,
                          long total) {
        this.departmentList = departmentList;
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

    public void setDeviceList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public List<Department> getDeviceList() {
        return departmentList;
    }

    @Override
    public String toString() {
        return "PageBeacon{" +
                "deviceList=" + departmentList +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}