package com.kunlun.firmwaresystem.gatewayJson.type_scan_report;

import java.util.Arrays;

public class Scan_report_data_info {
    String addr;
    String time;
    int rssi;
    String name;
    String ibcn_uuid;
    int ibcn_major;
    int ibcn_minor;
    int ibcn_rssi_at_1m;
    String adv_raw;
    String srp_raw;
    int motion;
    int keys;
    int batt;
    int ibcn_num;
    Ibcn_infos[] ibcn_infos;


    public Scan_report_data_info(String mac, int rssi, String time) {
        this.addr = mac;
        this.rssi = rssi;
        this.time = time;
    }

    public Scan_report_data_info() {

    }

    public int getMotion() {
        return motion;
    }

    public void setMotion(int motion) {
        this.motion = motion;
    }

    public int getKeys() {
        return keys;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }

    public int getBatt() {
        return batt;
    }

    public void setBatt(int batt) {
        this.batt = batt;
    }

    public int getIbcn_num() {
        return ibcn_num;
    }

    public void setIbcn_num(int ibcn_num) {
        this.ibcn_num = ibcn_num;
    }

    public Ibcn_infos[] getIbcn_infos() {
        return ibcn_infos;
    }

    public void setIbcn_infos(Ibcn_infos[] ibcn_infos) {
        this.ibcn_infos = ibcn_infos;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTime() {
        if (time != null && time.length() == 18) {
            time = time + "0";
        }
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIbcn_uuid() {
        return ibcn_uuid;
    }

    public void setIbcn_uuid(String ibcn_uuid) {
        this.ibcn_uuid = ibcn_uuid;
    }

    public int getIbcn_major() {
        return ibcn_major;
    }

    public void setIbcn_major(int ibcn_major) {
        this.ibcn_major = ibcn_major;
    }

    public int getIbcn_minor() {
        return ibcn_minor;
    }

    public void setIbcn_minor(int ibcn_minor) {
        this.ibcn_minor = ibcn_minor;
    }

    public int getIbcn_rssi_at_1m() {
        return ibcn_rssi_at_1m;
    }

    public void setIbcn_rssi_at_1m(int ibcn_rssi_at_1m) {
        this.ibcn_rssi_at_1m = ibcn_rssi_at_1m;
    }

    public String getAdv_raw() {
        return adv_raw;
    }

    public void setAdv_raw(String adv_raw) {
        this.adv_raw = adv_raw;
    }

    public String getSrp_raw() {
        return srp_raw;
    }

    public void setSrp_raw(String srp_raw) {
        this.srp_raw = srp_raw;
    }

    @Override
    public String toString() {
        return "Scan_report_data_info{" +
                "addr='" + addr + '\'' +
                ", time='" + time + '\'' +
                ", rssi=" + rssi +
                ", name='" + name + '\'' +
                ", ibcn_uuid='" + ibcn_uuid + '\'' +
                ", ibcn_major=" + ibcn_major +
                ", ibcn_minor=" + ibcn_minor +
                ", ibcn_rssi_at_1m=" + ibcn_rssi_at_1m +
                ", adv_raw='" + adv_raw + '\'' +
                ", srp_raw='" + srp_raw + '\'' +
                ", motion=" + motion +
                ", keys=" + keys +
                ", batt=" + batt +
                ", ibcn_num=" + ibcn_num +
                ", ibcn_infos=" + Arrays.toString(ibcn_infos) +
                '}';
    }
}
