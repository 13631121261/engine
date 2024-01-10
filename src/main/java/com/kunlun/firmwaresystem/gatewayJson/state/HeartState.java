package com.kunlun.firmwaresystem.gatewayJson.state;

import com.kunlun.firmwaresystem.gatewayJson.GatewayHand;

public class HeartState<T> extends GatewayHand {
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
