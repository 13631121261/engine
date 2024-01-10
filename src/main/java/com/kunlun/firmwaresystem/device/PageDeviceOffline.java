package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.device.Device_offline;

import java.util.List;

public class PageDeviceOffline {
    List<Device_offline> device_offlines;
    long page;
    long total;

    public PageDeviceOffline(List<Device_offline> device_offlines,
                             long page,
                             long total) {
        this.device_offlines = device_offlines;
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

    public List<Device_offline> getDevice_offlines() {
        return device_offlines;
    }

    public void setDevice_offlines(List<Device_offline> device_offlines) {
        this.device_offlines = device_offlines;
    }
}