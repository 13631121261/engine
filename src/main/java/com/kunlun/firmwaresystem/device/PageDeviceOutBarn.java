package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.device.DeviceBarn;
import com.kunlun.firmwaresystem.entity.device.DeviceOutBarn;

import java.util.List;

public class PageDeviceOutBarn {
    List<DeviceOutBarn> barnList;
    long page;
    long total;

    public PageDeviceOutBarn(List<DeviceOutBarn> barnList,
                             long page,
                             long total) {
        this.barnList = barnList;
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

    public void setBarnList(List<DeviceOutBarn> barnList) {
        this.barnList = barnList;
    }

    public List<DeviceOutBarn> getBarnList() {
        return barnList;
    }


}