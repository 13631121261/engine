package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Ble_firmware;
import com.kunlun.firmwaresystem.entity.Wifi_firmware;

import java.util.List;

public class PageWifiVersion {
    List<Wifi_firmware> wifi_firmwares;
    long page;
    long total;

    public PageWifiVersion(List<Wifi_firmware> wifi_firmwares,
                           long page,
                           long total) {
        this.wifi_firmwares = wifi_firmwares;
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

    public List<Wifi_firmware> getWifi_firmwares() {
        return wifi_firmwares;
    }

    public void setBle_firmwares(List<Ble_firmware> ble_firmwares) {
        this.wifi_firmwares = wifi_firmwares;
    }

    @Override
    public String toString() {
        return "PageWifiVersion{" +
                "ble_firmwares=" + wifi_firmwares +
                ", page=" + page +
                ", total=" + total +
                '}';
    }
}