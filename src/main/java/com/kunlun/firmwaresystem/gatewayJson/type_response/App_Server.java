package com.kunlun.firmwaresystem.gatewayJson.type_response;

import com.kunlun.firmwaresystem.gatewayJson.GatewayHand;

public class App_Server extends GatewayHand {
    App_ServerDetail data;

    public void setData(App_ServerDetail data) {
        this.data = data;
    }

    public App_ServerDetail getData() {
        return data;
    }
}
