package com.kunlun.firmwaresystem.gatewayJson.type_response;

import com.kunlun.firmwaresystem.gatewayJson.GatewayHand;

public class Scan_filter extends GatewayHand {
    Scan_filterDetail data;

    public Scan_filterDetail getData() {
        return data;
    }

    public void setData(Scan_filterDetail data) {
        this.data = data;
    }
}
