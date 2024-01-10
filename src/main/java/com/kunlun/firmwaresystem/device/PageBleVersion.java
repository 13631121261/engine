package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Ble_firmware;

import java.util.List;

public class PageBleVersion {
    List<Ble_firmware> ble_firmwares;
    long page;
    long total;

    public PageBleVersion(List<Ble_firmware> ble_firmwares,
                          long page,
                          long total) {
        this.ble_firmwares = ble_firmwares;
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

    public List<Ble_firmware> getBle_firmwares() {
        return ble_firmwares;
    }

    public void setBle_firmwares(List<Ble_firmware> ble_firmwares) {
        this.ble_firmwares = ble_firmwares;
    }

    @Override
    public String toString() {
        return "PageBleVersion{" +
                "ble_firmwares=" + ble_firmwares +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}