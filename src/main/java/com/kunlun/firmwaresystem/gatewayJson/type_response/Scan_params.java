package com.kunlun.firmwaresystem.gatewayJson.type_response;

import com.kunlun.firmwaresystem.gatewayJson.GatewayHand;

public class Scan_params extends GatewayHand {
    Scan_paramsDetail data;

    public Scan_paramsDetail getData() {
        return data;
    }

    public void setData(Scan_paramsDetail data) {
        this.data = data;
    }
}
