package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Beacon;
import com.kunlun.firmwaresystem.entity.device.DeviceBarn;

import java.util.List;

public class PageDeviceBarn {
    List<DeviceBarn> barnList;
    long page;
    long total;

    public PageDeviceBarn(List<DeviceBarn> barnList,
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

    public void setBeaconList(List<DeviceBarn> barnList) {
        this.barnList = barnList;
    }

    public List<DeviceBarn> getBarnList() {
        return barnList;
    }


}