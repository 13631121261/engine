package com.kunlun.firmwaresystem.gatewayJson.type_scan_report;

import com.kunlun.firmwaresystem.gatewayJson.GatewayHand;

public class Scan_report<T> extends GatewayHand {
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
