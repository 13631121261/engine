package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Record;
import com.kunlun.firmwaresystem.entity.device.DeviceCode;

import java.util.List;

public class PageDeviceCode {
    List<DeviceCode> deviceCodes;
    long page;
    long total;

    public PageDeviceCode(List<DeviceCode> deviceCodes,
                          long page,
                          long total) {
        this.deviceCodes = deviceCodes;
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

    public void setDeviceCodes(List<DeviceCode> deviceCodes) {
        this.deviceCodes = deviceCodes;
    }

    public List<DeviceCode> getDeviceCodes() {
        return deviceCodes;
    }


}