package com.kunlun.firmwaresystem.gatewayJson.type_response;

import com.kunlun.firmwaresystem.gatewayJson.GatewayHand;

public class WifiVersion<T> extends GatewayHand {
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
