package com.kunlun.firmwaresystem.gatewayJson.type_response;

import com.kunlun.firmwaresystem.gatewayJson.GatewayHand;

public class Adv_params extends GatewayHand {
    public Adv_paramsDetail data;

    public void setData(Adv_paramsDetail data) {
        this.data = data;
    }

    public Adv_paramsDetail getData() {
        return data;
    }
}
