package com.kunlun.firmwaresystem.gatewayJson.type_response;

import com.kunlun.firmwaresystem.gatewayJson.GatewayHand;

public class Network_Status extends GatewayHand {
    Network_Status_Detail data;

    public Network_Status_Detail getData() {
        return data;
    }

    public void setData(Network_Status_Detail data) {
        this.data = data;
    }
}
