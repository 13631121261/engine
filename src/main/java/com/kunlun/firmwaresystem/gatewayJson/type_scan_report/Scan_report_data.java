package com.kunlun.firmwaresystem.gatewayJson.type_scan_report;

public class Scan_report_data {
    int flags;
    String report_type;
    int pkt_total;
    int pkt_index;
    int dev_total;
    int dev_num;
    Scan_report_data_info[] dev_infos;

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public int getPkt_total() {
        return pkt_total;
    }

    public void setPkt_total(int pkt_total) {
        this.pkt_total = pkt_total;
    }

    public int getPkt_index() {
        return pkt_index;
    }

    public void setPkt_index(int pkt_index) {
        this.pkt_index = pkt_index;
    }

    public int getDev_total() {
        return dev_total;
    }

    public void setDev_total(int dev_total) {
        this.dev_total = dev_total;
    }

    public int getDev_num() {
        return dev_num;
    }

    public void setDev_num(int dev_num) {
        this.dev_num = dev_num;
    }

    public Scan_report_data_info[] getDev_infos() {
        return dev_infos;
    }

    public void setDev_infos(Scan_report_data_info[] dev_infos) {
        this.dev_infos = dev_infos;
    }
}
